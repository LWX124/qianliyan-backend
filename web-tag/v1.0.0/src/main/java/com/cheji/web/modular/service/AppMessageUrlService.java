package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.web.modular.domain.AppMessageUrlEntity;
import com.cheji.web.modular.mapper.AppMessageCarMapper;
import com.cheji.web.modular.mapper.AppMessageUrlMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2021-01-14
 */
@Service
public class AppMessageUrlService extends ServiceImpl<AppMessageUrlMapper, AppMessageUrlEntity> implements IService<AppMessageUrlEntity> {

    @Resource
    private AppMessageCarMapper appMessageCarMapper;

    @Resource
    private AppMessageUrlMapper appMessageUrlMapper;

    public List<String> selectScenePhotos(Long id,Integer source) {
        return appMessageCarMapper.selectScenePhotos(id,source);
    }

    public List<String> selectUrlPhotos(Integer cid, Integer type, Integer source) {
        return appMessageUrlMapper.selectUrlPhotos(cid,type,source);
    }
}
