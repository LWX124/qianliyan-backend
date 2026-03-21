package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.constant.*;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.lock.RedisLock;
import com.cheji.web.modular.mapper.AppAuctionBidMapper;
import com.cheji.web.util.AssertUtil;
import com.cheji.web.util.COrderNoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 出价相关
 * </p>
 *
 * @author yang
 */
@Service
@Slf4j
public class AppAuctionBidService extends ServiceImpl<AppAuctionBidMapper, AppAuctionBidEntity> implements IService<AppAuctionBidEntity> {


    @Resource
    private AppAuctionBidMapper appAuctionBidMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AppAuctionService appAuctionService;

    @Autowired
    private AppAuctionUpService appAuctionUpService;

    @Autowired
    private AppAuctionOrderService appAuctionOrderService;

    @Autowired
    private AppAuctionVipControlService appAuctionVipControlService;

    @Autowired
    private UserService userService;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private AppAuctionBailLogService bailLogService;

    @Resource
    private RedisLock redisLock;

    /**
     * carId 拍卖车辆ID
     * 参数校验,
     * 条件判断,
     * vip等级,参拍车数量是否符合
     * 出价redis设置过期时间
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public JSONObject bidLog(Integer userId, AppAuctionBidEntity appAuctionBidEntity) {

        String lockKey = RedisConstant.AUCTION_BID_LOCK + userId;
        redisLock.lock(lockKey);//防止因为网络原因导致用户连点两次提现

        try {
            JSONObject result = new JSONObject();
            // 验证状态
            AppAuctionEntity auction = appAuctionService.selectById(appAuctionBidEntity.getCarId());
            if (auction == null) {
                result.put("code", 502);
                result.put("msg", "无该车信息!");
                return result;
            }

            //-1小于, 0 等于,  1 大于
            if (appAuctionBidEntity.getBid().compareTo(auction.getPrice()) < 1) {
                result.put("code", 532);
                result.put("msg", "出价金额必须大于起拍价");
                return result;
            }

            EntityWrapper<AppAuctionUpEntity> appAuctionUpWrapper = new EntityWrapper<>();
            appAuctionUpWrapper.eq("car_id", appAuctionBidEntity.getCarId());
            AppAuctionUpEntity appAuctionUpEntity = appAuctionUpService.selectOne(appAuctionUpWrapper);

            if (appAuctionUpEntity == null) {
                result.put("code", 502);
                result.put("msg", "无该车信息!");
                return result;
            }

            if (new Date().getTime() < appAuctionUpEntity.getBeginTime().getTime()) {
                result.put("code", 503);
                result.put("msg", "拍卖还没开始!");
                return result;
            }

            if (appAuctionUpEntity.getEndTime().getTime() - new Date().getTime() < 0) {
                result.put("code", 501);
                result.put("msg", "拍卖已结束!");
                return result;
            }

            UserEntity appUserEntity = userService.selectById(userId);
            // 验证vip和用户单台车保证金情况
            Integer vipLv = appUserEntity.getVipLv();
            Boolean singleCarFlag = false;// 单车保证金标识  false表示没有缴纳单台车保证金   true 表示缴纳了单台车保证金

            AppAuctionBailLogEntity appAuctionBailLogEntity = bailLogService.selectOne(new EntityWrapper<AppAuctionBailLogEntity>().eq("user_id", userId).eq("car_id", appAuctionBidEntity.getCarId()).eq("status", AppAuctionBailConstant.AUCTION_BAIL_SUCCESS));
            if (appAuctionBailLogEntity != null) {
                singleCarFlag = true;//用户交了单车保证金
            } else if (vipLv == null || vipLv < 1) {
                //用户没有交单车保证金 并且没有开通vip
                result.put("code", 502);
                result.put("msg", "请升级竞拍账号或者缴纳拍卖保证金!");
                return result;

            }


            //必须是拍卖中状态
            if (!AppAuctionConstant.SEVEN.equals(auction.getCarState())) {
                result.put("code", 505);
                result.put("msg", "拍卖状态错误!");
                return result;
            }

            //出价验证,不能比最高的低
            String bidCarKey = String.format(AuctionConstant.KEY_AUCTION_BID_TOKEN, appAuctionBidEntity.getCarId());
            String mustPrice = (String) redisTemplate.opsForValue().get(bidCarKey);
            if (mustPrice != null) {
                //-1小于, 0 等于,  1 大于
                int res = appAuctionBidEntity.getBid().compareTo(new BigDecimal(mustPrice));
                if (res != 1) {
                    result.put("code", 507);
                    result.put("msg", "不能低于当前价格!");
                    result.put("currentPrice", mustPrice);
                    return result;
                }
            }

//            if (mustPrice != null) {
//                boolean b = new BigDecimal(mustPrice.split(":")[1]).compareTo(appAuctionBidEntity.getBid()) == -1;
//
//                boolean c = appAuctionBidEntity.getBid().compareTo(auction.getPrice()) == -1;
//
//                if (!b || c) {
//                    result.put("code", 507);
//                    result.put("msg", "不能低于当前价格!");
//                    result.put("currentPrice", mustPrice.split(":")[1]);
//                    return result;
//                }
//
//            }

            //竞拍数量验证
            EntityWrapper<AppAuctionVipControlEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("user_id", appUserEntity.getId());
            wrapper.eq("state", 1);//1使用中
            AppAuctionVipControlEntity appAuctionVipControlEntity = appAuctionVipControlService.selectOne(wrapper);
            if (!singleCarFlag) {//如果是缴纳的单车保证金，就不需要验证 竞拍数量
                //进入这里表示 非单车保证金
                if(appAuctionVipControlEntity == null){
                    result.put("code", 502);
                    result.put("msg", "请开通会员或者缴纳单车保证金之后再出价！");
                    return result;
                }

                if ( appAuctionVipControlEntity.getCarCount() < AppAuctionVipSet.VIP_COMMON_ONE) {
                    result.put("code", 503);
                    result.put("msg", "拍卖车数量过多,请升级账号获取更多权限!");
                    return result;
                }

                if (appAuctionVipControlEntity == null || appAuctionVipControlEntity.getOffer() < AppAuctionVipSet.VIP_COMMON_ONE) {
                    result.put("code", 508);
                    result.put("msg", "出价次数过多,请升级账号获取更多权限!");
                    return result;
                }

                //第一次有效出价修改vip竞拍台次  如果是缴纳的单车保证金，就不需要 修改vip竞拍台次
                EntityWrapper<AppAuctionBidEntity> wrapper2 = new EntityWrapper<>();
                wrapper2.eq("car_id", appAuctionBidEntity.getCarId()).eq("valid", AppAuctionConstant.ZERO).eq("user_id", userId);
                AppAuctionBidEntity appAuctionBidEntity1 = selectOne(wrapper2);
                if (appAuctionBidEntity1 == null) {
                    appAuctionVipControlEntity.setCarCount(appAuctionVipControlEntity.getCarCount() - AppAuctionVipSet.VIP_COMMON_ONE);
                    appAuctionVipControlEntity.setFreezeCount(appAuctionVipControlEntity.getFreezeCount() + AppAuctionVipSet.VIP_COMMON_ONE);
                    appAuctionVipControlEntity.setOffer(appAuctionVipControlEntity.getOffer() - AppAuctionVipSet.VIP_COMMON_ONE);
                    appAuctionVipControlService.updateById(appAuctionVipControlEntity);
                }
            }

            //记录出价,并记录有效出价车辆次
            appAuctionBidEntity.setCreateTime(new Date());
            appAuctionBidEntity.setUserId(Long.valueOf(userId));
            insert(appAuctionBidEntity);

            log.info("##结束时间:" + appAuctionUpEntity.getEndTime());
            AssertUtil.isNotNull(appAuctionUpEntity.getEndTime(), "##获取结束时间为空");
            //计算redis过期时间
            long endTimes = appAuctionUpEntity.getEndTime().getTime() - new Date().getTime();
            if (endTimes < 0) {
                log.info("redis过期时间设置错误!");
                throw new RuntimeException();
            }
            redisTemplate.opsForValue().set(bidCarKey, appAuctionBidEntity.getBid().toString(), endTimes + 10000, TimeUnit.MILLISECONDS);

            Set<String> bidUsers = redisTemplate.opsForSet().members(AuctionConstant.CAR_DETAIL_INFO_SET + appAuctionBidEntity.getCarId());

            log.info("#### 获取进入详情的用户集合 bidUsers ={}", bidUsers);
            for (String bidUser : bidUsers) {
                // bidUser 数据结构= userId:出价金额
                log.info("### 发送报价长连接 carId={};userId={};当前最高价={}", appAuctionBidEntity.getCarId(), bidUser, appAuctionBidEntity.getBid());
                boolean flag = webSocketService.sendMessageToUser(appAuctionBidEntity.getCarId() + ":" + bidUser, new TextMessage("当前最高价格:" + appAuctionBidEntity.getBid()));
                if (!flag) { //如果flag是false  表示用户没有在线  删除用户所在redis
                    redisTemplate.opsForSet().remove(AuctionConstant.CAR_DETAIL_INFO_SET + appAuctionBidEntity.getCarId(), bidUser);
                }
            }

            result.put("code", 200);
            result.put("msg", "出价成功!");
            return result;
        } finally {
            redisLock.unlock(lockKey);
        }
    }

    public static void main(String[] args) {
        //-1小于, 0 等于,  1 大于

        int res = new BigDecimal("1000").compareTo(new BigDecimal("900"));
        System.out.println(res);
    }

    public JSONObject bidRecord(JSONObject result, Long carId) {
        if (carId != null && carId > 0) {
            List<AppAuctionBidEntity> bidList = selectList(new EntityWrapper<AppAuctionBidEntity>().eq("car_id", carId));
            List<AppAuctionBidEntity> collect = bidList.stream().map(a -> {
                Calendar calendar = Calendar.getInstance();
                if (a.getCreateTime() != null) {
                    calendar.setTime(a.getCreateTime());
                    a.setBidTime(calendar.getTimeInMillis());
                }
                UserEntity userEntity = userService.selectById(a.getUserId());
                a.setUserName(userEntity.getName());
                return a;
            }).sorted(Comparator.comparing(AppAuctionBidEntity::getBid).reversed()).collect(Collectors.toList());
            if (collect.size() > 0) {
                result.put("code", 200);
                result.put("msg", "查询成功!");
                result.put("data", collect);
            } else {
                result.put("code", 200);
                result.put("msg", "暂无数据!");
                result.put("data", bidList);
            }
            return result;
        }
        result.put("code", 501);
        result.put("msg", "未知错误!");
        return result;
    }


    /**
     * 参数验证
     * 修改状态 1下架，2拍卖完成，3过户中
     * 录入订单
     *
     * @param result
     * @param carId
     * @param id
     * @return
     */
    public JSONObject fixedPrice(JSONObject result, Long carId, Integer id) {
        AppAuctionVipControlEntity appAuctionVipControl = appAuctionVipControlService.selectOne(new EntityWrapper<AppAuctionVipControlEntity>().eq("user_id", id).eq("state", AppAuctionConstant.ONE));
        UserEntity userEntity = userService.selectById(id);
        if (Objects.isNull(appAuctionVipControl) || AppAuctionVipSet.VIP_STATE_USE != appAuctionVipControl.getState() || StringUtils.isEmpty(userEntity.getVipLv().toString()) || userEntity.getVipLv() < AppAuctionVipSet.VIP_COMMON_ONE) {
            result.put("code", 201);
            result.put("msg", "请升级vip！");
            return result;
        }
        AppAuctionEntity auction = appAuctionService.selectById(carId);
        if (Objects.nonNull(auction) && AppAuctionConstant.ONE.toString().equals(auction.getFixedPrice())) {
            return addOrder(carId, id);
        } else {
            result.put("code", 401);
            result.put("msg", "无信息！");
            return result;
        }
    }

