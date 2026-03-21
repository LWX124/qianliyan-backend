package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.constant.ConsEnum;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.domain.AppUserBankEntity;
import com.cheji.web.modular.domain.AppWxBankEntity;
import com.cheji.web.modular.domain.AppWxCashOutRecordEntity;
import com.cheji.web.modular.lock.RedisLock;
import com.cheji.web.modular.mapper.AppUserBankMapper;
import com.cheji.web.modular.mapper.AppWxBankMapper;
import com.cheji.web.modular.mapper.AppWxCashOutRecordEntityMapper;
import com.cheji.web.modular.service.UserService;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * 用户
 */
@Api("用户相关2")
@RestController
@RequestMapping("/user")
public class User2Controller extends BaseController {

    private Logger logger = LoggerFactory.getLogger(User2Controller.class);


    @Resource
    private AppWxBankMapper appWxBankMapper;

    @Resource
    private AppUserBankMapper appUserBankMapper;

    @Resource
    private RedisLock redisLock;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserService userService;

    @Resource
    private AppWxCashOutRecordEntityMapper appWxCashOutRecordEntityMapper;

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

        if (StringUtils.isEmpty(bankUserName)) {
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
            appUserBankEntity1.setStatus(1);
            appUserBankMapper.updateById(appUserBankEntity1);
            result.put("code", 200);
            result.put("msg", "开通成功");
            return result;
        }

        AppUserBankEntity appUserBankEntity = new AppUserBankEntity();
        appUserBankEntity.setBankId(bankId);
        appUserBankEntity.setCode(code);
        appUserBankEntity.setUserId(userId);
        appUserBankEntity.setStatus(ConsEnum.USER_BANK_STATUS_OK.getCode());
        appUserBankEntity.setBankUserName(bankUserName.trim());
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
        Integer id = currentLoginUser.getAppUserEntity().getId();

        //查询一分钟内不能有提现
        List<Integer> cashOutlist = appWxCashOutRecordEntityMapper.findAgoWxCashOut(id);
        if (!cashOutlist.isEmpty()){
            result.put("code", 401);
            result.put("msg", "一分钟内请不要再次提现");
            return result;
        }

        AppUserBankEntity appUserBankEntity = appUserBankMapper.selectById(userBankId);
        if (appUserBankEntity == null) {
            result.put("code", 401);
            result.put("msg", "银行卡id错误");
            return result;
        }
        Integer userId = appUserBankEntity.getUserId();
        if (!userId.equals(id)) {
            logger.error("### 银行卡id与当前用户不匹配 ### userId={};currentLoginUser={};currentLoginUser.getAppUserEntity().getId()={};in={}", userId, currentLoginUser, currentLoginUser.getAppUserEntity().getId(), in);
            result.put("code", 401);
            result.put("msg", "银行卡id与当前用户不匹配");
            return result;
        }
        String lockKey = RedisConstant.ADD_CASH_OUT_LOCK + currentLoginUser.getAppUserEntity().getId();
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
            appWxCashOutRecordEntity.setSources(ConsEnum.CASH_OUT_CLIENT.getCode());
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
