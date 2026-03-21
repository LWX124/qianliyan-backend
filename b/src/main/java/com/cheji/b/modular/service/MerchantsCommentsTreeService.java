package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.constant.RedisConstant;
import com.cheji.b.modular.domain.CUserEntity;
import com.cheji.b.modular.domain.ImgEntity;
import com.cheji.b.modular.domain.MerchantsCommentsTree;
import com.cheji.b.modular.dto.CommentsDto;
import com.cheji.b.modular.dto.MerchCommertsDto;
import com.cheji.b.modular.mapper.MerchantsCommentsTreeMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商户评论树表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-09-26
 */
@Service
public class MerchantsCommentsTreeService extends ServiceImpl<MerchantsCommentsTreeMapper, MerchantsCommentsTree> implements IService<MerchantsCommentsTree> {
    @Resource
    private MerchantsCommentsTreeMapper merchantsCommentsTreeMapper;
    @Resource
    private CUserService cUserService;
    @Resource
    private ImgService imgService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final static Logger logger = LoggerFactory.getLogger(MerchantsCommentsTreeService.class);


    //查询评价总数
    public BigDecimal findEvaCount(Integer user_b_id) {
        return merchantsCommentsTreeMapper.findEvaCount(user_b_id);
    }

    //查询到好评总数
    public BigDecimal findhighPraiseCount(Integer user_b_id) {
        return merchantsCommentsTreeMapper.highPraiseCount(user_b_id);
    }

    //查询到回复的评价总数
    public BigDecimal findevaComment(Integer user_b_id) {
        return merchantsCommentsTreeMapper.findevaComment(user_b_id);
    }

    //拿到回复中差评的总数
    public BigDecimal findmidBadComment(Integer user_b_id) {
        return merchantsCommentsTreeMapper.findmidBadComment(user_b_id);
    }


    //查询到评价数据
    public List<CommentsDto>  findMerchantsComments(Integer userBId, Integer type, Integer pagesize) {
        //查询到各个条件的数量
        //  MerchCommertsDto merchCommertsDto = merchantsCommentsTreeMapper.findallCount(userBId);
        MerchCommertsDto merchCommertsDto = new MerchCommertsDto();
        pagesize = (pagesize-1)*20;
        //操作评论数据
        //根据type查询数据
        List<CommentsDto> commentsDtos = new ArrayList<>();
        if (type==null){
            type=11;
        }
        Integer genre = 1;
        switch (type){
            case 1: commentsDtos = merchantsCommentsTreeMapper.findhighPraise(userBId,pagesize);break;
            case 2: commentsDtos = merchantsCommentsTreeMapper.findbadReview(userBId,pagesize);break;
            case 3: commentsDtos = merchantsCommentsTreeMapper.findnoResponse(userBId,pagesize);break;
            case 4: commentsDtos = merchantsCommentsTreeMapper.findAllComments(userBId,pagesize,genre);break;
            case 5: commentsDtos = merchantsCommentsTreeMapper.findGoodTcchologt(userBId,pagesize);break;
            case 6: commentsDtos = merchantsCommentsTreeMapper.findFastSpeed(userBId,pagesize);break;
            case 7: commentsDtos = merchantsCommentsTreeMapper.findRepeatCustomers(userBId,pagesize);break;
            case 8: commentsDtos = merchantsCommentsTreeMapper.findServiceEnthusiasm(userBId,pagesize);break;
            default:commentsDtos = merchantsCommentsTreeMapper.findAllComments(userBId,pagesize,genre);break;
        }
        for (CommentsDto commentsDto : commentsDtos) {
            //获取到c端用户
            Integer userId = commentsDto.getUserId();
            //查询到用户名和头像
            CUserEntity cUserEntity = cUserService.selectById(userId);
            String avatar = null;
            String name = null;
            if (null!=cUserEntity){
                avatar = cUserEntity.getAvatar();
                name = cUserEntity.getName();
            }
            //根据评论code查询到图片
            String commentsCode = commentsDto.getCommentsCode();
            List<ImgEntity> imgEntityList = imgService.findCommentsImg(commentsCode);
            commentsDto.setCommentsList(imgEntityList);
            commentsDto.setName(name);
            commentsDto.setAvatar(avatar);

            //查询是否有回复
            EntityWrapper<MerchantsCommentsTree> wrapper = new EntityWrapper<>();
            wrapper.eq("parent_code", commentsCode);
            List<MerchantsCommentsTree> merchantsCommentsTrees = selectList(wrapper);
            if (!merchantsCommentsTrees.isEmpty()){
                for (MerchantsCommentsTree commentsTree : merchantsCommentsTrees) {
                    commentsDto.setCommentsTree(commentsTree);
                }
            }

            //查询访问量
            int i = pagesize + 1;
            String s = stringRedisTemplate.opsForValue().get(RedisConstant.MERCHANTS_COMMENT_COUNT + userBId + i);
            if (StringUtils.isNotEmpty(s)){
                commentsDto.setTraffic(Integer.valueOf(s));
            }
        }
        return commentsDtos;
    }

    public void addReplyComment(MerchantsCommentsTree merchantsComments) {
        //新增加回复数据
        merchantsComments.setParentCode(merchantsComments.getCommentsCode());
        //获取到上级评论
        MerchantsCommentsTree firstcomment = selectById(merchantsComments.getCommentsCode());
        String commentsName = firstcomment.getCommentsName();
        merchantsComments.setCommentsCode(commentsName+"001");
        merchantsComments.setCommentsName(commentsName+"001");
        merchantsComments.setTreeSort(BigDecimal.valueOf(30));
        //保存parents_codes
        String parentCodes = firstcomment.getParentCodes();
        merchantsComments.setParentCodes(parentCodes+merchantsComments.getParentCode()+",");
        //保存tree_sorts
        String string = merchantsComments.getTreeSort().toString();
        int i = Integer.parseInt(string);
        String treeSorts = firstcomment.getTreeSorts();
        String s = bySting(string, i);
        merchantsComments.setTreeSorts(treeSorts+s+",");
        merchantsComments.setTreeLeaf("1");

        BigDecimal treeLevel = firstcomment.getTreeLevel();
        BigDecimal v = new BigDecimal(1);
        merchantsComments.setTreeLevel(treeLevel.add(v));
        //设置tree_Names
        String treeNames = firstcomment.getTreeNames();
        merchantsComments.setTreeNames(treeNames+"/"+merchantsComments.getCommentsName());

        merchantsComments.setState("1");
        merchantsComments.setCreateDate(new Date());
        merchantsComments.setUpdateDate(new Date());
        firstcomment.setTreeLeaf("0");
        updateById(firstcomment);
        insert(merchantsComments);
    }

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

    public List<CommentsDto> findPushIndentComments(Integer userBId, Integer pagesize, int i) {
        pagesize = (pagesize-1)*20;
        return merchantsCommentsTreeMapper.findAllComments(userBId,pagesize,i);
    }
}
