package com.cheji.b.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.AppCleanIndetEntity;
import com.cheji.b.modular.domain.AppRescueIndentEntity;
import com.cheji.b.modular.domain.LableDetailsReviewTreeEntity;
import com.cheji.b.modular.dto.CenterDetailsDto;
import com.cheji.b.modular.excep.CusException;
import com.cheji.b.modular.mapper.AppRescueIndentMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * <p>
 * 救援表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2020-03-05
 */
@Service
public class AppRescueIndentService extends ServiceImpl<AppRescueIndentMapper, AppRescueIndentEntity> implements IService<AppRescueIndentEntity> {

    @Resource
    private LableDetailsReviewTreeService lableDetailsReviewTreeService;

    @Resource
    private AppRescueIndentMapper appRescueIndentMapper;


    //添加服务

    @Transactional(rollbackFor = Exception.class)
    public String  addService(Integer userBId,Integer lableid) {
        try {
            lableDetailsReviewTreeService.addFirst(userBId);
            EntityWrapper<LableDetailsReviewTreeEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("user_b_id", userBId)
                    .eq("lable_id", lableid);
            LableDetailsReviewTreeEntity lableDetailsReview = lableDetailsReviewTreeService.selectOne(wrapper);
            if (lableDetailsReview==null){
                lableDetailsReviewTreeService.addSecond(userBId,lableid,null);
            }else {
                lableDetailsReview.setState(0);
                lableDetailsReviewTreeService.updateById(lableDetailsReview);
                return "等待重新审核";
            }
        } catch (CusException e) {
            e.printStackTrace();
        }
        return "成功";
    }

    public CenterDetailsDto selectRescueIndentCenterMes(Integer userBId) {
        return appRescueIndentMapper.selelctRescueIndentCenterMes(userBId);
    }

    public List<AppCleanIndetEntity> newRescueIndentList(Integer userBId, Integer type, Integer pagesize) {
        pagesize = (pagesize-1)*20;
        List<AppCleanIndetEntity> rescueIndentList = appRescueIndentMapper.newRescueIndentList(userBId, type, pagesize);
        for (AppCleanIndetEntity appCleanIndetEntity : rescueIndentList) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(appCleanIndetEntity.getCreateTime());
            appCleanIndetEntity.setTime(dateString);
        }
        return rescueIndentList;
    }
}
