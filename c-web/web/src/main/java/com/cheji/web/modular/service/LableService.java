package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.LableEntity;
import com.cheji.web.modular.mapper.LableMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-08-20
 */
@Service
public class LableService extends ServiceImpl<LableMapper, LableEntity> implements IService<LableEntity> {

}
