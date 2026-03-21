package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.AppSubstituteDrivingImgEntity;
import com.cheji.b.modular.mapper.AppSubstituteDrivingImgMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 代驾图片表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2020-06-09
 */
@Service
public class AppSubstituteDrivingImgService extends ServiceImpl<AppSubstituteDrivingImgMapper, AppSubstituteDrivingImgEntity> implements IService<AppSubstituteDrivingImgEntity> {

    public void saveindentImg(String[] url, Integer id,Integer type) {
        //保存表
        for (int i = 0; i < url.length; i++) {
            AppSubstituteDrivingImgEntity imgEntity = new AppSubstituteDrivingImgEntity();
            imgEntity.setIndex(i+1);
            imgEntity.setSubsituteDrivingId(id);
            imgEntity.setType(type);
            imgEntity.setUrl(url[i]);
            imgEntity.setCreateTime(new Date());
            imgEntity.setUpdateTime(new Date());
            this.insert(imgEntity);
        }
    }
}
