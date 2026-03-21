package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.cheji.web.modular.cwork.Personal;
import com.cheji.web.modular.domain.AppSprayPaintIndentEntity;
import com.cheji.web.modular.domain.AppUserAccountRecordEntity;
import com.cheji.web.modular.domain.AppUserBMessageEntity;
import com.cheji.web.modular.domain.BUserEntity;
import com.cheji.web.modular.mapper.AppSprayPaintIndentMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.mapper.AppUserAccountRecordMapper;
import com.cheji.web.modular.mapper.BUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 喷漆订单表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2020-03-13
 */
@Service
public class AppSprayPaintIndentService extends ServiceImpl<AppSprayPaintIndentMapper, AppSprayPaintIndentEntity> implements IService<AppSprayPaintIndentEntity> {

    @Resource
    private BUserMapper bUserMapper;

    @Resource
    private AppUserAccountRecordMapper appUserAccountRecordMapper;

    @Resource
    private AppSprayPaintIndentMapper appSprayPaintIndentMapper;

    @Resource
    private BUserService bUserService;

    @Resource
    private AppUserBMessageService appUserBMessageService;


    @Transactional(rollbackFor = Exception.class)
    public void SprayPaintAddMerchantsAmount(AppSprayPaintIndentEntity appSprayPaintIndentEntity) {
        Integer technicianId = appSprayPaintIndentEntity.getTechnicianId();
        AppUserBMessageEntity appUserBMessageEntity = appUserBMessageService.selectById(technicianId);
        BUserEntity bUserEntity = bUserMapper.updateBalance(appUserBMessageEntity.getUserBId().toString());
        //商户加钱
        BigDecimal price = appSprayPaintIndentEntity.getPrice();
        //商户加钱80%
        BigDecimal multiply = price.multiply(new BigDecimal("0.7").setScale(2, BigDecimal.ROUND_HALF_UP));
        bUserEntity.setBalance(bUserEntity.getBalance().add(multiply));

        AppUserAccountRecordEntity appUserAccount = new AppUserAccountRecordEntity();
        appUserAccount.setMomey(multiply);
        appUserAccount.setUserId(appSprayPaintIndentEntity.getUserBId());
        appUserAccount.setType(16);
        appUserAccount.setCreateTime(new Date());
        appUserAccount.setAddFlag(1);
        appUserAccount.setSource(1);
        appUserAccount.setBusinessId(appSprayPaintIndentEntity.getSprayPaintNumber());
        appUserAccountRecordMapper.insert(appUserAccount);

        AppUserAccountRecordEntity chejiBalance = new AppUserAccountRecordEntity();
        chejiBalance.setMomey(price.subtract(multiply));
        chejiBalance.setUserId(appSprayPaintIndentEntity.getUserBId());
        chejiBalance.setType(-1);
        chejiBalance.setCreateTime(new Date());
        chejiBalance.setAddFlag(1);
        chejiBalance.setSource(1);
        chejiBalance.setBusinessId(appSprayPaintIndentEntity.getSprayPaintNumber());
        appUserAccountRecordMapper.insert(chejiBalance);

        bUserMapper.updateById(bUserEntity);


    }

    public Personal findIndentCount(String userId) {
        return appSprayPaintIndentMapper.findIndentCount(userId);
    }

    public List<Integer> findOtherIndent(Integer userid) {
        return appSprayPaintIndentMapper.findOtherIndent(userid);
    }

    //查询等待技师时的数据
    public JSONObject findmessage(AppSprayPaintIndentEntity appSprayPaintIndentEntity) {
        JSONObject json = new JSONObject();
        Integer technicianId = appSprayPaintIndentEntity.getTechnicianId();
        Integer userBId = appSprayPaintIndentEntity.getUserBId();

        //商户经纬度，技师经纬度，头像，名称，编号，技龄，驾龄，分数，商户电话
        BUserEntity bUserEntity = bUserService.selectById(userBId);

        json.put("id",appSprayPaintIndentEntity.getId());

        json.put("state",appSprayPaintIndentEntity.getState());
        //技师id
        json.put("technicianId",technicianId);
        json.put("technicianPhone",appSprayPaintIndentEntity.getPhone());

        //商户经纬度
        json.put("merchantsLat",bUserEntity.getLat());
        json.put("merchantsLng",bUserEntity.getLng());
        json.put("merchantsAddress",bUserEntity.getAddress());
        json.put("merchantsId",bUserEntity.getId());
        //技师经纬度
        AppUserBMessageEntity appUserBMessageEntity = appUserBMessageService.selectById(technicianId);
        if (appUserBMessageEntity==null){
            return null;
        }
        appUserBMessageEntity.setOrderNumber(appUserBMessageEntity.getOrderNumber()+1);
        appUserBMessageService.updateById(appUserBMessageEntity);
        json.put("technicianLng",appUserBMessageEntity.getLng());
        json.put("technicianLat",appUserBMessageEntity.getLat());
        json.put("technicianAddress",appUserBMessageEntity.getWorkPlace());
        //头像
        json.put("headImg",appUserBMessageEntity.getHeadImg());
        //名称
        json.put("name",appUserBMessageEntity.getName());
        //编号
        json.put("serialNumber",appUserBMessageEntity.getSerialNumber());
        //技龄
        json.put("technologyYear",appUserBMessageEntity.getTechnologyYear());
        //驾龄
        json.put("driverYear",appUserBMessageEntity.getDriverYear());
        //分数
        json.put("score",appUserBMessageEntity.getScore());
        //商户电话
        json.put("merchantsPhone",bUserEntity.getMerchantsPhone());
        return json;

    }

    public AppSprayPaintIndentEntity selectByNumber(String orderNumber) {
        return appSprayPaintIndentMapper.selectByNumber(orderNumber);
    }
}
