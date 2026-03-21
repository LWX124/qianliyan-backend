package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.service.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.parser.Entity;

import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/merchantsCommentsTree")
public class MerchantsCommentsTreeController extends BaseController {
    @Resource
    private MerchantsCommentsTreeService merchantsCommentsTreeService;

    @Resource
    private ImgService imgService;

    @Resource
    private CleanIndetService cleanIndetService;

    @Resource
    private IndentService indentService;

    @Resource
    private AppSprayPaintIndentService appSprayPaintIndentService;

    @Resource
    private AppRescueIndentService appRescueIndentService;

    @Resource
    private AppYearCheckIndentService appYearCheckIndentService;

    @Resource
    private AppSubstituteDrivingIndentService appSubstituteDrivingIndentService;

    //商户评论接口
    @ApiOperation(value = "保存商户评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "merchantsId", value = "商户id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "content", value = "评论内容", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "commentImg", value = "评论图片", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "efficiensyScore", value = "效率分", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "serviceScore", value = "服务分", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "parentCode", value = "父级评论", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "lable", value = "标签", required = false, dataType = "String")
    })
    @RequestMapping(value = "/saveComment", method = RequestMethod.POST)
    public JSONObject saveComment(@RequestBody MerchantsCommentsTreeEntity merchantsComments, HttpServletRequest request) {
        //判断登陆
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        merchantsComments.setUserId(id);
        if (merchantsComments.getUserBId() == null) {
            if (merchantsComments.getMerchantsId() != null) {
                String merchantsId = merchantsComments.getMerchantsId();
                merchantsComments.setUserBId(Integer.valueOf(merchantsId));
            }
        }
        //根据订单查询拦截
        EntityWrapper<IndentEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", id)
                .eq("user_b_id", merchantsComments.getUserBId());
        //查询订单数量
        List<IndentEntity> indentEntities = indentService.selectList(wrapper);
        //判断list是否为空
        if (indentEntities == null || indentEntities.size() == 0) {
            result.put("code", 200);
            result.put("msg", "没有相应订单");
            return result;
        } else {
            //根据id查询到评论数据和订单数据
            List<MerchantsCommentsTreeEntity> commentsList = merchantsCommentsTreeService.findListByUseridAndMer(String.valueOf(merchantsComments.getUserBId()), id);
            if (indentEntities.size() < commentsList.size()) {
                result.put("code", 200);
                result.put("msg", "继续下单才可评论");
                return result;
            }
        }

        //保存商户评论
        merchantsComments.setType(1);
        merchantsCommentsTreeService.save(merchantsComments);
        //保存商户评论图片，且不为空
        if (merchantsComments.getCommentImg() != null && merchantsComments.getCommentImg().length != 0) {
            imgService.saveComments(merchantsComments.getCommentImg(), Long.valueOf(merchantsComments.getCommentsCode()));
        }

        //
        result.put("code", 200);
        result.put("msg", "保存成功");
        return result;

    }

    //洗车订单接口
    @ApiOperation(value = "保存洗车等订单评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "merchantsId", value = "商户id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "cleanIndentNumber", value = "订单编号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "content", value = "评论内容", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "commentImg", value = "评论图片", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "efficiensyScore", value = "效率分", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "serviceScore", value = "服务分", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "parentCode", value = "父级评论", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "lable", value = "标签", required = false, dataType = "String")
    })
    @RequestMapping(value = "/saveCleanComment", method = RequestMethod.POST)
    public JSONObject saveCleanComment(@RequestBody MerchantsCommentsTreeEntity merchantsComments, HttpServletRequest request) {
        //判断登陆
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        merchantsComments.setUserId(id);
        if (merchantsComments.getUserBId() == null && merchantsComments.getMerchantsId() == null) {
            result.put("code", 409);
            result.put("msg", "商户id为空");
            return result;
        }
        if (merchantsComments.getUserBId() == null) {
            if (merchantsComments.getMerchantsId() != null) {
                String merchantsId = merchantsComments.getMerchantsId();
                merchantsComments.setUserBId(Integer.valueOf(merchantsId));
            }
        }

        //还没有评论过的订单
//        List<CleanIndetEntity> cleanIndetEntities = cleanIndetService.selectIndentUser(id, merchantsComments.getUserBId());
//        if (cleanIndetEntities == null || cleanIndetEntities.size() == 0) {
//            result.put("code", 403);
//            result.put("msg", "没有相应订单");
//            return result;
//        } else {
//            List<MerchantsCommentsTreeEntity> commentsList = merchantsCommentsTreeService.findListByUseridAndMer(String.valueOf(merchantsComments.getUserBId()), id);
//            if (cleanIndetEntities.size() < commentsList.size()) {
//                result.put("code", 403);
//                result.put("msg", "继续下单才可评论");
//                return result;
//            }
//        }
        merchantsComments.setType(0);

        //保存商户评论
        merchantsCommentsTreeService.save(merchantsComments);


        //保存商户评论图片，且不为空
        if (merchantsComments.getCommentImg() != null && merchantsComments.getCommentImg().length != 0) {
            imgService.saveComments(merchantsComments.getCommentImg(), Long.valueOf(merchantsComments.getCommentsCode()));
            merchantsComments.setHaveImg(1);
        }
        String cleanIndentNumber = merchantsComments.getCleanIndentNumber();
        String substring = cleanIndentNumber.substring(0, 2);
        //如果是喷漆订单
        if (substring.equals("PQ")){
            EntityWrapper<AppSprayPaintIndentEntity> appSprayPaintWrapper = new EntityWrapper<>();
            appSprayPaintWrapper.eq("spray_paint_number",cleanIndentNumber);
            AppSprayPaintIndentEntity appSprayPaintIndentEntity = appSprayPaintIndentService.selectOne(appSprayPaintWrapper);
            appSprayPaintIndentEntity.setIsEvaluation(1);
            appSprayPaintIndentEntity.setState(9);
            appSprayPaintIndentService.updateById(appSprayPaintIndentEntity);
            merchantsComments.setType(12);
        }else if (substring.equals("ER")){
            //救援
            EntityWrapper<AppRescueIndentEntity> appRescueIndentWrapper = new EntityWrapper<>();
            appRescueIndentWrapper.eq("rescue_number",cleanIndentNumber);
            AppRescueIndentEntity rescueIndentEntity = appRescueIndentService.selectOne(appRescueIndentWrapper);
            rescueIndentEntity.setState(5);
            appRescueIndentService.updateById(rescueIndentEntity);
            merchantsComments.setType(13);
        } else if(substring.equals("NJ")){
            EntityWrapper<AppYearCheckIndentEntity> yearCheckIndentWrapper = new EntityWrapper<>();
            yearCheckIndentWrapper.eq("year_check_number",cleanIndentNumber);
            AppYearCheckIndentEntity appYearCheckIndentEntity = appYearCheckIndentService.selectOne(yearCheckIndentWrapper);
            appYearCheckIndentEntity.setState(5);
            appYearCheckIndentService.updateById(appYearCheckIndentEntity);
            merchantsComments.setType(15);
        }else if (substring.equals("SC")){
            EntityWrapper<AppSubstituteDrivingIndentEntity> appSubstituteDrivingWrapper = new EntityWrapper<>();
            appSubstituteDrivingWrapper.eq("substitute_driving_number",cleanIndentNumber);
            AppSubstituteDrivingIndentEntity appSubstituteDrivingIndentEntity = appSubstituteDrivingIndentService.selectOne(appSubstituteDrivingWrapper);
            appSubstituteDrivingIndentEntity.setIndentState(7);
            appSubstituteDrivingIndentService.updateById(appSubstituteDrivingIndentEntity);
            merchantsComments.setType(19);
        }else {
            EntityWrapper<CleanIndetEntity> entityWrapper = new EntityWrapper<>();
            entityWrapper.eq("clean_indent_number", cleanIndentNumber);
            CleanIndetEntity cleanIndetEntity = cleanIndetService.selectOne(entityWrapper);
            //查询到订单  修改订单到已评价
            cleanIndetEntity.setIsEvaluation("1");
            cleanIndetEntity.setIndentState("6");
            cleanIndetService.updateById(cleanIndetEntity);
            merchantsComments.setType(4);
        }
        merchantsCommentsTreeService.updateById(merchantsComments);

        //
        result.put("code", 200);
        result.put("msg", "保存成功");
        return result;

    }



    //查询所有商户评论
    @ApiOperation(value = "查询商户评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "brandId", value = "品牌", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "score", value = "推荐排序", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "页数", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/showComment", method = RequestMethod.GET)
    public JSONObject showComment(String brandId,String score,@RequestParam(required = false, defaultValue = "1") Integer pageSize) {
        //根据查询到所有有这个品牌下面的评论
        JSONObject result = new JSONObject();

        JSONArray showComment = merchantsCommentsTreeService.findShowComment(brandId, score, pageSize);

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", showComment);
        return result;

    }


    @ApiOperation(value = "查询商户评论")
    @RequestMapping(value = "/reportComment", method = RequestMethod.GET)
    public JSONObject reportComment() {
        //根据查询到所有有这个品牌下面的评论
        JSONObject result = new JSONObject();
        result.put("code", 200);
        result.put("msg", "成功");
        return result;

    }


}
