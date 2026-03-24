package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.cwork.*;
import com.cheji.web.modular.domain.AppUserPlusEntity;
import com.cheji.web.modular.domain.UserEntity;
import com.cheji.web.modular.mapper.AppUserAccountRecordMapper;
import com.cheji.web.modular.service.AccidentRecordService;
import com.cheji.web.modular.service.AppUserPlusService;
import com.cheji.web.modular.service.UserService;
import com.cheji.web.pojo.TokenPojo;
import com.cheji.web.util.CjStringUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @Resource
    private AppUserPlusService AppUserPlusService;

    @Resource
    private AccidentRecordService accidentRecordService;

    @Resource
    private AppUserAccountRecordMapper appUserAccountRecordMapper;


    @ApiOperation(value = "修改个人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "用户id", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "avatar", value = "头像", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "password", value = "旧密码", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "newPassword", value = "新密码", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "昵称", required = false, dataType = "String"),
    })
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JSONObject update(@RequestBody UserEntity userEntity, HttpServletRequest request) {
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        Integer id = currentLoginUser.getAppUserEntity().getId();
        userEntity.setId(Long.valueOf(id));
        return userService.update(userEntity);
    }


    //我的钱包
    @ApiOperation(value = "我的钱包")
    @RequestMapping(value = "/myWallet", method = RequestMethod.GET)
    public JSONObject myWallet(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        //根据用户id查询用户钱包信息
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        UserWallet walletById = userService.findWalletById(String.valueOf(id));
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", walletById);
        return result;
    }

    //我的团队，根据用户id查询到团队列表
    @ApiOperation(value = "根据用户id查询到团队列表，我的团队")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "用户id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/myTeam", method = RequestMethod.GET)
    public JSONObject myTeam(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        MyTeam teamListById = userService.findTeamListById(String.valueOf(id));
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", teamListById);
        return result;
    }

    //查询个人中心
    @ApiOperation(value = "个人中心")
    @RequestMapping(value = "/personalCenter", method = RequestMethod.GET)
    public JSONObject personalCenter(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        Personal personalByUserId = userService.findPersonalByUserId(String.valueOf(id));
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", personalByUserId);
        return result;
    }

    @ApiOperation(value = "我的银行卡列表")
    @RequestMapping(value = "/userBankList", method = RequestMethod.GET)
    public JSONObject bankList(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();

        List<Map> list = userService.findBankList(id);
        for (Map map : list) {
            Object bank_user_name = map.get("bank_user_name");
            if (bank_user_name == null) {
                continue;
            }
            String s = String.valueOf(bank_user_name);
            String starString2 = CjStringUtils.getStarString2(s, 1, 0);
            map.put("bank_user_name", starString2);
        }
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", list);
        return result;
    }


    //零钱账单列表
    @ApiOperation(value = "我的零钱账单" +
            ",日期格式“YYYY-MM”")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "date", value = "日期", required = false, dataType = "String")
    })
    @RequestMapping(value = "/ChangeList", method = RequestMethod.GET)
    public JSONObject ChangeList(HttpServletRequest request, @RequestParam(required = false, defaultValue = "1") Integer pagesize, String date) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        //根据用户id查询到零钱列表
        Integer id = currentLoginUser.getAppUserEntity().getId();
        //   Log log = LogFactory.getLog(getClass());
        // log.info("id============"+id);
        List<AmountList> amountLists = userService.findChangeList(id, pagesize, date);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", amountLists);
        return result;
    }


    //账单提现详细接口
    @ApiOperation(value = "账单提现详细接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "partnerTradeNo", value = "订单号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/withdrawalDetails", method = RequestMethod.GET)
    public JSONObject withdrawalDetails(String partnerTradeNo) {
        JSONObject result = new JSONObject();
        WithdDetails withdDetails = userService.findWithdrawalDetails(partnerTradeNo);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", withdDetails);
        return result;
    }

    //plus会员接口
    @ApiOperation(value = "plus会员")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String")
    })
    @RequestMapping(value = "/plusUser", method = RequestMethod.GET)
    public JSONObject plusUser(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        //根据id查询plus会员表中id判断时间是否过期
        Integer id = currentLoginUser.getAppUserEntity().getId();
        //判断有无数据和是否过期
        AppUserPlusEntity userPlusEntity = AppUserPlusService.findPlusUserByid(String.valueOf(id));
        if (userPlusEntity == null) {
            result.put("code", 200);
            result.put("msg", "未开通plus会员");
            return result;
        }
        Date invalidTimeEnd = userPlusEntity.getInvalidTimeEnd();
        Date date = new Date();
        if (invalidTimeEnd.before(date)) {
            result.put("code", 200);
            result.put("msg", "plus会员已过期");
            return result;
        }
        //有数据就是plus会员
        UserPlus userPlus = AppUserPlusService.findMes(userPlusEntity);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", userPlus);
        return result;
    }

    @ApiOperation(value = "推销界面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型，1：事故。2：推广", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/sellList", method = RequestMethod.GET)
    public JSONObject sellList(HttpServletRequest request, Integer type, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        //根据用户id查询到零钱列表
        Integer id = currentLoginUser.getAppUserEntity().getId();
        //查询到推广列表
        if (type == null) {
            result.put("code", 407);
            result.put("msg", "type为空");
            return result;
        } else if (type == 1) {
            //事故奖励列表
            List<AccidentReward> accidByUserid = accidentRecordService.findAccYourself(id, pagesize);
            result.put("data", accidByUserid);
            result.put("msg", "成功");
            result.put("code", 200);
            return result;

        } else if (type == 2) {
            //推广奖励
            List<PromoteList> proList = userService.findPromote(id, pagesize);
            //总人数
            Integer allPeopNum = userService.findProple(id);
            //总金额
            BigDecimal allPushReward = appUserAccountRecordMapper.findAllPushReward(id);
            //通过人数
            BigDecimal divide = allPushReward.divide(new BigDecimal("10"), 0, BigDecimal.ROUND_HALF_UP);
            JSONObject object = new JSONObject();
            object.put("allPeopNum", allPeopNum);
            object.put("passNumber", divide);
            object.put("allPushReward", allPushReward);
            object.put("proList", proList);

            result.put("data", object);
            result.put("msg", "成功");
            result.put("code", 200);
            return result;
        } else {
            result.put("code", 408);
            result.put("msg", "检查type参数");
            return result;
        }
    }


    @ApiOperation(value = "开通plus会员")
    @RequestMapping(value = "/openPlusUser", method = RequestMethod.GET)
    public JSONObject openPlusUser() {
        JSONObject result = new JSONObject();
        UserPlus userPlus = AppUserPlusService.findOtherMes();
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", userPlus);
        return result;
    }

    @ApiOperation(value = "删除银行卡")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "银行卡id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/removeCard", method = RequestMethod.GET)
    public JSONObject removeCard(Integer id) {
        JSONObject result = new JSONObject();
        userService.removeCard(id);
        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }


    @ApiOperation(value = "更新")
    @RequestMapping(value = "/updateC", method = RequestMethod.GET)
    public JSONObject updateC() {
        JSONObject result = new JSONObject();
        UpdateDto updateDto = new UpdateDto();
        updateDto.setDescribe("检测到新版本,请直接点击更新按钮更新。卸载可能会丢失数据噢！");
        updateDto.setUrl("https://apps.apple.com/cn/app/%E8%BD%A6%E5%B7%B1%E8%BD%A6%E6%9C%8D/id1485670998");
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", updateDto);
        return result;
    }


    @ApiOperation(value = "粉丝提报视频")
    @RequestMapping(value = "/fansSubmit", method = RequestMethod.GET)
    public JSONObject fansSubmit(HttpServletRequest request, @RequestParam(required = false, defaultValue = "1") Integer pagesize, String beginTime, String endTime) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        //其中之一为空
        if (StringUtils.isEmpty(beginTime) || StringUtils.isEmpty(endTime)) {
            result.put("code", 402);
            result.put("msg", "有一个时间为空");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        //根据用户id查询到粉丝提报的视频
        List<String> users = userService.findFans(id);
        if (users == null) {
            result.put("code", 402);
            result.put("msg", "还没有粉丝哦，请继续努力");
            return result;
        }
        List<FansVideoDto> recordVideo = userService.findVideo(users, pagesize, beginTime, endTime);
        if (recordVideo == null) {
            result.put("code", 402);
            result.put("msg", "粉丝还没有提报的视频哦");
            return result;
        }

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", recordVideo);
        return result;
    }

    @ApiOperation(value = "验证token")
    @RequestMapping(value = "/checkToken", method = RequestMethod.GET)
    public JSONObject checkToken(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);

        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        Integer id = currentLoginUser.getAppUserEntity().getId();
        String name = currentLoginUser.getAppUserEntity().getName();

        JSONObject data = new JSONObject();
        data.put("userId", id);
        data.put("name", name);

        result.put("code", 200);
        result.put("msg", "token有效");
        result.put("data", data);
        return result;
    }

}
