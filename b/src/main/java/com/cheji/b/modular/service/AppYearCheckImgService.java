package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.AppYearCheckImgEntity;
import com.cheji.b.modular.mapper.AppYearCheckImgMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 年检图片表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2020-04-02
 */
@Service
public class AppYearCheckImgService extends ServiceImpl<AppYearCheckImgMapper, AppYearCheckImgEntity> implements IService<AppYearCheckImgEntity> {

}
