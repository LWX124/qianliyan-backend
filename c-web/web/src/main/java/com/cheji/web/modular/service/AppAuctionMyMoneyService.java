package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cheji.web.constant.ConsEnum;
import com.cheji.web.modular.domain.AppAuctionEntity;
import com.cheji.web.modular.domain.AppAuctionImgEntity;
import com.cheji.web.modular.domain.AppAuctionOrderEntity;
import com.cheji.web.modular.domain.AppAuctionTransactionLogEntity;
import com.cheji.web.modular.mapper.AppAuctionTransactionLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 我的金额相关
 * </p>
 *
 * @author yang
 */
@Slf4j
@Service
public class AppAuctionMyMoneyService {

    @Autowired
    private AppAuctionOrderService appAuctionOrderService;

    @Autowired
    private AppAuctionService auctionService;

    @Autowired
    private AppAuctionImgService appAuctionImgService;

    @Autowired
    private AppAuctionTransactionLogService appAuctionTransactionLogService;
    @Autowired
    private AppAuctionTransactionLogMapper appAuctionTransactionLogMapper;


    /**
     * @param result
     * @param page
     * @param userId 金额，时间，vip购买说明
     * @return
     */
    public JSONObject bail(JSONObject result, Page page, Integer userId, Integer queryType) {
        //根据用户id和搜索类型查询
        log.info("单车保证金列表 参数##  userId={};queryType={};offset={};limit={}", userId, queryType, page.getOffset(), page.getLimit());
        List<AppAuctionTransactionLogEntity> logs = appAuctionTransactionLogMapper.selectByUserAndType(userId, queryType, page.getOffset(), page.getLimit());
        log.info("单车保证金列表 结果##  logs={};", logs);
//        JSONArray array = new JSONArray();
//        for (AppAuctionTransactionLogEntity log : logs) {
//            if (log != null) {
//                log.setAmount(log.getAmount().divide(new BigDecimal("100")));
//                log.setCreateTimeLong(log.getCreateTime().getTime());
//                array.add(log);
//            }
//        }
        List<AppAuctionTransactionLogEntity> collect = logs.stream().map(log -> {
            log.setAmount(log.getAmount().divide(new BigDecimal("100")));
            log.setCreateTimeLong(log.getCreateTime().getTime());
            return log;
        }).collect(Collectors.toList());
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", collect);
        return result;
    }


    /**
     * @param result
     * @param page
     * @param id     车辆id，金额，时间，车牌号，
     * @return
     */
    public JSONObject servicefee(Page page, JSONObject result, Integer id) {
        List<AppAuctionOrderEntity> orders = appAuctionOrderService.selectPage(page, new EntityWrapper<AppAuctionOrderEntity>().eq("user_id", id)).getRecords();
        JSONArray array = new JSONArray();
        for (AppAuctionOrderEntity order : orders) {
            if (order != null) {
                JSONObject object = new JSONObject();
                object.put("servicefee", order.getServiceFee());
                object.put("time", order.getCreateTime());
                object.put("carId", order.getCarId());
                AppAuctionEntity auction = auctionService.selectById(order.getCarId());
                if (auction != null) {
                    object.put("plateNo", auction.getPlateNo());
                    object.put("brand", auction.getBrand());
                    object.put("accidentType", auction.getAccidentType());
                }
                List<AppAuctionImgEntity> appAuctionImgEntities = appAuctionImgService.selectByCarId(order.getCarId());
                if (appAuctionImgEntities.size() > 0) {
                    for (AppAuctionImgEntity appAuctionImgEntity : appAuctionImgEntities) {
                        if (ConsEnum.AUCTION_IMG_CAR.equals(appAuctionImgEntity.getState())) {
                            String url = appAuctionImgEntities.get(0).getUrl();
                            object.put("cover", url);
                            break;
                        }
                    }
                }
                array.add(object);
            }
        }
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", array);
        return result;
    }

    public JSONObject signRecord(Page page, JSONObject result, Integer id) {
        List<AppAuctionTransactionLogEntity> logs = appAuctionTransactionLogService.selectPage(page, new EntityWrapper<AppAuctionTransactionLogEntity>().eq("user_id", id)).getRecords();
        JSONArray array = new JSONArray();
        for (AppAuctionTransactionLogEntity log : logs) {
            if (log != null) {
                log.setAmount(log.getAmount().divide(new BigDecimal("100")));
                array.add(log);
            }
        }
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", array);
        return result;
    }
}
