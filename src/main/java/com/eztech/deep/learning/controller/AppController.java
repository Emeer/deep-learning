package com.eztech.deep.learning.controller;

import java.io.IOException;


import com.eztech.deep.learning.service.ClassifyImageService;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    private ClassifyImageService classifyImageService;


    @PostMapping(value = "/classify")
    @CrossOrigin(origins = "*")
    public ClassifyImageService.LabelWithProbability classifyImage(@RequestParam MultipartFile file) throws IOException {
        checkImageContents(file);
        return classifyImageService.classifyImage(file.getBytes());
    }

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    private void checkImageContents(MultipartFile file) {
        MagicMatch match;
        try {
            match = Magic.getMagicMatch(file.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String mimeType = match.getMimeType();
        if (!mimeType.startsWith("image")) {
            throw new IllegalArgumentException("Not an image type: " + mimeType);
        }
    }

}
