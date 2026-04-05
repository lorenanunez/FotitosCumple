package dev.lorena.fotitoscumple.service;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final S3Client s3Client;

    @Value("${BUCKET_NAME:${bucket.name}}")
    private String bucketName;

    @Async("virtualThreadTaskExecutor")
    public Future<Void> uploadPhotosToS3(Map<String, byte[]> files) {
        if (files.isEmpty()) {
            log.warn("Nothing to upload, filelist is empty.");
            return CompletableFuture.completedFuture(null);
        }

        files.forEach((k, v) -> {
            log.info("Uploading file: {}", k);
            var filename = RandomStringUtils.secureStrong().nextAlphanumeric(16) + " - " + k;
            var putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(v));
            var request = GetUrlRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .build();

            var url = s3Client.utilities().getUrl(request);
            log.info("Saved media {} to S3 -> {}", k, url.toExternalForm());
        });

        return CompletableFuture.completedFuture(null);
    }

    @Async("virtualThreadTaskExecutor")
    public Future<List<URL>> getLast10Pictures() {
        var request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();
        var response = s3Client.listObjectsV2(request);
        var value = response.contents().stream()
                .sorted(Comparator.comparing(S3Object::lastModified, Comparator.reverseOrder()))
                .limit(10)
                .map(object -> s3Client.utilities().getUrl(GetUrlRequest.builder().bucket(bucketName).key(object.key()).build()))
                .toList();
        return CompletableFuture.completedFuture(value);
    }
}
