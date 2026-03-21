package com.cheji.b.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.b.modular.domain.MerchantsInfoBannerEntity;
import com.cheji.b.modular.dto.ImgSaveDto;
import com.cheji.b.modular.service.MerchantsInfoBannerService;
import com.cheji.b.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/merchantsInfoBanner")
public class MerchantsInfoBannerController extends BaseController{
    @Autowired
    public MerchantsInfoBannerService merchantsInfoBannerService;

    //添加图片
    @ApiOperation(value = "添加图片")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "token",value = "token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "otherImg",value ="其他图片集合",required = true,dataType = "ArrayList"),
    })
    @RequestMapping(value = "/addImg",method = RequestMethod.POST)
    public JSONObject addMasterImg(HttpServletRequest request,@RequestBody ImgSaveDto imgSaveDto){
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser==null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        //保存门店主图
        //根据商户id来保存数据
        merchantsInfoBannerService.addImg(imgSaveDto,userBId);


        EntityWrapper<MerchantsInfoBannerEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_b_id", userBId);
        List<MerchantsInfoBannerEntity> bannerEntityList = merchantsInfoBannerService.selectList(wrapper);
        ArrayList<MerchantsInfoBannerEntity> merchantsInfoBanner = new ArrayList<>(10);
        if (bannerEntityList.size()<7){
            flag: for (int i = 1; i < 8; i++) {              //  1234567
                for (MerchantsInfoBannerEntity infoBannerEntity : bannerEntityList) {  //13
                    Integer index = infoBannerEntity.getIndex();
                    if (index==i){
                        merchantsInfoBanner.add(infoBannerEntity);
                        continue flag;
                    }
                }
                MerchantsInfoBannerEntity merchantsInfoBannerEntity = new MerchantsInfoBannerEntity();
                merchantsInfoBannerEntity.setIndex(i);
                merchantsInfoBanner.add(merchantsInfoBannerEntity);
                if (merchantsInfoBanner.size()==7){
                    break;
                }
            }
            result.put("data", merchantsInfoBanner);
        }else {
            result.put("data", bannerEntityList);
        }

        result.put("code", 200);
        result.put("msg", "成功");

        return result;
    }

    @ApiOperation(value = "删除图片")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "token",value = "token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "id",value ="图片id",required = true,dataType = "String"),
    })
    @RequestMapping(value = "/deletImg",method = RequestMethod.GET)
    public JSONObject deletImg(String id){
        JSONObject result = new JSONObject();
        //根据id删除数据
        merchantsInfoBannerService.deleteById(Long.valueOf(id));
        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }
}
