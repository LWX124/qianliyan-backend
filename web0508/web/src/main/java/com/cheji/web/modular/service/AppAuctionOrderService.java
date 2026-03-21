package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppAuctionOrderEntity;
import com.cheji.web.modular.domain.AppAuctionTransactionLogEntity;
import com.cheji.web.modular.mapper.AppAuctionOrderMapper;
import com.cheji.web.util.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单相关
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionOrderService extends ServiceImpl<AppAuctionOrderMapper, AppAuctionOrderEntity> implements IService<AppAuctionOrderEntity> {

    @Autowired
    private AppAuctionTransactionLogService transactionLogService;

//    /**
//     * 验证车辆信息，不能重复录入
//     * 通过carId查询出价表中该车是否在拍卖,
//     * 通过redis取出最高价用户出价信息
//     * 通过bid表查询该车的最高出价信息,对比redis验证信息,
//     * 生成订单,存入订单表,更改车辆状态,更改上架表状态,更改过户审核状态
//     * 交易失败返还手续费@todo
//     * @param carId
//     * @return
//     */
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public JSONObject readyOrder(String carId, Long userId) {
//        JSONObject result = new JSONObject();
//        // 验证重复录入
//        EntityWrapper<AppAuctionOrderEntity> wrapper4 = new EntityWrapper<>();
//        wrapper4.eq("auction_car_id",carId).eq("user_Id",userId);
//        AppAuctionOrderEntity appAuctionOrderEntity1 = selectOne(wrapper4);
//        if(appAuctionOrderEntity1 != null){
//            result.put("code",512);
//            result.put("msg","该车已录入");
//            return result;
//        }
//
//        //获取redis中的出价信息
//        String key = String.format(AuctionConstant.KEY_AUCTION_BID_TOKEN, carId);
//        String userAndBid = (String)redisTemplate.opsForValue().get(key);
//        EntityWrapper<AppAuctionBidEntity> wrapper1 = new EntityWrapper<>();
//        wrapper1.eq("car_id",carId).eq("valid","0");
//        List<AppAuctionBidEntity> appAuctionBidEntities = appAuctionBidService.selectList(wrapper1);
//        if(appAuctionBidEntities.size() > 0 && userAndBid != null){
//            AppAuctionBidEntity appAuctionBidEntity = appAuctionBidEntities.stream().sorted(Comparator.comparing(AppAuctionBidEntity::getBid))
//                    .collect(Collectors.toList()).get(0);
//            try {
//                AssertUtil.isNotNull(appAuctionBidEntity,"error");
//            }catch (Exception e){
//                result.put("code",516);
//                result.put("msg","车辆查询错误,请联系管理员!");
//                return result;
//            }
//            String[] split = userAndBid.split(":");
//            String carUserId = split[0];
//            String bid = split[1];
//            //验证出价信息
//            if(carUserId.equals(String.valueOf(appAuctionBidEntity.getUserId()))
//                    && String.valueOf(carId).equals(appAuctionBidEntity.getCarId())
//                    && bid.equals(appAuctionBidEntity.getBid().toString())){
//                //修改订单状态,生成订单
//                EntityWrapper<AppAuctionEntity> wrapper2 = new EntityWrapper<>();
//                wrapper2.eq("id",carId)
//                        .eq("state","1")
//                        .eq("up_state","1")
//                        .eq("audit_state","3");
//                AppAuctionEntity auction = appAuctionService.selectOne(wrapper2);
//                if(auction == null){
//                    result.put("code",513);
//                    result.put("msg","车辆表状态错误");
//                    return result;
//                }
//                EntityWrapper<AppAuctionUpEntity> wrapper3 = new EntityWrapper<>();
//                wrapper3.eq("up_id",carId)
//                        .eq("auction_state","1")
//                        .eq("up_state","1")
//                        .eq("audit_state","3");
//                AppAuctionUpEntity appAuctionUpEntity = appAuctionUpService.selectOne(wrapper3);
//                if(appAuctionUpEntity == null){
//                    result.put("code",514);
//                    result.put("msg","车辆上架信息错误");
//                    return result;
//                }
//                auction.setState("2");
//                auction.setUpState("0");
//                auction.setTransferState("0");
//                appAuctionService.updateById(auction);
//
//                appAuctionUpEntity.setAuctionState("2");
//                appAuctionUpEntity.setUpState("0");
//                appAuctionUpEntity.setTransferState("0");
//                appAuctionUpService.updateById(appAuctionUpEntity);
//                //创建订单信息
//                AppAuctionOrderEntity appAuctionOrderEntity = new AppAuctionOrderEntity();
//                appAuctionOrderEntity.setOrderAmount(appAuctionBidEntity.getBid());
//                appAuctionOrderEntity.setUserId(appAuctionBidEntity.getUserId());
//                appAuctionOrderEntity.setAuctionCarId(Long.valueOf(carId));
//                appAuctionOrderEntity.setCreateTime(new Date());
//                //生成订单号
//                String orderNo = COrderNoUtil.getUniqueOrder();
//                appAuctionOrderEntity.setOrderNo(orderNo);
//                //获取服务费率
//                Integer rate = appAuctionUpEntity.getServiceFee();
//                if(rate != null && rate != 0){
//                    BigDecimal bigDecimal = new BigDecimal(rate);
//                    BigDecimal serviceFee = bigDecimal.divide(new BigDecimal("100")).multiply(new BigDecimal(bid));
//                    appAuctionOrderEntity.setServiceFee(serviceFee);
//                    //扣服务费,记录交易信息
////                    AppAuctionVipControlEntity user_id = appAuctionVipControlService.selectOne(new EntityWrapper<AppAuctionVipControlEntity>().eq("user_id", userId));
////                    if(user_id !=null && user_id.getAmount() != null && user_id.getAmount().compareTo(serviceFee) > 0){
////                        BigDecimal balance = user_id.getAmount().subtract(serviceFee);
////                        user_id.setAmount(balance);
////
////                        appAuctionVipControlService.updateById(user_id);
////                    }else{
////                        result.put("code",503);
////                        return result;
////                    }
//                }else{
//                    BigDecimal bigDecimal = new BigDecimal(bid);
//                    BigDecimal serviceFee = bigDecimal.multiply(new BigDecimal("0.05"));
//                    appAuctionOrderEntity.setServiceFee(serviceFee);
//                }
//
//                //录入信息
//                boolean insert1 = appAuctionOrderService.insert(appAuctionOrderEntity);
//                if(!insert1){
//                    result.put("code",506);
//                    result.put("msg","录入订单信息异常,请联系管理员!");
//                    return result;
//                }
//
////                AppAuctionTransactionLogEntity appAuctionTransactionLogEntity = new AppAuctionTransactionLogEntity();
////                appAuctionTransactionLogEntity.setType(TransactionConstant.TRANSACTION_CONSTANT_PAY);
////                appAuctionTransactionLogEntity.setCreateTime(new Date());
////                appAuctionTransactionLogEntity.setAmount(appAuctionOrderEntity.getServiceFee());
////                appAuctionTransactionLogEntity.setDesc("拍卖成功手续费");
////                appAuctionTransactionLogEntity.setUserId(appAuctionBidEntity.getUserId());
////                appAuctionTransactionLogEntity.setOrderState("2");
////                appAuctionTransactionLogEntity.setOrderId(String.valueOf(appAuctionOrderEntity.getId()));
////                boolean insert = transactionLogService.insert(appAuctionTransactionLogEntity);
////                if(!insert){
////                    result.put("code",504);
////                    result.put("msg","记录消费信息异常,请联系管理员!");
////                    return result;
////                }
//
//                result.put("code",200);
//                return result;
//            }else {
//                result.put("code",515);
//                result.put("msg","出价信息有误");
//                return result;
//            }
//        }else{
//            result.put("code",518);
//            return result;
//        }
//    }


    public JSONObject queryOrder(Page page, JSONObject result, Integer id) {
        try {
            AssertUtil.isNotNull(id, "error");
        } catch (Exception e) {
            result.put("code", 515);
            result.put("msg", "用户信息错误!");
            return result;
        }
        List<AppAuctionTransactionLogEntity> logs = transactionLogService.selectPage(page, new EntityWrapper<AppAuctionTransactionLogEntity>()
                .eq("user_id", id).orderBy("id", false)).getRecords();
        //改变金额单位
        List<AppAuctionTransactionLogEntity> collect = logs.stream().map(a -> {
            a.setAmount(a.getAmount().divide(new BigDecimal("100.00")));
            a.setCreateTimeLong(a.getCreateTime().getTime());
            return a;
        }).collect(Collectors.toList());
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", collect);
        return result;
    }

}
