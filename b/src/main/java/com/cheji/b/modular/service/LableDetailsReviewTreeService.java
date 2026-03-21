package com.cheji.b.modular.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.b.modular.controller.LableDetailsReviewTreeController;
import com.cheji.b.modular.domain.*;
import com.cheji.b.modular.dto.DetailsListDto;
import com.cheji.b.modular.dto.LableDto;
import com.cheji.b.modular.excep.CusException;
import com.cheji.b.modular.mapper.LableDetailsReviewTreeMapper;
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
 * 标签和明细表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-09-28
 */
@Service
public class LableDetailsReviewTreeService extends ServiceImpl<LableDetailsReviewTreeMapper, LableDetailsReviewTreeEntity> implements IService<LableDetailsReviewTreeEntity> {

    @Resource
    private LableDetailsReviewTreeMapper lableDetailsReviewTreeMapper;

    @Resource
    private LableService lableService;

    @Resource
    private UserService userService;

    @Resource
    private LableDetailsService lableDetailsService;

    @Resource
    private AppUserBMessageService appUserBMessageService;

    @Resource
    private AppUserBMessageImgService appUserBMessageImgService;

    private Logger logger = LoggerFactory.getLogger(LableDetailsReviewTreeService.class);

    public LableDetailsReviewTreeEntity addFirst(Integer user_b_id) throws CusException {

        LableDetailsReviewTreeEntity lableTree = new LableDetailsReviewTreeEntity();

        //先根据商户id查询是否有商户
        EntityWrapper<LableDetailsReviewTreeEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_b_id", user_b_id);
        List<LableDetailsReviewTreeEntity> lableDetailsReview = selectList(wrapper);
        //如果为空就是第一次添加
        if (lableDetailsReview.isEmpty()) {
            lableTree.setParentCode("0");
            //拿到最后一个一级标签
            Integer lastFirstLable = getLastLable();
            if (lastFirstLable == null) {
                lastFirstLable = 0;
            }
            //根据id获取到对象
            BigDecimal bigDecimal;
            LableDetailsReviewTreeEntity lastlable = selectById(lastFirstLable);
            if (lastlable == null) {
                bigDecimal = BigDecimal.ZERO;
            } else {
                bigDecimal = lastlable.getTreeSort();
            }
            lableTree.setLableCode(String.valueOf(lastFirstLable + 1));
            lableTree.setParentCode("0");
            lableTree.setParentCodes("0" + ",");
            //设置tree_sort,tree_sorts
            BigDecimal a = new BigDecimal(30);
            lableTree.setTreeSort(bigDecimal.add(a));
            String s = String.valueOf(bigDecimal.add(a));
            int i = Integer.parseInt(s);
            String s1 = bySting(s, i);
            lableTree.setTreeSorts(s1 + ",");

            //查询是否最末级
            List<LableDetailsReviewTreeEntity> treeLeaf = findidTreeleaf(lableTree.getLableCode());
            if (treeLeaf.isEmpty()) {
                lableTree.setTreeLeaf("1");
            } else {
                lableTree.setTreeLeaf("0");
            }
            //设置节点层次级别
            lableTree.setTreeLevel(BigDecimal.valueOf(0));
            //tree_names
            lableTree.setTreeNames(String.valueOf(lastFirstLable + 1));
            lableTree.setShow(0);
            lableTree.setUserBId(user_b_id);
            lableTree.setStatus("0");
            lableTree.setCreateDate(new Date());
            lableTree.setUpdateDate(new Date());
            //通过userBid查询到商户名称
            AppUserEntity appUserEntity = userService.selectById(user_b_id);
            String merchantsName = appUserEntity.getMerchantsName();
            if (StringUtils.isEmpty(merchantsName)) {
                throw new CusException(301, "商户名字不能为空！");
            }
            lableTree.setLableName(merchantsName);
            insert(lableTree);
            return lableTree;
        }


        return null;
    }

