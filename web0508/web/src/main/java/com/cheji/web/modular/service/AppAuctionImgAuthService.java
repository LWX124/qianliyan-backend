package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppAuctionImgAdvEntity;
import com.cheji.web.modular.domain.AppAuctionImgAuthEntity;
import com.cheji.web.modular.domain.AppAuctionMessageIdentifyEntity;
import com.cheji.web.modular.domain.UserEntity;
import com.cheji.web.modular.mapper.AppAuctionImgAdvMapper;
import com.cheji.web.modular.mapper.AppAuctionImgAuthMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *     认证图片
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionImgAuthService extends ServiceImpl<AppAuctionImgAuthMapper, AppAuctionImgAuthEntity> implements IService<AppAuctionImgAuthEntity> {

    @Autowired
    private UserService userService;

    @Autowired
    private AppAuctionImgAuthService appAuctionImgAuthService;

    @Autowired
    private AppAuctionMessageIdentifyService appAuctionMessageIdentifyService;

    public JSONObject addAuthentication(AppAuctionMessageIdentifyEntity auth, Integer id) {
        JSONObject object = new JSONObject();
        List<AppAuctionImgAuthEntity> authImg = auth.getAuthImg();
        String type = auth.getType();
        UserEntity userEntity = userService.selectById(id);
        if(userEntity.getAuthentication() != null){
            object.put("code",200);
            object.put("msg","该账号已认证!");
            return object;
        }

        if(authImg.size() > 0){
            List<AppAuctionImgAuthEntity> collect = authImg.stream().map(a -> {
                AppAuctionImgAuthEntity appAuctionImgAuthEntity = new AppAuctionImgAuthEntity();
                appAuctionImgAuthEntity.setUrl(a.getUrl());
                appAuctionImgAuthEntity.setCreateTime(new Date());
                appAuctionImgAuthEntity.setType(type);
                appAuctionImgAuthEntity.setUserId(Long.valueOf(id));
                return appAuctionImgAuthEntity;
            }).collect(Collectors.toList());
            if(appAuctionImgAuthService.insertBatch(collect)){
                auth.setCreateTime(new Date());
                auth.setUserId(Long.valueOf(id));
                appAuctionMessageIdentifyService.insert(auth);
                //更改认证状态
                userEntity.setAuthentication(type);
                userService.updateById(userEntity);
                object.put("code",200);
                object.put("msg","操作成功!");
                return object;
            }
        }
        object.put("code",202);
        object.put("msg","未知错误!");
        return object;
    }

    public JSONObject getAuthentication(JSONObject result, Integer id) {
        if(id != null){
            AppAuctionMessageIdentifyEntity message = appAuctionMessageIdentifyService.selectOne(new EntityWrapper<AppAuctionMessageIdentifyEntity>().eq("user_id", id));
            List<AppAuctionImgAuthEntity> imgAuths = selectList(new EntityWrapper<AppAuctionImgAuthEntity>().eq("user_id", id));
            if(message != null && imgAuths != null){
                message.setAuthImg(imgAuths);
                result.put("code",200);
                result.put("msg","查询成功!");
                result.put("data",message);
                return result;
            }
            result.put("code",200);
            result.put("msg","信息不全,请联系管理员!");
            return result;
        }
        result.put("code",202);
        result.put("msg","用户不存在!");
        return result;
    }
}
