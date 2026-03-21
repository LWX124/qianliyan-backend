package com.cheji.b.modular.controller;

import com.cheji.b.modular.service.MerchantsLableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merchantsLable")
public class MerchantsLableController {
    @Autowired
    public MerchantsLableService merchantsLableService;


}
