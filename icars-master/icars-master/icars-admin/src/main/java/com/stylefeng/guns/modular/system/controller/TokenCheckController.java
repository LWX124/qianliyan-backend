package com.stylefeng.guns.modular.system.controller;

import com.stylefeng.guns.modular.system.service.impl.WxService;
import com.stylefeng.guns.modular.system.utils.TokenCheckUtils;
import com.stylefeng.guns.modular.system.utils.XMLUtils;
import com.stylefeng.guns.modular.system.vo.ReceiveMsg;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: weg
 * @description: 微信公众号平台token验证
 * @author: lvyq
 * @create: 2023-03-07 14:49
 **/
@RequestMapping
@Controller
@CrossOrigin
public class TokenCheckController {

    private final static String token = "AshesToken";

    @Resource(name = "styleFengWxService")
    private WxService wxService;



}