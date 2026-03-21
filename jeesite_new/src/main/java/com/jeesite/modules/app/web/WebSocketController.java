package com.jeesite.modules.app.web;

import com.jeesite.modules.app.service.WebSocketService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/api/socket")
public class WebSocketController {

    /**
     * 查询列表
     */
    @RequestMapping(value = {"page", ""})
    public String list() {
        return "modules/test/testWebSocket";
    }

    @Resource
    private WebSocketService webSocketService;

    @GetMapping("sendMsg")
    public void sendMsg() {
        webSocketService.sendMessageToAllUsers(new TextMessage("1"));

    }



}
