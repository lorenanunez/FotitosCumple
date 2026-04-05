package dev.lorena.fotitoscumple.exception;

import java.io.IOException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("uploadSizeError", true);
        return "redirect:/";
    }

    @ExceptionHandler(IOException.class)
    public String handleIOException(IOException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("networkIOError", true);
        return "redirect:/";
    }
}
