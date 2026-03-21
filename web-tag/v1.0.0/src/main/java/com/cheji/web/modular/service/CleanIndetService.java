package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.cwork.Personal;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.excep.CusException;
import com.cheji.web.modular.mapper.AppOrderRollBackMapper;
import com.cheji.web.modular.mapper.CleanIndetMapper;
import com.cheji.web.modular.mapper.CleanPriceDetailMapper;
import com.cheji.web.util.GenerateDigitalUtil;
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
 * 洗车订单表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-12-13
 */
@Service
public class CleanIndetService extends ServiceImpl<CleanIndetMapper, CleanIndetEntity> implements IService<CleanIndetEntity> {


    private Logger logger = LoggerFactory.getLogger(CleanIndetService.class);

    @Resource
    private CleanPriceDetailService cleanPriceDetailService;

    @Resource
    private AppOrderRollBackMapper appOrderRollBackMapper;

    @Resource
    private AppBeautyPriceDetailService appBeautyPriceDetailService;

    @Resource
    private CleanIndetMapper cleanIndetMapper;


    @Resource
    private CleanPriceDetailMapper cleanPriceDetailMapper;

    @Resource
    private DefaultMQProducer mqProducer;

    @Resource
    private AppRescueIndentService appRescueIndentService;

    @Resource
    private AppSprayPaintIndentService appSprayPaintIndentService;

    @Resource
    private AppYearCheckIndentService appYearCheckIndentService;

    @Resource
    private AppSubstituteDrivingIndentService appSubstituteDrivingIndentService;

    @Resource
    private AppUserCouponService appUserCouponService;

    @Resource
    private AppCouponTypeService appCouponTypeService;

    @Resource
    private PushBillService pushBillService;

    @Resource
    private AppSendOutSheetService appSendOutSheetService;


    public JSONObject saveCleanIndent(String cleanType, String carType, BigDecimal amount, String userBId, String userid) {
        JSONObject result = new JSONObject();
        if (cleanType == null) {
            result.put("code", 400);
            result.put("data", "没有清洗类型");
            return result;
        }
        if (carType == null) {
            result.put("code", 400);
            result.put("data", "没有车型");
            return result;
        }
        if (amount == null) {
            result.put("code", 400);
            result.put("data", "没有支付金额");
            return result;
        }
        CleanIndetEntity cleanIndetEntity = new CleanIndetEntity();
        //保存订单
        cleanIndetEntity.setAmount(amount);
        cleanIndetEntity.setUserId(String.valueOf(userid));
        cleanIndetEntity.setCarType(Integer.valueOf(carType));
        cleanIndetEntity.setCleanType(Integer.valueOf(cleanType));
        cleanIndetEntity.setUserBId(userBId);
        String orderNo = GenerateDigitalUtil.getOrderNo();
        orderNo = "xc" + orderNo;
        cleanIndetEntity.setCleanIndentNumber(orderNo);
        cleanIndetEntity.setPayState("1");
        cleanIndetEntity.setCreateTime(new Date());
        cleanIndetEntity.setUpdateTime(new Date());
        insert(cleanIndetEntity);
        result.put("code", 200);
        result.put("mesg", "保存洗车订单成功");
        result.put("data", orderNo);
        return result;
    }

