package com.cheji.b.modular.mapper;

import com.cheji.b.modular.domain.AppSendOutSheetEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.b.modular.dto.CheckImgList;

import java.util.List;

/**
 * <p>
 * web派单记录表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2020-10-14
 */
public interface AppSendOutSheetMapper extends BaseMapper<AppSendOutSheetEntity> {

    List<String> findUrlbyid(Integer id);

    List<String> findIndex1Img(Integer id);

    List<CheckImgList> selectSenceImg(Integer id);

    List<Integer> findBrandMerIdList(Integer parseInt);
}
