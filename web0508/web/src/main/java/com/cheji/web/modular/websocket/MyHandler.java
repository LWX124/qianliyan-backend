package com.cheji.web.modular.websocket;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MyHandler implements WebSocketHandler {
    private final static Logger logger = LoggerFactory.getLogger(MyHandler.class);

    //    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        Map<String, String> map = JSONObject.parseObject(payload, HashMap.class);
//        System.out.println("=====接受到的数据" + map);
//        session.sendMessage(new TextMessage("服务器返回收到的信息," + payload));
//    }
//在线用户列表
    private static final ConcurrentHashMap<String, WebSocketSession> users;

    static {
        users = new ConcurrentHashMap<>();
    }

    //新增socket
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info(" 成功建立连接  ");
        String ID = session.getUri().toString().split("ID=")[1];
        if (ID != null) {
            users.put(ID, session);
            session.sendMessage(new TextMessage("成功建立socket连接"));
        }
//        System.out.println("当前在线人数：" + users.size());
        logger.info("当前在线人数: " + users.size());
    }

    //接收socket信息
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        try {
            JSONObject jsonobject = JSONObject.fromObject(webSocketMessage.getPayload());
            if (jsonobject.getString("message").startsWith("HeartBeat")) {
                logger.info("##长连接收到来自：{} 的心跳;消息={}", jsonobject.get("id"), jsonobject.get("message"));
                sendMessageToUser(jsonobject.get("id") + "", new TextMessage("HeartBeat"));
                return;
            }
            logger.info("##长连接收到来自：{} 的消息;消息内容={}", jsonobject.get("id"), jsonobject.get("message"));
            sendMessageToUser(jsonobject.get("id") + "", new TextMessage("收到!"));
        } catch (Exception e) {
            logger.info("####webSocketMessage.getPayload()={}===", webSocketMessage.getPayload());
            e.printStackTrace();
        }
    }

    /**
     * 发送信息给指定用户
     *
     * @param clientId //id + carId
     * @param message
     * @return
     */
    public boolean sendMessageToUser(String clientId, TextMessage message) {
        WebSocketSession session = users.get(clientId);
        if (session == null) {
            logger.info("### 发送信息给指定用户失败，用户已下线。 clientId={}；message={}", clientId, message);
            return false;
        }
        logger.info("### 发送信息给指定用户。 clientId={};session={};message={}", clientId, session, message);
        if (!session.isOpen()) return false;
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

//    public boolean sendMessageBySession(WebSocketSession session, TextMessage message) {
//        if (session == null) {
//            logger.info("### 发送信息给指定用户失败，用户已下线。 message={}", message);
//            return false;
//        }
//        logger.info("### 发送信息给指定用户。 session={};message={}", session, message);
//        if (!session.isOpen()) return false;
//        try {
//            session.sendMessage(message);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }


    /**
     * 广播信息
     *
     * @param message
     * @return
     */
    public boolean sendMessageToAllUsers(TextMessage message) {
        boolean allSendSuccess = true;
        Set<String> clientIds = users.keySet();
        WebSocketSession session = null;
        for (String clientId : clientIds) {
            try {
                session = users.get(clientId);
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


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        System.out.println("连接出错");
        users.remove(getClientId(session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("连接已关闭：" + status);
        users.remove(getClientId(session));
        System.out.println(users);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 获取用户标识
     *
     * @param session
     * @return
     */
    private String getClientId(WebSocketSession session) {
        try {
            String id = "";
            String path = session.getUri().getPath();
            if (StringUtils.isNotEmpty(path)) {
                String[] split = path.split("=");
                if (split.length >= 2) {
                    id = split[1];
                }
            }
            return id;
//            Integer clientId = (Integer) session.getAttributes().get("WEBSOCKET_USERID");
//            return clientId;
        } catch (Exception e) {
            return null;
        }
    }
}
