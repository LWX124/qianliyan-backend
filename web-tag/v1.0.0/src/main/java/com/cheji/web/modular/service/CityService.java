package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.CityEntity;
import com.cheji.web.modular.mapper.CityMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 城市表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-09-20
 */
@Service
public class CityService extends ServiceImpl<CityMapper, CityEntity> implements IService<CityEntity> {

}
