package com.cheji.web.modular.service;

import com.cheji.web.modular.websocket.MyHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

@Service
public class WebSocketService {

    private MyHandler myHandler = new MyHandler();
    /**
     * 广播信息
     *
     * @param message
     * @return
     */
    public boolean sendMessageToAllUsers(TextMessage message) {
        return myHandler.sendMessageToAllUsers(message);
    }

    /**
     * 发送信息给指定用户
     *
     * @param clientId //id + carId
     * @param message
     * @return
     */
    public boolean sendMessageToUser(String clientId, TextMessage message) {
      return myHandler.sendMessageToUser(clientId,message);
    }
}
