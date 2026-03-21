package com.cheji.b.modular.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.AppCleanIndetEntity;
import com.cheji.b.modular.domain.AppSubstituteDrivingIndentEntity;
import com.cheji.b.modular.domain.AppUserBMessageEntity;
import com.cheji.b.modular.mapper.AppSprayPaintDetailsMapper;
import com.cheji.b.modular.mapper.AppSprayPaintIndentMapper;
import com.cheji.b.modular.mapper.AppSubstituteDrivingIndentMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
 * @since 2020-06-09
 */
@Service
public class AppSubstituteDrivingIndentService extends ServiceImpl<AppSubstituteDrivingIndentMapper, AppSubstituteDrivingIndentEntity> implements IService<AppSubstituteDrivingIndentEntity> {

    @Resource
    private AppUserBMessageService appUserBMessageService;

    @Resource
    private AppSubstituteDrivingIndentMapper appSubstituteDrivingIndentMapper;

    @Resource
    private AppSprayPaintIndentMapper appSprayPaintIndentMapper;


    public Integer gettime(AppSubstituteDrivingIndentEntity appSubstitute) {
        //计算等待时间

        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long pickCarTime = df.parse(df.format(appSubstitute.getPickCarTime())).getTime();
            //从对象中拿到时间
            long beginTime = df.parse(df.format(appSubstitute.getBeginTime())).getTime();
            long diff = (beginTime - pickCarTime) / 1000 / 60;
            return (int) diff;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public JSONObject findSubDirCenterDetails(Integer userBId) {
        JSONObject object = new JSONObject();
        //查询头像，编号，名称，电话， 本月收入，本月次数，总收入，总次数
        EntityWrapper<AppUserBMessageEntity> appUserBMessageWrapper = new EntityWrapper<>();
        appUserBMessageWrapper.eq("user_b_id", userBId)
                .eq("wrok_type", 4);
        AppUserBMessageEntity appUserBMessageEntity = appUserBMessageService.selectOne(appUserBMessageWrapper);
        if (appUserBMessageEntity == null) {
            return null;
        }
        object.put("headImg", appUserBMessageEntity.getHeadImg());
        object.put("name", appUserBMessageEntity.getName());
        object.put("serialNumber", appUserBMessageEntity.getSerialNumber());
        object.put("phone", appUserBMessageEntity.getPhone());
        object.put("businessType",appUserBMessageEntity.getBusinessType());

        //查询  本月收入，本月次数，总收入，总次数
        AppSubstituteDrivingIndentEntity monthMess = appSubstituteDrivingIndentMapper.selectMonthIncome(appUserBMessageEntity.getId());
        BigDecimal monthIncome = monthMess.getMonthIncome();
        if (monthIncome == null) {
            object.put("monthIncome", 0);
        } else {//setScale(10, BigDecimal.ROUND_HALF_UP);
            BigDecimal multiply = monthIncome.multiply(new BigDecimal("0.8"));
            object.put("monthIncome",multiply.setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        Integer monthCount = monthMess.getMonthCount();
        if (monthCount == null) {
            object.put("monthCount", 0);
        } else {
            object.put("monthCount", monthMess.getMonthCount());
        }

        AppSubstituteDrivingIndentEntity allMess = appSubstituteDrivingIndentMapper.selectAllMess(appUserBMessageEntity.getId());

        BigDecimal income = allMess.getIncome();
        if (income == null) {
            object.put("income", 0);
        } else {
            BigDecimal multiply = income.multiply(new BigDecimal("0.8"));
            object.put("income", multiply.setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        Integer count = allMess.getCount();
        if (count == null) {
            object.put("count", 0);
        } else {
            object.put("count", allMess.getCount());
        }

        return object;
    }

    public List<AppCleanIndetEntity> newSubDriIndentList(Integer userBId, Integer type, Integer pagesize) {
        //查询到技师id
        pagesize = (pagesize-1)*20;
        //查询到代驾技师id
        EntityWrapper<AppUserBMessageEntity> appUserBMessage = new EntityWrapper<>();
        appUserBMessage.eq("user_b_id",userBId)
                .eq("wrok_type",4);
        AppUserBMessageEntity bMessage = appUserBMessageService.selectOne(appUserBMessage);
        Integer id  = bMessage.getId();

        List<AppCleanIndetEntity> substituteDriIndentList = appSubstituteDrivingIndentMapper.newSubDriIndentList(id,type,pagesize);
        for (AppCleanIndetEntity appCleanIndetEntity : substituteDriIndentList) {

            Integer integer = appSprayPaintIndentMapper.selectByCoupon(appCleanIndetEntity.getCleanIndentNumber());
            if (integer==null){
                appCleanIndetEntity.setAmount(appCleanIndetEntity.getAmount().multiply(new BigDecimal("0.8")).setScale(2,BigDecimal.ROUND_HALF_UP));
            }

            Date createTime = appCleanIndetEntity.getCreateTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(createTime);
            appCleanIndetEntity.setTime(dateString);
        }
        return substituteDriIndentList;

    }
}
