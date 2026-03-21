package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppSendUrlEntity;
import com.cheji.web.modular.mapper.AppSendUrlMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 派单记录图片表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2021-01-13
 */
@Service
public class AppSendUrlService extends ServiceImpl<AppSendUrlMapper, AppSendUrlEntity> {

    @Resource
    private AppSendUrlMapper appSendUrlMapper;

    public String findMapImg(Integer id) {
        return appSendUrlMapper.findMapImg(id);
    }

    public String[] findAccidentImg(Integer id) {
        return appSendUrlMapper.findAccidentImg(id);
    }

    public String findCheckImg(Long id) {
        return appSendUrlMapper.findCheckImg(id);
    }
}
