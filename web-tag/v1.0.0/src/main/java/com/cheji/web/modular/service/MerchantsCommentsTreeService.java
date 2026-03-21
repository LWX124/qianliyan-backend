package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.cwork.AllCommentDto;
import com.cheji.web.modular.cwork.MerchantsComment;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.mapper.MerchantsCommentsTreeMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商户评论树表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-08-23
 */
@Service
public class MerchantsCommentsTreeService extends ServiceImpl<MerchantsCommentsTreeMapper, MerchantsCommentsTreeEntity> implements IService<MerchantsCommentsTreeEntity> {
    @Resource
    private MerchantsCommentsTreeMapper MerchantsCommentsTreeMapper;
    @Resource
    private BUserService bUserService;
    @Resource
    private UserService userService;
    @Resource
    private CarBrandService carBrandService;
    @Resource
    private IndentService indentService;
    @Resource
    private ImgService imgService;

    private Logger logger = LoggerFactory.getLogger(MerchantsCommentsTreeService.class);

    public List<MerchantsComment> getComByMerId(String merchantsId, Integer pagesize, Integer type, Integer genre) {
        return MerchantsCommentsTreeMapper.getComByMerId(merchantsId, pagesize, type, genre);
    }

    public void save(MerchantsCommentsTreeEntity merchantsComments) {
        //获取到父id
        String parentCode = merchantsComments.getParentCode();
        //判断是否为空
        if (StringUtils.isEmpty(parentCode)) {
            merchantsComments.setParentCode("0");
            //父id为空就是一级评论按照一级评论得格式处理数据
            //新建一个评论对象
            //拿到最后一个一级评论
            Integer lastParentCode = getLatParentCode();
            if (lastParentCode == null) {
                lastParentCode = 0;
            }
            BigDecimal bigDecimal;
            MerchantsCommentsTreeEntity lashComByParentCode = selectById(lastParentCode);
            if (lashComByParentCode == null) {
                bigDecimal = BigDecimal.ZERO;
            } else {
                bigDecimal = lashComByParentCode.getTreeSort();
            }
            //设置treecode和treename
            merchantsComments.setCommentsCode(String.valueOf(lastParentCode + 1));
            merchantsComments.setCommentsName(String.valueOf(lastParentCode + 1));
            //一级评论的parent_code为零
            merchantsComments.setParentCodes(merchantsComments.getParentCode() + ",");
            //设置tree_sort,tree_sorts
            BigDecimal a = new BigDecimal(30);
            merchantsComments.setTreeSort(bigDecimal.add(a));
            String s = String.valueOf(bigDecimal.add(a));
            int i = Integer.parseInt(s);
            String s1 = bySting(s, i);
            merchantsComments.setTreeSorts(s1 + ",");

            //查询是否最末级
            List<MerchantsCommentsTreeEntity> idTreeleaf = findIdTreeleaf(merchantsComments.getCommentsCode());
            if (idTreeleaf.isEmpty()) {
                merchantsComments.setTreeLeaf("1");
            } else {
                merchantsComments.setTreeLeaf("0");
            }
            //设置节点层级级别
            merchantsComments.setTreeLevel(BigDecimal.valueOf(0));
            //tree_names
            merchantsComments.setTreeNames(String.valueOf(lastParentCode + 1));
            merchantsComments.setState("1");

            merchantsComments.setCreateDate(new Date());
            merchantsComments.setUpdateDate(new Date());

            insert(merchantsComments);
        } else {
            //不是一级评论
            //判断是第几级回复
            List<MerchantsCommentsTreeEntity> list = findIdTreeleaf(merchantsComments.getParentCode());
            for (int i = 0; i < list.size(); i++) {
                if (i == list.size() - 1) {
                    MerchantsCommentsTreeEntity merchantsComment1 = list.get(i);
                    String commentsCode = merchantsComment1.getCommentsCode();
                    merchantsComments.setCommentsCode(String.valueOf(Integer.valueOf(commentsCode) + 1));
                    merchantsComments.setCommentsName(String.valueOf(Integer.valueOf(commentsCode) + 1));
                    //遍历第一次就是30 两次就是60
                    merchantsComments.setTreeSort(BigDecimal.valueOf((list.size() + 1) * 30));
                }
            }
            //获取到上级评论
            MerchantsCommentsTreeEntity parentMesg = findParentMesg(merchantsComments.getParentCode());
            if (list.isEmpty()) {
                String commentsName = parentMesg.getCommentsName();
                merchantsComments.setCommentsCode(commentsName + "001");
                merchantsComments.setCommentsName(commentsName + "001");
                merchantsComments.setTreeSort(BigDecimal.valueOf(30));
            }
            //保存parents_codes
            String parentCodes = parentMesg.getParentCodes();
            merchantsComments.setParentCodes(parentCodes + merchantsComments.getParentCode() + ",");
            //保存tree_sorts
            String string = merchantsComments.getTreeSort().toString();
            int i = Integer.parseInt(string);
            String treeSorts = parentMesg.getTreeSorts();
            String s = bySting(string, i);
            merchantsComments.setTreeSorts(treeSorts + s + ",");
            List<MerchantsCommentsTreeEntity> idTreeleaf = findIdTreeleaf(merchantsComments.getCommentsCode());
            //设置tree_leaf
            //判断是否最下级
            if (idTreeleaf.isEmpty()) {
                //为空就证明是最末级
                merchantsComments.setTreeLeaf("1");
            } else {
                merchantsComments.setTreeLeaf("0");
            }
            //设置tree_level
            BigDecimal treeLevel = parentMesg.getTreeLevel();
            BigDecimal v = new BigDecimal(1);
            merchantsComments.setTreeLevel(treeLevel.add(v));
            //设置tree_names
            String treeNames = parentMesg.getTreeNames();
            merchantsComments.setTreeNames(treeNames + "/" + merchantsComments.getCommentsName());

            merchantsComments.setState("1");
            merchantsComments.setCreateDate(new Date());
            merchantsComments.setUpdateDate(new Date());

            parentMesg.setTreeLeaf("0");
            updateById(parentMesg);
            insert(merchantsComments);
        }
        //保存总分
        Integer efficiensyScore = merchantsComments.getEfficiensyScore();
        Integer serviceScore = merchantsComments.getServiceScore();
        int score = (serviceScore + efficiensyScore) / 2;
        //获取到商户数据
        Integer merchantsId = merchantsComments.getUserBId();
        //商戶id为空
        BUserEntity merchantsTreeEntity = bUserService.selectById(merchantsId);
        BigDecimal score1 = merchantsTreeEntity.getScore();
        int i = score1.intValue();
        int i1 = (i + score) / 2;
        BigDecimal bigDecimal = new BigDecimal(i1);
        merchantsTreeEntity.setServiceSorce((merchantsTreeEntity.getServiceSorce() + serviceScore) / 2);
        merchantsTreeEntity.setEffciencyScore((merchantsTreeEntity.getEffciencyScore() + efficiensyScore) / 2);
        merchantsTreeEntity.setScore(bigDecimal);
        bUserService.updateById(merchantsTreeEntity);
    }

