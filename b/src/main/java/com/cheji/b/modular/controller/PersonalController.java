package com.cheji.b.modular.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Api("个人中心")
@Controller
@RequestMapping("/personal")
public class PersonalController {

    @RequestMapping("/down")
    public String downPage() {
        return "aboutMe";
    }
}
