package com.stylefeng.guns.modular.system.Job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.config.properties.SysProperties;
import com.stylefeng.guns.core.util.JedisUtil;
import com.stylefeng.guns.huanxin.HuanXinUtil;
import com.stylefeng.guns.modular.system.constant.RedisKey;
import com.stylefeng.guns.modular.system.constant.SysActive;
import com.stylefeng.guns.modular.system.model.Dept;
import com.stylefeng.guns.modular.system.model.User;
import com.stylefeng.guns.modular.system.service.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 单机定时任务控制中心
 */
@Component
public class BizJobController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IBizAlipayBillService bizAlipayBillService;

    @Resource
    private IBizWxpayBillService bizWxpayBillService;

    @Resource
    private DataCountService dataCountService;

    @Resource
    private IUserService iUserService;

    @Resource
    private IDeptService iDeptService;

    @Resource
    private SysProperties sysProperties;

    @Resource
    private JedisUtil jedisUtil;

    @Resource
    private HuanXinUtil huanXinUtil;

    //@Scheduled(cron = "0 0 3 * * ?")//每天凌晨3点
    //@Scheduled(cron = "0 0/1 * * * ?")//每隔一分钟
//    @Scheduled(cron = "0 0 0/1 * * ?")//每隔1小时
//    public void cleanWxSessionCache() {
//        log.info("开始清理微信用户过期缓存");
//        if (sysProperties.getActive().equals(SysActive.PRO)) {
//            wxService.clean();
//        }
//    }

    /**
     * 定时对支付失败记录发起重新支付
     */
    //@Scheduled(cron = "0 0 3 * * ?")//每天凌晨3点
//    @Scheduled(cron = "0 0/1 * * * ?")//每隔一分钟
//    @Scheduled(cron = "0 0 0/1 * * ?")//每隔1小时
//    public void rePay(){
//        log.info("对支付失败记录发起重新支付----开始");
//        bizAlipayBillService.rePay();
//        log.info("对支付失败记录发起重新支付----结束");
//    }

//    @Scheduled(cron = "0 0 3 * * ?")//每天凌晨3点
    @Scheduled(cron = "0 0/10 * * * ?")//每隔一分钟
//    @Scheduled(cron = "0 0 0/10 * * ?")//每隔1小时
    public void wxRePay() {
        if (!sysProperties.getActive().equals(SysActive.PRO)) {
            return;
        }
        log.info("微信支付----重新支付----开始");
        if (sysProperties.getActive().equals(SysActive.PRO)) {
            bizWxpayBillService.wxRePay();
        }
        log.info("微信支付----重新支付----结束");
    }

    @Scheduled(cron = "0 0/5 * * * ?")//每隔5分钟
    public void intitHuanxinFriend() {
        if (!sysProperties.getActive().equals(SysActive.PRO)) {
            return;
        }

        log.info("初始化环信好友----开始");
        JSONArray entities = huanXinUtil.findAllUser().getJSONArray("entities");
        EntityWrapper<User> wrapper = new EntityWrapper<>();
        User user = new User();

        JSONArray resultList = new JSONArray();
        JSONArray resultListLP = new JSONArray();
        JSONArray resultList4S = new JSONArray();
        for (int i = 0; i < entities.size(); i++) {
            JSONObject obj = entities.getJSONObject(i);
            String account = obj.getString("username");
            user.setAccount(account);
            wrapper.setEntity(user);
            User resultUser = iUserService.selectOne(wrapper);
            if (resultUser == null || resultUser.getDeptid() == null || resultUser.getName() == null) {
                continue;
            }
            Dept dept = iDeptService.selectById(resultUser.getDeptid());
            if (dept == null || StringUtils.isEmpty(dept.getSimplename())) {
                continue;
            }
            JSONObject resultObj = new JSONObject();
            resultObj.put("account", resultUser.getAccount());
            resultObj.put("deptid", resultUser.getDeptid());
            resultObj.put("nickName", resultUser.getName() + "-" + dept.getSimplename().replace("爱车士汽车服务","四川车己汽车科技"));
            if (resultUser.getHeadImg() == null) {
                resultUser.setHeadImg("");
            }
            resultObj.put("avatar", resultUser.getHeadImg());
            resultList.add(resultObj);
            if (resultUser.getRoleid().equals("7") || resultUser.getRoleid().equals("8")) {
                resultListLP.add(resultObj);
            }else {
                resultList4S.add(resultObj);
            }
        }
        jedisUtil.set(RedisKey.HUANXIN_FRIEND_LIST, resultList.toJSONString());
        jedisUtil.set(RedisKey.HUANXIN_FRIEND_LIST_LP, resultListLP.toJSONString());
        jedisUtil.set(RedisKey.HUANXIN_FRIEND_LIST_4S, resultList4S.toJSONString());

        log.info("初始化环信好友----结束");
    }


}
