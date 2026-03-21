package com.cheji.b.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.b.constant.ConsEnum;
import com.cheji.b.constant.RedisConstant;
import com.cheji.b.modular.domain.AppUserBankEntity;
import com.cheji.b.modular.domain.AppWxBankEntity;
import com.cheji.b.modular.domain.AppWxCashOutRecordEntity;
import com.cheji.b.modular.dto.ChangeDetailsDto;
import com.cheji.b.modular.dto.ChangeListDto;
import com.cheji.b.modular.lock.RedisLock;
import com.cheji.b.modular.mapper.AppUserBankMapper;
import com.cheji.b.modular.mapper.AppWxBankMapper;
import com.cheji.b.modular.service.UserService;
import com.cheji.b.pojo.TokenPojo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.math.RoundingMode.CEILING;

@Api("用户相关")
@RequestMapping("/user")
@RestController
public class UserController extends BaseController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AppWxBankMapper appWxBankMapper;

    @Resource
    private UserService userService;

    @Resource
    private AppUserBankMapper appUserBankMapper;

    @Resource
    private RedisLock redisLock;


    @ApiOperation(value = "查找银行卡")
    @GetMapping("/getBankList")
    public JSONObject addBank() {
        JSONObject result = new JSONObject();
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.WX_BANK_LIST);
        if (StringUtils.isEmpty(s)) {
            List<AppWxBankEntity> appWxBankEntities = appWxBankMapper.selectList(null);
            s = JSONObject.toJSONString(appWxBankEntities);
            stringRedisTemplate.opsForValue().set(RedisConstant.WX_BANK_LIST, s, 60 * 60 * 24, TimeUnit.SECONDS);//保存一天
        }

        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", JSONObject.parseArray(s));
        return result;
    }

    @ApiOperation(value = "添加银行卡 401：参数错误" +
            "530：用户未登录" +
            "403:用户已有相同的卡号")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "code", value = "银行卡号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "bankId", value = "银行卡编码id", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "bankUserName", value = "银行卡登记的用户姓名", required = true, dataType = "int")
    })
    @PostMapping("/addBank")
    public JSONObject addBank(HttpServletRequest request, @RequestBody JSONObject in) {
        String code = in.getString("code");
        Integer bankId = in.getInteger("bankId");
        String bankUserName = in.getString("bankUserName");
        JSONObject result = new JSONObject();

        if (StringUtils.isEmpty(code) || code.length() < 15 || code.length() > 19) {
            result.put("code", 401);
            result.put("msg", "参数错误");
            return result;
        }

        if (bankId == 0) {
            result.put("code", 401);
            result.put("msg", "参数错误");
            return result;
        }

        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userId = currentLoginUser.getAppUserEntity().getId();

        AppUserBankEntity paramer = new AppUserBankEntity();
        paramer.setCode(code);
        paramer.setUserId(userId);
        AppUserBankEntity appUserBankEntity1 = appUserBankMapper.selectOne(paramer);
        if (appUserBankEntity1 != null) {
            result.put("code", 403);
            result.put("msg", "用户已有相同的卡号");
            return result;
        }

        AppUserBankEntity appUserBankEntity = new AppUserBankEntity();
        appUserBankEntity.setBankId(bankId);
        appUserBankEntity.setCode(code);
        appUserBankEntity.setUserId(userId);
        appUserBankEntity.setStatus(ConsEnum.USER_BANK_STATUS_OK.getCode());
        appUserBankEntity.setBankUserName(bankUserName);
        appUserBankEntity.setCreateTime(new Date());
        appUserBankMapper.insert(appUserBankEntity);
        result.put("code", 200);
        return result;
    }

    @ApiOperation(value = "增加提现记录" +
            " 401：参数错误" +
            "530：用户未登录" +
            "403:账户余额不够扣")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userBankId", value = "银行卡id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "amount", value = "提现金额(元)", required = true, dataType = "decimal")
    })
    @PostMapping("/addCashOut")
    public JSONObject addCashOut(HttpServletRequest request, @RequestBody JSONObject in) {
        String userBankId = in.getString("userBankId");
        BigDecimal amount = in.getBigDecimal("amount");

        JSONObject result = new JSONObject();
        if (StringUtils.isEmpty(userBankId) || amount == null) {
            result.put("code", 401);
            result.put("msg", "参数错误");
            return result;
        }
        if (amount.compareTo(new BigDecimal(2)) == -1) {
            result.put("code", 401);
            result.put("msg", "提现金额应该大于2元");
            return result;
        }
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        AppUserBankEntity appUserBankEntity = appUserBankMapper.selectById(userBankId);
        if (appUserBankEntity == null) {
            result.put("code", 401);
            result.put("msg", "银行卡id错误");
            return result;
        }
        Integer userId = appUserBankEntity.getUserId();
        if (!userId.equals(currentLoginUser.getAppUserEntity().getId())) {
            result.put("code", 401);
            result.put("msg", "银行卡id与当前用户不匹配");
            return result;
        }
        String lockKey = RedisConstant.ADD_CASH_OUT_LOCK + "_b_" + currentLoginUser.getAppUserEntity().getId();
        redisLock.lock(lockKey);//防止因为网络原因导致用户连点两次提现
        try {
            //计算手续费
            BigDecimal fee = calcAmount(amount);
            //计算最终向用户打的钱
            BigDecimal sendAmount = amount.subtract(fee);

            AppWxBankEntity appWxBankEntity = appWxBankMapper.selectById(appUserBankEntity.getBankId());

            AppWxCashOutRecordEntity appWxCashOutRecordEntity = new AppWxCashOutRecordEntity();
            appWxCashOutRecordEntity.setSendAmount(sendAmount);
            appWxCashOutRecordEntity.setAmount(amount);
            appWxCashOutRecordEntity.setFee(fee);
            appWxCashOutRecordEntity.setCreateTime(new Date());
            appWxCashOutRecordEntity.setStatus(ConsEnum.CASH_OUT_DEFAULT.getCode());
            appWxCashOutRecordEntity.setBankCode(appWxBankEntity.getBankCode());
            appWxCashOutRecordEntity.setBankNumber(appUserBankEntity.getCode());
            appWxCashOutRecordEntity.setUserBankName(appUserBankEntity.getBankUserName());
            appWxCashOutRecordEntity.setUserId(userId);
            appWxCashOutRecordEntity.setSource(ConsEnum.CASH_OUT_BUSINESS.getCode());
            appWxCashOutRecordEntity.setResult("DEFAULT");
            appWxCashOutRecordEntity.setResultInfo("处理中");

            String re = userService.addCashOut(amount, currentLoginUser.getAppUserEntity().getId(), appWxCashOutRecordEntity);
            if (StringUtils.isEmpty(re)) {
                result.put("code", 403);
                result.put("msg", "账户余额不够");
                return result;
            }
            result.put("code", 200);
            result.put("msg", "成功");
            result.put("data", re);
            return result;
        } finally {
            redisLock.unlock(lockKey);
        }
    }


    @ApiOperation(value = "银行卡列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "int")
    })
    @RequestMapping(value = "/bankCardList", method = RequestMethod.GET)
    public JSONObject bankCardList(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        //根据b端用户id查询到银行卡列表
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        List<AppUserBankEntity> bankCard = userService.findBankCard(userBId);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", bankCard);
        return result;
    }

    @ApiOperation(value = "删除银行卡")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "银行卡id", required = true, dataType = "int")
    })
    @RequestMapping(value = "/removeBankCard", method = RequestMethod.GET)
    public JSONObject removeBankCard(Integer id) {
        JSONObject result = new JSONObject();
        userService.removeBankCard(id);
        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }


    @ApiOperation(value = "零钱账单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String")
    })
    @RequestMapping(value = "/changeList",method = RequestMethod.GET)
    public JSONObject changeList (HttpServletRequest request,@RequestParam( required = false,defaultValue = "1")Integer pagesize,String date){
        JSONObject result = new JSONObject();
        //通过用户id查询到零钱账单的提现和充值信息
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser==null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        //System.out.println(new Date());
        Integer userBId = currentLoginUser.getAppUserEntity().getId();

        List<ChangeListDto> changeList = userService.findChangeList(userBId,pagesize,date);
        result.put("code", 200);
        result.put("msg", "成功!");
        result.put("data", changeList);
       // System.out.println(new Date());
        return result;
}



    @ApiOperation(value = "零钱详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "changeId",value = "零钱列表id",required = true,dataType = "String")
    })
    @RequestMapping(value = "/changeDetails",method = RequestMethod.GET)
    public JSONObject changeDitails(String changeId){
        JSONObject result = new JSONObject();
        //查询到零钱详情
        ChangeDetailsDto changeDto = userService.findChangeDetails(changeId);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", changeDto);
        return result;
    }





    /**
     * 计算手续费
     */
    public BigDecimal calcAmount(BigDecimal amount) {

        //手续费
        BigDecimal fee = amount.divide(new BigDecimal(1000), 3, RoundingMode.HALF_UP);

        //不满一元的 往上+1
        fee = fee.setScale(0, CEILING);

        if (fee.compareTo(new BigDecimal(25)) == 1) {//如果手续费大于25元按25元算
            fee = new BigDecimal(25);
        }
        return fee;
    }
}
