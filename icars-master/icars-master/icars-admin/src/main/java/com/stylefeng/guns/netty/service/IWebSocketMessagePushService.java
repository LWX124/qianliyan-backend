package com.stylefeng.guns.netty.service;

import java.io.Serializable;

/**
 * @author kosan
 * @description
 * @date 2018-03-28 15:14
 */
public interface IWebSocketMessagePushService {
    void push(Serializable obj, String thirdSessionKey);

    boolean pushFS(Serializable obj, String account);

    boolean pushOpenClaimToFS(Serializable obj, String account);

    boolean pushClaims(Serializable obj, String account);

    void pushInner(Serializable obj);

    void pushInnerForClaim(Serializable obj);
}
