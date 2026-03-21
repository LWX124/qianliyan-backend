package com.cheji.b.modular.mapper;

import com.cheji.b.modular.domain.MerchantsCommentsTree;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.b.modular.dto.CommentsDto;
import com.cheji.b.modular.dto.EvaluationDto;
import com.cheji.b.modular.dto.MerchCommertsDto;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * <p>
 * 商户评论树表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-09-26
 */
public interface MerchantsCommentsTreeMapper extends BaseMapper<MerchantsCommentsTree> {

    BigDecimal findEvaCount(Integer user_b_id);

    BigDecimal highPraiseCount(Integer user_b_id);

    BigDecimal findevaComment(Integer user_b_id);

    BigDecimal findmidBadComment(Integer user_b_id);

    EvaluationDto findallCount(Integer userBId);

    ArrayList<CommentsDto> findhighPraise(@Param("userBId")Integer userBId, @Param("pagesize")Integer pagesize);

    ArrayList<CommentsDto> findbadReview(@Param("userBId")Integer userBId, @Param("pagesize")Integer pagesize);

    ArrayList<CommentsDto> findnoResponse(@Param("userBId")Integer userBId, @Param("pagesize")Integer pagesize);

    ArrayList<CommentsDto> findAllComments(@Param("userBId")Integer userBId, @Param("pagesize")Integer pagesize,@Param("type") Integer type);

    ArrayList<CommentsDto> findGoodTcchologt(@Param("userBId")Integer userBId, @Param("pagesize")Integer pagesize);

    ArrayList<CommentsDto> findFastSpeed(@Param("userBId")Integer userBId, @Param("pagesize")Integer pagesize);

    ArrayList<CommentsDto> findRepeatCustomers(@Param("userBId")Integer userBId, @Param("pagesize")Integer pagesize);

    ArrayList<CommentsDto> findServiceEnthusiasm(@Param("userBId")Integer userBId, @Param("pagesize")Integer pagesize);
}
