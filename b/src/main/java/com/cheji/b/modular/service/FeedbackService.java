package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.FeedbackEntity;
import com.cheji.b.modular.mapper.FeedbackMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-09-28
 */
@Service
public class FeedbackService extends ServiceImpl<FeedbackMapper, FeedbackEntity> implements IService<FeedbackEntity> {

    public void saveFeed(FeedbackEntity feedbackEntity) {
        insert(feedbackEntity);
    }
}
