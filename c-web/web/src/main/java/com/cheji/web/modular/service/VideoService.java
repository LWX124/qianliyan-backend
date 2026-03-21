package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.VideoEntity;
import com.cheji.web.modular.mapper.VideoMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 视频信息表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-09-17
 */
@Service
public class VideoService extends ServiceImpl<VideoMapper, VideoEntity> implements IService<VideoEntity> {

}