    private List<LableDetailsReviewTreeEntity> findidTreeleaf(String lableCode) {
        return lableDetailsReviewTreeMapper.findIdTreeleaf(lableCode);
    }

    private Integer getLastLable() {
        return lableDetailsReviewTreeMapper.getLastLable();
    }

    //拼接tree_codes
    private String bySting(String str, Integer stelength) {
        //int数据是90
        //字符串长度是10
        int steleng = str.length();
        steleng = 10;
        int length = String.valueOf(stelength).length();
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

    //查询有无对应服务
    public List<LableDetailsReviewTreeEntity> findserviceById(Integer userBId, Integer lableId) {
        EntityWrapper<LableDetailsReviewTreeEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_b_id", userBId)
                .eq("lable_id", lableId)
                .eq("`show`", 1);
        List<LableDetailsReviewTreeEntity> lableDetail = selectList(wrapper);
        return lableDetail;
    }

    //添加服务数据
    public LableDetailsReviewTreeEntity addSecond(Integer userBId, Integer lableId, String remake) {
        //获取到对应服务得名称
        LableEntity lableEntity = lableService.selectById(lableId);
        String label = lableEntity.getLabel();
        Integer index = lableEntity.getIndex();
        LableDetailsReviewTreeEntity lableDetailsReview = new LableDetailsReviewTreeEntity();
        //根据商户id查询到一级数据
        EntityWrapper<LableDetailsReviewTreeEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_b_id", userBId)
                .eq("parent_code", 0);
        List<LableDetailsReviewTreeEntity> lablemerchants = selectList(wrapper);
        for (LableDetailsReviewTreeEntity lablemerchant : lablemerchants) {
            //保存服务
            //一级内容得code
            String lableCode = lablemerchant.getLableCode();
            //找到下面得所有二级
            List<LableDetailsReviewTreeEntity> list = findidTreeleaf(lableCode);
            for (int i = 0; i < list.size(); i++) {
                if (i == list.size() - 1) {
                    LableDetailsReviewTreeEntity lastSecond = list.get(i);
                    String lableCode1 = lastSecond.getLableCode();
                    lableDetailsReview.setLableCode(String.valueOf(Integer.valueOf(lableCode1) + 1));
                    lableDetailsReview.setLableName(label);
                    lableDetailsReview.setTreeSort(BigDecimal.valueOf((list.size() + 1) * 30));

                }
            }
            if (list.isEmpty()) {
                String lableCode1 = lablemerchant.getLableCode();
                lableDetailsReview.setLableCode(lableCode1 + "001");
                lableDetailsReview.setLableName(label);
                lableDetailsReview.setTreeSort(BigDecimal.valueOf(30));
            }
            //保存parent_codes
            lableDetailsReview.setParentCode(lableCode);
            lableDetailsReview.setParentCodes(lablemerchant.getParentCodes() + lableCode + ",");
            //保存tree_sorts
            String s = lableDetailsReview.getTreeSort().toString();
            int i = Integer.parseInt(s);
            String treeSorts = lablemerchant.getTreeSorts();
            String s1 = bySting(s, i);
            lableDetailsReview.setTreeSorts(treeSorts + s1 + ",");
            lableDetailsReview.setTreeLeaf("1");
            //设置tree_level
            BigDecimal treeLevel = lablemerchant.getTreeLevel();
            BigDecimal v = new BigDecimal(1);
            lableDetailsReview.setTreeLevel(treeLevel.add(v));
            //tree_names
            String treeNames = lablemerchant.getTreeNames();
            lableDetailsReview.setTreeNames(treeNames + "/" + lableDetailsReview.getLableCode());

            lableDetailsReview.setState(0);
            lableDetailsReview.setShow(1);
            lableDetailsReview.setStatus("0");
            lableDetailsReview.setLableId(lableId);
            lableDetailsReview.setRemake(remake);
            lableDetailsReview.setUserBId(userBId);
            lableDetailsReview.setIndex(index);
            lableDetailsReview.setCleanType(77);
            lableDetailsReview.setCreateDate(new Date());
            lableDetailsReview.setUpdateDate(new Date());

            lablemerchant.setTreeLeaf("0");
            updateById(lablemerchant);
            insert(lableDetailsReview);
        }

        return lableDetailsReview;
    }

    public void addThird(DetailsListDto listDto, LableDetailsReviewTreeEntity lableser) {
        LableDetailsReviewTreeEntity lableDetailsReview = new LableDetailsReviewTreeEntity();
        Integer detailsId = listDto.getDetailsId();
        LableDetailsEntity lableDetailsEntity = lableDetailsService.selectById(detailsId);
        String content = lableDetailsEntity.getContent();

        //保存明细
        String lableCode = lableser.getLableCode();
        List<LableDetailsReviewTreeEntity> list = findidTreeleaf(lableCode);
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                LableDetailsReviewTreeEntity lableDetails = list.get(i);
                String lableCode1 = lableDetails.getLableCode();
                lableDetailsReview.setLableCode(String.valueOf(Integer.valueOf(lableCode1) + 1));
                lableDetailsReview.setLableName(content);
                lableDetailsReview.setTreeSort(BigDecimal.valueOf((list.size() + 1) * 30));
            }
        }
        if (list.isEmpty()) {
            String Code = lableser.getLableCode();
            lableDetailsReview.setLableCode(Code + "001");
            lableDetailsReview.setLableName(content);
            lableDetailsReview.setTreeSort(BigDecimal.valueOf(30));
        }

