package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.cwork.AllCommentDto;
import com.cheji.web.modular.cwork.MerchantsComment;
import com.cheji.web.modular.domain.MerchantsCommentsTreeEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商户评论树表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-08-23
 */
public interface MerchantsCommentsTreeMapper extends BaseMapper<MerchantsCommentsTreeEntity> {

    //根据id查询到商户评论
    List<MerchantsComment> getComByMerId(@Param("merchantsId") String merchantsId, @Param("pagesize") Integer pagesize, @Param("type")Integer type,@Param("genre")Integer genre);

    //拿到最后一个一级评论
    MerchantsCommentsTreeEntity getLastComByParentCode();

    //查询父级评论
    MerchantsCommentsTreeEntity findParentMessg(String parentCode);

    //查询是否最末级评论
    List<MerchantsCommentsTreeEntity> findTreeLeaf(String parentCode);


    List<MerchantsCommentsTreeEntity> findListByUseridAndMer(@Param("merchantsCode")String merchantsCode, @Param("id")Integer id);

    Integer getLastParentCode();

    AllCommentDto selectAllComment(Integer userBId);

    List<MerchantsCommentsTreeEntity> finfCommentNoId(@Param("brandId")String brandId, @Param("score")String score,@Param("pageSize")Integer pageSize);
}
