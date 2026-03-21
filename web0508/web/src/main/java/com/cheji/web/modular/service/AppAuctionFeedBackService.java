package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppAuctionFeedBackEntity;
import com.cheji.web.modular.mapper.AppAuctionFeedBackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *     反馈
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionFeedBackService extends ServiceImpl<AppAuctionFeedBackMapper, AppAuctionFeedBackEntity> implements IService<AppAuctionFeedBackEntity> {

    @Autowired
    private AppAuctionFeedBackService appAuctionFeedBackService;

    public JSONObject subFeedback(JSONObject result, String content, Integer id) {
        if (content != null) {
            List<AppAuctionFeedBackEntity> feeds = selectList(new EntityWrapper<AppAuctionFeedBackEntity>().eq("user_id", id));
            if(feeds.size() > 5){
                result.put("code",200);
                result.put("msg","您的建议已收到!");
                return result;
            }
            AppAuctionFeedBackEntity appAuctionFeedBackEntity = new AppAuctionFeedBackEntity();
            appAuctionFeedBackEntity.setUserId(Long.valueOf(id));
            appAuctionFeedBackEntity.setFeedback(content);
            appAuctionFeedBackService.insert(appAuctionFeedBackEntity);
            result.put("code",200);
            result.put("msg","成功!");
            return result;
        }else{
            result.put("code",200);
            result.put("msg","请填入建议内容");
            return result;
        }
    }
}
