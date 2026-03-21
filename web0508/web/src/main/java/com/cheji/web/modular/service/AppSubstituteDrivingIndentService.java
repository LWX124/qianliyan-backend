package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.cheji.web.modular.cwork.Personal;
import com.cheji.web.modular.domain.AppSubstituteDrivingIndentEntity;
import com.cheji.web.modular.domain.AppUserAccountRecordEntity;
import com.cheji.web.modular.domain.AppUserBMessageEntity;
import com.cheji.web.modular.domain.BUserEntity;
import com.cheji.web.modular.mapper.AppSubstituteDrivingIndentMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.mapper.AppUserAccountRecordMapper;
import com.cheji.web.modular.mapper.BUserMapper;
import com.cheji.web.util.MapNavUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 代驾订单表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2020-06-08
 */
@Service
public class AppSubstituteDrivingIndentService extends ServiceImpl<AppSubstituteDrivingIndentMapper, AppSubstituteDrivingIndentEntity> implements IService<AppSubstituteDrivingIndentEntity> {
    @Resource
    private AppUserBMessageService appUserBMessageService;

    @Resource
    private BUserMapper bUserMapper;

    @Resource
    private AppUserAccountRecordMapper appUserAccountRecordMapper;

    @Resource
    private AppSubstituteDrivingIndentMapper appSubstituteDrivingIndentMapper;

    private static final String key = "a11e3020a4b82ce9390044286910f02f";

    public JSONObject getprice(Integer type, BigDecimal startLng, BigDecimal startLat, BigDecimal endLng, BigDecimal endLat) {
        JSONObject object = new JSONObject();
        MapNavUtil mapNavUtil = new MapNavUtil(startLng + "," + startLat, endLng + "," + endLat, key, 1, "JSON");
        Long results = mapNavUtil.getResults();
        BigDecimal distance2 = new BigDecimal(results);
        distance2 = distance2.divide(new BigDecimal("1000"), 2, BigDecimal.ROUND_HALF_UP);
//        if (type == 1) {
//            BigDecimal price = new BigDecimal("0.1");
//            object.put("price", price);
//        } else {
//            BigDecimal price = new BigDecimal("0.2");
//            object.put("price", price);
//        }
        int falg = distance2.compareTo(new BigDecimal("8"));//和8公里比较
        if (falg > 0) {
            BigDecimal subtract = distance2.subtract(new BigDecimal("8"));//超出8公里的部分
            BigDecimal multiply = subtract.multiply(new BigDecimal("3"));
            //算出来的价格四舍五入
            BigDecimal price = multiply.setScale(2, BigDecimal.ROUND_HALF_UP);// 0.24
            price = price.add(new BigDecimal("30"));
            object.put("price", price);
        } else {
            BigDecimal price = new BigDecimal("30");
            object.put("price", price);
        }
        object.put("distance", distance2);
        return object;
    }