    public JSONObject addOrder(Long carId, Integer userId) {
        JSONObject result = new JSONObject();
        // 验证重复录入
        EntityWrapper<AppAuctionOrderEntity> wrapper4 = new EntityWrapper<>();
        wrapper4.eq("car_id", carId).eq("user_Id", userId);
        AppAuctionOrderEntity appAuctionOrderEntity1 = appAuctionOrderService.selectOne(wrapper4);
        if (Objects.nonNull(appAuctionOrderEntity1)) {
            result.put("code", 512);
            result.put("msg", "该车已录入");
            return result;
        }

        EntityWrapper<AppAuctionEntity> wrapper2 = new EntityWrapper<>();
        wrapper2.eq("id", carId).eq("car_state", AppAuctionConstant.SEVEN);
        AppAuctionEntity appAuction = appAuctionService.selectOne(wrapper2);
        if (appAuction == null) {
            result.put("code", 513);
            result.put("msg", "车辆表状态错误");
            return result;
        }

        EntityWrapper<AppAuctionUpEntity> wrapper3 = new EntityWrapper<>();
        wrapper3.eq("car_id", carId).eq("car_state", AppAuctionConstant.SEVEN);
        AppAuctionUpEntity appAuctionUpEntity = appAuctionUpService.selectOne(wrapper3);
        if (appAuctionUpEntity == null) {
            result.put("code", 514);
            result.put("msg", "车辆上架信息错误");
            return result;
        }

        appAuction.setCarState(AppAuctionConstant.EIGHT);
        appAuction.setUpState(AppAuctionConstant.ZERO);
        appAuctionService.updateById(appAuction);

        //创建订单信息
        AppAuctionOrderEntity appAuctionOrderEntity = new AppAuctionOrderEntity();
        appAuctionOrderEntity.setOrderAmount(appAuction.getPrice());
        appAuctionOrderEntity.setUserId(Long.valueOf(userId));
        appAuctionOrderEntity.setCarId(Long.valueOf(carId));
        appAuctionOrderEntity.setCreateTime(new Date());
        appAuctionOrderEntity.setState(AppAuctionConstant.ZERO);
        //生成订单号
        String orderNo = COrderNoUtil.getUniqueOrder();
        appAuctionOrderEntity.setOrderNo(orderNo);
        //获取服务费率
        Integer rate = appAuctionUpEntity.getServiceFee();
        if (rate != null && rate > 0) {
            BigDecimal bigDecimal = new BigDecimal(rate);
            BigDecimal serviceFee = bigDecimal.divide(new BigDecimal("100")).multiply(appAuction.getPrice());
            appAuctionOrderEntity.setServiceFee(serviceFee);
        } else {
            appAuctionOrderEntity.setServiceFee(new BigDecimal("0.00"));
        }

        //删除rediskey
        String endbidOrderKey = String.format(AppAuctionConstant.END_BID_ORDER_CARID, carId);
        Set keys = redisTemplate.keys(endbidOrderKey);
        if (Objects.nonNull(endbidOrderKey) && keys.size() > 0) {
            for (Object key : keys) {
                redisTemplate.delete(key);
                log.info("##删除rediskey成功");
            }
        }

        //录入信息
        boolean insert1 = appAuctionOrderService.insert(appAuctionOrderEntity);
        if (!insert1) {
            result.put("code", 506);
            result.put("msg", "录入订单信息异常,请联系管理员!");
            return result;
        }

        result.put("code", 200);
        result.put("msg", "成功!");
        return result;

    }

    public Integer getValidCount(Integer id) {
        return appAuctionBidMapper.getValidCount(id);
    }
}
