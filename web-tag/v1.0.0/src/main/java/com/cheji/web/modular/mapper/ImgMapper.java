package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.ImgEntity;

import java.util.List;

/**
 * <p>
 * 图片表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-08-23
 */
public interface ImgMapper extends BaseMapper<ImgEntity> {

    //根据传得商户id拿到图片
    List<ImgEntity> getImgEntityByMerchantsAndType(String keyId);

    //根据订单找到图片
    List<ImgEntity> findImgByndentId(String indentId);

    List<String> findImgByIndentidndIndex(String indentid);
}