    private Integer getLatParentCode() {
        return MerchantsCommentsTreeMapper.getLastParentCode();
    }

    //查询是否最末级评论
    private List<MerchantsCommentsTreeEntity> findIdTreeleaf(String commentCode) {
        return MerchantsCommentsTreeMapper.findTreeLeaf(commentCode);
    }

    private MerchantsCommentsTreeEntity findParentMesg(String parentCode) {
        return MerchantsCommentsTreeMapper.findParentMessg(parentCode);
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

    public List<MerchantsCommentsTreeEntity> findListByUseridAndMer(String merchantsCode, Integer id) {
        return MerchantsCommentsTreeMapper.findListByUseridAndMer(merchantsCode, id);
    }

    public AllCommentDto selectAllComment(Integer userBId) {
        return MerchantsCommentsTreeMapper.selectAllComment(userBId);
    }

    public JSONArray findShowComment(String brandId, String score, Integer pageSize) {
        pageSize = (pageSize - 1) * 20;
        //查询到数据
        JSONArray jsonArray = new JSONArray();
        //品牌为空；直接查询  查询到所有一级，并且评分超过5，字数多余3
        List<MerchantsCommentsTreeEntity> merchantsComments  = MerchantsCommentsTreeMapper.finfCommentNoId(brandId, score, pageSize);
        for (MerchantsCommentsTreeEntity merchantsComment : merchantsComments) {
            JSONObject json = new JSONObject();

            Integer userId = merchantsComment.getUserId();
            UserEntity userEntity = userService.selectById(userId);

            json.put("name", userEntity.getName());
            json.put("avatar", userEntity.getAvatar());
            Date createDate = merchantsComment.getCreateDate();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dateString = formatter.format(createDate);
            json.put("creatTime", dateString);

            //品牌，
            Integer userBId = merchantsComment.getUserBId();
            BUserEntity bUserEntity = bUserService.selectById(userBId);

            Integer type = bUserEntity.getType();
            if (type == 2) {
                json.put("brand", "不限");
            } else {
                CarBrandEntity carBrandEntity = carBrandService.selectById(bUserEntity.getBrandId());
                if (carBrandEntity == null) {
                    json.put("brand", "不限");
                } else {
                    json.put("brand", carBrandEntity.getName());
                }
            }
            //查询到就近订单
            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
            String dateString1 = formatter1.format(createDate);
            IndentEntity indent = indentService.findTime(dateString1, userId);
            if (indent == null) {
                json.put("fixloss", "暂无");
                json.put("fislossUser", "暂无");
            } else {
                String fixloss = indent.getFixloss();
                BigDecimal fixlossUser = indent.getFixlossUser();
                json.put("fixloss", fixloss);
                json.put("fislossUser", fixlossUser);
            }
            json.put("content", merchantsComment.getContent());
            //图片
            List<ImgEntity> imgEntityByMerchentsAndType = imgService.getImgEntityByMerchentsAndType(merchantsComment.getCommentsCode());
            if (!imgEntityByMerchentsAndType.isEmpty()) {
                ArrayList<String> strings = new ArrayList<>();
                for (ImgEntity imgEntity : imgEntityByMerchentsAndType) {
                    String url = imgEntity.getUrl();
                    strings.add(url);
                }
                json.put("imgList", strings);
            } else {
                ArrayList<String> strings = new ArrayList<>();
                json.put("imgList", strings);
            }

            json.put("merchantsName", bUserEntity.getMerchantsName());
            json.put("socre", merchantsComment.getServiceScore());
            jsonArray.add(json);
        }
        return jsonArray;
    }


}
