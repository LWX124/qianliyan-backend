package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cheji.web.modular.domain.AppSubUsualAddressEntity;
import com.cheji.web.modular.mapper.AppSubUsualAddressMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 常用地址 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2020-06-10
 */
@Service
public class AppSubUsualAddressService extends ServiceImpl<AppSubUsualAddressMapper, AppSubUsualAddressEntity> implements IService<AppSubUsualAddressEntity> {

    @Resource
    private AppSubUsualAddressMapper appSubUsualAddressMapper;

    public List<AppSubUsualAddressEntity> selectHistoryAddress(Integer userid) {
        return appSubUsualAddressMapper.selectHistoryAddress(userid);
    }

    public List<AppSubUsualAddressEntity> selectUsualAddress(Integer userid) {
        EntityWrapper<AppSubUsualAddressEntity> usualAddressEntityEntityWrapper = new EntityWrapper<>();
        usualAddressEntityEntityWrapper.eq("user_id", userid);

        List<AppSubUsualAddressEntity> appSubUsualAddressEntities = this.selectList(usualAddressEntityEntityWrapper);
        if (appSubUsualAddressEntities.isEmpty()) {
            return null;
        }
        return appSubUsualAddressEntities;
    }
}