    @Transactional(rollbackFor = Exception.class)
    public void substituAddMerchantsAmount(AppSubstituteDrivingIndentEntity appSubstituteDrivingIndent, Integer type) {
        //技师id
        Integer userBId = appSubstituteDrivingIndent.getUserBId();//技师id
        AppUserBMessageEntity appUserBMessageEntity = appUserBMessageService.selectById(userBId);//技师

        BUserEntity bUserEntity = bUserMapper.updateBalance(appUserBMessageEntity.getUserBId().toString());

        //商户加钱
        BigDecimal actualPrice = appSubstituteDrivingIndent.getActualPrice();
        if (type==0) {
           // BigDecimal multiply = actualPrice.multiply(new BigDecimal("0.8").setScale(2, BigDecimal.ROUND_HALF_UP));
            bUserEntity.setBalance(bUserEntity.getBalance().add(actualPrice));

            AppUserAccountRecordEntity appUserAccountRecord = new AppUserAccountRecordEntity();
            appUserAccountRecord.setUserId(appSubstituteDrivingIndent.getUserBId());
            appUserAccountRecord.setMomey(actualPrice);
            appUserAccountRecord.setType(18);
            appUserAccountRecord.setCreateTime(new Date());
            appUserAccountRecord.setAddFlag(1);
            appUserAccountRecord.setSource(1);
            appUserAccountRecord.setBusinessId(appSubstituteDrivingIndent.getSubstituteDrivingNumber());
            appUserAccountRecordMapper.insert(appUserAccountRecord);

//            AppUserAccountRecordEntity chejiBalance = new AppUserAccountRecordEntity();
//            chejiBalance.setMomey(actualPrice.subtract(actualPrice));
//            chejiBalance.setUserId(appUserBMessageEntity.getUserBId());
//            chejiBalance.setType(-1);
//            chejiBalance.setCreateTime(new Date());
//            chejiBalance.setAddFlag(1);
//            chejiBalance.setSource(1);
//            chejiBalance.setBusinessId(appSubstituteDrivingIndent.getSubstituteDrivingNumber());
//            appUserAccountRecordMapper.insert(chejiBalance);
        }else {
            bUserEntity.setBalance(bUserEntity.getBalance().add(actualPrice));

            AppUserAccountRecordEntity appUserAccountRecord = new AppUserAccountRecordEntity();
            appUserAccountRecord.setUserId(appSubstituteDrivingIndent.getUserBId());
            appUserAccountRecord.setMomey(actualPrice);
            appUserAccountRecord.setType(18);
            appUserAccountRecord.setCreateTime(new Date());
            appUserAccountRecord.setAddFlag(1);
            appUserAccountRecord.setSource(1);
            appUserAccountRecord.setBusinessId(appSubstituteDrivingIndent.getSubstituteDrivingNumber());
            appUserAccountRecordMapper.insert(appUserAccountRecord);
        }
        bUserMapper.updateById(bUserEntity);


    }

    public Integer getTime(AppSubstituteDrivingIndentEntity appSubstitute) {
        //计算等待时间
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long pickCarTime = df.parse(df.format(appSubstitute.getPickCarTime())).getTime();
            //从对象中拿到时间
            long beginTime = df.parse(df.format(appSubstitute.getEndTime())).getTime();
            long diff = (beginTime - pickCarTime) / 1000 / 60;
            return (int) diff;
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

    public Personal findIndentCount(String userId) {
        return appSubstituteDrivingIndentMapper.findIndentCount(userId);
    }

    public JSONObject findMessage(AppSubstituteDrivingIndentEntity appSubstituteDrivingIndent) {
        JSONObject json = new JSONObject();
        Integer userBId = appSubstituteDrivingIndent.getUserBId();
        //技师经纬度，头像，名称，编号，技龄，驾龄，分数，电话
        AppUserBMessageEntity appUserBMessageEntity = appUserBMessageService.selectById(userBId);

        json.put("id",appSubstituteDrivingIndent.getId());

        json.put("state",appSubstituteDrivingIndent.getIndentState());
        //技师id
        json.put("technicianId",appUserBMessageEntity);
        json.put("technicianPhone",appUserBMessageEntity.getPhone());
        json.put("technicianLng",appUserBMessageEntity.getLng());
        json.put("technicianLat",appUserBMessageEntity.getLat());
        json.put("technicianAddress",appUserBMessageEntity.getWorkPlace());

        //起点终点经纬度
        json.put("startAddress",appSubstituteDrivingIndent.getStartAddress());
        json.put("startPoint",appSubstituteDrivingIndent.getStartPoint());
        json.put("startLng",appSubstituteDrivingIndent.getStartLng());
        json.put("startLat",appSubstituteDrivingIndent.getStartLat());

        json.put("endAddress",appSubstituteDrivingIndent.getEndAddress());
        json.put("endPoint",appSubstituteDrivingIndent.getEndPoint());
        json.put("endLng",appSubstituteDrivingIndent.getEndLng());
        json.put("endLat",appSubstituteDrivingIndent.getEndLat());

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
        json.put("merchantsPhone",appUserBMessageEntity.getPhone());

        json.put("userBId",appUserBMessageEntity.getUserBId());
        //时间
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString1 = formatter1.format(appSubstituteDrivingIndent.getUpdateTime());
        json.put("creatTime",dateString1);
        return json;
    }

    public List<Integer> selectNoPayList(Integer userid) {
        return appSubstituteDrivingIndentMapper.selectNoPayList(userid);

    }

    public AppSubstituteDrivingIndentEntity selectBySubNumber(String orderNumber) {
        return appSubstituteDrivingIndentMapper.selectBySubNumber(orderNumber);
    }
}
