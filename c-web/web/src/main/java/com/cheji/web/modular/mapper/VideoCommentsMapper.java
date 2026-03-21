package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.VideoCommentsEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 视频评论表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-08-13
 */
public interface VideoCommentsMapper extends BaseMapper<VideoCommentsEntity> {


    VideoCommentsEntity getLastParentCode();

    List<VideoCommentsEntity> findTreeLeaf(String treeCode);

    VideoCommentsEntity findParent(String parentCode);

    Long findVideoCommentCount(@Param("videoId")Long videoId, @Param("type")Integer type);

    List<VideoCommentsEntity> findOneComments(@Param("videoId")String videoId, @Param("pages")Integer pages,@Param("type")Integer type);

    Integer getLastPaTreeCode();

    Integer findUnderSecondComment(String treeCode);

    List<VideoCommentsEntity> findSecondComment(@Param("treeCode") String treeCode,  @Param("pages") Integer pages,@Param("type")Integer type);
}