        //保存parents_code
        lableDetailsReview.setParentCode(lableCode);
        lableDetailsReview.setParentCodes(lableser.getParentCodes() + lableCode + ",");

        //tree_sorts
        String ssring = lableDetailsReview.getTreeSort().toString();
        int i = Integer.parseInt(ssring);
        String treeSorts = lableser.getTreeSorts();
        String s = bySting(ssring, i);
        lableDetailsReview.setTreeSorts(treeSorts + s + ",");
        lableDetailsReview.setTreeLeaf("1");
        //tree_level
        lableDetailsReview.setTreeLevel(BigDecimal.valueOf(2));
        String treeNames = lableser.getTreeNames();
        lableDetailsReview.setTreeNames(treeNames + "/" + lableDetailsReview.getLableCode());

        lableDetailsReview.setStatus("0");
        lableDetailsReview.setShow(0);
        lableDetailsReview.setLableDetailsId(detailsId);
        lableDetailsReview.setUserBId(lableser.getUserBId());
        lableDetailsReview.setLableId(lableser.getLableId());
        lableDetailsReview.setRebates(listDto.getRebats());
        lableDetailsReview.setCreateDate(new Date());
        lableDetailsReview.setUpdateDate(new Date());

        insert(lableDetailsReview);
        lableser.setTreeLeaf("0");
        updateById(lableser);

    }

    public LableDetailsReviewTreeEntity addCleanSecond(Integer userBId, Integer lableId, String remake) {
        //获取到对应服务得名称
        LableEntity lableEntity = lableService.selectById(lableId);
        String label = lableEntity.getLabel();
        Integer index = lableEntity.getIndex();
        LableDetailsReviewTreeEntity lableDetailsReview = new LableDetailsReviewTreeEntity();
        //根据商户id查询到一级数据
        EntityWrapper<LableDetailsReviewTreeEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_b_id", userBId)
                .eq("parent_code", 0);
        List<LableDetailsReviewTreeEntity> lablemerchants = selectList(wrapper);
        for (LableDetailsReviewTreeEntity lablemerchant : lablemerchants) {
            //保存服务
            //一级内容得code
            String lableCode = lablemerchant.getLableCode();
            //找到下面得所有二级
            List<LableDetailsReviewTreeEntity> list = findidTreeleaf(lableCode);
            for (int i = 0; i < list.size(); i++) {
                if (i == list.size() - 1) {
                    LableDetailsReviewTreeEntity lastSecond = list.get(i);
                    String lableCode1 = lastSecond.getLableCode();
                    lableDetailsReview.setLableCode(String.valueOf(Integer.valueOf(lableCode1) + 1));
                    lableDetailsReview.setLableName(label);
                    lableDetailsReview.setTreeSort(BigDecimal.valueOf((list.size() + 1) * 30));

                }
            }
            if (list.isEmpty()) {
                String lableCode1 = lablemerchant.getLableCode();
                lableDetailsReview.setLableCode(lableCode1 + "001");
                lableDetailsReview.setLableName(label);
                lableDetailsReview.setTreeSort(BigDecimal.valueOf(30));
            }
            //保存parent_codes
            lableDetailsReview.setParentCode(lableCode);
            lableDetailsReview.setParentCodes(lablemerchant.getParentCodes() + lableCode + ",");
            //保存tree_sorts
            String s = lableDetailsReview.getTreeSort().toString();
            int i = Integer.parseInt(s);
            String treeSorts = lablemerchant.getTreeSorts();
            String s1 = bySting(s, i);
            lableDetailsReview.setTreeSorts(treeSorts + s1 + ",");
            lableDetailsReview.setTreeLeaf("1");
            //设置tree_level
            BigDecimal treeLevel = lablemerchant.getTreeLevel();
            BigDecimal v = new BigDecimal(1);
            lableDetailsReview.setTreeLevel(treeLevel.add(v));
            //tree_names
            String treeNames = lablemerchant.getTreeNames();
            lableDetailsReview.setTreeNames(treeNames + "/" + lableDetailsReview.getLableCode());

            lableDetailsReview.setState(1);
            lableDetailsReview.setShow(1);
            lableDetailsReview.setStatus("0");
            lableDetailsReview.setLableId(lableId);
            lableDetailsReview.setRemake(remake);
            lableDetailsReview.setUserBId(userBId);
            lableDetailsReview.setIndex(index);
            lableDetailsReview.setCleanType(99);
            lableDetailsReview.setCreateDate(new Date());
            lableDetailsReview.setUpdateDate(new Date());

            lablemerchant.setTreeLeaf("0");
            updateById(lablemerchant);
            insert(lableDetailsReview);
        }

        return lableDetailsReview;
    }

    public void addThirdClean(CleanPriceDetailEntity priceDetailEntity, LableDetailsReviewTreeEntity lableser) {
        LableDetailsReviewTreeEntity lableDetailsReview = new LableDetailsReviewTreeEntity();
        //车型
        Integer carType = priceDetailEntity.getCarType();
        //清洗类型
        Integer cleanType = priceDetailEntity.getCleanType();

        String carT = "";
        String cleanT = "";
        switch (carType) {
            case 1:
                carT = "车型1";
                break;
            case 2:
                carT = "车型2";
                break;
            case 3:
                carT = "车型3";
                break;
            case 4:
                carT = "车型4";
                break;
            case 5:
                carT = "车型5";
                break;
            case 6:
                carT = "车型6";
                break;
            case 7:
                carT = "车型7";
                break;
            case 8:
                carT = "车型8";
                break;
            default:
                carT = "车型1";
                break;
        }

        switch (cleanType) {
            case 1:
                cleanT = "普洗";
                break;
            case 2:
                cleanT = "精洗";
                break;
            case 3:
                cleanT = "自动";
                break;
            case 4:
                cleanT = "夜洗";
                break;
            case 5:
                cleanT = "免费";
                break;
            default:
                cleanT = "普洗";
                break;
        }


        //保存明细
        String lableCode = lableser.getLableCode();
        List<LableDetailsReviewTreeEntity> list = findidTreeleaf(lableCode);
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                LableDetailsReviewTreeEntity lableDetails = list.get(i);
                String lableCode1 = lableDetails.getLableCode();
                lableDetailsReview.setLableCode(String.valueOf(Integer.valueOf(lableCode1) + 1));
                lableDetailsReview.setLableName(carT + "/" + cleanT);
                lableDetailsReview.setTreeSort(BigDecimal.valueOf((list.size() + 1) * 30));
            }
        }
        if (list.isEmpty()) {
            String Code = lableser.getLableCode();
            lableDetailsReview.setLableCode(Code + "001");
            lableDetailsReview.setLableName(carT + "/" + cleanT);
            lableDetailsReview.setTreeSort(BigDecimal.valueOf(30));
        }

        //保存parents_code
        lableDetailsReview.setParentCode(lableCode);
        lableDetailsReview.setParentCodes(lableser.getParentCodes() + lableCode + ",");

        //tree_sorts
        String ssring = lableDetailsReview.getTreeSort().toString();
        int i = Integer.parseInt(ssring);
        String treeSorts = lableser.getTreeSorts();
        String s = bySting(ssring, i);
        lableDetailsReview.setTreeSorts(treeSorts + s + ",");
        lableDetailsReview.setTreeLeaf("1");
        //tree_level
        lableDetailsReview.setTreeLevel(BigDecimal.valueOf(2));
        String treeNames = lableser.getTreeNames();
        lableDetailsReview.setTreeNames(treeNames + "/" + lableDetailsReview.getLableCode());

        lableDetailsReview.setStatus("0");
        lableDetailsReview.setShow(0);
        lableDetailsReview.setUserBId(lableser.getUserBId());
        lableDetailsReview.setLableId(lableser.getLableId());
        lableDetailsReview.setOriginalPrice(priceDetailEntity.getOriginalPrice());
        lableDetailsReview.setPreferentialPrice(priceDetailEntity.getPreferentialPrice());
        lableDetailsReview.setThriePrice(priceDetailEntity.getThriePrice());
        lableDetailsReview.setCleanType(priceDetailEntity.getCleanType());
        lableDetailsReview.setCreateDate(new Date());
        lableDetailsReview.setUpdateDate(new Date());

        insert(lableDetailsReview);
        lableser.setTreeLeaf("0");
        updateById(lableser);

    }

    //修改对应明细数据
    public List<DetailsListDto> updatelist(LableDto list, Integer userBId, String remake) {
        List<DetailsListDto> lableDetail = new ArrayList<>();
        Integer lableId = list.getLableId();
        DetailsListDto[] listDtos = list.getListDtos();
        for (DetailsListDto listDto : listDtos) {
            Integer detailsId = listDto.getDetailsId();
            //根据商户id，服务id，和明细id查询到对应数据
            EntityWrapper<LableDetailsReviewTreeEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("user_b_id", userBId)
                    .eq("lable_id", lableId)
                    .eq("lable_details_id", detailsId);
            List<LableDetailsReviewTreeEntity> list1 = selectList(wrapper);
            if (list1.isEmpty()) {
                lableDetail.add(listDto);
            } else {
                for (LableDetailsReviewTreeEntity lableDet : list1) {
                    lableDet.setRebates(listDto.getRebats());
                    updateById(lableDet);
                }
            }
        }

        EntityWrapper<LableDetailsReviewTreeEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_b_id", userBId)
                .eq("lable_id", lableId)
                .eq("`show`", 1);
        List<LableDetailsReviewTreeEntity> list111 = selectList(wrapper);
        logger.error(" 查询刚刚保存的数据 #### list  list111={}", list111);
        if (!list111.isEmpty()) {
            LableDetailsReviewTreeEntity lableDetailsReviewTreeEntity = list111.get(0);
            logger.error(" 查询的列表数据 ####  lableDetailsReviewTreeEntity={}", lableDetailsReviewTreeEntity);
            lableDetailsReviewTreeEntity.setRemake(remake);
            lableDetailsReviewTreeEntity.setState(0);
            updateById(lableDetailsReviewTreeEntity);
        }
        return lableDetail;
    }

    public List<LableDetailsEntity> findProjectDetails(Integer user_b_id, Integer id) {
        return lableDetailsReviewTreeMapper.findProjectDetails(user_b_id, id);
    }

    public LableDetailsReviewTreeEntity findNowProject(String id, Integer userBId) {
        return lableDetailsReviewTreeMapper.findNowProject(id, userBId);
    }

    public LableDetailsReviewTreeEntity addBeautySecond(Integer userBId, Integer lableId, String remake) {
        //获取到对应服务得名称
        LableEntity lableEntity = lableService.selectById(lableId);
        String label = lableEntity.getLabel();
        Integer index = lableEntity.getIndex();
        LableDetailsReviewTreeEntity lableDetailsReview = new LableDetailsReviewTreeEntity();
        //根据商户id查询到一级数据
        EntityWrapper<LableDetailsReviewTreeEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_b_id", userBId)
                .eq("parent_code", 0);
        List<LableDetailsReviewTreeEntity> lablemerchants = selectList(wrapper);
        for (LableDetailsReviewTreeEntity lablemerchant : lablemerchants) {
            //保存服务
            //一级内容得code
            String lableCode = lablemerchant.getLableCode();
            //找到下面得所有二级
            List<LableDetailsReviewTreeEntity> list = findidTreeleaf(lableCode);
            for (int i = 0; i < list.size(); i++) {
                if (i == list.size() - 1) {
                    LableDetailsReviewTreeEntity lastSecond = list.get(i);
                    String lableCode1 = lastSecond.getLableCode();
                    lableDetailsReview.setLableCode(String.valueOf(Integer.valueOf(lableCode1) + 1));
                    lableDetailsReview.setLableName(label);
                    lableDetailsReview.setTreeSort(BigDecimal.valueOf((list.size() + 1) * 30));

                }
            }
            if (list.isEmpty()) {
                String lableCode1 = lablemerchant.getLableCode();
                lableDetailsReview.setLableCode(lableCode1 + "001");
                lableDetailsReview.setLableName(label);
                lableDetailsReview.setTreeSort(BigDecimal.valueOf(30));
            }
            //保存parent_codes
            lableDetailsReview.setParentCode(lableCode);
            lableDetailsReview.setParentCodes(lablemerchant.getParentCodes() + lableCode + ",");
            //保存tree_sorts
            String s = lableDetailsReview.getTreeSort().toString();
            int i = Integer.parseInt(s);
            String treeSorts = lablemerchant.getTreeSorts();
            String s1 = bySting(s, i);
            lableDetailsReview.setTreeSorts(treeSorts + s1 + ",");
            lableDetailsReview.setTreeLeaf("1");
            //设置tree_level
            BigDecimal treeLevel = lablemerchant.getTreeLevel();
            BigDecimal v = new BigDecimal(1);
            lableDetailsReview.setTreeLevel(treeLevel.add(v));
            //tree_names
            String treeNames = lablemerchant.getTreeNames();
            lableDetailsReview.setTreeNames(treeNames + "/" + lableDetailsReview.getLableCode());

            lableDetailsReview.setState(1);
            lableDetailsReview.setShow(1);
            lableDetailsReview.setStatus("0");
            lableDetailsReview.setLableId(lableId);
            lableDetailsReview.setRemake(remake);
            lableDetailsReview.setUserBId(userBId);
            lableDetailsReview.setIndex(index);
            lableDetailsReview.setCleanType(88);
            lableDetailsReview.setCreateDate(new Date());
            lableDetailsReview.setUpdateDate(new Date());

            lablemerchant.setTreeLeaf("0");
            updateById(lablemerchant);
            insert(lableDetailsReview);
        }
        return lableDetailsReview;
    }

    public void addThirdBeauty(AppBeautyPriceDetailEntity appBeautyPriceDetailEntity, LableDetailsReviewTreeEntity addSecond) {
        LableDetailsReviewTreeEntity lableDetailsReview = new LableDetailsReviewTreeEntity();
        //车型
        Integer carType = appBeautyPriceDetailEntity.getCarType();
        //清洗类型
        Integer beautyType = appBeautyPriceDetailEntity.getBeautyType();

        String carT = "";
        String cleanT = "";
        switch (carType) {
            case 1:
                carT = "车型1";
                break;
            case 2:
                carT = "车型2";
                break;
            case 3:
                carT = "车型3";
                break;
            case 4:
                carT = "车型4";
                break;
            case 5:
                carT = "车型5";
                break;
            case 6:
                carT = "车型6";
                break;
            case 7:
                carT = "车型7";
                break;
            case 8:
                carT = "车型8";
                break;
            default:
                carT = "车型1";
                break;
        }

        switch (beautyType) {
            case 1:
                cleanT = "打蜡";
                break;
            case 2:
                cleanT = "抛光";
                break;
            case 3:
                cleanT = "内堂";
                break;
            case 4:
                cleanT = "机舱";
                break;
            case 5:
                cleanT = "装甲";
                break;
            //case 6: cleanT="免费";break;
            //case 7: cleanT="免费";break;
            //case 9: cleanT="免费";break;
            default:
                cleanT = "打蜡";
                break;
        }


        //保存明细
        String lableCode = addSecond.getLableCode();
        List<LableDetailsReviewTreeEntity> list = findidTreeleaf(lableCode);
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                LableDetailsReviewTreeEntity lableDetails = list.get(i);
                String lableCode1 = lableDetails.getLableCode();
                lableDetailsReview.setLableCode(String.valueOf(Integer.valueOf(lableCode1) + 1));
                lableDetailsReview.setLableName(carT + "/" + cleanT);
                lableDetailsReview.setTreeSort(BigDecimal.valueOf((list.size() + 1) * 30));
            }
        }
        if (list.isEmpty()) {
            String Code = addSecond.getLableCode();
            lableDetailsReview.setLableCode(Code + "001");
            lableDetailsReview.setLableName(carT + "/" + cleanT);
            lableDetailsReview.setTreeSort(BigDecimal.valueOf(30));
        }

        //保存parents_code
        lableDetailsReview.setParentCode(lableCode);
        lableDetailsReview.setParentCodes(addSecond.getParentCodes() + lableCode + ",");

        //tree_sorts
        String ssring = lableDetailsReview.getTreeSort().toString();
        int i = Integer.parseInt(ssring);
        String treeSorts = addSecond.getTreeSorts();
        String s = bySting(ssring, i);
        lableDetailsReview.setTreeSorts(treeSorts + s + ",");
        lableDetailsReview.setTreeLeaf("1");
        //tree_level
        lableDetailsReview.setTreeLevel(BigDecimal.valueOf(2));
        String treeNames = addSecond.getTreeNames();
        lableDetailsReview.setTreeNames(treeNames + "/" + lableDetailsReview.getLableCode());

        lableDetailsReview.setStatus("0");
        lableDetailsReview.setShow(0);
        lableDetailsReview.setUserBId(addSecond.getUserBId());
        lableDetailsReview.setLableId(addSecond.getLableId());
        lableDetailsReview.setOriginalPrice(appBeautyPriceDetailEntity.getOriginalPrice());
        lableDetailsReview.setPreferentialPrice(appBeautyPriceDetailEntity.getPreferentialPrice());
        lableDetailsReview.setThriePrice(appBeautyPriceDetailEntity.getThriePrice());
        lableDetailsReview.setCleanType(appBeautyPriceDetailEntity.getBeautyType());
        lableDetailsReview.setCreateDate(new Date());
        lableDetailsReview.setUpdateDate(new Date());

        insert(lableDetailsReview);
        addSecond.setTreeLeaf("0");
        updateById(addSecond);

    }

    public JSONObject addnewSer(JSONObject in, Integer wrokType, Integer userBId) {
        JSONObject result = new JSONObject();
        AppUserBMessageEntity appUserBMessage = new AppUserBMessageEntity();
        appUserBMessage.setName(in.getString("name"));
        appUserBMessage.setHeadImg(in.getString("headImg"));
        appUserBMessage.setWorkPlace(in.getString("workPlace"));
        appUserBMessage.setLng(in.getBigDecimal("lng"));
        appUserBMessage.setLat(in.getBigDecimal("lat"));
        appUserBMessage.setTrailerPlate(in.getString("trailerPlate"));
        appUserBMessage.setIntroduction(in.getString("introduction"));
        appUserBMessage.setWrokType(wrokType);
        appUserBMessage.setDriverYear(in.getString("driverYear"));
        appUserBMessage.setPhone(in.getString("phone"));
        appUserBMessage.setTechnologyYear(in.getString("technologyYear"));
        appUserBMessage.setScore(new BigDecimal(5));
        appUserBMessage.setOrderNumber(0);
        appUserBMessage.setLevel(1);
        appUserBMessage.setUserBId(userBId);
        appUserBMessage.setCreateTime(new Date());
        appUserBMessage.setUpdateTime(new Date());
        appUserBMessage.setPrice(in.getBigDecimal("price"));
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);
        String substring = dateString.substring(dateString.length() - 6);
        appUserBMessage.setSerialNumber(String.valueOf(Integer.parseInt(substring) + 5000000));

        appUserBMessageService.insert(appUserBMessage);

        //保存图片
        JSONObject json = JSONObject.parseObject(String.valueOf(in));
        JSONArray jsonArray = (JSONArray) json.get("url");
        List<AppUserBMessageImgEntity> urls = JSONArray.parseArray(jsonArray.toString(), AppUserBMessageImgEntity.class);
        if (urls.isEmpty()) {
            result.put("code", 407);
            result.put("msg", "基本图片不能为空");
            return result;
        }


        //拖车
        if (appUserBMessage.getWrokType() == 1) {
            JSONArray trailer = (JSONArray) json.get("trailerUrl");
            if (trailer == null) {
                result.put("code", 407);
                result.put("msg", "拖车图片不能为空");
                return result;
            }
            List<String> trailerurls = JSONArray.parseArray(trailer.toString(), String.class);
            for (int i = 0; i < trailerurls.size(); i++) {
                AppUserBMessageImgEntity messageImgEntity = new AppUserBMessageImgEntity();
                messageImgEntity.setType(2);
                String s = trailerurls.get(i);
                messageImgEntity.setUrl(s);
                messageImgEntity.setIndex(i + 1);
                messageImgEntity.setCreateTime(new Date());
                messageImgEntity.setUpdateTime(new Date());
                messageImgEntity.setbMessageId(appUserBMessage.getId());
                appUserBMessageImgService.insert(messageImgEntity);
            }
        }


        for (AppUserBMessageImgEntity url : urls) {
            AppUserBMessageImgEntity messageImgEntity = new AppUserBMessageImgEntity();
            messageImgEntity.setType(1);
            messageImgEntity.setUrl(url.getUrl());
            messageImgEntity.setIndex(url.getIndex());
            messageImgEntity.setCreateTime(new Date());
            messageImgEntity.setUpdateTime(new Date());
            messageImgEntity.setbMessageId(appUserBMessage.getId());
            appUserBMessageImgService.insert(messageImgEntity);
        }


        result.put("code", 200);
        result.put("data", appUserBMessage);
        return result;
    }

    public void findser(String wrokType, Integer userBId) {
        if (wrokType.equals("1")) {
            //救援
            wrokType = "13";
        } else if (wrokType.equals("2")) {
            //年检
            wrokType = "15";
        } else if (wrokType.equals("3")) {
            //喷漆
            wrokType = "12";
        } else if (wrokType.equals("4")) {
            wrokType = "19";
        }
        EntityWrapper<LableDetailsReviewTreeEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_b_id", userBId)
                .eq("`show`", 1)
                .eq("lable_id", wrokType);
        LableDetailsReviewTreeEntity lableDetailsReviewTreeEntity = selectOne(wrapper);
        Integer state = lableDetailsReviewTreeEntity.getState();
        if (state != 0) {
            lableDetailsReviewTreeEntity.setState(0);
            updateById(lableDetailsReviewTreeEntity);
        }
    }
}
