package com.cheji.b.modular.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.cheji.b.modular.domain.*;
import com.cheji.b.modular.dto.*;
import com.cheji.b.modular.mapper.AppClaimTeacherMapper;
import com.cheji.b.modular.mapper.CarBrandMapper;
import com.cheji.b.modular.mapper.CdImgMapper;
import com.cheji.b.modular.service.*;
import com.cheji.b.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 车店图片表
 * 前端控制器
 * </p>
 *
 * @author Ashes
 * @since 2022-01-12
 */
@RestController
@RequestMapping("/cdImg")
public class CdImgController extends BaseController {

    @Resource
    private AppClaimTeacherService appClaimTeacherService;

    @Resource
    private PushBillService pushBillService;

    @Resource
    private CdImgService cdImgService;

    @Resource
    private IndentService indentService;

    @Resource
    private UserService userService;

    @Resource
    private AccidentRecordService accidentRecordService;

    @Resource
    private BizAccidentService bizAccidentService;

    @Resource
    private AppSendOutSheetService appSendOutSheetService;

    @Resource
    private AppSendUrlService appSendUrlService;
    
    @Resource
    private AppMessageUrlService appMessageUrlService;

    @Resource
    private AppMessageCarService appMessageCarService;

    @Resource
    private CarBrandMapper carBrandMapper;

    @Resource
    private AppUpMerchantsService appUpMerchantsService;


