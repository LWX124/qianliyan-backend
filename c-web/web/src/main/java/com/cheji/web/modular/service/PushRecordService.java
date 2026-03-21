package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.PushRecordEntity;
import com.cheji.web.modular.mapper.PushRecordMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * app事故推送记录表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-08-27
 */
@Service
public class PushRecordService extends ServiceImpl<PushRecordMapper, PushRecordEntity> implements IService<PushRecordEntity> {

    //消息保存到推送记录表里面
    public void save(String s, Integer id) {
        PushRecordEntity pushRecordEntity = new PushRecordEntity();
        pushRecordEntity.setUserId(s);
        pushRecordEntity.setAccid(id);
        pushRecordEntity.setCreateTime(new Date());
        pushRecordEntity.setUpdateTime(new Date());
        insert(pushRecordEntity);
    }

    public List<PushRecordEntity> findRecoreByUserId(String userId) {
        EntityWrapper<PushRecordEntity> wrapper = new EntityWrapper<>();
        //查询条件
        wrapper.eq("user_id", userId);
        List<PushRecordEntity> pushRecordEntities = selectList(wrapper);
        return pushRecordEntities;
    }
}
