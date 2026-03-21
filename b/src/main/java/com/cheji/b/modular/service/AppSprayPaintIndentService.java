package com.cheji.b.modular.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.*;
import com.cheji.b.modular.dto.CenterDetailsDto;
import com.cheji.b.modular.mapper.AppSprayPaintIndentMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.text.html.parser.Entity;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 喷漆订单表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2020-03-17
 */
@Service
public class AppSprayPaintIndentService extends ServiceImpl<AppSprayPaintIndentMapper, AppSprayPaintIndentEntity> implements IService<AppSprayPaintIndentEntity> {
    @Resource
    private AppSprayPaintIndentMapper appSprayPaintIndentMapper;

    @Resource
    private AppSprayPaintImgService appSprayPaintImgService;

    @Resource
    private AppUserBMessageService appUserBMessageService;


    public CenterDetailsDto selectSprayIndentCenterMes(Integer userBId) {
        return appSprayPaintIndentMapper.selectSprayIndentCenterMes(userBId);
    }

    public Integer selectByCoupon(String cleanIndentNumber) {
        return appSprayPaintIndentMapper.selectByCoupon(cleanIndentNumber);
    }

    public List<AppCleanIndetEntity> newSprayPaintIndentList(Integer userBId, Integer pagesize, Integer type) {
        pagesize = (pagesize-1)*20;
        //查询到技师id
        EntityWrapper<AppUserBMessageEntity> appUserBMessage = new EntityWrapper<>();
        appUserBMessage.eq("user_b_id",userBId)
                .eq("wrok_type",3);
        AppUserBMessageEntity bMessage = appUserBMessageService.selectOne(appUserBMessage);
        Integer id = bMessage.getId();
        List<AppCleanIndetEntity> appCleanIndetEntities = appSprayPaintIndentMapper.newSprayPaintIndentList(id, pagesize, type);
        for (AppCleanIndetEntity appCleanIndetEntity : appCleanIndetEntities) {
            //判断是否使用优惠卷
            Integer cleanIndetEntity = appSprayPaintIndentMapper.selectByCoupon(appCleanIndetEntity.getCleanIndentNumber());
            if (cleanIndetEntity!=null){
                appCleanIndetEntity.setAmount(appCleanIndetEntity.getAmount().multiply(new BigDecimal("0.7")).setScale(2,BigDecimal.ROUND_HALF_UP));
            }
            Date createTime = appCleanIndetEntity.getCreateTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(createTime);
            appCleanIndetEntity.setTime(dateString);
        }
        return appCleanIndetEntities;
    }

