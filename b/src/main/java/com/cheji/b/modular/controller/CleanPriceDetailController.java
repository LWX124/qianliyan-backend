package com.cheji.b.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.b.constant.RedisConstant;
import com.cheji.b.modular.domain.AppBUserConfigEntity;
import com.cheji.b.modular.domain.CleanPriceDetailEntity;
import com.cheji.b.modular.domain.LableDetailsReviewTreeEntity;
import com.cheji.b.modular.dto.PriceDetailsDto;
import com.cheji.b.modular.excep.CusException;
import com.cheji.b.modular.service.AppBUserConfigService;
import com.cheji.b.modular.service.CleanPriceDetailService;
import com.cheji.b.modular.service.LableDetailsReviewTreeService;
import com.cheji.b.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/cleanPriceDetail")
public class CleanPriceDetailController extends BaseController {
    @Autowired
    public CleanPriceDetailService cleanPriceDetailService;

    @Resource
    private AppBUserConfigService appBUserConfigService;

    @Resource
    private LableDetailsReviewTreeService lableDetailsReviewTreeService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation(value = "保存洗车价格明细")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "businessType", value = "营业状态", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "passFreeCarwash", value = "是否开通免费洗车", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "manualAntomatic", value = "人工/自动", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "startTime", value = "开始时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endTime", value = "结束时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "openWashTime", value = "是否开通营业时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "data", value = "清洗类型，车型，原价，优惠价，到手价", required = false, dataType = "List<PriceDetailsDto>")
    })
    @RequestMapping(value = "/saveCleanDeatils", method = RequestMethod.POST)
    public JSONObject saveCleanDeatils(@RequestBody CleanPriceDetailEntity priceDetailEntity, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        if (priceDetailEntity == null) {
            result.put("code", 520);
            result.put("msg", "参数无数据");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();

        priceDetailEntity.setUserBId(userBId);

        //开通免费洗车
        if (priceDetailEntity.getPassFreeCarwash().equals("1")){
            //判断是不是合约用户
            EntityWrapper<CleanPriceDetailEntity> entityWrapper = new EntityWrapper<>();
            entityWrapper.eq("user_b_id", priceDetailEntity.getUserBId())
                    .eq("contract_project",1);
            List<CleanPriceDetailEntity> cleanPriceDetailEntities = cleanPriceDetailService.selectList(entityWrapper);
            //是合约用户
            if (cleanPriceDetailEntities!=null){
                //不能修改
                result.put("code", 402);
                result.put("msg", "合约用户不能修改免费洗车");
                return result;
            }
        }

        //没有其他参数，只修改营业数据
        EntityWrapper<AppBUserConfigEntity> appBUserConfigWrapper = new EntityWrapper<>();
        appBUserConfigWrapper.eq("user_b_id", userBId);
        AppBUserConfigEntity appBUserConfigEntity = appBUserConfigService.selectOne(appBUserConfigWrapper);
        appBUserConfigService.isadd(userBId, priceDetailEntity, appBUserConfigEntity);

        //判断数据data
        //保存到审核页面
        //先查询是否有商户，
        //没有就新增，
        //有就添加一个服务   0.1
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.CAR_WASH_SERVICE_PLATFORM_RATIO);
        BigDecimal bigDecimal = new BigDecimal(1);
        //0.9
        BigDecimal subtract = bigDecimal.subtract(new BigDecimal(s));
        if (priceDetailEntity.getData() != null) {
            for (PriceDetailsDto datum : priceDetailEntity.getData()) {
                if (datum.getOriginalPrice()==null){
                    result.put("code", 402);
                    result.put("msg", "原价不能为空");
                    return result;
                }else if (datum.getCarType()==null){
                    result.put("code", 402);
                    result.put("msg", "车型不能为空");
                    return result;
                }else if (datum.getCleanType()==null){
                    result.put("code", 402);
                    result.put("msg", "清洗类型不能为空");
                    return result;
                }else if (datum.getPreferentialPrice()==null){
                    result.put("code", 402);
                    result.put("msg", "现价不能为空");
                    return result;
                }
            }

            try {
                lableDetailsReviewTreeService.addFirst(priceDetailEntity.getUserBId());
            } catch (CusException e) {
                result.put("code", e.getCode());
                result.put("msg", e.getMessage());
                return result;
            }
            //为空就是已经添加了服务
            //添加服务
            //先查询是否有这层
            EntityWrapper<LableDetailsReviewTreeEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("user_b_id", priceDetailEntity.getUserBId())
                    .eq("lable_id", 4)
                    .eq("`show`", 1);
            LableDetailsReviewTreeEntity addSecond = lableDetailsReviewTreeService.selectOne(wrapper);
            if (addSecond == null) {
                addSecond = lableDetailsReviewTreeService.addCleanSecond(priceDetailEntity.getUserBId(), 4, null);
            }
            EntityWrapper<CleanPriceDetailEntity> entityWrapper = new EntityWrapper<>();
            entityWrapper.eq("user_b_id", priceDetailEntity.getUserBId());
            cleanPriceDetailService.delete(entityWrapper);
            for (PriceDetailsDto datum : priceDetailEntity.getData()) {
                //先删除明细表中的数据
                //根据商户id，对应车型，清洗类型删除唯一数据
                CleanPriceDetailEntity priceDetailEntity1 = new CleanPriceDetailEntity();
                if (priceDetailEntity.getBusinessType()==null){
                    result.put("code", 402);
                    result.put("msg", "BusinessType不能为空");
                    return result;
                }
                if (priceDetailEntity.getManualAntomatic()==null){
                    result.put("code", 402);
                    result.put("msg", "ManualAntomatic不能为空");
                    return result;
                }
                if (priceDetailEntity.getPassFreeCarwash()==null){
                    result.put("code", 402);
                    result.put("msg", "PassFreeCarwash不能为空");
                    return result;
                }
                priceDetailEntity1.setUserBId(priceDetailEntity.getUserBId());
                priceDetailEntity1.setCleanType(Integer.valueOf(datum.getCleanType()));
                priceDetailEntity1.setCarType(Integer.valueOf(datum.getCarType()));
                priceDetailEntity1.setOriginalPrice(datum.getOriginalPrice());
                priceDetailEntity1.setPreferentialPrice(datum.getPreferentialPrice());
                priceDetailEntity1.setThriePrice(datum.getPreferentialPrice().multiply(subtract));
                priceDetailEntity1.setState(1);
                priceDetailEntity1.setCreateTime(new Date());
                priceDetailEntity1.setUpdateTime(new Date());
                cleanPriceDetailService.insert(priceDetailEntity1);
                //lableDetailsReviewTreeService.addThirdClean(priceDetailEntity1, addSecond);
            }
        }
        result.put("code", 200);
        result.put("mesg", "成功");
        return result;
    }


    @ApiOperation(value = "洗车价格参数")
    @RequestMapping(value = "/cleanParameter", method = RequestMethod.GET)
    public JSONObject cleanParameter(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        CleanPriceDetailEntity cleanPriceDetailEntity = new CleanPriceDetailEntity();
        ArrayList<PriceDetailsDto> detailsDtos = new ArrayList<>();
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        //根据userbid查询到config中数据
        EntityWrapper<AppBUserConfigEntity> userConfigEntityEntityWrapper = new EntityWrapper<>();
        userConfigEntityEntityWrapper.eq("user_b_id", userBId);
        AppBUserConfigEntity appBUserConfigEntity = appBUserConfigService.selectOne(userConfigEntityEntityWrapper);

        if (appBUserConfigEntity != null) {
            cleanPriceDetailEntity.setBusinessType(appBUserConfigEntity.getBusinessType());
            if (appBUserConfigEntity.getPassFreeCarwash()==null){
                appBUserConfigEntity.setPassFreeCarwash("0");
            }
            cleanPriceDetailEntity.setPassFreeCarwash(appBUserConfigEntity.getPassFreeCarwash());
            cleanPriceDetailEntity.setManualAntomatic(appBUserConfigEntity.getManualAntomatic());
            cleanPriceDetailEntity.setStartTime(appBUserConfigEntity.getStartTime());
            cleanPriceDetailEntity.setEndTime(appBUserConfigEntity.getEndTime());
            cleanPriceDetailEntity.setOpenWashTime(appBUserConfigEntity.getOpenWashTime());
        }else {
            cleanPriceDetailEntity.setPassFreeCarwash("0");
        }
        for (int i = 1; i < 3; i++) {           //普洗，精洗
            for (int j = 1; j < 5; j++) {
                PriceDetailsDto priceDetailsDto = new PriceDetailsDto();
                if (i == 1) {
                    priceDetailsDto.setCleanName("普洗");
                } else {
                    priceDetailsDto.setCleanName("精洗");
                }
                priceDetailsDto.setCleanType(String.valueOf(i));
                if (j == 1) {
                    priceDetailsDto.setCarName("小型轿车");
                } else if (j == 2) {
                    priceDetailsDto.setCarName("小型越野");
                } else if (j == 3) {
                    priceDetailsDto.setCarName("大型越野");
                } else {
                    priceDetailsDto.setCarName("商务车");
                }
                priceDetailsDto.setCarType(String.valueOf(j));
                //查询对应数据
                EntityWrapper<CleanPriceDetailEntity> detailEntityWrapper = new EntityWrapper<>();
                detailEntityWrapper.eq("clean_type", i)
                        .eq("car_type", j)
                        .eq("user_b_id", userBId);
                CleanPriceDetailEntity cleanPriceDetail = cleanPriceDetailService.selectOne(detailEntityWrapper);
                if (cleanPriceDetail != null) {
                    priceDetailsDto.setOriginalPrice(cleanPriceDetail.getOriginalPrice());
                    priceDetailsDto.setPreferentialPrice(cleanPriceDetail.getPreferentialPrice());
                    priceDetailsDto.setThriePrice(cleanPriceDetail.getThriePrice());
                    Integer contractProject = cleanPriceDetail.getContractProject();
                    if (contractProject!=null){
                        priceDetailsDto.setContractProject(contractProject);
                    }
                    Integer residueDegree = cleanPriceDetail.getResidueDegree();
                    if (residueDegree!=null){
                        priceDetailsDto.setResidueDegree(String.valueOf(residueDegree));
                    }
                }
                detailsDtos.add(priceDetailsDto);
            }
        }
        cleanPriceDetailEntity.setData(detailsDtos);

        String s = stringRedisTemplate.opsForValue().get(RedisConstant.CAR_WASH_SERVICE_PLATFORM_RATIO);
        BigDecimal bigDecimal = new BigDecimal(1);
        BigDecimal subtract = bigDecimal.subtract(new BigDecimal(s));
        //结算到用户比例
        cleanPriceDetailEntity.setRabates(subtract);


        result.put("code", 200);
        result.put("data", cleanPriceDetailEntity);
        return result;
    }

}

