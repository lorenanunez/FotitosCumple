package dev.lorena.fotitoscumple.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.Strings;
import org.apache.tika.Tika;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.lorena.fotitoscumple.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final Tika tika;
    private final FileStorageService fileStorageService;

    @GetMapping("/")
    public String getMainPage(Model model) throws InterruptedException, ExecutionException {
        model.addAttribute("fotos", fileStorageService.getLast10Pictures().get());
        return "main";
    }

    @PostMapping("/files/upload")
    @SneakyThrows(IOException.class)
    public String uploadMedia(@RequestParam List<MultipartFile> media, RedirectAttributes attributes) {
        var copiedBytes = new HashMap<String, byte[]>();
        for (var element : media) {
            if (Strings.CI.startsWithAny(tika.detect(element.getBytes()), "image/", "video/")) {
                copiedBytes.put(element.getOriginalFilename(), element.getBytes());
            } else {
                log.error("{} is not a valid media file.", element.getOriginalFilename());
                copiedBytes.clear();
                break;
            }
        }
        if (copiedBytes.isEmpty()) {
            attributes.addFlashAttribute("uploadError", true);
        } else {
            fileStorageService.uploadPhotosToS3(copiedBytes);
            attributes.addFlashAttribute("uploadSuccess", true);
        }
        return "redirect:/";
    }

}
