package com.cheji.b.modular.controller;


import com.alibaba.fastjson.JSONObject;
import com.cheji.b.modular.domain.AppSendOutSheetEntity;
import com.cheji.b.modular.domain.AppSendUrlEntity;
import com.cheji.b.modular.dto.SendSheetDto;
import com.cheji.b.modular.service.AppSendOutSheetService;
import com.cheji.b.modular.service.AppSendUrlService;
import com.cheji.b.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * web派单记录表 前端控制器
 * </p>
 *
 * @author Ashes
 * @since 2020-10-14
 */
@RestController
@RequestMapping("/appSendOutSheet")
public class AppSendOutSheetController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(AppSendOutSheetController.class);

    @Resource
    private AppSendOutSheetService appSendOutSheetService;

    @Resource
    private AppSendUrlService appSendUrlService;


    @ApiOperation(value = "派单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = false, dataType = "int")
    })
    @RequestMapping(value = "/adjustersList", method = RequestMethod.GET)
    public JSONObject adjustersList(HttpServletRequest request, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        //获取到用户id
        Integer id = currentLoginUser.getAppUserEntity().getId();
        //查询到该用户所有的派单记录
        List<AppSendOutSheetEntity> sendsoutList = appSendOutSheetService.findSendOutList(id, pagesize);

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", sendsoutList);
        return result;
    }


    @ApiOperation(value = "派单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "派单id", required = false, dataType = "int")
    })
    @RequestMapping(value = "/adjustersDetails", method = RequestMethod.GET)
    public JSONObject adjustersDetails(HttpServletRequest request, String id) {
        JSONObject result = new JSONObject();
        //根据id查询到派单
        //
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        //获取到用户id
        Integer userBid = currentLoginUser.getAppUserEntity().getId();

        AppSendOutSheetEntity appSendOutSheet = appSendOutSheetService.selectById(id);
        if (appSendOutSheet == null) {
            result.put("code", 407);
            result.put("msg", "未查询到订单");
            return result;
        }
//        if (appSendOutSheet.getUserBId() != userBid) {
//            logger.info("### 订单账号  userBid ### userBid={}", appSendOutSheet.getUserBId());
//            logger.info("### token  Bid ### Bid={}", userBid);
//            result.put("code", 407);
//            result.put("msg", "请操作自己的订单");
//            return result;
//        }

        List<String> urls = appSendOutSheetService.findUrlbyid(appSendOutSheet.getId());
        appSendOutSheet.setUrl(urls);

        //查询到图片
//        String s = appSendUrlService.findimgById(appSendOutSheet.getId());
//        appSendOutSheet.setSrc(s);
        List<String> imgList =  appSendUrlService.findallsendImg(appSendOutSheet.getId());
        appSendOutSheet.setImgList(imgList);
        //转换时间格式
        Date createTime = appSendOutSheet.getCreateTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(createTime);
        appSendOutSheet.setTime(dateString);

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", appSendOutSheet);
        return result;

    }


    //修改数据
    @ApiOperation(value = "修改状态")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "派单id", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "falconTrajectory", value = "轨迹id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "tid", value = "设备编号", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "urlList", value = "签到图片", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "lat", value = "签到纬度", required = false, dataType = "BigDecimal"),
            @ApiImplicitParam(paramType = "query", name = "lng", value = "签到经度", required = false, dataType = "BigDecimal"),
            @ApiImplicitParam(paramType = "query", name = "nowState", value = "现场状态", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "backText", value = "反馈文字", required = false, dataType = "String")
    })
    @RequestMapping(value = "/updateAdjusters", method = RequestMethod.POST)
    public JSONObject updateAdjusters(HttpServletRequest request, @RequestBody SendSheetDto sendSheetDto) {
        JSONObject result = new JSONObject();
        //根据id查询到派单
        //
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        //获取到用户id
        Integer userBid = currentLoginUser.getAppUserEntity().getId();
        AppSendOutSheetEntity appSendOutSheet = appSendOutSheetService.selectById(sendSheetDto.getId());
        if (appSendOutSheet == null) {
            result.put("code", 407);
            result.put("msg", "未查询到订单");
            return result;
        }
//        if (appSendOutSheet.getUserBId() != userBid) {
//            result.put("code", 407);
//            result.put("msg", "请操作自己的订单");
//            return result;
//        }
        if (StringUtils.isNotEmpty(sendSheetDto.getFalconTrajectory())) {
            appSendOutSheet.setFalconTrajectory(sendSheetDto.getFalconTrajectory());
            appSendOutSheet.setTid(sendSheetDto.getTid());
            appSendOutSheet.setTcTime(new Date());
            appSendOutSheet.setState(2);
        }
        if (sendSheetDto.getUrlList()!=null && sendSheetDto.getLat() != null && sendSheetDto.getLng() != null) {

            //删除以前的图片
            List<AppSendUrlEntity> sendUrlList =  appSendUrlService.findCheckImgList(appSendOutSheet.getId());
            for (AppSendUrlEntity appSendUrlEntity : sendUrlList) {
                appSendUrlService.deleteById(appSendUrlEntity.getId());
            }

            //添加图片
            List<String> urlList = sendSheetDto.getUrlList();
            for (String s : urlList) {
                //遍历保存
                AppSendUrlEntity appSendUrl = new AppSendUrlEntity();
                appSendUrl.setType(2);
                appSendUrl.setUrl(s);
                appSendUrl.setSendId(sendSheetDto.getId());
                appSendUrl.setCreateTime(new Date());
                appSendUrl.setUpdateTime(new Date());
                appSendUrlService.insert(appSendUrl);
            }
            appSendOutSheet.setCheckLat(sendSheetDto.getLat());
            appSendOutSheet.setCheckLng(sendSheetDto.getLng());
            appSendOutSheet.setCheckTime(new Date());
            appSendOutSheet.setState(3);
        }
        if (sendSheetDto.getNowState()!= null){
            appSendOutSheet.setNowstate(sendSheetDto.getNowState());
            appSendOutSheet.setState(4);
        }
        if (StringUtils.isNotEmpty(sendSheetDto.getBackText())){
            appSendOutSheet.setBackText(sendSheetDto.getBackText());
            appSendOutSheet.setState(4);
        }
        appSendOutSheetService.updateById(appSendOutSheet);

        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }


}
