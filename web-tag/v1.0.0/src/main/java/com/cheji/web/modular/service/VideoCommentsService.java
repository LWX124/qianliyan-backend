package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.domain.UserEntity;
import com.cheji.web.modular.domain.VideoCommentsEntity;
import com.cheji.web.modular.domain.VideoCommontsThumbsEntity;
import com.cheji.web.modular.mapper.VideoCommentsMapper;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 视频评论表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-08-13
 */
@Service
public class VideoCommentsService extends ServiceImpl<VideoCommentsMapper, VideoCommentsEntity> implements IService<VideoCommentsEntity> {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(VideoCommentsService.class);

    @Resource
    private VideoCommentsMapper commentsMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private VideoCommontsThumbsService thumbsService;

    @Resource
    private UserService userService;


    //保存评论
    public VideoCommentsEntity save(VideoCommentsEntity videoCommentsEntity, Integer type) {
        String parentCode = videoCommentsEntity.getParentCode();
        //是否有父id，判断是否是一级评论
        if (StringUtils.isEmpty(parentCode) || "0".equals(parentCode)) {
            //父id为0就是一级评论，按照一级评论的格式处理数据
            VideoCommentsEntity where = new VideoCommentsEntity();
            //拿到最后一个一级评论code
            Integer lastpaTreeCode = getLastpaTreeCode();
            //根据id获取到数据
            EntityWrapper<VideoCommentsEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("tree_code", lastpaTreeCode);
            List<VideoCommentsEntity> videoCommentsEes = selectList(wrapper);
            VideoCommentsEntity lastParByParentCode = new VideoCommentsEntity();
            if (videoCommentsEes.isEmpty()) {
                lastParByParentCode.setTreeCode("1");
                lastParByParentCode.setTreeSort(new BigDecimal(0));
            } else {
                for (VideoCommentsEntity videoCommentsEe : videoCommentsEes) {
                    lastParByParentCode = videoCommentsEe;
                }
            }

            String treeCode = lastParByParentCode.getTreeCode();
            Integer integer = Integer.valueOf(treeCode);
            //设置treecode和treename
            videoCommentsEntity.setTreeCode(String.valueOf(integer + 1));
            videoCommentsEntity.setTreeName(String.valueOf(integer + 1));
            //一级评论的parent_code为零
            videoCommentsEntity.setParentCode("0");
            videoCommentsEntity.setParentCodes(videoCommentsEntity.getParentCode() + ",");
            //设置tree_sort,tree_soets
            BigDecimal a = new BigDecimal(30);
            videoCommentsEntity.setTreeSort(lastParByParentCode.getTreeSort().add(a));
            String s = String.valueOf(lastParByParentCode.getTreeSort().add(a));
            int i = Integer.parseInt(s);
            String s1 = bySting(s, i);
            videoCommentsEntity.setTreeSorts(s1 + ",");

            //查询是否最末级
            List<VideoCommentsEntity> list = finIsdTreeLeaf(videoCommentsEntity.getTreeCode());
            //查询出来数据为空就证明是
            if (list.isEmpty()) {
                videoCommentsEntity.setTreeLeaf("1");
            } else {
                //不为空就证明是不是最末级
                videoCommentsEntity.setTreeLeaf("0");
            }
            //设置节点层级级别
            videoCommentsEntity.setTreeLevel(BigDecimal.valueOf(0));
            //tree_names
            videoCommentsEntity.setTreeNames(String.valueOf(integer + 1));
            videoCommentsEntity.setStatus("0");//(0:正常,1:删除,2:停用)

            videoCommentsEntity.setCreateDate(new Date());
            videoCommentsEntity.setUpdateDate(new Date());
            videoCommentsEntity.setType(type);

            //删掉缓存数据
            //  redisTemplate.delete(RedisConstant.COMMENT_VIDEO_COMMENT + videoCommentsEntity.getVideoId());
            insert(videoCommentsEntity);
            return videoCommentsEntity;

        } else {
            //不是一级评论就按照其他评论来处理
            //判断是第几级回复
            List<VideoCommentsEntity> list = finIsdTreeLeaf(videoCommentsEntity.getParentCode());
            //如果有数据，就是第二级回复
            for (int i = 0; i < list.size(); i++) {
                //拿到遍历的最后一次
                if (i == list.size() - 1) {
                    VideoCommentsEntity videoCommentsEntity1 = list.get(i);
                    String treeCode = videoCommentsEntity1.getTreeCode();
                    videoCommentsEntity.setTreeCode(String.valueOf(Integer.valueOf(treeCode) + 1));
                    videoCommentsEntity.setTreeName(String.valueOf(Integer.valueOf(treeCode) + 1));
                    //遍历第一次就是30，两次就是60
                    videoCommentsEntity.setTreeSort(BigDecimal.valueOf((list.size() + 1) * 30));
                }
            }
            //获取到上级评论
            VideoCommentsEntity parentMesg = findParentMesg(videoCommentsEntity.getParentCode());
            //如果查询不到数据就是第一条回复
            if (list.size() == 0) {
                String treeName = parentMesg.getTreeName();
                videoCommentsEntity.setTreeCode(treeName + "001");
                videoCommentsEntity.setTreeName(treeName + "001");
                videoCommentsEntity.setTreeSort(BigDecimal.valueOf(30));
            }
            //保存parent_codes
            String parentCodes = parentMesg.getParentCodes();
            videoCommentsEntity.setParentCodes(parentCodes + videoCommentsEntity.getParentCode() + ",");
            //保存tree_sorts
            String string = videoCommentsEntity.getTreeSort().toString();
            int i = Integer.parseInt(string);
            String treeSorts = parentMesg.getTreeSorts();
            String s = bySting(string, i);
            videoCommentsEntity.setTreeSorts(treeSorts + s + ",");
            List<VideoCommentsEntity> list1 = finIsdTreeLeaf(videoCommentsEntity.getTreeCode());
            //设置tree_leaf
            //判断是否是最下级
            if (list1.size() == 0) {
                //为空就证明是最末级
                videoCommentsEntity.setTreeLeaf("1");
            } else {
                //不为空就不是
                videoCommentsEntity.setTreeLeaf("0");
            }
            //设置tree_level
            BigDecimal treeLevel = parentMesg.getTreeLevel();
            BigDecimal v = new BigDecimal(1);
            videoCommentsEntity.setTreeLevel(treeLevel.add(v));
            //设置tree_names
            String treeNames = parentMesg.getTreeNames();
            videoCommentsEntity.setTreeNames(treeNames + "/" + videoCommentsEntity.getTreeName());

            videoCommentsEntity.setStatus("0");
            videoCommentsEntity.setCreateDate(new Date());
            videoCommentsEntity.setUpdateDate(new Date());
            videoCommentsEntity.setType(type);

            //评论之后修改上一级评论的tree_leaf不是为最末级
            parentMesg.setTreeLeaf("0");
            updateById(parentMesg);

            //截取parent_codes的数据拿到parentid
            String parentCodes1 = videoCommentsEntity.getParentCodes();
            String[] split = parentCodes1.split(",");
            for (int j = 0; j < split.length; j++) {
                if (j == 1) {
                    String s1 = split[j];
                    videoCommentsEntity.setParentId(Integer.valueOf(s1));
                    break;
                }

            }
            //通过key删除掉缓存数据
            // redisTemplate.delete(RedisConstant.COMMENT_VIDEO_COMMENT + videoCommentsEntity.getVideoId());
            insert(videoCommentsEntity);

        }


        return videoCommentsEntity;
    }

