package com.stylefeng.guns.netty;

import com.stylefeng.guns.modular.system.constant.Location;
import com.stylefeng.guns.modular.system.constant.WxSession;
import com.stylefeng.guns.modular.system.model.BizWxUser;
import com.stylefeng.guns.modular.system.service.impl.WxService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kosan
 * @description
 * @date 2018-03-26 14:36
 */
public class Constant {
    //存放所有的ChannelHandlerContext
    public static Map<String, ChannelHandlerContext> pushCtxMap = new ConcurrentHashMap<String, ChannelHandlerContext>();

    //存放某一类的channel
    public static ChannelGroup aaChannelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    //缓存实时定位信息
    public static Map<String, Location> gisLocation = new ConcurrentHashMap<>();

    //存放4S店websocket连接上下文
    public static Map<String, ChannelHandlerContext> pushFSCtxMap = new ConcurrentHashMap<String, ChannelHandlerContext>();

    //存放内部员工websocket PC连接上下文
    public static ChannelGroup pushInnerChannelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    //存放app员工websocket的链接上下文
    public static Map<String, ChannelHandlerContext> pushappChannelGroup = new ConcurrentHashMap<String, ChannelHandlerContext>();

    private static final Logger LOGGER = LoggerFactory
            .getLogger(Constant.class);


    public static void freshLocation(Location lo) {
        if (gisLocation.size() == 0) {
            gisLocation.put(lo.getAccount(), lo);
            return;
        }
        Iterator<Map.Entry<String, Location>> it = gisLocation.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Location> var = it.next();
            if (var.getKey().equals(lo.getAccount())) {
                var.getValue().setLat(lo.getLat());
                var.getValue().setLng(lo.getLng());
                var.getValue().setThirdSessionKey(lo.getThirdSessionKey());
                break;
            } else {
                gisLocation.put(lo.getAccount(), lo);
                break;
            }
        }
    }

    @Resource(name="styleFengWxService")
    private WxService wxService;

    public boolean removeLocation(String thirdSessionKey) {
        if (StringUtils.isEmpty(thirdSessionKey) || gisLocation.size() == 0) {
            return false;
        }

        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        BizWxUser bizWxUser = wxSession.getBizWxUser();
        if (wxSession != null && bizWxUser != null && org.apache.commons.lang.StringUtils.isNotEmpty(bizWxUser.getAccount())) {
            Location removeObj = gisLocation.remove(bizWxUser.getAccount());
            LOGGER.error("### 删除websocket 成功！");
            return removeObj == null ? false : true;
        }
        return false;
    }
}
