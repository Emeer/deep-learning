package com.eztech.deep.learning.controller;

import com.eztech.deep.learning.service.DetectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Detection controller.
 * <p>
 * Created by jia on 08/06/2017.
 */
@RestController
@Slf4j
public class DetectionController {


    @Autowired
    private DetectionService detectionService;


    @RequestMapping(value = "/detect", method = RequestMethod.POST)
    String detect(@RequestParam("name") String name, @RequestParam("file") MultipartFile file) {

        return null;
    }


    @RequestMapping(value = "/train", method = RequestMethod.POST)
    public String train(@RequestParam("file") MultipartFile file) {

        return null;
    }

}
