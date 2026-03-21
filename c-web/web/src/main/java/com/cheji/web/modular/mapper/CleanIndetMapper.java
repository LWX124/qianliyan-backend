package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.CleanIndetEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 洗车订单表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-12-13
 */
public interface CleanIndetMapper extends BaseMapper<CleanIndetEntity> {

    List<CleanIndetEntity> findCleanIndent(@Param("id") Integer id, @Param("pagesize")Integer pagesize,@Param("type") String type);

    String findcarImgpx(Integer carType);

    String findcarImgJx(Integer carType);

    String findbeautyName(Integer cleanType);

    Integer findIndentCountNumber(String userBId);

    List<CleanIndetEntity> findisOnFreeIndent(Integer userid);

    String findbeautyImg(Integer cleanType);
}
