package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.web.constant.AppAuctionVipSet;
import com.cheji.web.modular.domain.AppAuctionBrandSubEntity;
import com.cheji.web.modular.domain.AppUserEntity;
import com.cheji.web.modular.domain.UserEntity;
import com.cheji.web.modular.service.AppAuctionBrandSubService;
import com.cheji.web.modular.service.AppAuctionFeedBackService;
import com.cheji.web.modular.service.UserService;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/brandSub")
public class AppAuctionBrandSubController extends BaseController {

    @Autowired
    private AppAuctionBrandSubService appAuctionBrandSubService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "品牌订阅")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "brand", value = "品牌", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "imgUrl", value = "图片地址", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/addBrand", method = RequestMethod.POST)
    public JSONObject addBrand(@RequestBody AppAuctionBrandSubEntity appAuctionBrandSub, HttpServletRequest httpServletRequest) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        Integer id = currentLoginUser.getAppUserEntity().getId();
        UserEntity appUserEntity = userService.selectById(id);
        if (Objects.isNull(appUserEntity.getVipLv()) || appUserEntity.getVipLv() < AppAuctionVipSet.VIP_COMMON_ONE) {
            result.put("code", 531);
            result.put("msg", "请升级vip后使用该功能!");
            return result;
        }

        JSONObject object = appAuctionBrandSubService.addBrand(result, appAuctionBrandSub, appUserEntity);
        return object;
    }


    @ApiOperation(value = "删除品牌订阅")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "delId", value = "删除的id", required = true, dataType = "Long"),
    })
    @RequestMapping(value = "/delBrand", method = RequestMethod.GET)
    public JSONObject delBrand(@RequestParam(required = false) Long delId, HttpServletRequest httpServletRequest) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);

        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        AppUserEntity appUserEntity = currentLoginUser.getAppUserEntity();
        UserEntity userEntity = userService.selectById(appUserEntity.getId());
        if (userEntity.getVipLv() == null) {
            result.put("code", 531);
            result.put("msg", "请升级vip后使用该功能!");
            return result;
        }

        if (Objects.nonNull(delId)) {
            boolean b = appAuctionBrandSubService.deleteById(delId);
            if (b) {
                result.put("code", 200);
                result.put("msg", "删除成功!");
                return result;
            } else {
                result.put("code", 200);
                result.put("msg", "删除失败,请联系管理员!");
                return result;
            }
        }

        result.put("code", 502);
        result.put("msg", "参数异常!");
        return result;
    }

    @ApiOperation(value = "查询品牌订阅")
    @RequestMapping(value = "/queryBrand", method = RequestMethod.GET)
    public JSONObject queryBrand(HttpServletRequest httpServletRequest) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        AppUserEntity appUserEntity = currentLoginUser.getAppUserEntity();
        UserEntity userEntity = userService.selectById(appUserEntity.getId());
        if (userEntity.getVipLv() == null) {
            result.put("code", 531);
            result.put("msg", "请升级vip后使用该功能!");
            return result;
        }
        List<AppAuctionBrandSubEntity> brands = appAuctionBrandSubService.selectList(new EntityWrapper<AppAuctionBrandSubEntity>().eq("user_id", appUserEntity.getId()));
        if (brands.size() > 0) {
            result.put("code", 200);
            result.put("msg", "查询成功!");
            result.put("data", brands);
            return result;
        } else {
            result.put("code", 200);
            result.put("msg", "暂无数据!");
            result.put("data", brands);
            return result;
        }
    }

}