    public Integer findMoney(Integer type, JSONObject in, Integer messageType) {
        Integer amount = 0;
        if (type == 4) {
            String id = in.getString("id");
            CleanPriceDetailEntity cleanPriceDetailEntity = cleanPriceDetailService.selectById(id);
            amount = cleanPriceDetailEntity.getPreferentialPrice().intValue();
            return amount;
        }
        if (type == 5) {
            String id = in.getString("id");
            AppBeautyPriceDetailEntity beautyPriceDetailEntity = appBeautyPriceDetailService.selectById(id);
            amount = beautyPriceDetailEntity.getPreferentialPrice().multiply(new BigDecimal("100")).intValue();
            return amount;
        }
        if (type == 6) {
            String id = in.getString("id");
            AppRescueIndentEntity appRescueIndentEntity = appRescueIndentService.selectById(id);
            amount = appRescueIndentEntity.getPrice().multiply(new BigDecimal("100")).intValue();
            return amount;
        }
        if (type == 7) {
            Integer couponId = in.getInteger("couponId");
            String id = in.getString("id");
            AppSprayPaintIndentEntity appSprayPaintIndentEntity = appSprayPaintIndentService.selectById(id);
            amount = appSprayPaintIndentEntity.getPrice().multiply(new BigDecimal("100")).intValue();
            //有优惠卷id
            if (couponId != null && amount >= 60000) {
                AppUserCouponEntity appUserCouponEntity = appUserCouponService.selectById(couponId);
                if (appUserCouponEntity == null || appUserCouponEntity.getIsUse() == 1 || appUserCouponEntity.getCouponId() != 1) {
                    return null;
                }
                AppCouponTypeEntity appCouponTypeEntity = appCouponTypeService.selectById(appUserCouponEntity.getCouponId());
                appUserCouponEntity.setOrderNumber(appSprayPaintIndentEntity.getSprayPaintNumber());
                appUserCouponService.updateById(appUserCouponEntity);
                BigDecimal money = appCouponTypeEntity.getMoney();
                amount = amount - money.multiply(new BigDecimal("100")).intValue();
            }
            return amount;
        }
        if (type == 8) {
            String id = in.getString("id");
            Integer couponId = in.getInteger("couponId");
            AppYearCheckIndentEntity appYearCheckIndentEntity = appYearCheckIndentService.selectById(id);
            amount = appYearCheckIndentEntity.getPrice().multiply(new BigDecimal("100")).intValue();
            if (couponId != null && amount >= 3000) {
                AppUserCouponEntity appUserCouponEntity = appUserCouponService.selectById(couponId);
                if (appUserCouponEntity == null || appUserCouponEntity.getIsUse() == 1 || appUserCouponEntity.getCouponId() != 3) {
                    return null;
                }
                AppCouponTypeEntity appCouponTypeEntity = appCouponTypeService.selectById(appUserCouponEntity.getCouponId());
                appUserCouponEntity.setOrderNumber(appYearCheckIndentEntity.getYearCheckNumber());
                appUserCouponService.updateById(appUserCouponEntity);
                BigDecimal money = appCouponTypeEntity.getMoney();
                amount = amount - money.multiply(new BigDecimal("100")).intValue();
            }
            return amount;
        }
        if (type == 9) {
            String id = in.getString("id");
            AppSubstituteDrivingIndentEntity appSubstituteDrivingIndentEntity = appSubstituteDrivingIndentService.selectById(id);
            Integer waitTime = appSubstituteDrivingIndentEntity.getWaitTime();
            BigDecimal actualPrice = appSubstituteDrivingIndentEntity.getActualPrice();
            BigDecimal waitMoney;
            logger.error("###  代驾订单##  appSubstituteDrivingIndentEntity={}", appSubstituteDrivingIndentEntity);
            if (waitTime != null && waitTime > 5) {
                int i = waitTime - 5;
                waitMoney = BigDecimal.valueOf(i);
                actualPrice = waitMoney.add(waitMoney);
            }
            amount = actualPrice.multiply(new BigDecimal("100")).intValue();
            Integer couponId = in.getInteger("couponId");
            if (couponId != null && amount >= 3000) {
                AppUserCouponEntity appUserCouponEntity = appUserCouponService.selectById(couponId);
                if (appUserCouponEntity == null || appUserCouponEntity.getIsUse() == 1 || appUserCouponEntity.getCouponId() != 2) {
                    return null;
                }
                AppCouponTypeEntity appCouponTypeEntity = appCouponTypeService.selectById(appUserCouponEntity.getCouponId());
                appUserCouponEntity.setOrderNumber(appSubstituteDrivingIndentEntity.getSubstituteDrivingNumber());
                appUserCouponService.updateById(appUserCouponEntity);
                BigDecimal money = appCouponTypeEntity.getMoney();
                amount = amount - money.multiply(new BigDecimal("100")).intValue();
            }
            return amount;
        }
        if (type == 10) {
            String id = in.getString("id");
            if (messageType == 1) {
                //pb
                PushBillEntity pushBillEntity = pushBillService.selectById(id);
                amount = pushBillEntity.getDeduction().multiply(new BigDecimal("100")).intValue();
                return amount;
            } else {
                AppSendOutSheetEntity appSendOutSheetEntity = appSendOutSheetService.selectById(id);
                amount = appSendOutSheetEntity.getPayAmount().multiply(new BigDecimal("100")).intValue();
                return amount;
            }
        }
        return amount;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addCleanIndent(JSONObject in, String orderNo, String outTradeNo, Integer userid) {
        String id = in.getString("id");
        CleanPriceDetailEntity priceDetailEntity = cleanPriceDetailService.selectById(id);
        String userBId = String.valueOf(priceDetailEntity.getUserBId());
        Integer carType = priceDetailEntity.getCarType();
        BigDecimal originalPrice = priceDetailEntity.getOriginalPrice();
        Integer IndentCleanType = priceDetailEntity.getCleanType();
        CleanIndetEntity cleanIndetEntity = new CleanIndetEntity();
        if (outTradeNo.equals("免费洗")) {
            cleanIndetEntity.setAmount(new BigDecimal(0));
            cleanIndetEntity.setOriginalPrice(new BigDecimal(0));
            cleanIndetEntity.setIndentState("2");
        } else {
            BigDecimal amount = priceDetailEntity.getPreferentialPrice();
            cleanIndetEntity.setAmount(amount);
            cleanIndetEntity.setOriginalPrice(originalPrice);
            cleanIndetEntity.setIndentState("1");
            cleanIndetEntity.setBussinessId(String.valueOf(priceDetailEntity.getId()));
            //如果是合约服务
            if (priceDetailEntity.getContractProject() != null) {
                if (priceDetailEntity.getContractProject() == 1) {
                    cleanIndetEntity.setContractFlag(1);
                }
            }
        }
        //保存订单
        cleanIndetEntity.setUserId(String.valueOf(userid));
        cleanIndetEntity.setCarType(carType);
        cleanIndetEntity.setCleanType(IndentCleanType);
        cleanIndetEntity.setUserBId(userBId);
        cleanIndetEntity.setCleanIndentNumber(orderNo);
        cleanIndetEntity.setPayState("0");
        cleanIndetEntity.setResource("1");
        cleanIndetEntity.setCreateTime(new Date());
        cleanIndetEntity.setUpdateTime(new Date());
        //支付的商户订单编号
        cleanIndetEntity.setMerchantsPayNumber(outTradeNo);
        insert(cleanIndetEntity);
        logger.info("###  保存洗车订单详情##  cleanIndetEntity={}", cleanIndetEntity);
        if (priceDetailEntity.getContractProject() != null) {
            if (priceDetailEntity.getContractProject() == 1) {//合约标识 1：是  2：否，  如果是合约订单，退款之后需要把次数加上
                //增加回滚辅助表 数据
                AppOrderRollBack appOrderRollBack = new AppOrderRollBack();
                appOrderRollBack.setCreateTime(new Date());
                appOrderRollBack.setOpsFlag(1);//是否处理标识  1：未处理  2：已处理
                appOrderRollBack.setPayFlag(1);//付款标识  1：未付款  2已付款
                appOrderRollBack.setType(1);//1:洗车
                appOrderRollBack.setOrderId(String.valueOf(cleanIndetEntity.getId()));
                appOrderRollBackMapper.insert(appOrderRollBack);
                //次数-1
                CleanPriceDetailEntity cleanPriceDetailEntity = cleanPriceDetailMapper.selectForUpdate(id);
                logger.info("### 合约订单减次数 ### cleanPriceDetailEntity={}；id={}", cleanPriceDetailEntity, id);
                if (cleanPriceDetailEntity != null && cleanPriceDetailEntity.getResidueDegree() != null) {
                    cleanPriceDetailEntity.setResidueDegree(cleanPriceDetailEntity.getResidueDegree() - 1);
                    cleanPriceDetailService.updateById(cleanPriceDetailEntity);
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void addBeautyIndent(JSONObject in, String orderNo, String outTradeNo, Integer userid) {
        String id = in.getString("id");
        AppBeautyPriceDetailEntity beautyPriceDetailEntity = appBeautyPriceDetailService.selectById(id);
        BigDecimal amount = beautyPriceDetailEntity.getPreferentialPrice();
        String userBId = String.valueOf(beautyPriceDetailEntity.getUserBId());
        Integer carType = beautyPriceDetailEntity.getCarType();
        BigDecimal originalPrice = beautyPriceDetailEntity.getOriginalPrice();
        Integer IndentCleanType = beautyPriceDetailEntity.getBeautyType();
        //保存订单
        CleanIndetEntity cleanIndetEntity = new CleanIndetEntity();
        cleanIndetEntity.setOriginalPrice(originalPrice);
        cleanIndetEntity.setAmount(amount);
        cleanIndetEntity.setUserId(String.valueOf(userid));
        cleanIndetEntity.setCarType(carType);
        cleanIndetEntity.setCleanType(IndentCleanType);
        cleanIndetEntity.setUserBId(userBId);
        cleanIndetEntity.setCleanIndentNumber(orderNo);
        cleanIndetEntity.setIndentState("1");
        cleanIndetEntity.setPayState("0");
        cleanIndetEntity.setResource("2");
        cleanIndetEntity.setBussinessId(String.valueOf(beautyPriceDetailEntity.getId()));
        cleanIndetEntity.setCreateTime(new Date());
        cleanIndetEntity.setUpdateTime(new Date());
        //支付的商户订单编号
        cleanIndetEntity.setMerchantsPayNumber(outTradeNo);
        insert(cleanIndetEntity);
        logger.error("###  保存美容订单详情##  cleanIndetEntity={}", cleanIndetEntity);
    }


    @Transactional(rollbackFor = Exception.class)
    public void addRescueIndent(String orderNo, String outTradeNo) {
        EntityWrapper<AppRescueIndentEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("rescue_number", orderNo);
        AppRescueIndentEntity appRescueIndentEntity = appRescueIndentService.selectOne(wrapper);
        appRescueIndentEntity.setMerchantsPayNumber(outTradeNo);
        appRescueIndentService.updateById(appRescueIndentEntity);
        logger.error("###  保存救援订单详情##  appRescueIndentEntity={}", appRescueIndentEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addSprayPaintIndent(String orderNo, String outTradeNo) {
        EntityWrapper<AppSprayPaintIndentEntity> appSprayPaintIndentWrapper = new EntityWrapper<>();
        appSprayPaintIndentWrapper.eq("spray_paint_number", orderNo);
        AppSprayPaintIndentEntity appSprayPaintIndentEntity = appSprayPaintIndentService.selectOne(appSprayPaintIndentWrapper);
        appSprayPaintIndentEntity.setPayNumber(outTradeNo);
        appSprayPaintIndentService.updateById(appSprayPaintIndentEntity);
        logger.error("###  保存喷漆订单详情##  appSprayPaintIndentEntity={}", appSprayPaintIndentEntity);

    }


    @Transactional(rollbackFor = Exception.class)
    public void addYearCheckIndent(String orderNo, String outTradeNo) {
        EntityWrapper<AppYearCheckIndentEntity> checkWarpper = new EntityWrapper<>();
        checkWarpper.eq("year_check_number", orderNo);
        AppYearCheckIndentEntity appYearCheckIndentEntity = appYearCheckIndentService.selectOne(checkWarpper);
        appYearCheckIndentEntity.setPayNumber(outTradeNo);
        appYearCheckIndentService.updateById(appYearCheckIndentEntity);
        logger.error("###  保存年检订单详情##  appSprayPaintIndentEntity={}", appYearCheckIndentEntity);
    }


    @Transactional(rollbackFor = Exception.class)
    public void addSusituteDriIndetn(String orderNo, String outTradeNo) {
        EntityWrapper<AppSubstituteDrivingIndentEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("substitute_driving_number", orderNo);
        AppSubstituteDrivingIndentEntity appSubstituteDriving = appSubstituteDrivingIndentService.selectOne(wrapper);
        appSubstituteDriving.setPayNumber(outTradeNo);
        appSubstituteDrivingIndentService.updateById(appSubstituteDriving);
        logger.error("###  保存代驾支付编号 ##  appSubstituteDriving={}", appSubstituteDriving);
    }


    @Transactional(rollbackFor = Exception.class)
    public void addPbSosIndent(String orderNo, String outTradeNo) {
        //根据orderNo 查询出来是具体哪个表，再保存到对应表中
        //截取前两位
        String substring = orderNo.substring(0, 2);
        if (substring.equals("pb")) {
            //pb表中
            EntityWrapper<PushBillEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("pb_number", orderNo);
            PushBillEntity pushBillEntity = pushBillService.selectOne(wrapper);
            pushBillEntity.setMessPayPumber(outTradeNo);
            pushBillService.updateById(pushBillEntity);
            logger.error("###  保存pb支付编号 ##  pushBillEntity={}", pushBillEntity);
        } else {
            EntityWrapper<AppSendOutSheetEntity> appSendWrapper = new EntityWrapper<>();
            appSendWrapper.eq("sos_number", orderNo);
            AppSendOutSheetEntity appSendOutSheetEntity = appSendOutSheetService.selectOne(appSendWrapper);
            appSendOutSheetEntity.setMessPayNumber(outTradeNo);
            appSendOutSheetService.updateById(appSendOutSheetEntity);
            logger.error("###  保存sos支付编号 ##  appSendOutSheetEntity={}", appSendOutSheetEntity);
        }

    }


    public List<CleanIndetEntity> findCleanIndent(Integer id, Integer pagesize, String type) {
        pagesize = (pagesize - 1) * 20;
        //类型为0
        if (type.equals("0")) {
            //查询全部数据
            type = null;
            return cleanIndetMapper.findCleanIndent(id, pagesize, type);
        } else if (type.equals("1")) {
            //查询待收货
            //  type = "2";
            return cleanIndetMapper.findCleanIndent(id, pagesize, type);
        } else if (type.equals("2")) {
            //服务中
            return cleanIndetMapper.findCleanIndent(id, pagesize, type);
        } else if (type.equals("3")) {
            //待评价
            return cleanIndetMapper.findCleanIndent(id, pagesize, type);
        } else if (type.equals("4")) {
            //取消
            return cleanIndetMapper.findCleanIndent(id, pagesize, type);
        } else if (type.equals("5")) {
            //超时
            return cleanIndetMapper.findCleanIndent(id, pagesize, type);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(String cleanIndentNumber, CleanIndetEntity cleanIndetEntity) throws CusException {
        logger.info("开始发送消息：" + cleanIndentNumber);
        Message sendMsg = new Message("all", "clearCancelOrder", cleanIndentNumber.getBytes());
        //默认3秒超时
        SendResult sendResult = null;
        try {
            sendResult = mqProducer.send(sendMsg);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        if (sendResult == null) {
            throw new CusException(405, "取消失败，请联系客服。");
        }
        logger.info("消息发送响应信息：" + sendResult.toString());
        //修改订单状态
        if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
            cleanIndetEntity.setIndentState("4");
            cleanIndetMapper.updateById(cleanIndetEntity);

            if (cleanIndetEntity.getContractFlag() != null && cleanIndetEntity.getContractFlag() == 1) {// 如果是合约订单，退款之后需要把次数加上
                CleanPriceDetailEntity cleanPriceDetailEntity = cleanPriceDetailMapper.selectForUpdate(cleanIndetEntity.getBussinessId());
                logger.info("### 退款成功   合约订单次数+1###   cleanIndetEntity={}；cleanPriceDetailEntity={}", cleanIndetEntity, cleanPriceDetailEntity);
                if (cleanPriceDetailEntity != null) {
                    cleanPriceDetailEntity.setResidueDegree(cleanPriceDetailEntity.getResidueDegree() + 1);//固定只有一次
                    cleanPriceDetailMapper.updateById(cleanPriceDetailEntity);
                } else {
                    logger.error("#### 回滚库存失败，没有找到bussinessId对应数据   ####    bussinessId={}", cleanIndetEntity.getBussinessId());
                }
            }
        } else {
            logger.error("消息发送失败 响应信息：{};cleanIndetEntity={}", sendResult.toString(), cleanIndetEntity);
        }
    }

    public Personal findIndentCount(String userId) {
        return cleanPriceDetailMapper.findIndentCount(userId);
    }

    public List<CleanIndetEntity> selectIndentUser(Integer id, Integer userBId) {
        return cleanPriceDetailMapper.selectIndentUser(id, userBId);
    }

    public String findcarImgpx(Integer carType) {
        if (carType == 0) {
            carType = 1;
        }
        return cleanIndetMapper.findcarImgpx(carType);
    }

    public String findcarImgJx(Integer carType) {
        if (carType == 0) {
            carType = 1;
        }
        return cleanIndetMapper.findcarImgJx(carType);
    }

    public String findbeautyName(Integer cleanType) {
        return cleanIndetMapper.findbeautyName(cleanType);
    }

    public Integer findIndentCountNumber(String userBId) {
        return cleanIndetMapper.findIndentCountNumber(userBId);
    }

    public List<CleanIndetEntity> findisOnFreeIndent(Integer userid) {
        return cleanIndetMapper.findisOnFreeIndent(userid);
    }

    public String findbeautyImg(Integer cleanType) {
        return cleanIndetMapper.findbeautyImg(cleanType);
    }


}
