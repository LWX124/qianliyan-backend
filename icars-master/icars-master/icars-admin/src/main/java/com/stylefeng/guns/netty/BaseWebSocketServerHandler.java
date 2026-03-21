package com.stylefeng.guns.netty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @author kosan
 * @description 发消息方式 抽象出来
 * @date 2018-03-26 14:42
 */
public abstract class BaseWebSocketServerHandler extends SimpleChannelInboundHandler<Object>{

    /**
     * 推送单个
     *
     * */
    public static final void push(final ChannelHandlerContext ctx,final String message){
        TextWebSocketFrame tws = new TextWebSocketFrame(message);
        ChannelFuture cf = ctx.channel().writeAndFlush(tws);
    }
    /**
     * 群发
     *
     * */
    public static final void push(final ChannelGroup ctxGroup,final String message){
        TextWebSocketFrame tws = new TextWebSocketFrame(message);
        ChannelGroupFuture cgf =  ctxGroup.writeAndFlush(tws);
    }
}