    //拼接tree_codes
    private String bySting(String str, Integer stelength) {
        //int数据是90
        //字符串长度是10
        int steleng = str.length();
        steleng = 10;
        int length = String.valueOf(stelength).length();
        System.out.println(length);
        if (length < steleng) {
            while (length < steleng) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);//左补0
                str = sb.toString();
                length = str.length();
            }
        }
        return str;
    }

    //拿到最后一个一级评论
    private VideoCommentsEntity getLastParByParentCode() {
        VideoCommentsEntity lastParentCode = commentsMapper.getLastParentCode();
        return lastParentCode;
    }

    //查询是否是最末级评论
    private List<VideoCommentsEntity> finIsdTreeLeaf(String treeCode) {
        List<VideoCommentsEntity> list = commentsMapper.findTreeLeaf(treeCode);
        return list;
    }


    //查询父级评论
    private VideoCommentsEntity findParentMesg(String parentCode) {
        VideoCommentsEntity list = commentsMapper.findParent(parentCode);
        return list;
    }


    //查询到一级评论
    @Transactional
    public List<VideoCommentsEntity> findOneComments(Integer id, String videoId, Integer pagesize) {
        //先从redis中取出数据,没有数据再从数据库里面拿出数据
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.COMMENT_VIDEO_LEVEL_ONE + videoId + "_first_" + pagesize);
        if (StringUtils.isEmpty(s)) {

            int pages = pagesize * 5 - 5;//* 5 - 5
            List<VideoCommentsEntity> oneComments = commentsMapper.findOneComments(videoId, pages, 1);
            for (VideoCommentsEntity oneComment : oneComments) {
                String treeCode = oneComment.getTreeCode();
                VideoCommontsThumbsEntity coment = thumbsService.findIsThumbs(id, treeCode);
                if (coment != null) {
                    oneComment.setThumbsStatu("1");
                } else {
                    oneComment.setThumbsStatu("0");
                }
                Integer totalnumber = commentsMapper.findUnderSecondComment(treeCode);
                oneComment.setTotalNumer(totalnumber);
                //查询点赞评论的数量
                //根据评论id来查询
                //拿到本条评论的点赞数据
                Long counts = thumbsService.findCommentThumbsCount(treeCode);
                int i = counts.intValue();
                //存的是一级评论的点赞数量
                oneComment.setThumbsUp(i);
                //赞数据存到redis里面
                if (oneComments.size() == 5) {
                    stringRedisTemplate.opsForValue().set(RedisConstant.COMMENT_VIDEO_THUMBS_COUNT + treeCode, String.valueOf(i), 60 * 5, TimeUnit.SECONDS);
                }
            }

            //把评论数据放到redis中,满足五条的才放，
            if (oneComments.size() == 5) {
                //视频id+一级评论标识+页码  过期时间5分钟
                stringRedisTemplate.opsForValue().set(RedisConstant.COMMENT_VIDEO_LEVEL_ONE + videoId + "_first_" + pagesize, JSONArray.toJSONString(oneComments), 60 * 5, TimeUnit.SECONDS);
            }

            return oneComments;
        } else {
            List<VideoCommentsEntity> videoComments = JSONArray.parseArray(s, VideoCommentsEntity.class);
            for (VideoCommentsEntity videoComment : videoComments) {
                //从redis里面获取到点赞数据
                String treeCode = videoComment.getTreeCode();
                String s1 = stringRedisTemplate.opsForValue().get(RedisConstant.COMMENT_VIDEO_THUMBS_COUNT + treeCode);
                if (StringUtils.isEmpty(s1)) {
                    //获取到redis为空
                    Long commentThumbsCount = thumbsService.findCommentThumbsCount(treeCode);
                    s1 = commentThumbsCount.toString();
                }
                videoComment.setThumbsUp(Integer.valueOf(s1));
            }
            return videoComments;
        }
    }


    //通过视频id找到视频评论的总数
    public Long findVideoCommentCount(Long videoId,Integer type) {
        return commentsMapper.findVideoCommentCount(videoId,type);
    }

    //查询到一级评论之下的评论
    @Transactional
    public List<VideoCommentsEntity> findOtherComment(Integer id, String videoId, String treeCode, Integer pagesize) {
        //先从redis中拿到数据
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.COMMENT_VIDEO_LEVEL_OTHER + videoId + treeCode + pagesize);

        //为空就查询
        if (StringUtils.isEmpty(s)) {
            int pages = pagesize * 5 - 5;
            //根据parentid来查询数据
            List<VideoCommentsEntity> records = commentsMapper.findSecondComment(treeCode, pages, 1);
            //获取到点赞数据
            for (VideoCommentsEntity record : records) {
                String treeCode1 = record.getTreeCode();
                VideoCommontsThumbsEntity coment = thumbsService.findIsThumbs(id, treeCode1);
                if (coment != null) {
                    record.setThumbsStatu("1");
                } else {
                    record.setThumbsStatu("0");
                }
                //拿到对应评论回复的数据
                BigDecimal treeLevel = record.getTreeLevel();
                int i1 = treeLevel.intValue();
                if (i1 >= 2) {
                    //获取到上一级的userid
                    String parentCode = record.getParentCode();
                    //获取到一条的数据
                    EntityWrapper<VideoCommentsEntity> wrapper = new EntityWrapper<>();
                    wrapper.eq("tree_code", parentCode);
                    List<VideoCommentsEntity> videoComments = selectList(wrapper);
                    for (VideoCommentsEntity videoComment : videoComments) {
                        Integer userId1 = videoComment.getUserId();
                        //根据userid查询到回复的人
                        EntityWrapper<UserEntity> userWrapper = new EntityWrapper<>();
                        userWrapper.eq("id", userId1);
                        List<UserEntity> userEn = userService.selectList(userWrapper);
                        for (UserEntity userEntity : userEn) {
                            String name = userEntity.getName();
                            record.setReplyUser(name);
                        }
                    }
                }
                //获取评论点赞状态
                //获取到对应点赞数据
                Long ThumbsCount = thumbsService.findCommentThumbsCount(treeCode1);
                int i = ThumbsCount.intValue();
                record.setThumbsUp(i);
                //点赞数据保存到redis中
                if (records.size() == 5) {
                    stringRedisTemplate.opsForValue().set(RedisConstant.COMMENT_VIDEO_THUMBS_COUNT + treeCode1, String.valueOf(i), 60 * 5, TimeUnit.SECONDS);
                }
            }
            //长度为5就放到redis中
            if (records.size() == 5) {
                stringRedisTemplate.opsForValue().set(RedisConstant.COMMENT_VIDEO_LEVEL_OTHER + videoId + treeCode + pagesize, JSONArray.toJSONString(records), 60 * 5, TimeUnit.SECONDS);
            }
            return records;
        } else {
            List<VideoCommentsEntity> videoComments = JSONArray.parseArray(s, VideoCommentsEntity.class);
            for (VideoCommentsEntity videoComment : videoComments) {
                //从redis里面获取到点赞数据
                String treeCode1 = videoComment.getTreeCode();
                String s1 = stringRedisTemplate.opsForValue().get(RedisConstant.COMMENT_VIDEO_THUMBS_COUNT + treeCode1);
                if (StringUtils.isEmpty(s1)) {
                    Long commentThumbsCount = thumbsService.findCommentThumbsCount(treeCode1);
                    s1 = commentThumbsCount.toString();
                }
                videoComment.setThumbsUp(Integer.valueOf(s1));
            }
            return videoComments;
        }
    }

    public Integer getLastpaTreeCode() {
        return commentsMapper.getLastPaTreeCode();
    }


    public List<VideoCommentsEntity> findAccidentOtherComment(String treeCode, Integer pagesize) {
        int pages = pagesize * 5 - 5;
        //根据parentid来查询数据
        List<VideoCommentsEntity> records = commentsMapper.findSecondComment(treeCode, pages, 2);
        //获取到点赞数据
        if (!records.isEmpty()){
            for (VideoCommentsEntity record : records) {
                //拿到对应评论回复的数据
                BigDecimal treeLevel = record.getTreeLevel();
                int i1 = treeLevel.intValue();
                if (i1 >= 2) {
                    //获取到上一级的userid
                    String parentCode = record.getParentCode();
                    //获取到一条的数据
                    EntityWrapper<VideoCommentsEntity> wrapper = new EntityWrapper<>();
                    wrapper.eq("tree_code", parentCode);
                    List<VideoCommentsEntity> videoComments = selectList(wrapper);
                    for (VideoCommentsEntity videoComment : videoComments) {
                        Integer userId1 = videoComment.getUserId();
                        //根据userid查询到回复的人
                        EntityWrapper<UserEntity> userWrapper = new EntityWrapper<>();
                        userWrapper.eq("id", userId1);
                        List<UserEntity> userEn = userService.selectList(userWrapper);
                        for (UserEntity userEntity : userEn) {
                            String name = userEntity.getName();
                            record.setReplyUser(name);
                        }
                    }
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateString = formatter.format(record.getCreateDate());
                    record.setTime(dateString);
                }
            }
        }
        return records;
    }

    public List<VideoCommentsEntity> findAccidentOneComments(String videoId, Integer pagesize) {
        int pages = pagesize * 5 - 5;//* 5 - 5
        List<VideoCommentsEntity> oneComments = commentsMapper.findOneComments(videoId, pages, 2);
        if (!oneComments.isEmpty()) {
            for (VideoCommentsEntity oneComment : oneComments) {
                String treeCode = oneComment.getTreeCode();
                Integer totalnumber = commentsMapper.findUnderSecondComment(treeCode);
                oneComment.setTotalNumer(totalnumber);
            }
        }
        return oneComments;
    }
}