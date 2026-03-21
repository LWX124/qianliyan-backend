package com.cheji.b.modular.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
public class PageController {

    private Logger logger = LoggerFactory.getLogger(PageController.class);

    @GetMapping("/agreement")
    public String agreement() {
        return "agreement";
    }

    @GetMapping("/register")
    public String register() {
        return "onlineRegister";
    }

    @GetMapping("/guide")
    public String guide() {
        return "guide";
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "privacy";
    }

    @GetMapping("/userAgreement")
    public String userAgreement() {
        return "userAgreement";
    }

}
