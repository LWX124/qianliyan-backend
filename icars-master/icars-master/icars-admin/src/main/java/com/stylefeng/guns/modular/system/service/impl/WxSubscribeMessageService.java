package com.stylefeng.guns.modular.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.stylefeng.guns.config.properties.WxAuthProperties;
import com.stylefeng.guns.core.util.HttpRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class WxSubscribeMessageService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "styleFengWxService")
    private WxService wxService;

    @Resource
    private WxAuthProperties wxAuthProperties;

    public void sendApprovalNotice(String openid, BigDecimal amount, String nickname) {
        String templateId = wxAuthProperties.getSubscribe().getTemplateId();
        if (StringUtils.isEmpty(templateId)) {
            log.warn("订阅消息模板ID未配置，跳过发送");
            return;
        }
        try {
            String accessToken = wxService.getXcxAccessToken();
            if (StringUtils.isEmpty(accessToken)) {
                log.error("获取access_token失败，无法发送订阅消息");
                return;
            }
            String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken;

            JSONObject data = new JSONObject();
            // 审核时间
            JSONObject date1 = new JSONObject();
            date1.put("value", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            data.put("date1", date1);
            // 审核状态
            JSONObject phrase2 = new JSONObject();
            phrase2.put("value", "审核成功");
            data.put("phrase2", phrase2);
            // 客户名称
            JSONObject name3 = new JSONObject();
            name3.put("value", StringUtils.isNotEmpty(nickname) ? nickname : "用户");
            data.put("name3", name3);
            // 活动名称
            JSONObject thing4 = new JSONObject();
            thing4.put("value", "审核成功，领取奖励");
            data.put("thing4", thing4);

            JSONObject body = new JSONObject();
            body.put("touser", openid);
            body.put("template_id", templateId);
            body.put("page", "pages/upload-history/upload-history");
            body.put("data", data);

            String res = HttpRequest.sendPost(url, body.toJSONString());
            log.info("订阅消息发送结果: openid={}, res={}", openid, res);

            if (StringUtils.isNotEmpty(res)) {
                JSONObject resJson = JSONObject.parseObject(res);
                int errcode = resJson.getIntValue("errcode");
                if (errcode != 0) {
                    log.warn("订阅消息发送失败: errcode={}, errmsg={}", errcode, resJson.getString("errmsg"));
                }
            }
        } catch (Exception e) {
            log.error("发送订阅消息异常: openid={}", openid, e);
        }
    }
}
