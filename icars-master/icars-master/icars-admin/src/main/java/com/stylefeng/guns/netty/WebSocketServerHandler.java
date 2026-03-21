package com.stylefeng.guns.netty;

import com.alibaba.fastjson.JSONObject;
import com.stylefeng.guns.modular.system.constant.Location;
import com.stylefeng.guns.modular.system.constant.WxSession;
import com.stylefeng.guns.modular.system.model.BizWxUser;
import com.stylefeng.guns.modular.system.service.impl.WxService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

/**
 * @author kosan
 * @description websocket 具体业务处理方法
 * @date 2018-03-26 14:39
 */
@Service
@Sharable
public class WebSocketServerHandler extends BaseWebSocketServerHandler {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(WebSocketServerHandler.class);

    private WebSocketServerHandshaker handshaker;

    @Resource(name="styleFengWxService")
    private WxService wxService;

    /**
     * 当客户端连接成功，返回个成功信息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        push(ctx, "连接成功");
//        push(Constant.aaChannelGroup,"群发");
    }

    /**
     * 当客户端断开连接
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub

        Iterator<Map.Entry<String, ChannelHandlerContext>> it = Constant.pushCtxMap.entrySet().iterator();

        while(it.hasNext()){
            Map.Entry<String, ChannelHandlerContext> aa = it.next();
            if (ctx.equals(aa.getValue())) {
                //从连接池内剔除
                LOGGER.info("websocket连接池大小：" + Constant.pushCtxMap.size());
                it.remove();
                LOGGER.info("剔除" + aa.getKey());
                //从位置连接池内剔除
                LOGGER.info("location websocket连接池大小：" + Constant.gisLocation.size());
                String thirdSessionKey = aa.getKey();
                Constant constant = new Constant();
                boolean flag = constant.removeLocation(thirdSessionKey);
                if(flag){
                    LOGGER.info("剔除" + aa.getKey());
                }
                return;
            }
        }
//        Constant.aaChannelGroup.remove(ctx);

        Iterator<Map.Entry<String, ChannelHandlerContext>> fsIt = Constant.pushFSCtxMap.entrySet().iterator();

        while(fsIt.hasNext()){
            Map.Entry<String, ChannelHandlerContext> bb = fsIt.next();
            if (ctx.equals(bb.getValue())) {
                //从连接池内剔除
                LOGGER.info("4s 店 websocket连接池大小：" + Constant.pushFSCtxMap.size());
                LOGGER.info("剔除" + bb.getKey());
                fsIt.remove();
                return;
            }
        }
        Constant.pushInnerChannelGroup.remove(ctx);



    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        ctx.flush();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        // TODO Auto-generated method stub

        //http：//xxxx
        if (msg instanceof FullHttpRequest) {

            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            //ws://xxxx
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }


    }


    public void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        //关闭请求
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        //ping请求
        if (frame instanceof PingWebSocketFrame) {

            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));

            return;
        }
        //只支持文本格式，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {

            throw new Exception("仅支持文本格式");
        }

        //客服端发送过来的消息

        String request = ((TextWebSocketFrame) frame).text();
        LOGGER.info("Websocket服务端收到客户端消息：" + request);
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(request);
            System.out.println(jsonObject.toJSONString());
        } catch (Exception e) {
            LOGGER.error("websocket请求消息转化JSON异常", e);
        }
        if (jsonObject == null) {
            return;
        }
        String thirdSessionKey = (String) jsonObject.get("thirdSessionKey");
        if (StringUtils.isNotEmpty(thirdSessionKey)) {
            WxSession wxSession = wxService.getWxSession(thirdSessionKey);
            BizWxUser bizWxUser = wxSession.getBizWxUser();
            if (wxSession != null && bizWxUser != null && StringUtils.isNotEmpty(bizWxUser.getAccount())) {
                if (jsonObject.get("lng") != null && jsonObject.get("lat") != null) {
                    BigDecimal lng = new BigDecimal(jsonObject.get("lng").toString());
                    BigDecimal lat = new BigDecimal(jsonObject.get("lat").toString());
                    Location lo = new Location(bizWxUser.getAccount(), thirdSessionKey, lat, lng);
                    Constant.freshLocation(lo);
                } else {
                    LOGGER.info("添加到websocket连接池,验证信息：" + request + ",员工账号：" + bizWxUser.getAccount());
                    Constant.pushCtxMap.put(thirdSessionKey, ctx);
                }
            }
        }else{
            //app端websocket的处理
            String thirdSessionKeyForApp = (String) jsonObject.get("account");

                Constant.pushappChannelGroup.put(thirdSessionKeyForApp, ctx);

        }
        if(jsonObject.get("deptid") != null && jsonObject.get("account") != null){
            if(Integer.parseInt(jsonObject.get("deptid").toString()) == 30){
                LOGGER.info("添加到内部员工 websocket PC连接池,验证信息：" + request + ",员工账号：" + jsonObject.get("account"));
                Constant.pushInnerChannelGroup.add(ctx.channel());
            } else {
                LOGGER.info("添加到4s 店 websocket连接池,验证信息：" + request + ",员工账号：" + jsonObject.get("account"));
                Constant.pushFSCtxMap.put(jsonObject.get("account").toString(), ctx);
            }
        }
    }

    //第一次请求是http请求，请求头包括ws的信息
    public void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        if (!req.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws:/" + ctx.channel() + "/websocket", null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            //不支持
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }


    public static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {


        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }

        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }

    }

    private static boolean isKeepAlive(FullHttpRequest req) {
        return false;
    }


    //异常处理，netty默认是关闭channel
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        // TODO Auto-generated method stub
        //输出日志
        cause.printStackTrace();
        ctx.close();
    }
}
