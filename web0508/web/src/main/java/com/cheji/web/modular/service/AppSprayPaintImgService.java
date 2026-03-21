package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.web.modular.domain.AppSprayPaintImgEntity;
import com.cheji.web.modular.mapper.AppSprayPaintImgMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 喷漆图片表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2020-03-13
 */
@Service
public class AppSprayPaintImgService extends ServiceImpl<AppSprayPaintImgMapper, AppSprayPaintImgEntity> implements IService<AppSprayPaintImgEntity> {

    @Resource
    private AppSprayPaintImgMapper appSprayPaintImgMapper;

    public List<AppSprayPaintImgEntity> selectInService(Integer id) {
        return appSprayPaintImgMapper.selectInService(id);
    }
}
