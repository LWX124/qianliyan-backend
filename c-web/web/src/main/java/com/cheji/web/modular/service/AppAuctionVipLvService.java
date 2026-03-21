package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.constant.AppAuctionConstant;
import com.cheji.web.modular.domain.AppAuctionImgAdvEntity;
import com.cheji.web.modular.domain.AppAuctionVipControlEntity;
import com.cheji.web.modular.domain.AppAuctionVipLvEntity;
import com.cheji.web.modular.domain.UserEntity;
import com.cheji.web.modular.mapper.AppAuctionVipLvMapper;
import com.cheji.web.util.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *     vip设置
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionVipLvService extends ServiceImpl<AppAuctionVipLvMapper, AppAuctionVipLvEntity> implements IService<AppAuctionVipLvEntity> {

    @Autowired
    private UserService userService;

    @Autowired
    private AppAuctionImgAdvService appAuctionImgAdvService;

    @Autowired
    private AppAuctionVipControlService appAuctionVipControlService;

    public JSONObject vipExplain(Integer userId) {
        JSONObject result = new JSONObject();
        UserEntity userEntity = userService.selectById(userId);
        Integer lv = userEntity.getVipLv();
        String authentication = userEntity.getAuthentication();
        AppAuctionVipControlEntity vipCon = appAuctionVipControlService.selectOne(
                new EntityWrapper<AppAuctionVipControlEntity>()
                        .eq("user_id", userId)
                        .eq("state", AppAuctionConstant.ONE));
        if(Objects.isNull(authentication)){
            result.put("code",202);
            result.put("msg","请先认证身份!");
            return result;
        }

        if(lv == null || vipCon == null){
            result.put("code",201);
            result.put("msg","您还不是vip,请升级vip!");
            return result;
        }


        EntityWrapper<AppAuctionVipLvEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("lv",vipCon.getVipLv());
        AppAuctionVipLvEntity appAuctionVipLvEntity = selectOne(wrapper);

        String head = userEntity.getAvatar();
        String name = userEntity.getName();
        result.put("code",200);
        result.put("msg","查询成功!");
        result.put("data",vipLv2DTO(appAuctionVipLvEntity, vipCon, name ,head));
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public JSONObject vipSet(AppAuctionVipLvEntity vip) {
        JSONObject result = new JSONObject();
        Long id = vip.getId();
        List<AppAuctionImgAdvEntity> imgList = vip.getImgList();
        if(id == null){
            insert(vip);
            if(imgList.size() > 0){
                imgList.stream().forEach(a -> {
                    if(Objects.equals(a.getId(),null)){
                        appAuctionImgAdvService.insert(a);
                    }else {
                        appAuctionImgAdvService.updateById(a);
                    }
                });
            }
        }else {
            updateById(vip);
            if(imgList.size() > 0){
                imgList.stream().forEach(a -> {
                    if(Objects.equals(a.getId(),null)){
                        appAuctionImgAdvService.insert(a);
                    }else {
                        appAuctionImgAdvService.updateById(a);
                    }
                });
            }
        }
        result.put("code",200);
        result.put("msg","操作成功!");
        return result;
    }

    public JSONObject vipInfoList() {
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        List<AppAuctionVipLvEntity> appAuctionVipLvEntities = selectList(new EntityWrapper<>());
        for (AppAuctionVipLvEntity appAuctionVipLvEntity : appAuctionVipLvEntities) {
            EntityWrapper<AppAuctionImgAdvEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("vip_lv",appAuctionVipLvEntity.getLv())
                    .eq("type","2")
                    .and("(state = '1' or state like '%3%')");
            List<AppAuctionImgAdvEntity> appAuctionImgAdvEntities = appAuctionImgAdvService.selectList(wrapper);
            appAuctionVipLvEntity.setImgList(appAuctionImgAdvEntities);
            appAuctionVipLvEntity.setOpenTitle("保证金开通说明");
            appAuctionVipLvEntity.setCloseTitle("保证金结算说明");
            array.add(appAuctionVipLvEntity);
        }
        result.put("code",200);
        result.put("msg","查询成功");
        result.put("data",array);
        return result;
    }

    private JSONObject vipLv2DTO(AppAuctionVipLvEntity vipLv, AppAuctionVipControlEntity vipCon,String name,String head){
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            AssertUtil.isNotNull(vipLv,"请传入参数!");
        }catch (Exception e){
            object.put("code",510);
            object.put("msg","传参错误");
            return object;
        }
        EntityWrapper<AppAuctionImgAdvEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("vip_lv",vipLv.getLv())
                .eq("type","2")
                .and("(state = '2' or state = '4' or state = '5' or state like '%3%')");
        List<AppAuctionImgAdvEntity> appAuctionImgAdvEntities = appAuctionImgAdvService.selectList(wrapper);
        if(appAuctionImgAdvEntities.size() > 0){
            appAuctionImgAdvEntities.forEach(a -> array.add(a));
        }
        object.put("lvName",vipLv.getLvName());
        object.put("name",name);
        object.put("head",head);
        //签约统计
        vipCon.setAmount(vipCon.getAmount().divide(new BigDecimal("100.00")));
        object.put("signData",vipCon);
        object.put("imgList",array);
        return object;
    }
}
