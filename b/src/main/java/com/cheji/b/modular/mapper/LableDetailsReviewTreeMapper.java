package com.cheji.b.modular.mapper;

import com.cheji.b.modular.domain.LableDetailsEntity;
import com.cheji.b.modular.domain.LableDetailsReviewTreeEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.b.modular.service.LableDetailsReviewTreeService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 标签和明细表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-09-28
 */
public interface LableDetailsReviewTreeMapper extends BaseMapper<LableDetailsReviewTreeEntity> {

    Integer getLastLable();

    List<LableDetailsReviewTreeEntity> findIdTreeleaf(String lableCode);

    List<LableDetailsEntity> findProjectDetails(@Param("user_b_id") Integer user_b_id, @Param("id")Integer id);

    LableDetailsReviewTreeEntity findNowProject(@Param("id")String id, @Param("userBId")Integer userBId);
}