    public JSONObject updataIndentState(JSONObject in, JSONArray url, AppSprayPaintIndentEntity appSprayPaintIndentEntity) {
        JSONObject result = new JSONObject();
        String type = in.getString("type");
        if (StringUtils.isEmpty(type)) {
            result.put("code", 408);
            return result;
        }

        //1.拍照接车，2.服务完成，3.交车完成
        if (type.equals("1")) {
            if (appSprayPaintIndentEntity.getState() != 3) {
                result.put("code", 408);
                result.put("msg", "订单状态不能操作");
                return result;
            }
            List<String> urls = JSONArray.parseArray(url.toString(), String.class);
            //保存喷漆接车图片
            for (int i = 0; i < urls.size(); i++) {
                String s = urls.get(i);
                AppSprayPaintImgEntity appSprayPaintImgEntity = new AppSprayPaintImgEntity();
                appSprayPaintImgEntity.setUrl(s);
                appSprayPaintImgEntity.setType(2);
                appSprayPaintImgEntity.setIndex(i + 1);
                appSprayPaintImgEntity.setSprayPaintId(appSprayPaintIndentEntity.getId());
                appSprayPaintImgEntity.setCreateTime(new Date());
                appSprayPaintImgEntity.setUpdateTime(new Date());
                appSprayPaintImgService.insert(appSprayPaintImgEntity);
            }
            appSprayPaintIndentEntity.setState(4);
            appSprayPaintIndentEntity.setPickCarTime(new Date());
            appSprayPaintIndentEntity.setUpdateTime(new Date());
            updateById(appSprayPaintIndentEntity);
        } else if (type.equals("2")) {

            List<String> urls = JSONArray.parseArray(url.toString(), String.class);
            //先删除
            //查询图片
            EntityWrapper<AppSprayPaintImgEntity> paintImgWrapper = new EntityWrapper<>();
            paintImgWrapper.eq("spray_paint_id", appSprayPaintIndentEntity.getId())
                    .eq("type", 3);
            List<AppSprayPaintImgEntity> paintImgEntities = appSprayPaintImgService.selectList(paintImgWrapper);

            if (!paintImgEntities.isEmpty()) {
                for (AppSprayPaintImgEntity paintImgEntity : paintImgEntities) {
                    appSprayPaintImgService.deleteById(paintImgEntity);
                }
            }
            //覆盖图片
            for (int i = 0; i < urls.size(); i++) {
                String s = urls.get(i);
                AppSprayPaintImgEntity appSprayPaintImgEntity = new AppSprayPaintImgEntity();
                appSprayPaintImgEntity.setUrl(s);
                appSprayPaintImgEntity.setType(3);
                appSprayPaintImgEntity.setIndex(i + 1);
                appSprayPaintImgEntity.setSprayPaintId(appSprayPaintIndentEntity.getId());
                appSprayPaintImgEntity.setCreateTime(new Date());
                appSprayPaintImgEntity.setUpdateTime(new Date());
                appSprayPaintImgService.insert(appSprayPaintImgEntity);
            }
            //服务完成
        } else if (type.equals("3")) {
            List<String> urls = JSONArray.parseArray(url.toString(), String.class);
            if (appSprayPaintIndentEntity.getState() != 5) {
                result.put("code", 408);
                result.put("msg", "订单状态不能操作");
                return result;
            }
            //先删除
            //查询图片
            EntityWrapper<AppSprayPaintImgEntity> paintImgWrapper = new EntityWrapper<>();
            paintImgWrapper.eq("spray_paint_id", appSprayPaintIndentEntity.getId())
                    .eq("type", 3);
            List<AppSprayPaintImgEntity> paintImgEntities = appSprayPaintImgService.selectList(paintImgWrapper);

            if (!paintImgEntities.isEmpty()) {
                for (AppSprayPaintImgEntity paintImgEntity : paintImgEntities) {
                    appSprayPaintImgService.deleteById(paintImgEntity);
                }
            }
            //覆盖图片
            for (int i = 0; i < urls.size(); i++) {
                String s = urls.get(i);
                AppSprayPaintImgEntity appSprayPaintImgEntity = new AppSprayPaintImgEntity();
                appSprayPaintImgEntity.setUrl(s);
                appSprayPaintImgEntity.setType(3);
                appSprayPaintImgEntity.setIndex(i + 1);
                appSprayPaintImgEntity.setSprayPaintId(appSprayPaintIndentEntity.getId());
                appSprayPaintImgEntity.setCreateTime(new Date());
                appSprayPaintImgEntity.setUpdateTime(new Date());
                appSprayPaintImgService.insert(appSprayPaintImgEntity);
            }

            appSprayPaintIndentEntity.setState(6);
            updateById(appSprayPaintIndentEntity);
        } else if (type.equals("4")) {
            //交车完成
            if (appSprayPaintIndentEntity.getState() != 6) {
                result.put("code", 408);
                result.put("msg", "订单状态不能操作");
                return result;
            }
            List<String> urls = JSONArray.parseArray(url.toString(), String.class);
            for (int i = 0; i < urls.size(); i++) {
                String s = urls.get(i);
                AppSprayPaintImgEntity appSprayPaintImgEntity = new AppSprayPaintImgEntity();
                appSprayPaintImgEntity.setUrl(s);
                appSprayPaintImgEntity.setType(4);
                appSprayPaintImgEntity.setIndex(i + 1);
                appSprayPaintImgEntity.setSprayPaintId(appSprayPaintIndentEntity.getId());
                appSprayPaintImgEntity.setCreateTime(new Date());
                appSprayPaintImgEntity.setUpdateTime(new Date());
                appSprayPaintImgService.insert(appSprayPaintImgEntity);
            }

            appSprayPaintIndentEntity.setState(7);
            updateById(appSprayPaintIndentEntity);
        }
        result.put("code", 200);
        return result;
    }
}
