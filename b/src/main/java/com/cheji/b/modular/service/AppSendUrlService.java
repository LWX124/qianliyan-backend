package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.b.modular.domain.AppSendUrlEntity;
import com.cheji.b.modular.mapper.AppSendUrlMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 派单记录图片表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2020-10-15
 */
@Service
public class AppSendUrlService extends ServiceImpl<AppSendUrlMapper, AppSendUrlEntity> implements IService<AppSendUrlEntity> {


    @Resource
    private AppSendUrlMapper appSendUrlMapper;


    public String findimgById(Integer id) {
        return appSendUrlMapper.findimgByid(id);
    }

    public List<String> findallsendImg(Integer id) {
        return appSendUrlMapper.findallsendImg(id);
    }

    public List<AppSendUrlEntity> findCheckImgList(Integer id) {
        return appSendUrlMapper.findCheckImgList(id);
    }

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