    //事故线索
    //查询理赔老师信息
    @ApiOperation(value = "事故线索")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "年份", value = "year", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "月份", value = "month", required = true, dataType = "String")
    })
    @RequestMapping(value = "/ClaimTeaMessage", method = RequestMethod.GET)
    public JSONObject ClaimTeaMessage(HttpServletRequest request, String year, String month) {
        //查询到服务顾问各种信息
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

//        String id = currentLoginUser.getAppUserEntity().getcId();
        String id = currentLoginUser.getAppUserEntity().getId().toString();

        if (StringUtils.isEmpty(id)) {
            JSONObject list = appClaimTeacherService.noHaveId();
            result.put("code", 200);
            result.put("data", list);
            return result;
        }

        if (StringUtils.isEmpty(year) || StringUtils.isEmpty(month)) {
            result.put("code", 407);
            result.put("msg", "年月数据为空");
            return result;
        }

        if (month.length() == 1) {
            month = "0" + month;
        }

        //根据id查询到对应扣费数据
        JSONObject list = appClaimTeacherService.findClaimTeaMessage(Integer.parseInt(id), year, month);

        result.put("code", 200);
        result.put("data", list);
        return result;

    }


    //查询商户接口
    @ApiOperation(value = "事故信息账单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "年份", value = "year", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "月份", value = "month", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "页数", value = "pagesize", required = true, dataType = "String")
    })
    @RequestMapping(value = "/findAccidMessBill", method = RequestMethod.GET)
    public JSONObject findAccidMessBill(HttpServletRequest request, String year, String month, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {

        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        String id = currentLoginUser.getAppUserEntity().getcId();

        if (StringUtils.isEmpty(id)) {
            result.put("code", 407);
            result.put("msg", "用户未绑定c端账号，联系管理员");
            return result;
        }
        //根据id查询到对应扣费数
        JSONObject list = appClaimTeacherService.findAccidMessBill(Integer.parseInt(id), pagesize, year, month);

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", list);
        return result;
    }


    //查询C端。模糊查询字段
    @ApiOperation(value = "c端搜索信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "text", value = "搜索文字", required = true, dataType = "String")
    })
    @RequestMapping(value = "/searchCMessageList", method = RequestMethod.GET)
    public JSONObject searchCMessageList(HttpServletRequest request, String text, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        //判断登陆
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        String id = currentLoginUser.getAppUserEntity().getcId();
        if (StringUtils.isEmpty(id)) {
            result.put("code", 200);
            result.put("message", "未查询到b端账号");
            result.put("data", "");
            return result;
        }
        //根据b端id查询到事故
        List<PushBillEntity> billList = pushBillService.findBillListBySreach(Integer.valueOf(id), pagesize, text);
        //根据查询出来的数据修改

        //地址，时间，图片，状态
        JSONArray objects = new JSONArray();
        for (PushBillEntity pushBillEntity : billList) {
            JSONObject object = new JSONObject();
            object.put("messageId", pushBillEntity.getId());
            object.put("type", pushBillEntity.getType());//1 bp  2 sos
            String address = pushBillEntity.getAddress();
            String substring = address.substring(0, 9);
            object.put("address", substring + "...");     //地址只展示9位 。。。
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = sdf.format(pushBillEntity.getCreateTime());
            object.put("time", format);
            object.put("url", pushBillEntity.getThumbnailUrl());
            //新增视频
            object.put("videoUrl", pushBillEntity.getVideoUrl());
            object.put("trackState", pushBillEntity.getTrackState());
            objects.add(object);
        }

        //查询两张表格中的新任务，再次跟踪，同意到店，拒绝到店
        TrackStateNumber stateNumber = pushBillService.selectTrackStateNumber(id);
        JSONObject object = new JSONObject();
        object.put("List", objects);
        object.put("tarckNumber", stateNumber);
        result.put("code", 200);
        result.put("data", object);
        return result;
    }


    //查询到web 信息派单和地图派单
    @ApiOperation(value = "c端查询信息推送的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "state", value = "状态1,2,3,4,5,6,7,8", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = false, dataType = "int")
    })
    @RequestMapping(value = "/cMessageList2", method = RequestMethod.GET)
    public JSONObject cMessageList2(HttpServletRequest request, @RequestParam(required = false, defaultValue = "1") Integer state, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        //判断登陆
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        String id = currentLoginUser.getAppUserEntity().getcId();
        if (StringUtils.isEmpty(id)) {
            JSONObject object = new JSONObject();
            object.put("List", new JSONArray());
            TrackStateNumber trackStateNumber = new TrackStateNumber();
            trackStateNumber.setAgree(0);
            trackStateNumber.setComeScene(0);
            trackStateNumber.setNewTask(0);
            trackStateNumber.setRefuse(0);
            object.put("tarckNumber", trackStateNumber);
            result.put("code", 200);
            result.put("data", object);
            return result;
        }

        return appClaimTeacherService.MessageList(Integer.parseInt(id), state, pagesize);

    }

    //显示订单详情
    @ApiOperation(value = "c端查询信息详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "messageId", value = "信息id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型", required = true, dataType = "int")
    })
    @RequestMapping(value = "/cMessageDetails2", method = RequestMethod.GET)
    public JSONObject cMessageDetails2(HttpServletRequest request, String messageId, Integer type) {
        //查询信息详情
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        String id = currentLoginUser.getAppUserEntity().getcId();


        if (StringUtils.isEmpty(messageId)) {
            result.put("code", 407);
            result.put("message", "信息id为空");
            return result;
        }

        if (type == null) {
            result.put("code", 407);
            result.put("message", "类型为空");
            return result;
        }

        return cdImgService.findMessageDetails(result, messageId, type, id);

    }

    //查询到web 信息派单和地图派单
    @ApiOperation(value = "c端查询信息推送的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "state", value = "状态1,2,3,4,5,6,7,8", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = false, dataType = "int")
    })
    @RequestMapping(value = "/cMessageList", method = RequestMethod.GET)
    public JSONObject cMessageList(HttpServletRequest request, @RequestParam(required = false, defaultValue = "1") Integer state, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        //判断登陆
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();

        AppUserEntity userEntity = userService.selectById(id);
//        UserEntity userEntity = userService.selectById(id);
        //String username = userEntity.getUsername();
        //根据c端电话号码查询到b端id
        // String Bid = bUserService.selectByUsername(username);
        //        if (StringUtils.isEmpty(Bid)) {
        //            result.put("code", 200);
        //            result.put("message", "未查询到b端账号");
        //            result.put("data", "");
        //            return result;
        //        }
        //根据b端id查询到事故
        List<PushBillEntity> billList = pushBillService.findBillList(id, pagesize, state);
        //根据查询出来的数据修改

        //地址，时间，图片，状态
        JSONArray objects = new JSONArray();
        for (PushBillEntity pushBillEntity : billList) {
            JSONObject object = new JSONObject();
            object.put("messageId", pushBillEntity.getId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            object.put("trackState", pushBillEntity.getTrackState());
            String format = sdf.format(pushBillEntity.getCreateTime());
            object.put("time", format);
            object.put("type", pushBillEntity.getType());//1 pb  2 sos
            String address = "...";
            //type1，根据pb类型操作
            if (pushBillEntity.getType().equals("1")) {
                //是小程序或者app
                //查询具体的信息
                Integer source = pushBillEntity.getSource();
                //1,app,2.小程序
                if (source == 1) {
                  //  AccidentRecordEntity accidentRecordEntity = accidentRecordService.selectById(pushBillEntity.getAccid());
                    AccidentRecord accidentRecordEntity = accidentRecordService.selectById(pushBillEntity.getAccid());
                    address = accidentRecordEntity.getAddress();
                    object.put("url", accidentRecordEntity.getThumbnailUrl());
                    object.put("videoUrl", accidentRecordEntity.getVideo());
                    if (accidentRecordEntity.getVideo() == null) {
                        object.put("url", accidentRecordEntity.getImgUrl());
                    }
                    pushBillEntity.setLat(new BigDecimal(String.valueOf(accidentRecordEntity.getLat())));
                    pushBillEntity.setLng(new BigDecimal(String.valueOf(accidentRecordEntity.getLng())));
                } else {
//                    BizAccidentEntity bizAccidentEntity = bizAccidentService.findById(pushBillEntity.getAccid());
                    BizAccident bizAccidentEntity = bizAccidentService.findById(pushBillEntity.getAccid());
                    address = bizAccidentEntity.getAddress();
                    object.put("url", bizAccidentEntity.getThumbnailUrl());
                    object.put("videoUrl", bizAccidentEntity.getVideo());
                    pushBillEntity.setLat(bizAccidentEntity.getLat());
                    pushBillEntity.setLng(bizAccidentEntity.getLng());
                }
                BigDecimal deduction = pushBillEntity.getDeduction();
//                if (new BigDecimal("0").compareTo(deduction) > -1) {
//                    //a >= b  0大于价格
//                    object.put("deduction", new BigDecimal("10"));
//                } else {
//                object.put("deduction", deduction);
//                }
                if(deduction != null){
                    object.put("deduction", deduction);
                }
                PushBillEntity pushBillEntity1 = pushBillService.selectById(pushBillEntity.getId());
                object.put("payState", pushBillEntity1.getPayState());
                object.put("state", pushBillEntity1.getState());
                object.put("reward", pushBillEntity1.getReward());
            } else {
                address = pushBillEntity.getAddress();
                Long id1 = pushBillEntity.getId();
                AppSendOutSheetEntity appSendOutSheetEntity = appSendOutSheetService.selectById(id1);
                pushBillEntity.setLat(appSendOutSheetEntity.getLat());
                pushBillEntity.setLng(appSendOutSheetEntity.getLng());
                if (appSendOutSheetEntity.getPayAmount() == null) {
//                    object.put("deduction", new BigDecimal("10"));
                    object.put("deduction", new BigDecimal("0"));
                } else if (new BigDecimal("0").compareTo(appSendOutSheetEntity.getPayAmount()) > -1) {
//                    object.put("deduction", new BigDecimal("10"));
                    object.put("deduction", new BigDecimal("0"));
                } else {
                    object.put("deduction", appSendOutSheetEntity.getPayAmount());
                }
                object.put("payState", appSendOutSheetEntity.getPayState());

                //查询图片
                EntityWrapper<AppSendUrlEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("send_id", id1)
                        .eq("`index`", 1)
                        .eq("type", 1);
                AppSendUrlEntity appSendUrlEntity = appSendUrlService.selectOne(wrapper);
                if (appSendUrlEntity == null) {
                    object.put("url", pushBillEntity.getThumbnailUrl());
                } else {
                    object.put("url", appSendUrlEntity.getUrl());
                }
                //新增视频
                object.put("videoUrl", pushBillEntity.getVideoUrl());
                object.put("state", appSendOutSheetEntity.getState());
                object.put("reward", "");
            }
            int length = address.length();
            if (length > 6) {
                String substring = address.substring(0, 6);
                object.put("address", substring + "...");     //地址只展示6位 。。。
            } else {
                object.put("address", address + "...");     //地址只展示6位
            }
            //查询距离
            //查询到新表中数据
            //            EntityWrapper<AppClaimTeacherEntity> wrapper = new EntityWrapper<>();
            //            wrapper.eq("user_id", id);
            //            AppClaimTeacherEntity appClaimTeacher = appClaimTeacherService.selectOne(wrapper);
            //            String currentPosition = appClaimTeacher.getCurrentPosition();
            //            if (StringUtils.isEmpty(currentPosition)) {
            //                object.put("distance", "定位失败");
            //            } else {
            //                JSONObject in = JSONObject.parseObject(currentPosition);
            //                BigDecimal Blng = in.getBigDecimal("lng");
            //                BigDecimal Blat = in.getBigDecimal("lat");
            //                //查询距离
            //                MapNavUtil mapNavUtil = new MapNavUtil(pushBillEntity.getLng() + "," + pushBillEntity.getLat(), Blng + "," + Blat, key, 1, "JSON");
            //                System.out.println(mapNavUtil.getResults().toString());
            //                Long results = mapNavUtil.getResults();
            //                BigDecimal distance3 = new BigDecimal(results);
            //                distance3 = distance3.divide(new BigDecimal("1000"), 2, BigDecimal.ROUND_HALF_UP);
            //                object.put("distance", distance3 + "km");
            //            }
            Integer accid = pushBillEntity.getAccid();
            if(accid != null){
                ListDetailsDto accidentDetails = accidentRecordService.findAccidentDetail(accid);
                object.put("lng", accidentDetails.getLng());
                object.put("lat", accidentDetails.getLat());
            }
            objects.add(object);
        }

        //查询两张表格中的新任务，再次跟踪，同意到店，拒绝到店
        TrackStateNumber stateNumber = pushBillService.selectTrackStateNumber(id.toString());
        Integer isLocation = userEntity.getIsLocation();
        if (isLocation == null) {
            isLocation = 0;
        }
        stateNumber.setIsLocation(isLocation);
        JSONObject object = new JSONObject();
        object.put("List", objects);
        object.put("tarckNumber", stateNumber);
        result.put("code", 200);
        result.put("data", object);
        return result;

    }

    //显示订单详情
    @ApiOperation(value = "c端查询信息详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "messageId", value = "信息id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型", required = true, dataType = "int")
    })
    @RequestMapping(value = "/cMessageDetails", method = RequestMethod.GET)
    public JSONObject cMessageDetails(HttpServletRequest request, String messageId, Integer type) {
        //查询信息详情
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        if (org.apache.commons.lang3.StringUtils.isEmpty(messageId)) {
            result.put("code", 407);
            result.put("message", "信息id为空");
            return result;
        }

        if (type == null) {
            result.put("code", 407);
            result.put("message", "类型为空");
            return result;
        }

        JSONObject messageDetails = new JSONObject();
        String messageCarId;
        //查询到详情
        //创建时间，地点，经纬度，备注，图片或者视频，理赔老师，签到时间，现场照片
        //1 pb  2 sos
        PushBillEntity pushBillEntity = new PushBillEntity();
        if (type == 1) {
            pushBillEntity = pushBillService.selectById(messageId);
            if (pushBillEntity == null) {
                result.put("code", 407);
                result.put("message", "未查询到信息");
                return result;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            messageDetails.put("accidentTime", sdf.format(pushBillEntity.getCreateTime()));

            if (pushBillEntity.getCheckTime() != null) {
                messageDetails.put("checkTime", sdf.format(pushBillEntity.getCheckTime()));
                messageDetails.put("isCheck", "1");
            } else {
                messageDetails.put("checkTime", pushBillEntity.getCheckTime());
                messageDetails.put("isCheck", "0");
            }
            messageDetails.put("checkAddress", pushBillEntity.getCheckAddress());
            messageDetails.put("checkState", pushBillEntity.getCheckState());

            messageDetails.put("vioce", pushBillEntity.getVoice());
            messageDetails.put("accidentType", pushBillEntity.getAccidentType());

            List<String> appMessageUrlEntityList = appMessageUrlService.selectScenePhotos(pushBillEntity.getId(), 1);

            messageDetails.put("scenePhotos", appMessageUrlEntityList);

            messageDetails.put("managerRemark", pushBillEntity.getManagerRemark());

            List<CheckImgList> imgList = pushBillService.selectSceneImg(pushBillEntity.getId());
            messageDetails.put("sceneImg", imgList);
            messageCarId = pushBillEntity.getMessageCarId();

            BigDecimal deduction = pushBillEntity.getDeduction();
            if (deduction == null || new BigDecimal("0").compareTo(deduction) > -1) {
//                messageDetails.put("deduction", new BigDecimal("10"));
                messageDetails.put("deduction", new BigDecimal("0"));
            } else {
                messageDetails.put("deduction", deduction);
            }

            if (pushBillEntity.getSource() == 1) {
                //app
//                AccidentRecordEntity accidentRecordEntity = accidentRecordService.selectById(pushBillEntity.getAccid());
                AccidentRecord accidentRecordEntity = accidentRecordService.selectById(pushBillEntity.getAccid());
                messageDetails.put("address", accidentRecordEntity.getAddress());
                messageDetails.put("lng", accidentRecordEntity.getLng());
                messageDetails.put("lat", accidentRecordEntity.getLat());
                messageDetails.put("remark", accidentRecordEntity.getIntroduce());
                //视频
                messageDetails.put("url", accidentRecordEntity.getThumbnailUrl());
                messageDetails.put("videoUrl", accidentRecordEntity.getVideo());
                if (accidentRecordEntity.getVideo() == null) {
                    messageDetails.put("url", accidentRecordEntity.getImgUrl());
                }
//                UserEntity userEntity = userService.selectById(accidentRecordEntity.getUserId());
                AppUserEntity userEntity = userService.selectById(accidentRecordEntity.getUserId());
                messageDetails.put("name", userEntity.getName());
                messageDetails.put("phone", userEntity.getUsername());

                //查询到地点图片
                String mapImg = appSendUrlService.findMapImg(accidentRecordEntity.getId());
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(mapImg)) {
                    messageDetails.put("mapImg", mapImg);
                } else {
                    messageDetails.put("mapImg", "");
                }
            } else {
//                BizAccidentEntity bizAccidentEntity = bizAccidentService.findById(pushBillEntity.getAccid());
                BizAccident bizAccidentEntity = bizAccidentService.findById(pushBillEntity.getAccid());
                messageDetails.put("address", bizAccidentEntity.getAddress());
                messageDetails.put("lng", bizAccidentEntity.getLng());
                messageDetails.put("lat", bizAccidentEntity.getLat());
                messageDetails.put("remark", "车辆事故");
                //视频
                messageDetails.put("url", bizAccidentEntity.getThumbnailUrl());
                messageDetails.put("videoUrl", bizAccidentEntity.getVideo());
                String openid = bizAccidentEntity.getOpenid();
//                UserEntity userEntity = userService.selectWxuser(openid);
                AppUserEntity userEntity = userService.selectWxuser(openid);
                if (userEntity != null) {
                    messageDetails.put("name", userEntity.getName());
                    messageDetails.put("phone", userEntity.getPhoneNumber());
                } else {
                    messageDetails.put("name", "");
                    messageDetails.put("phone", "");
                }
                messageDetails.put("mapImg", "");
            }

        } else {
            AppSendOutSheetEntity appSendOutSheetEntity = appSendOutSheetService.selectById(messageId);
            if (appSendOutSheetEntity == null) {
                result.put("code", 407);
                result.put("message", "未查询到信息");
                return result;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = sdf.format(appSendOutSheetEntity.getCreateTime());
            messageDetails.put("accidentTime", format);
            messageDetails.put("address", appSendOutSheetEntity.getAdress());
            messageDetails.put("lng", appSendOutSheetEntity.getLng());
            messageDetails.put("lat", appSendOutSheetEntity.getLat());
            messageDetails.put("remark", appSendOutSheetEntity.getRemark());

            messageDetails.put("voice", appSendOutSheetEntity.getVoice());
            messageDetails.put("accidentType", appSendOutSheetEntity.getAccidentType());

            List<String> appMessageUrlEntityList = appMessageUrlService.selectScenePhotos(Long.valueOf(appSendOutSheetEntity.getId()), 2);
            messageDetails.put("scenePhotos", appMessageUrlEntityList);

            messageDetails.put("mapImg", "");
            //查询到图片
            List<String> imgUrl = appSendOutSheetService.findIndex1Img(appSendOutSheetEntity.getId());
            messageDetails.put("videoUrl", "");
            if (imgUrl.isEmpty()) {
                messageDetails.put("url", "");
            } else {
                for (String s : imgUrl) {
                    if (org.apache.commons.lang3.StringUtils.isNotEmpty(s)) {
                        messageDetails.put("url", s);
                        break;
                    } else {
                        messageDetails.put("url", "");
                    }
                }
            }
            BigDecimal payAmount = appSendOutSheetEntity.getPayAmount();

            if (payAmount == null || new BigDecimal("0").compareTo(payAmount) > -1) {
                messageDetails.put("deduction", new BigDecimal("10"));
            } else {
                messageDetails.put("deduction", payAmount);
            }

            if (appSendOutSheetEntity.getCheckTime() != null) {
                messageDetails.put("checkTime", sdf.format(appSendOutSheetEntity.getCheckTime()));
                messageDetails.put("isCheck", "1");
            } else {
                messageDetails.put("checkTime", appSendOutSheetEntity.getCheckTime());
                messageDetails.put("isCheck", "0");
            }
            messageDetails.put("checkAddress", appSendOutSheetEntity.getCheckAddress());
            messageDetails.put("checkState", appSendOutSheetEntity.getCheckState());

            //查询到url图片
            List<CheckImgList> imgList = appSendOutSheetService.selectSenceImg(appSendOutSheetEntity.getId());
            messageDetails.put("sceneImg", imgList);
            messageCarId = appSendOutSheetEntity.getMessageCarId();
            messageDetails.put("name", "");
            messageDetails.put("phone", "");
        }

        messageDetails.put("taskSource", "公司信息");

        Integer id = currentLoginUser.getAppUserEntity().getId();
//        UserEntity userEntity = userService.selectById(id);
        AppUserEntity userEntity = userService.selectById(id);
        //        String username = userEntity.getUsername();
        //        //根据c端电话号码查询到b端id
        //        String Bid = bUserService.selectByUsername(username);
        //        if (StringUtils.isEmpty(Bid)) {
        //            result.put("code", 200);
        //            result.put("message", "未查询到b端账号");
        //            result.put("data", "");
        //            return result;
        //        }
        messageDetails.put("claimsConsultant", userEntity.getName());


        JSONArray objects = new JSONArray();

        BigDecimal lng = messageDetails.getBigDecimal("lng");
        BigDecimal lat = messageDetails.getBigDecimal("lat");
        AccidentRecord accidentRecordEntity = new AccidentRecord();
        accidentRecordEntity.setLng(new BigDecimal(lng.toString()));
        accidentRecordEntity.setLat(new BigDecimal(lat.toString()));

        if (org.apache.commons.lang3.StringUtils.isNotEmpty(messageCarId)) {
            String[] split = messageCarId.split(",");
            for (String s : split) {
                //查询
                AppMessageCarEntity appMessageCarEntity = appMessageCarService.selectById(s);
                if (appMessageCarEntity != null && org.apache.commons.lang3.StringUtils.isNotEmpty(appMessageCarEntity.getBrandId())) {
                    String brandId = appMessageCarEntity.getBrandId();
                    //取到品牌id
                    CarBrandEntity carBrandEntity = carBrandMapper.selectById(brandId);
                    String brandName = carBrandEntity.getName();
                    List<TableMess> tableMesses = appSendOutSheetService.find4sStorebyAppsend(accidentRecordEntity, brandName, brandId, appMessageCarEntity);
                    objects.add(tableMesses);
                }
            }
            messageDetails.put("merchants", objects);
        } else {
            messageDetails.put("merchants", objects);
        }


        //处理address
        String address = messageDetails.getString("address");
        String replace = address.replace("& #40;", "(");
        String replace1 = replace.replace("& #41;", ")");
        messageDetails.put("address", replace1);


        messageDetails.put("estimateLoss", "");

        //查询车辆信息
        //根据信息 id和type查询到list
        EntityWrapper<AppMessageCarEntity> appMessageCarWrapper = new EntityWrapper<>();
        appMessageCarWrapper.eq("mess_id", messageId)
                .eq("type", type);
        List<AppMessageCarEntity> appMessageCarEntities = appMessageCarService.selectList(appMessageCarWrapper);
        if (appMessageCarEntities.isEmpty()) {
            messageDetails.put("messageCarList", appMessageCarEntities);
        } else {
            JSONArray array = new JSONArray();
            for (AppMessageCarEntity appMessageCar : appMessageCarEntities) {
                JSONObject object = new JSONObject();
                object.put("carId", appMessageCar.getId());
                object.put("licensePlate", appMessageCar.getLicensePlate());
                object.put("brandId", appMessageCar.getBrandId());
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(appMessageCar.getBrandId())) {
                    CarBrandEntity appCarBrandEntity = carBrandMapper.selectById(appMessageCar.getBrandId());
                    object.put("brand", appCarBrandEntity.getName());
                    object.put("brandUrl", appCarBrandEntity.getPicUrl());
                } else {
                    object.put("brand", "");
                    object.put("brandUrl", "");
                }
                object.put("localInsurance", appMessageCar.getLocalInsurance());
                object.put("customerName", appMessageCar.getCustomerName());
                object.put("phone", appMessageCar.getPhone());
                object.put("casualties", appMessageCar.getCasualties());
                object.put("financialLoss", appMessageCar.getFinancialLoss());
                object.put("buyCar", appMessageCar.getBuyCar());
                object.put("channelsIns", appMessageCar.getChannelsIns());
                object.put("leaveMessage", appMessageCar.getLeaveMessage());
                object.put("vehicleResults", appMessageCar.getVehicleResults());
                object.put("accidentResponsibility", appMessageCar.getAccidentResponsibility());
                object.put("amount", appMessageCar.getAmount());
                //审核结果
                String remark = pushBillEntity.getRemark();
                object.put("award", remark);

                String userId = appMessageCar.getUserBId();
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(userId)) {
                    if (userId.endsWith("Z")) {
                        //upmerchants商户
                        userId = userId.substring(0, userId.length() - 1);
                        //修改已读未读
                        AppUpMerchantsEntity appUpMerchantsEntity = appUpMerchantsService.selectById(userId);
                        object.put("merchantsName", appUpMerchantsEntity.getName());
                        object.put("userBId", "150");
                    } else {
                        AppUserEntity bUserEntity = userService.selectById(userId);
                        if (bUserEntity == null) {
                            result.put("code", 407);
                            result.put("msg", "未查询到该商户");
                            return result;
                        }
                        object.put("merchantsName", bUserEntity.getMerchantsName());
                        object.put("userBId", userId);
                    }
                } else {
                    object.put("merchantsName", "暂无");
                }

                EntityWrapper<AppMessageUrlEntity> wrapper3 = new EntityWrapper<>();
                wrapper3.eq("type", 3)
                        .eq("message_id", appMessageCar.getId());
                List<AppMessageUrlEntity> credentialsImgList = appMessageUrlService.selectList(wrapper3);
                object.put("credentialsImgList", credentialsImgList);
                array.add(object);

            }
            messageDetails.put("messageCarList", array);
        }


        result.put("code", 200);
        result.put("data", messageDetails);
        return result;
    }

    //订单列表2
    @ApiOperation(value = "我的订单列表" +
            "状态(1,新订单,2.未到店,3.服务中,4.已交车,5.已结算）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "year", value = "年", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "month", value = "月", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "state", value = "订单状态", required = true, dataType = "String")
    })
    @RequestMapping(value = "/indentList22", method = RequestMethod.GET)
    public JSONObject indentList22(HttpServletRequest request /*Integer id*/, String state, String year, String month, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        AppUserEntity appUserEntity = currentLoginUser.getAppUserEntity();
        Integer id = appUserEntity.getId();
        String upId = appUserEntity.getUpId();

        JSONObject indentListByUserid = indentService.findIndentListByUserid22(String.valueOf(id), year, month, state, pagesize, upId);

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", indentListByUserid);
        return result;
    }


    //订单详情,根据订单id查询到订单详情
    @ApiOperation(value = "订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "IndentId", value = "订单id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/IndentDetails", method = RequestMethod.GET)
    public JSONObject IndentDetails(HttpServletRequest request, String IndentId) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        IndentDetails indentDetilsByIndentId = indentService.findIndentDetilsByIndentId(IndentId);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", indentDetilsByIndentId);
        return result;
    }


    //订单列表2
    @ApiOperation(value = "推修账单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "year", value = "年", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "month", value = "月", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = true, dataType = "Integer"),
    })
    @RequestMapping(value = "/pushRepairBill", method = RequestMethod.GET)
    public JSONObject indentList22(HttpServletRequest request /*Integer id*/, String year, String month, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        AppUserEntity appUserEntity = currentLoginUser.getAppUserEntity();
        Integer id = appUserEntity.getId();
        String upId = appUserEntity.getUpId();

        JSONObject indentListByUserid = indentService.findIndentListByUserid33(String.valueOf(id), year, month, pagesize, upId);

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", indentListByUserid);
        return result;
    }


    @ApiOperation(value = "保存车辆信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "messageId", value = "信息id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "voice", value = "录音", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "vehiclePhotoImgList", value = "现场照片", required = false, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "userBId", value = "商户id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "amount", value = "维修金额", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "brandId", value = "品牌id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "车辆订单id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "accidentResponsibility", value = "事故责任", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "licensePlate", value = "车牌", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "brandId", value = "车辆品牌", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "localInsurance", value = "保险公司", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "customerName", value = "客户姓名", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "联系电话", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "casualties", value = "人员伤亡", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "financialLoss", value = "财务损失", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "buyCar", value = "购车单位", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "channelsIns", value = "保险渠道", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "vehicleResults", value = "维修结果 1.到店 2.成功。3.跟踪，4.失败", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "leaveMessage", value = "留言", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "credentialsImgList", value = "推修凭证", required = false, dataType = "String[]"),
    })
    @RequestMapping(value = "/saveCarMessage", method = RequestMethod.POST)
    public JSONObject saveCarMessage(HttpServletRequest request, @RequestBody JSONObject in) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        String userid = currentLoginUser.getAppUserEntity().getcId();
        if (StringUtils.isEmpty(userid) ) {
            result.put("code", 530);
            result.put("msg", "用户未绑定c端账号");
            return result;
        }

        return cdImgService.saveCarMess(result,in, userid);
    }



    //签到
    @ApiOperation(value = "签到")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "messageId", value = "信息id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "checkAddress", value = "签到地址", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "checkLng", value = "签到经度", required = true, dataType = "Bigdecimal"),
            @ApiImplicitParam(paramType = "query", name = "checkLat", value = "签到维度", required = true, dataType = "Bigdecimal"),
            @ApiImplicitParam(paramType = "query", name = "checkState", value = "签到状态", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "imgList", value = "签到图片", required = true, dataType = "String[]")
    })
    @RequestMapping(value = "/checkSign", method = RequestMethod.POST)
    public JSONObject checkSign(HttpServletRequest request, @RequestBody JSONObject in) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        String userid = currentLoginUser.getAppUserEntity().getcId();
        if (StringUtils.isEmpty(userid) ) {
            result.put("code", 530);
            result.put("msg", "用户未绑定c端账号");
            return result;
        }

        return cdImgService.checkSign(result,in);

    }


    //车辆详情 查询详情
    //根据id查询订单详情
    @ApiOperation(value = "查询车辆订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "messageCarId", value = "信息id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "userBId", value = "商户Id", required = true, dataType = "Integer"),

    })
    @RequestMapping(value = "/findMessageCar", method = RequestMethod.GET)
    public JSONObject findMessageCar(HttpServletRequest request, Integer messageCarId, Integer userBId) {
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        if (messageCarId == null) {
            result.put("code", 407);
            result.put("msg", "信息id为空");
            return result;
        }

        if (userBId == null) {
            result.put("code", 407);
            result.put("msg", "userBId为空");
            return result;
        }

        return cdImgService.findMessageCar(result,messageCarId,userBId);

    }



    //新增派单操作
    @ApiOperation(value = "事故服务")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "经度", value = "lng", required = true, dataType = "Bigdecimal"),
            @ApiImplicitParam(paramType = "query", name = "纬度", value = "lat", required = true, dataType = "Bigdecimal"),
            @ApiImplicitParam(paramType = "query", name = "地址", value = "address", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "车牌号", value = "plate", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "联系人名字", value = "name", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "品牌", value = "brand", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "电话号码", value = "phone", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "公司名字", value = "company", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "备注", value = "remark", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "图片", value = "urlList", required = true, dataType = "String[]")
    })
    @RequestMapping(value = "/accidentSer", method = RequestMethod.POST)
    public JSONObject accidentSer(HttpServletRequest request, @RequestBody JSONObject in) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        String id = currentLoginUser.getAppUserEntity().getcId();
        if (StringUtils.isEmpty(id) ) {
            result.put("code", 530);
            result.put("msg", "用户未绑定c端账号");
            return result;
        }
        cdImgService.addAccidentSer(in, Integer.valueOf(id));

        result.put("code", 200);
        result.put("msg", "成功");
        return result;

    }

    //事故服务列表
    @ApiOperation(value = "事故列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "state", value = "事故状态", required = true, dataType = "int")
    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JSONObject list(HttpServletRequest request, Integer state, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        //查询列表
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        String id = currentLoginUser.getAppUserEntity().getcId();
        if (StringUtils.isEmpty(id) ) {
            result.put("code", 530);
            result.put("msg", "用户未绑定c端账号");
            return result;
        }

        JSONObject object = cdImgService.findSendSheentList(Integer.valueOf(id),state,pagesize);

        result.put("code", 200);
        result.put("data", object);
        return result;

    }


    @ApiOperation(value = "事故服务详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "事故id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "cid", value = "事故cid", required = true, dataType = "String")
    })
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public JSONObject details(/*HttpServletRequest request*/Integer id,String cid) {
        //查询列表
        JSONObject result = new JSONObject();
        if (id==null){
            result.put("code", 407);
            result.put("msg", "id为空");
            return result;
        }
        JSONObject object = cdImgService.findAccidentDetails(id,cid);

        result.put("code", 200);
        result.put("data", object);
        return result;
    }


}
