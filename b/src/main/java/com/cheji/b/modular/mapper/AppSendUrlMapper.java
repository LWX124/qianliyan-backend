package com.cheji.b.modular.mapper;

import com.cheji.b.modular.domain.AppSendUrlEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 派单记录图片表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2020-10-15
 */
public interface AppSendUrlMapper extends BaseMapper<AppSendUrlEntity> {

    String findimgByid(Integer id);

    List<String> findallsendImg(Integer id);

    List<AppSendUrlEntity> findCheckImgList(Integer id);

    String findMapImg(Integer id);

    String[] findAccidentImg(Integer id);

    String findCheckImg(Long id);
}
