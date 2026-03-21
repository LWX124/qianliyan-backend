package com.cheji.web.modular.controller;


import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.domain.AppCouponTypeEntity;
import com.cheji.web.modular.domain.AppUserCouponEntity;
import com.cheji.web.modular.domain.UserEntity;
import com.cheji.web.modular.service.AppCouponTypeService;
import com.cheji.web.modular.service.AppUserCouponService;
import com.cheji.web.modular.service.UserService;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * <p>
 * 用户优惠卷表 前端控制器
 * </p>
 *
 * @author Ashes
 * @since 2020-07-17
 */
@RestController
@RequestMapping("/appUserCoupon")
public class AppUserCouponController extends BaseController {

    @Resource
    private AppUserCouponService appUserCouponService;

    @Resource
    private UserService userService;

    @Resource
    private AppCouponTypeService appCouponTypeService;

    //新增优惠卷
//    @ApiOperation(value = "添加优惠卷用户")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "id", value = "id", required = true, dataType = "Integer"),
//    })
//    @RequestMapping(value = "addCoupon", method = RequestMethod.GET)
//    public JSONObject addCoupon(Integer userId) {
//        JSONObject object = new JSONObject();
//        if (userId == null) {
//            object.put("code", 407);
//            object.put("msg", "id为空");
//            return object;
//        }
//        UserEntity userEntity = userService.selectById(userId);
//        if (userEntity == null) {
//            object.put("code", 407);
//            object.put("msg", "id有误");
//            return object;
//        }
//        appUserCouponService.addCoupon(userId);
//        object.put("code", 200);
//        object.put("msg", "成功");
//        return object;
//    }


    //优惠卷列表，
    @ApiOperation(value = "优惠卷列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
    })
    @RequestMapping(value = "couponList", method = RequestMethod.GET)
    public JSONObject couponList(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userid = currentLoginUser.getAppUserEntity().getId();
        //查询下面优惠卷
        List<AppUserCouponEntity> appUserCouponList = appUserCouponService.findCoupon(userid);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", appUserCouponList);
        return result;
    }


    @ApiOperation(value = "优惠卷详情" +
            "407:id有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "优惠卷id", required = true, dataType = "Integer"),
    })
    @RequestMapping(value = "couponDetails", method = RequestMethod.GET)
    public JSONObject couponDetails(Integer id) {
        JSONObject result = new JSONObject();
        if (id == null) {
            result.put("code", 407);
            result.put("msg", "id有误");
            return result;
        }
        AppUserCouponEntity appUserCouponEntity = appUserCouponService.selectById(id);
        if (appUserCouponEntity == null) {
            result.put("code", 407);
            result.put("msg", "id有误");
            return result;
        }
        Integer couponId = appUserCouponEntity.getCouponId();
        AppCouponTypeEntity appCouponTypeEntity = appCouponTypeService.selectById(couponId);

        JSONObject object = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(appUserCouponEntity.getValidityTime());
        //有效期
        object.put("periodValidity", format);
        //金额
        object.put("money", appCouponTypeEntity.getMoney());
        //可用时间
        object.put("userTime", "不限");
        //订单类别
        object.put("type", appCouponTypeEntity.getType());
        //订单金额
        if (appCouponTypeEntity.getType() == 1) {
            object.put("userMoney","喷漆两面以上使用");
        }else {
            object.put("userMoney","满一元以上可用");
        }
        object.put("city","成都");

        //查询下面优惠卷
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", object);
        return result;
    }

}
