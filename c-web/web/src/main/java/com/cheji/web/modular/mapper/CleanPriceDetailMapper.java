package com.cheji.web.modular.mapper;

import com.cheji.web.modular.cwork.Personal;
import com.cheji.web.modular.domain.CleanIndetEntity;
import com.cheji.web.modular.domain.CleanPriceDetailEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商户清洗价格明细表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-12-12
 */
public interface CleanPriceDetailMapper extends BaseMapper<CleanPriceDetailEntity> {

    //通过清洗类型查询到所有商户
    //List<String> findUsersByCleanType(@Param("cleanType")String cleanType,@Param("cityCode")Integer cityCode);

    List<CleanPriceDetailEntity> findCleanDetails(@Param("userid")String userid,@Param("cleanType")String cleanType);

    List<CleanPriceDetailEntity> findDetailsByUserid(@Param("userBId")String userBId,@Param("cleanType")String cleanType);

    //精洗，普洗
    List<String> findUsersByCleanType1(@Param("cleanType")String cleanType, @Param("cityCode")Integer cityCode);

    //自动
    List<String> findUsersByCleanType3(@Param("cleanType")String cleanType,@Param("cityCode")Integer cityCode);

    //夜洗
    List<String> findUsersByCleanType4(@Param("cleanType")String cleanType,@Param("cityCode")Integer cityCode);

    List<String> findUserByCleanType5(@Param("cleanType")String cleanType,@Param("cityCode")Integer cityCode);

    CleanPriceDetailEntity selectForUpdate(String bussinessId);

    CleanPriceDetailEntity selectPriceDetailsForUpdate(@Param("userBId")String userBId, @Param("carType")String carType, @Param("indentCleanType")String indentCleanType);

    Personal findIndentCount(String userId);

    List<CleanIndetEntity> selectIndentUser(@Param("id")Integer id,@Param("userBId") Integer userBId);
}
