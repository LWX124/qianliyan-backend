package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.cwork.Personal;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.excep.CusException;
import com.cheji.web.modular.mapper.AppUserAccountRecordMapper;
import com.cheji.web.modular.mapper.AppYearCheckIndentMapper;
import com.cheji.web.modular.mapper.BUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 年检订单表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2020-04-02
 */
@Service
public class AppYearCheckIndentService extends ServiceImpl<AppYearCheckIndentMapper, AppYearCheckIndentEntity> implements IService<AppYearCheckIndentEntity> {

    private Logger logger = LoggerFactory.getLogger(AppYearCheckIndentService.class);


    @Resource
    private AppYearCheckIndentMapper appYearCheckIndentMapper;

    @Resource
    private BUserMapper bUserMapper;

    @Resource
    private AppUserAccountRecordMapper appUserAccountRecordMapper;

    @Resource
    private AppUserBMessageService appUserBMessageService;

    @Resource
    private BUserService bUserService;

    @Resource
    private AppUserCouponService appUserCouponService;

    public Personal findIndentCount(String userId) {
        return appYearCheckIndentMapper.findIndentCount(userId);
    }

//    @Transactional(rollbackFor = Exception.class)
//    public void merchantsAddBanlance(AppYearCheckIndentEntity appYearCheckIndentEntity) {
//       // JSONObject result = new JSONObject();
//        //行锁
//        BUserEntity bUser = bUserMapper.updateBalance(appYearCheckIndentEntity.getUserBId().toString());
//        //给b端商户加钱
//        BigDecimal price = appYearCheckIndentEntity.getPrice();
//
//        //查询是否使用优惠卷
//        EntityWrapper<AppUserCouponEntity> couponWrapper = new EntityWrapper<>();
//        couponWrapper.eq("order_number", appYearCheckIndentEntity.getYearCheckNumber());
//        AppUserCouponEntity appUserCouponEntity = appUserCouponService.selectOne(couponWrapper);
//        if (appUserCouponEntity == null) {
//            BigDecimal multiply = price.multiply(new BigDecimal(0.8).setScale(2, BigDecimal.ROUND_HALF_UP));
//            bUser.setBalance(bUser.getBalance().add(multiply));
//
//            AppUserAccountRecordEntity appUserAccount = new AppUserAccountRecordEntity();
//            appUserAccount.setMomey(multiply);
//            appUserAccount.setUserId(appYearCheckIndentEntity.getUserBId());
//            appUserAccount.setType(17);
//            appUserAccount.setCreateTime(new Date());
//            appUserAccount.setAddFlag(1);
//            appUserAccount.setSource(1);
//            appUserAccount.setBusinessId(appYearCheckIndentEntity.getYearCheckNumber());
//            appUserAccountRecordMapper.insert(appUserAccount);
//
//            AppUserAccountRecordEntity chejiBalance = new AppUserAccountRecordEntity();
//            chejiBalance.setMomey(price.subtract(multiply));
//            chejiBalance.setUserId(appYearCheckIndentEntity.getUserBId());
//            chejiBalance.setType(-1);
//            chejiBalance.setCreateTime(new Date());
//            chejiBalance.setAddFlag(1);
//            chejiBalance.setSource(1);
//            chejiBalance.setBusinessId(appYearCheckIndentEntity.getYearCheckNumber());
//            appUserAccountRecordMapper.insert(chejiBalance);
//        }else {
//            AppUserAccountRecordEntity appUserAccount = new AppUserAccountRecordEntity();
//            appUserAccount.setMomey(price);
//            appUserAccount.setUserId(appYearCheckIndentEntity.getUserBId());
//            appUserAccount.setType(17);
//            appUserAccount.setCreateTime(new Date());
//            appUserAccount.setAddFlag(1);
//            appUserAccount.setSource(1);
//            appUserAccount.setBusinessId(appYearCheckIndentEntity.getYearCheckNumber());
//            appUserAccountRecordMapper.insert(appUserAccount);
//        }
//        bUserMapper.updateById(bUser);
//
//
//        //修改订单状态
//        appYearCheckIndentEntity.setState(3);
//        updateById(appYearCheckIndentEntity);
//        //mq
//
//        //救援
//        Message sendMsg = new Message("all", "jgts_NJ", appYearCheckIndentEntity.getYearCheckNumber().getBytes());
//        //默认3秒超时
//        SendResult sendResult = null;
//        try {
//            sendResult = mqProducer.send(sendMsg);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    public List<Integer> findNoPayIndent(Integer userid) {
        return appYearCheckIndentMapper.findNoPayIndent(userid);
    }

    public JSONObject finfYearIndentMessage(AppYearCheckIndentEntity appYearCheckIndent) {
        JSONObject json = new JSONObject();
        Integer userBId = appYearCheckIndent.getUserBId();

        //查询年检技师
        EntityWrapper<AppUserBMessageEntity> appUserBMessageWrapper = new EntityWrapper<>();
        appUserBMessageWrapper.eq("user_b_id",userBId)
                .eq("wrok_type",2);

        AppUserBMessageEntity appUserBMessageEntity = appUserBMessageService.selectOne(appUserBMessageWrapper);
        if (appUserBMessageEntity==null){
            return null;
        }
        appUserBMessageEntity.setOrderNumber(appUserBMessageEntity.getOrderNumber()+1);
        appUserBMessageService.updateById(appUserBMessageEntity);
        json.put("id",appYearCheckIndent.getId());
        json.put("state",appYearCheckIndent.getState());
        //技师id
        json.put("technicianId",appUserBMessageEntity.getId());
        json.put("technicianPhone",appUserBMessageEntity.getPhone());
        //技师经纬度
        json.put("technicianLat",appUserBMessageEntity.getLat());
        json.put("technicianLng",appUserBMessageEntity.getLng());
        json.put("technicianAddress",appUserBMessageEntity.getWorkPlace());
        //商户经纬度
        BUserEntity bUserEntity = bUserService.selectById(userBId);
        json.put("merchantsLat",bUserEntity.getLat());
        json.put("merchantsLng",bUserEntity.getLng());
        json.put("merchantsAddress",bUserEntity.getAddress());
        json.put("merchantsId",bUserEntity.getId());

        json.put("headImg",appUserBMessageEntity.getHeadImg());

        json.put("name",appUserBMessageEntity.getName());

        json.put("serialNumber",appUserBMessageEntity.getSerialNumber());

        json.put("technologyYear",appUserBMessageEntity.getTechnologyYear());

        json.put("driverYear",appUserBMessageEntity.getDriverYear());

        json.put("score",appUserBMessageEntity.getScore());

        json.put("merchantsPhone",bUserEntity.getMerchantsPhone());

        return json;


    }


//    @Transactional(rollbackFor = Exception.class)
//    public void cancelYearCheckOrder(String orderNumber, AppYearCheckIndentEntity appYearCheckIndent) throws CusException {
//        logger.info("取消救援开始发送消息：" + orderNumber);
//        Message sendMsg = new Message("all", "yearCheckCancelOrder", orderNumber.getBytes());
//        SendResult sendResult = null;
//        try {
//            sendResult = mqProducer.send(sendMsg);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//        if (sendResult == null) {
//            throw new CusException(405, "取消失败，请联系客服。");
//        }
//        logger.info("消息发送响应信息：" + sendResult.toString());
//
//        if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
//            appYearCheckIndent.setState(4);
//            updateById(appYearCheckIndent);
//        } else {
//            logger.error("消息发送失败 响应信息：{};cleanIndetEntity={}", sendResult.toString(), appYearCheckIndent);
//        }
//    }

    public AppYearCheckIndentEntity selectByYearNumber(String orderNumber) {
        return appYearCheckIndentMapper.selectByYearNumber(orderNumber);
    }
}
