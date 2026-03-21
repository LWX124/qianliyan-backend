package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppAuctionEntity;
import com.cheji.web.modular.domain.AppAuctionUpEntity;
import com.cheji.web.modular.dto.AuctionCarListDto;
import com.cheji.web.modular.mapper.AppAuctionUpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *     上架相关
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionUpService extends ServiceImpl<AppAuctionUpMapper, AppAuctionUpEntity> implements IService<AppAuctionUpEntity> {

    @Resource
    private AppAuctionUpMapper appAuctionUpMapper;

    @Autowired
    private AppAuctionService appAuctionService;


    public JSONArray todayNew(Integer page, AuctionCarListDto auctionCarListDto,Long id) {
        List<AppAuctionEntity> appAuctionEntities = appAuctionUpMapper.todayNew(page, auctionCarListDto);
        auctionCarListDto.setAuctionState(1);//查询拍卖中数量
        Integer auctionIngCount = appAuctionUpMapper.todayNewCount(page, auctionCarListDto);
        auctionCarListDto.setAuctionState(2);//查询待开始数量
        Integer auctionWaitCount = appAuctionUpMapper.todayNewCount(page, auctionCarListDto);
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("auctionIngCount",auctionIngCount);
        object.put("auctionWaitCount",auctionWaitCount);
        array.add(object);
        return appAuctionService.auction2Dto(array,appAuctionEntities,id);
    }
    
}
