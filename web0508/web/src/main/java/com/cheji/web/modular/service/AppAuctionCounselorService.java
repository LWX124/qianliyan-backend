package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppAuctionCounselorEntity;
import com.cheji.web.modular.domain.AppAuctionMyEntity;
import com.cheji.web.modular.domain.AppAuctionUpEntity;
import com.cheji.web.modular.mapper.AppAuctionCounselorMapper;
import com.cheji.web.modular.mapper.AppAuctionMyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * <p>
 *     顾问
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionCounselorService extends ServiceImpl<AppAuctionCounselorMapper, AppAuctionCounselorEntity> implements IService<AppAuctionCounselorEntity> {

    @Autowired
    private AppAuctionCounselorService appAuctionCounselorService;
    @Autowired
    private AppAuctionUpService auctionUpService;

    public JSONObject delCounselor(JSONObject result,Long counselorId) {
        boolean b = appAuctionCounselorService.deleteById(counselorId);
        if(b){
            result.put("code", 200);
            result.put("msg", "删除成功!");
            return result;
        }else{
            result.put("code", 202);
            result.put("msg", "删除失败!");
            return result;
        }
    }

    public AppAuctionCounselorEntity queryCounselor(JSONObject result, Long carId) {
        //暂时没用到counselorId,需要对接的时候用
        AppAuctionUpEntity upEntity = auctionUpService.selectOne(new EntityWrapper<AppAuctionUpEntity>().eq("car_id", carId));
        String counselorId = upEntity.getCounselorId();
        AppAuctionCounselorEntity appAuctionCounselorEntity = null;
        if(Objects.nonNull(counselorId)){
            appAuctionCounselorEntity = selectOne(new EntityWrapper<AppAuctionCounselorEntity>().eq("user_id", counselorId));
        }
        return appAuctionCounselorEntity;
//        if(Objects.nonNull(appAuctionCounselorEntity)){
//            result.put("code", 200);
//            result.put("msg", "查询成功!");
//            result.put("data", appAuctionCounselorEntity);
//            return result;
//        }else{
//            result.put("code", 200);
//            result.put("msg", "无顾问!");
//            result.put("data", appAuctionCounselorEntity);
//            return result;
//        }
    }
}
