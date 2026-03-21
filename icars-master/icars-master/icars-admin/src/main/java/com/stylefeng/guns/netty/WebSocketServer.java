package com.stylefeng.guns.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author kosan
 * @description
 * @date 2018-03-26 14:37
 */
@Component
public class WebSocketServer{
    private static final Logger LOGGER = LoggerFactory
            .getLogger(WebSocketServer.class);
    //用于客户端连接请求
    private EventLoopGroup bossGroup = new NioEventLoopGroup();

    //用于处理客户端I/O操作
    private EventLoopGroup workerGroup  = new NioEventLoopGroup();;

    //服务器的辅助启动类
    private ServerBootstrap serverBootstrap = new ServerBootstrap();;

    //BS的I/O处理类
    private ChannelHandler childChannelHandler;

    private ChannelFuture channelFuture;

    //服务端口
    @Value("${server.websocket.port}")
    private String port;

    public WebSocketServer(){

        LOGGER.info("WebSocketServer 初始化");
    }


    public void bulid(int port) throws Exception{

        try {

            //（1）boss辅助客户端的tcp连接请求  worker负责与客户端之前的读写操作
            //（2）配置客户端的channel类型
            //(3)配置TCP参数，握手字符串长度设置
            //(4)TCP_NODELAY是一种算法，为了充分利用带宽，尽可能发送大块数据，减少充斥的小块数据，true是关闭，可以保持高实时性,若开启，减少交互次数，但是时效性相对无法保证
            //(5)开启心跳包活机制，就是客户端、服务端建立连接处于ESTABLISHED状态，超过2小时没有交流，机制会被启动
            //(6)netty提供了2种接受缓存区分配器，FixedRecvByteBufAllocator是固定长度，但是拓展，AdaptiveRecvByteBufAllocator动态长度
            //(7)绑定I/O事件的处理类,WebSocketChildChannelHandler中定义
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(592048))
                    .childHandler(new WebSocketChildChannelHandler());


            channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();

            System.out.println("websocket 监听成功，端口："+port);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }

    }

    //执行之后关闭
    @PreDestroy
    public void close(){
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }


    public EventLoopGroup getBossGroup() {
        return bossGroup;
    }

    public void setBossGroup(EventLoopGroup bossGroup) {
        this.bossGroup = bossGroup;
    }

    public EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

    public void setWorkerGroup(EventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }

    public ServerBootstrap getServerBootstrap() {
        return serverBootstrap;
    }

    public void setServerBootstrap(ServerBootstrap serverBootstrap) {
        this.serverBootstrap = serverBootstrap;
    }

    public ChannelHandler getChildChannelHandler() {
        return childChannelHandler;
    }

    public void setChildChannelHandler(ChannelHandler childChannelHandler) {
        this.childChannelHandler = childChannelHandler;
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    public void setChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }


    @PostConstruct()
    public void init(){
        //需要开启一个新的线程来执行netty server 服务器
        new Thread(new Runnable() {
            public void run() {
                try {
                    bulid(Integer.parseInt(port));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
