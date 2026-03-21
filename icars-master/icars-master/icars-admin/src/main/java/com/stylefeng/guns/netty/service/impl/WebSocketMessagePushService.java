package com.stylefeng.guns.netty.service.impl;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.modular.system.constant.Location;
import com.stylefeng.guns.modular.system.service.impl.AccdServiceImpl;
import com.stylefeng.guns.modular.system.vo.AccidentVo;
import com.stylefeng.guns.netty.BaseWebSocketServerHandler;
import com.stylefeng.guns.netty.Constant;
import com.stylefeng.guns.netty.service.IWebSocketMessagePushService;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @author kosan
 * @description
 * @date 2018-03-28 15:16
 */
@Service
public class WebSocketMessagePushService implements IWebSocketMessagePushService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccdServiceImpl.class);

    @Override
    public void  push(Serializable obj, String thirdSessionKey) {
        if(obj == null || !(obj instanceof AccidentVo)){
            return;
        }
        AccidentVo accidentVo = (AccidentVo)obj;
        BaseWebSocketServerHandler.push(Constant.pushCtxMap.get(thirdSessionKey), JSON.toJSONString(accidentVo));
    }

    @Override
    public boolean pushFS(Serializable obj, String account) {
        if(obj == null || !(obj instanceof AccidentVo)){
            return false;
        }
        ChannelHandlerContext ctx = Constant.pushFSCtxMap.get(account);
        if(ctx == null){
            return false;
        }
        AccidentVo accidentVo = (AccidentVo)obj;
        BaseWebSocketServerHandler.push(ctx, JSON.toJSONString(accidentVo));
        return true;
    }

    @Override
    public boolean pushOpenClaimToFS(Serializable obj, String account) {
        if(obj == null){
            return false;
        }
        ChannelHandlerContext ctx = Constant.pushFSCtxMap.get(account);
        if(ctx == null){
            return false;
        }
        BaseWebSocketServerHandler.push(ctx, JSON.toJSONString(obj));
        return true;
    }

    @Override
    public boolean pushClaims(Serializable obj, String account) {
        if(obj == null || !(obj instanceof AccidentVo)){
            return false;
        }
        AccidentVo accidentVo = (AccidentVo)obj;
        Location lo = Constant.gisLocation.get(account);
        if(lo == null){
            LOGGER.info("事故id: "+accidentVo.getId()+",账户："+account+",手工推送websocket失败，未查找到websocket链接信息");
            return false;
        }
        LOGGER.info("事故id: "+accidentVo.getId()+",推送至："+account);
        //推送websocket消息至理赔顾问
        this.push(obj, lo.getThirdSessionKey());
        return true;
    }

    @Override
    public void pushInner(Serializable obj) {
        if(obj == null || !(obj instanceof AccidentVo)){
            return;
        }
        AccidentVo accidentVo = (AccidentVo)obj;
        BaseWebSocketServerHandler.push(Constant.pushInnerChannelGroup, JSON.toJSONString(accidentVo));
    }

    @Override
    public void pushInnerForClaim(Serializable obj) {
        if(obj == null ){
            return;
        }
        BaseWebSocketServerHandler.push(Constant.pushInnerChannelGroup, JSON.toJSONString(obj));
    }
}
