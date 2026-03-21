package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.VideoCommontsThumbsEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户点赞表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-08-14
 */
public interface VideoCommontsThumbsMapper extends BaseMapper<VideoCommontsThumbsEntity> {

    /**
     * 根据被点赞评论的id查询点赞列表（即查询谁给这个评论点赞过）
     * @return
     */
    Integer findByVideoIdAndStatus(@Param("videoCommontsId")String videoCommontsId, Integer code);

    //这个人给什么评论点过赞
    List<VideoCommontsThumbsEntity> findByUserIdAndStatus(@Param("videoCommontsId")String videoCommontsId, @Param("code")Integer code);

    /**
     * 通过被点赞评论和点赞人id查询是否存在点赞记录
     */
    VideoCommontsThumbsEntity findByUserIdAndComId(@Param("videoCommontsId")String videoCommontsId, @Param("userId")String userId);

    Long findCommentThumbsCount(String treeCode);

    VideoCommontsThumbsEntity findIsThumbs(@Param("userId")Integer userId,@Param("treeCode") String treeCode);
}
