package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppUploadRecordEntity;
import com.cheji.web.modular.mapper.AppUploadRecordMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUploadRecordService extends ServiceImpl<AppUploadRecordMapper, AppUploadRecordEntity> {

    public List<AppUploadRecordEntity> listByUserId(Integer userId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return baseMapper.selectList(
                new EntityWrapper<AppUploadRecordEntity>()
                        .eq("user_id", userId)
                        .orderBy("create_time", false)
                        .last("LIMIT " + offset + "," + pageSize)
        );
    }

    public int countByUserId(Integer userId) {
        return baseMapper.selectCount(
                new EntityWrapper<AppUploadRecordEntity>().eq("user_id", userId)
        );
    }
}
