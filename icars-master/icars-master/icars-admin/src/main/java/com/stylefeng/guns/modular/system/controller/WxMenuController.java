package com.stylefeng.guns.modular.system.controller;

import com.stylefeng.guns.modular.system.service.impl.WxService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/menu")
public class WxMenuController extends BaseController {

    @Resource(name = "styleFengWxService")
    private WxService wxService;




}
