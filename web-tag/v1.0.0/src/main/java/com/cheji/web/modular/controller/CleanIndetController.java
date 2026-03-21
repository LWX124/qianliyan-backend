package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.cwork.CleanIndentDetailsDto;
import com.cheji.web.modular.cwork.CleanMerDto;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.excep.CusException;
import com.cheji.web.modular.mapper.CleanIndetMapper;
import com.cheji.web.modular.service.*;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/cleanIndet")
public class CleanIndetController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    public CleanIndetService cleanIndetService;

    @Resource
    private CleanPriceDetailService cleanPriceDetailService;

    @Resource
    private BUserService bUserService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AppBeautyPriceDetailService appBeautyPriceDetailService;

    @Resource
    private AppBUserConfigService appBUserConfigService;

    @Resource
    private UserService userService;

    @Resource
    private CleanIndetMapper cleanIndetMapper;

    @Resource
    private DefaultMQProducer mqProducer;

    /**
     * @param cleanIndentNumber
     * @return
     */
    @ApiOperation(value = "查询洗车等订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "cleanIndentNumber", value = "订单编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/cleanIndentDetails", method = RequestMethod.GET)
    public JSONObject cleanIndentDetails(String cleanIndentNumber, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        CleanIndentDetailsDto cleanIndentDetailsDto = new CleanIndentDetailsDto();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        //根据订单编号查询到订单
        EntityWrapper<CleanIndetEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("clean_indent_number", cleanIndentNumber);
        //查询到订单
        CleanIndetEntity cleanIndetEntity = cleanIndetService.selectOne(wrapper);
        if (cleanIndetEntity == null) {
            result.put("code", 405);
            result.put("msg", "没有查询到该订单，请稍后再试");
            return result;
        }

        if (cleanIndetEntity.getCarType() == 1) {
            cleanIndetEntity.setCarName("小型轿车");
        } else if (cleanIndetEntity.getCarType() == 2) {
            cleanIndetEntity.setCarName("小型越野");
        } else if (cleanIndetEntity.getCarType() == 3) {
            cleanIndetEntity.setCarName("大型越野");
        } else {
            cleanIndetEntity.setCarName("商务车");
        }

        String resource = cleanIndetEntity.getResource();
        CleanPriceDetailEntity priceDetailEntity = new CleanPriceDetailEntity();
        String bussinessId = cleanIndetEntity.getBussinessId();
        //洗车订单
        if (resource.equals("1")) {
            if (cleanIndetEntity.getCleanType() == 1) {
                cleanIndetEntity.setCleanName("普洗");
                cleanIndentDetailsDto.setDirectionsUse("1.普洗一次(含内饰和脚垫清洗)\n\n" +
                        "2.商家完成服务后请点击下方(收货)\n\n" +
                        "3.您完成收货后可在订单中留下评价\n\n" +
                        "4.自购买之日起七天内使用");
            } else {
                cleanIndetEntity.setCleanName("精洗");
                cleanIndentDetailsDto.setDirectionsUse("1.精洗一次(车内精致清洗)\n\n" +
                        "2.商家完成服务后请点击下方(收货)\n\n" +
                        "3.您完成收货后可在订单中留下评价\n\n" +
                        "4.自购买之日起七天内使用");
            }
            //查询到服务
            priceDetailEntity = cleanPriceDetailService.selectById(bussinessId);
            if (priceDetailEntity==null){
                priceDetailEntity = new CleanPriceDetailEntity();
                priceDetailEntity.setCarName("不限");
                priceDetailEntity.setCleanName("免费");
            }else {
                priceDetailEntity.setPreferentialPrice(cleanIndetEntity.getAmount());
            }
            //图片
            String img = "";
            if (cleanIndetEntity.getCleanType() == 1) {
                //普洗
                img = cleanIndetService.findcarImgpx(cleanIndetEntity.getCarType());
            } else {
                img = cleanIndetService.findcarImgJx(cleanIndetEntity.getCarType());
            }
            priceDetailEntity.setImgUrl(img);
        } else {
            priceDetailEntity.setPreferentialPrice(cleanIndetEntity.getAmount());
            Integer cleanType = cleanIndetEntity.getCleanType();
            String beautyname = cleanIndetService.findbeautyName(cleanType);
            cleanIndetEntity.setCleanName(beautyname);
            cleanIndentDetailsDto.setDirectionsUse(
                    "1.商家完成服务后请点击下方(收货)\n\n" +
                    "2.您完成收货后可在订单中留下评价\n\n" +
                    "3.自购买之日起七天内使用");
            String img = cleanIndetService.findbeautyImg(cleanType);
            priceDetailEntity.setImgUrl(img);
        }
        Date time = cleanIndetEntity.getCreateTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(time);
        cleanIndetEntity.setTime(dateString);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
        time = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endTime = format.format(time);
        cleanIndetEntity.setEndTime(endTime);


        CleanMerDto cleanMerDto = bUserService.findCleanDetails(cleanIndetEntity.getUserBId());
        EntityWrapper<AppBUserConfigEntity> configEntityEntityWrapper = new EntityWrapper<>();
        configEntityEntityWrapper.eq("user_b_id",cleanIndetEntity.getUserBId());
        AppBUserConfigEntity appBUserConfigEntity = appBUserConfigService.selectOne(configEntityEntityWrapper);
        cleanMerDto.setStartTime(appBUserConfigEntity.getStartTime());
        cleanMerDto.setEndTime(appBUserConfigEntity.getEndTime());
        //查询订单数
        //查询总订单数该商家的
        EntityWrapper<CleanIndetEntity> cleanIndetWrapper = new EntityWrapper<>();
        cleanIndetWrapper.eq("user_b_id", cleanIndetEntity.getUserBId())
                .eq("resource", "1");
        List<CleanIndetEntity> cleanIndetEntities = cleanIndetService.selectList(cleanIndetWrapper);
        if (cleanIndetEntities.isEmpty()) {
            cleanMerDto.setIndentCount(0);
        } else {
            cleanMerDto.setIndentCount(cleanIndetEntities.size());
        }


        cleanIndentDetailsDto.setCleanIndetEntity(cleanIndetEntity);
        cleanIndentDetailsDto.setCleanMerDto(cleanMerDto);
        cleanIndentDetailsDto.setCleanPriceDetailEntity(priceDetailEntity);
        result.put("code", 200);
        result.put("data", cleanIndentDetailsDto);
        return result;
    }


//    @ApiOperation(value = "查询美容订单详情")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "beautyIndentNumber", value = "订单编号", required = true, dataType = "String")
//    })
//    @RequestMapping(value = "/cleanBeautyDetails", method = RequestMethod.GET)
//    public JSONObject cleanBeautyDetails(String beautyIndentNumber, HttpServletRequest request) {
//        JSONObject result = new JSONObject();
//        TokenPojo currentLoginUser = getCurrentLoginUser(request);
//        if (currentLoginUser == null) {
//            result.put("code", 530);
//            result.put("msg", "用户未登录");
//            return result;
//        }
//        //根据订单编号查询到订单
//        EntityWrapper<CleanIndetEntity> wrapper = new EntityWrapper<>();
//        wrapper.eq("clean_indent_number", beautyIndentNumber);
//        //查询到订单
//        CleanIndetEntity cleanIndetEntity = cleanIndetService.selectOne(wrapper);
//        Date time = cleanIndetEntity.getCreateTime();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String dateString = formatter.format(time);
//        cleanIndetEntity.setTime(dateString);
//        //查询服务，查询商户
//        EntityWrapper<AppBeautyPriceDetailEntity> cleanPriceDetailWrapper = new EntityWrapper<>();
//        cleanPriceDetailWrapper.eq("user_b_id", cleanIndetEntity.getUserBId())
//                .eq("beauty_type", cleanIndetEntity.getCleanType())
//                .eq("car_type", cleanIndetEntity.getCarType());
//        //查询到服务
//        AppBeautyPriceDetailEntity beautyPriceDetailEntity = appBeautyPriceDetailService.selectOne(cleanPriceDetailWrapper);
//
//        CleanMerDto cleanMerDto = bUserService.findCleanDetails(cleanIndetEntity.getUserBId());
//
//        CleanIndentDetailsDto cleanIndentDetailsDto = new CleanIndentDetailsDto();
//        cleanIndentDetailsDto.setCleanIndetEntity(cleanIndetEntity);
//        cleanIndentDetailsDto.setCleanMerDto(cleanMerDto);
//        cleanIndentDetailsDto.setBeautyPriceDetailEntity(beautyPriceDetailEntity);
//        result.put("code", 200);
//        result.put("data", cleanIndentDetailsDto);
//        return result;
//    }


    /**
     * @param cleanIndentNumber
     * @return
     */
    @ApiOperation(value = "收货接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "cleanIndentNumber", value = "订单编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/receiveGoods", method = RequestMethod.GET)
    public JSONObject receiveGoods(String cleanIndentNumber, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userid = currentLoginUser.getAppUserEntity().getId();
        //token 判断登陆
        //收货方法
        //订单编号,查询到订单，然后查询到到手价和优惠后价格，然后查询该用户有无上级，根据比例给上级发钱
        //根据订单编号查询到订单
        EntityWrapper<CleanIndetEntity> indetWrapper = new EntityWrapper<>();
        indetWrapper.eq("clean_indent_number", cleanIndentNumber);
        CleanIndetEntity cleanIndetEntity = cleanIndetService.selectOne(indetWrapper);
        if (cleanIndetEntity == null) {
            result.put("code", 401);
            result.put("msg", "未查询到订单，请稍后再试");
            return result;
        }

        //洗车
        if (cleanIndetEntity.getResource().equals("1")){
            //查询免费，如果是免费的就直接结算
            if (cleanIndetEntity.getMerchantsPayNumber().equals("免费洗")) {
                cleanIndetEntity.setIndentState("3");
                cleanIndetService.updateById(cleanIndetEntity);

                result.put("code", 200);
                result.put("mesg", "收货成功");
                return result;
            }
            //拿到订单中的数据  原价和支付价格，，算出到手价格，修改订单状态，
            //支付价格
        }
        BigDecimal amount = cleanIndetEntity.getAmount();
        if (cleanIndetEntity.getContractFlag() != 1) {
            //算出到手价格   公司抽成比例 10%
            String s = stringRedisTemplate.opsForValue().get(RedisConstant.CAR_WASH_SERVICE_PLATFORM_RATIO);
            if (StringUtils.isEmpty(s)) {
                result.put("code", 402);
                result.put("msg", "缺少比例数据");
                return result;
            }
            //公司抽成
            BigDecimal compantAmount = amount.multiply(new BigDecimal(s));
            //到手价格
            BigDecimal merchantsAmount = amount.subtract(compantAmount);
            //给商户加钱 加锁，事务
            bUserService.updateBalance(cleanIndetEntity.getUserBId(), merchantsAmount, cleanIndentNumber);
        }

        //判断有无上级
        UserEntity userEntity = userService.selectById(userid);
        Integer parentId = userEntity.getParentId();
        //父id不为空
        if (parentId != 0) {
            //有父id
            //判断是否过期plus会员，然后给父id加钱
            AppUserPlusEntity appUserPlus = userService.addAmountToParent(parentId);
            Date invalidTimeEnd = appUserPlus.getInvalidTimeEnd();
            Date date = new Date();
            if (!invalidTimeEnd.before(date)) {
                //没过期
                //给plus会员发钱,根据比例修改plus会员得余额
                userService.updateBalance(parentId, cleanIndentNumber, amount);
            }
        }

        cleanIndetEntity.setIndentState("3");
        cleanIndetService.updateById(cleanIndetEntity);
        if (cleanIndetEntity.getResource().equals("1")){
            Message sendMsg = new Message("all", "jgts_xc", cleanIndentNumber.getBytes());
            //默认3秒超时
            SendResult sendResult = null;
            try {
                sendResult = mqProducer.send(sendMsg);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }else if (cleanIndetEntity.getResource().equals("2")){
            //美容
            Message sendMsg = new Message("all", "jgts_mr", cleanIndentNumber.getBytes());
            //默认3秒超时
            SendResult sendResult = null;
            try {
                sendResult = mqProducer.send(sendMsg);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }

        result.put("code", 200);
        result.put("mesg", "收货成功");
        return result;
    }

    @ApiOperation(value = "取消订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "cleanIndentNumber", value = "订单编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    public JSONObject cancelOrder(@RequestBody JSONObject in, HttpServletRequest request) {
        String cleanIndentNumber = in.getString("cleanIndentNumber");
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        if (StringUtils.isEmpty(cleanIndentNumber)) {
            result.put("code", 401);
            result.put("msg", "参数错误");
            return result;
        }
        CleanIndetEntity cleanIndetParamer = new CleanIndetEntity();
        cleanIndetParamer.setCleanIndentNumber(cleanIndentNumber);
        CleanIndetEntity cleanIndetEntity = cleanIndetMapper.selectOne(cleanIndetParamer);

        if (cleanIndetEntity == null) {
            result.put("code", 401);
            result.put("msg", "没有这个订单！");
            return result;
        }

        if (!cleanIndetEntity.getUserId().equals(String.valueOf(currentLoginUser.getAppUserEntity().getId()))) {
            result.put("code", 402);
            result.put("msg", "请操作自己的订单！");
            return result;
        }

        if (!cleanIndetEntity.getPayState().equals("1")) {
            result.put("code", 401);
            result.put("msg", "订单未支付！");
            return result;
        }

        if (cleanIndetEntity.getIndentState().equals("3")) {
            result.put("code", 401);
            result.put("msg", "已完成订单不能取消！");
            return result;
        }

        if (cleanIndetEntity.getMerchantsPayNumber().equals("免费洗")) {
            //免费订单
            cleanIndetEntity.setIndentState("4");
            cleanIndetService.updateById(cleanIndetEntity);
            result.put("code", 200);
            result.put("msg", "取消订单成功");
            return result;
        }

        try {
            cleanIndetService.cancelOrder(cleanIndentNumber, cleanIndetEntity);
        } catch (CusException e) {
            result.put("code", e.getCode());
            result.put("msg", e.getMessage());
            return result;
        }

        result.put("code", 200);
        result.put("msg", "取消订单成功");
        return result;
    }
}
