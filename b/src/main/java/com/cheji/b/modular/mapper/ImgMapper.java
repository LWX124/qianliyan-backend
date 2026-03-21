package com.cheji.b.modular.mapper;

import com.cheji.b.modular.domain.ImgEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 图片表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-09-25
 */
public interface ImgMapper extends BaseMapper<ImgEntity> {

    List<ImgEntity> findCOmmentsImg(String commentsCode);

    List<String> findImgByIndentidndIndex(String indentid);

    List<ImgEntity> findImgByndentId(String indentId);
}
