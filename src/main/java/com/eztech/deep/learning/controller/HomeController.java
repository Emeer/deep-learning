package com.eztech.deep.learning.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home controller.
 *
 * @author Jia ZHOU
 */
@RestController
public class HomeController {

    /**
     * @return
     */
    @RequestMapping("/")
    public String index() {
        return "Hello, spring big data!";
    }

}
