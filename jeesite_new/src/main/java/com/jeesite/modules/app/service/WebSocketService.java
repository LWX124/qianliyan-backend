package com.jeesite.modules.app.service;

import com.jeesite.modules.websocket.MyHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;

@Service
public class WebSocketService {

    /**
     * 广播信息
     *
     * @param message
     * @return
     */
    public boolean sendMessageToAllUsers(TextMessage message) {
        boolean allSendSuccess = true;
        Set<String> clientIds = MyHandler.users.keySet();
        WebSocketSession session = null;
        for (String clientId : clientIds) {
            try {
                session = MyHandler.users.get(clientId);
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                allSendSuccess = false;
            }
        }
        return allSendSuccess;
    }
}
