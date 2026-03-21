package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cheji.web.modular.cwork.AllCommentDto;
import com.cheji.web.modular.cwork.BUserMessageDto;
import com.cheji.web.modular.cwork.MerchantsComment;
import com.cheji.web.modular.domain.AppUserBMessageEntity;
import com.cheji.web.modular.domain.MerchantsCommentsTreeEntity;
import com.cheji.web.modular.mapper.AppUserBMessageMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户服务信息表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2020-05-06
 */
@Service
public class AppUserBMessageService extends ServiceImpl<AppUserBMessageMapper, AppUserBMessageEntity> implements IService<AppUserBMessageEntity> {

    @Resource
    private AppUserBMessageMapper appUserBMessageMapper;

    @Resource
    private BUserService bUserService;

    @Resource
    private MerchantsCommentsTreeService merchantsCommentsTreeService;


    public List<BUserMessageDto> findInWork(Integer score,Integer pagesize) {
        pagesize = (pagesize - 1) * 20;
        return appUserBMessageMapper.findInWork(score,pagesize);
    }

    //查询技师详情
    public JSONObject findTechnicaianDetails(Integer technicianId, Integer pagesize,Integer genre) {
        //头像，评分，名称，服务次数，简介
        JSONObject as = new JSONObject();
        AppUserBMessageEntity appUserBMessageEntity = appUserBMessageMapper.selectById(technicianId);
        as.put("headImg", appUserBMessageEntity.getHeadImg());
        as.put("score", appUserBMessageEntity.getScore());
        as.put("name", appUserBMessageEntity.getName());
        as.put("orderNumber", appUserBMessageEntity.getOrderNumber());
        as.put("introduciton", appUserBMessageEntity.getIntroduction());

        //全部评价，好评，中评，差评，有图，
        //查询评价
        AllCommentDto allComment = merchantsCommentsTreeService.selectAllComment(appUserBMessageEntity.getUserBId());
        as.put("allCount",allComment.getAllCount());
        as.put("highPraiseCount",allComment.getHaveImgCount());
        as.put("midBabCount",allComment.getMidBabCount());
        as.put("badReviewCount",allComment.getBadReviewCount());
        as.put("haveImgCount",allComment.getHaveImgCount());

        //评论内容
        List<MerchantsComment> merchantsCommentByid = bUserService.findMerchantsCommentByid(String.valueOf(appUserBMessageEntity.getUserBId()), pagesize, 12,genre);
        as.put("comments", merchantsCommentByid);
        return as;

    }

    public AppUserBMessageEntity selectByBIdWorkType(Integer userBId, int i) {
        return appUserBMessageMapper.selectByBIdWorkType(userBId,i);
    }

    public AppUserBMessageEntity selectByUserId(Integer userBId) {
        return appUserBMessageMapper.selectByUserId(userBId);
    }
}
