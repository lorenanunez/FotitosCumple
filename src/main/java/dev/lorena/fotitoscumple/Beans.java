package dev.lorena.fotitoscumple;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class Beans {

    @Bean
    S3Client s3Client() {
        return S3Client
            .builder()
            .region(Region.US_EAST_2)
            .credentialsProvider(DefaultCredentialsProvider.builder().build())
            .build();
    }

    @Bean
    Tika tika() {
        return new Tika();
    }

    @Bean
    ExecutorService virtualThreadTaskExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

}
