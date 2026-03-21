package com.cheji.web.modular.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.web.constant.ConsEnum;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.cwork.CheckImgList;
import com.cheji.web.modular.cwork.ListMessage;
import com.cheji.web.modular.cwork.TableMess;
import com.cheji.web.modular.cwork.TrackStateNumber;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.mapper.AppCarBrandEntityMapper;
import com.cheji.web.modular.mapper.AppMessageUrlMapper;
import com.cheji.web.modular.service.*;
import com.cheji.web.pojo.TokenPojo;
import com.cheji.web.util.MapNavUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 * web派单记录表 前端控制器
 * </p>
 *
 * @author Ashes
 * @since 2021-01-11
 */
@RestController
@RequestMapping("/appSendOutSheet")
public class AppSendOutSheetController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(AppSendOutSheetController.class);

    @Resource
    private UserService userService;

    @Resource
    private BUserService bUserService;

    @Resource
    private PushBillService pushBillService;

    @Resource
    private AppSendOutSheetService appSendOutSheetService;

    @Resource
    private AccidentRecordService accidentRecordService;

    @Resource
    private BizAccidentService bizAccidentService;

    @Resource
    private AppSendUrlService appSendUrlService;

    @Resource
    private AppMessageCarService addCarMessage;

    @Resource
    private AppCarBrandEntityMapper appCarBrandEntityMapper;

    @Resource
    private AppMessageCarService appMessageCarService;

    @Resource
    private AppMessageUrlService appMessageUrlService;

    @Resource
    private AppPushMessService appPushMessService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AccidentRecordService AccidentRecordEntityService;

    @Resource
    private AppClaimTeacherService appClaimTeacherService;

    @Resource
    private AppMessageUrlMapper appMessageUrlMapper;

    @Resource
    private AppUpMerchantsService appUpMerchantsService;

    @Resource
    private IndentService indentService;


    private static final String key = "a11e3020a4b82ce9390044286910f02f";


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
        Integer id = currentLoginUser.getAppUserEntity().getId();
        UserEntity userEntity = userService.selectById(id);
        String username = userEntity.getUsername();
        //根据c端电话号码查询到b端id
        String Bid = bUserService.selectByUsername(username);
        if (StringUtils.isEmpty(Bid)) {
            result.put("code", 200);
            result.put("message", "未查询到b端账号");
            result.put("data", "");
            return result;
        }
        //根据b端id查询到事故
            List<PushBillEntity> billList = pushBillService.findBillListBySreach(Integer.valueOf(Bid), pagesize, text);
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
            TrackStateNumber stateNumber = pushBillService.selectTrackStateNumber(Bid);
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
            UserEntity userEntity = userService.selectById(id);
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
                        AccidentRecordEntity accidentRecordEntity = accidentRecordService.selectById(pushBillEntity.getAccid());
                        address = accidentRecordEntity.getAddress();
                        object.put("url", accidentRecordEntity.getThumbnailUrl());
                        object.put("videoUrl", accidentRecordEntity.getVideo());
                        if (accidentRecordEntity.getVideo() == null) {
                            object.put("url", accidentRecordEntity.getImgUrl());
                        }
                        pushBillEntity.setLat(new BigDecimal(accidentRecordEntity.getLat()));
                        pushBillEntity.setLng(new BigDecimal(accidentRecordEntity.getLng()));
                    } else {
                        BizAccidentEntity bizAccidentEntity = bizAccidentService.findById(pushBillEntity.getAccid());
                        address = bizAccidentEntity.getAddress();
                        object.put("url", bizAccidentEntity.getThumbnailUrl());
                        object.put("videoUrl", bizAccidentEntity.getVideo());
                        pushBillEntity.setLat(bizAccidentEntity.getLat());
                        pushBillEntity.setLng(bizAccidentEntity.getLng());
                    }
                    BigDecimal deduction = pushBillEntity.getDeduction();
                    if (new BigDecimal("0").compareTo(deduction) > -1) {
                        //a >= b  0大于价格
                        object.put("deduction", new BigDecimal("10"));
                    } else {
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
                        object.put("deduction", new BigDecimal("10"));
                    } else if (new BigDecimal("0").compareTo(appSendOutSheetEntity.getPayAmount()) > -1) {
                        object.put("deduction", new BigDecimal("10"));
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
                object.put("lng", pushBillEntity.getLng());
                object.put("lat", pushBillEntity.getLat());
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
                    messageDetails.put("deduction", new BigDecimal("10"));
                } else {
                    messageDetails.put("deduction", deduction);
                }

                if (pushBillEntity.getSource() == 1) {
                    //app
                    AccidentRecordEntity accidentRecordEntity = accidentRecordService.selectById(pushBillEntity.getAccid());
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
                    UserEntity userEntity = userService.selectById(accidentRecordEntity.getUserId());
                    messageDetails.put("name", userEntity.getName());
                    messageDetails.put("phone", userEntity.getUsername());

                    //查询到地点图片
                    String mapImg = appSendUrlService.findMapImg(accidentRecordEntity.getId());
                    if (StringUtils.isNotEmpty(mapImg)) {
                        messageDetails.put("mapImg", mapImg);
                    } else {
                        messageDetails.put("mapImg", "");
                    }
                } else {
                    BizAccidentEntity bizAccidentEntity = bizAccidentService.findById(pushBillEntity.getAccid());
                    messageDetails.put("address", bizAccidentEntity.getAddress());
                    messageDetails.put("lng", bizAccidentEntity.getLng());
                    messageDetails.put("lat", bizAccidentEntity.getLat());
                    messageDetails.put("remark", "车辆事故");
                    //视频
                    messageDetails.put("url", bizAccidentEntity.getThumbnailUrl());
                    messageDetails.put("videoUrl", bizAccidentEntity.getVideo());
                    String openid = bizAccidentEntity.getOpenid();
                    UserEntity userEntity = userService.selectWxuser(openid);
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

                List<String> appMessageUrlEntityList = appMessageUrlService.selectScenePhotos(appSendOutSheetEntity.getId(), 2);
                messageDetails.put("scenePhotos", appMessageUrlEntityList);

                messageDetails.put("mapImg", "");
                //查询到图片
                List<String> imgUrl = appSendOutSheetService.findIndex1Img(appSendOutSheetEntity.getId());
                messageDetails.put("videoUrl", "");
                if (imgUrl.isEmpty()) {
                    messageDetails.put("url", "");
                } else {
                    for (String s : imgUrl) {
                        if (StringUtils.isNotEmpty(s)) {
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
            UserEntity userEntity = userService.selectById(id);
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
            AccidentRecordEntity accidentRecordEntity = new AccidentRecordEntity();
            accidentRecordEntity.setLng(lng.doubleValue());
            accidentRecordEntity.setLat(lat.doubleValue());

            if (StringUtils.isNotEmpty(messageCarId)) {
                String[] split = messageCarId.split(",");
                for (String s : split) {
                    //查询
                    AppMessageCarEntity appMessageCarEntity = appMessageCarService.selectById(s);
                    if (appMessageCarEntity != null && StringUtils.isNotEmpty(appMessageCarEntity.getBrandId())) {
                        String brandId = appMessageCarEntity.getBrandId();
                        //取到品牌id
                        AppCarBrandEntity appCarBrandEntity = appCarBrandEntityMapper.selectById(brandId);
                        String brandName = appCarBrandEntity.getName();
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
                    if (StringUtils.isNotEmpty(appMessageCar.getBrandId())) {
                        AppCarBrandEntity appCarBrandEntity = appCarBrandEntityMapper.selectById(appMessageCar.getBrandId());
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
                    if (StringUtils.isNotEmpty(userId)) {
                        if (userId.endsWith("Z")) {
                            //upmerchants商户
                            userId = userId.substring(0, userId.length() - 1);
                            //修改已读未读
                            AppUpMerchantsEntity appUpMerchantsEntity = appUpMerchantsService.selectById(userId);
                            object.put("merchantsName", appUpMerchantsEntity.getName());
                            object.put("userBId", "150");
                        } else {
                            BUserEntity bUserEntity = bUserService.selectById(userId);
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


        //显示订单详情
        @ApiOperation(value = "接受任务")
        @ApiImplicitParams({
                @ApiImplicitParam(paramType = "query", name = "messageId", value = "信息id", required = true, dataType = "String"),
                @ApiImplicitParam(paramType = "query", name = "type", value = "类型", required = true, dataType = "int")
        })
        @RequestMapping(value = "/acceptTask", method = RequestMethod.GET)
        public JSONObject acceptTask(String messageId, Integer type) {
            //修改状态
            JSONObject result = new JSONObject();

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

            //1 pb  2 sos
            if (type == 1) {
                PushBillEntity pushBillEntity = pushBillService.selectById(messageId);
                if (pushBillEntity == null) {
                    result.put("code", 407);
                    result.put("message", "未查询到信息");
                    return result;
                }
                pushBillEntity.setTrackState(2);        //已经接受任务
                pushBillService.updateById(pushBillEntity);
                //1,app,2.小程序
                Integer accid = pushBillEntity.getAccid();
                Integer source = pushBillEntity.getSource();
                if (source == 1) {
                    //app
                    AccidentRecordEntity accidentRecordEntity = AccidentRecordEntityService.selectById(accid);
                    accidentRecordEntity.setIsOrder(3);
                    accidentRecordEntity.setUpdateTime(new Date());
                    accidentRecordService.updateById(accidentRecordEntity);
                } else {
                    BizAccidentEntity byId = bizAccidentService.findById(accid);
                    byId.setIsOrder(3);
                    byId.setUpdateTime(new Date());
                    bizAccidentService.updateById(byId);
                }
            } else {
                AppSendOutSheetEntity appSendOutSheetEntity = appSendOutSheetService.selectById(messageId);
                if (appSendOutSheetEntity == null) {
                    result.put("code", 407);
                    result.put("message", "未查询到信息");
                    return result;
                }
                appSendOutSheetEntity.setTrackState(2);  //已经接受任务
                appSendOutSheetService.updateById(appSendOutSheetEntity);
            }

            result.put("code", 200);
            result.put("message", "成功");
            return result;
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
        public JSONObject checkSign(@RequestBody JSONObject in) {
            JSONObject result = new JSONObject();

            String messageId = in.getString("messageId");
            if (StringUtils.isEmpty(messageId)) {
                result.put("code", 407);
                result.put("message", "messageId为空");
                return result;
            }
            Integer type = in.getInteger("type");
            if (type == null) { // 1 bp  2 sos
                result.put("code", 407);
                result.put("message", "type为空");
                return result;
            }

            Integer checkState = in.getInteger("checkState");
            if (checkState == null) {
                result.put("code", 407);
                result.put("message", "checkState为空");
                return result;
            }
            String checkAddress = in.getString("checkAddress");
            if (StringUtils.isEmpty(checkAddress)) {
                result.put("code", 407);
                result.put("message", "checkAddress为空");
                return result;
            }

            BigDecimal checkLng = in.getBigDecimal("checkLng");
            BigDecimal checkLat = in.getBigDecimal("checkLat");
            if (checkLat == null || checkLng == null) {
                result.put("code", 407);
                result.put("message", "经纬度为空");
                return result;
            }

            JSONObject json = JSONObject.parseObject(String.valueOf(in));
            JSONArray imgList = (JSONArray) json.get("imgList");
            if (imgList == null) {
                result.put("code", 407);
                result.put("msg", "图片不能为空");
                return result;
            }

            //先删
            EntityWrapper<AppSendUrlEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("send_id", messageId)
                    .eq("type", 2)
                    .eq("source", type);
            appSendUrlService.delete(wrapper);


            List<String> stringList = JSONArray.parseArray(imgList.toString(), String.class);
            for (int i = 0; i < stringList.size(); i++) {
                //保存图片
                AppSendUrlEntity appSendUrlEntity = new AppSendUrlEntity();
                appSendUrlEntity.setType(2);
                appSendUrlEntity.setUrl(stringList.get(i));
                appSendUrlEntity.setSource(type);
                appSendUrlEntity.setIndex(i);
                appSendUrlEntity.setSendId(messageId);
                appSendUrlEntity.setCreateTime(new Date());
                appSendUrlEntity.setUpdateTime(new Date());
                appSendUrlService.insert(appSendUrlEntity);
            }

            Date time = new Date();

            if (type == 1) {
                PushBillEntity pushBillEntity = pushBillService.selectById(messageId);
                if (pushBillEntity == null) {
                    result.put("code", 407);
                    result.put("message", "未查询到信息");
                    return result;
                }
                //查询到具体事故
                Integer source = pushBillEntity.getSource();
                BigDecimal inlng = new BigDecimal("0");
                BigDecimal inlat = new BigDecimal("0");
                if (source == 1) {
                    //1.app 2 小程序
                    AccidentRecordEntity accidentApp = accidentRecordService.selectById(pushBillEntity.getAccid());
                    inlng = BigDecimal.valueOf(accidentApp.getLng());
                    inlat = BigDecimal.valueOf(accidentApp.getLat());
                } else {
                    BizAccidentEntity bizAccidentWx = bizAccidentService.findById(pushBillEntity.getAccid());
                    inlng = bizAccidentWx.getLng();
                    inlat = bizAccidentWx.getLat();
                }

                //查询经纬度
                MapNavUtil mapNavUtil = new MapNavUtil(checkLng + "," + checkLat, inlng + "," + inlat, key, 1, "JSON");
                Long results = mapNavUtil.getResults();
                BigDecimal distance3 = new BigDecimal(results);
                distance3 = distance3.divide(new BigDecimal("1000"), 2, BigDecimal.ROUND_HALF_UP);

                if (new BigDecimal("3").compareTo(distance3) > -1) {

                    pushBillEntity.setState(2);
                } else {
                    //签到距离太远
                    pushBillEntity.setState(3);
                }

                pushBillEntity.setCheckAddress(checkAddress);
                pushBillEntity.setCheckTime(new Date());
                pushBillEntity.setCheckLng(checkLng);
                pushBillEntity.setCheckLat(checkLat);
                pushBillEntity.setCheckState(checkState);
                if (checkState == 4) {
                    pushBillEntity.setState(4);
                }
                time = pushBillEntity.getCheckTime();
                pushBillService.updateById(pushBillEntity);

            } else {
                AppSendOutSheetEntity appSendOutSheetEntity = appSendOutSheetService.selectById(messageId);
                if (appSendOutSheetEntity == null) {
                    result.put("code", 407);
                    result.put("message", "未查询到信息");
                    return result;
                }
                MapNavUtil mapNavUtil = new MapNavUtil(checkLng + "," + checkLat, appSendOutSheetEntity.getLng() + "," + appSendOutSheetEntity.getLat(), key, 1, "JSON");
                Long results = mapNavUtil.getResults();
                BigDecimal distance3 = new BigDecimal(results);
                distance3 = distance3.divide(new BigDecimal("1000"), 2, BigDecimal.ROUND_HALF_UP);

                if (new BigDecimal("3").compareTo(distance3) > -1) {

                    appSendOutSheetEntity.setState(2);
                } else {
                    //签到距离太远
                    appSendOutSheetEntity.setState(3);
                }

                appSendOutSheetEntity.setCheckAddress(checkAddress);
                appSendOutSheetEntity.setCheckTime(new Date());
                appSendOutSheetEntity.setLng(checkLng);
                appSendOutSheetEntity.setLat(checkLat);
                appSendOutSheetEntity.setCheckState(checkState);
                if (checkState == 4) {
                    appSendOutSheetEntity.setState(4);
                }
                time = appSendOutSheetEntity.getCheckTime();
                appSendOutSheetService.updateById(appSendOutSheetEntity);

            }

            result.put("code", 200);
            result.put("message", "成功");
            result.put("checkTime", time);
            return result;

        }

        @ApiOperation(value = "查询就近商户")
        @ApiImplicitParams({
                @ApiImplicitParam(paramType = "query", name = "messageId", value = "信息id", required = true, dataType = "String"),
                @ApiImplicitParam(paramType = "query", name = "type", value = "类型", required = true, dataType = "int"),
                @ApiImplicitParam(paramType = "query", name = "brandId", value = "品牌id", required = true, dataType = "int")
        })
        @RequestMapping(value = "/find4sStrore", method = RequestMethod.GET)
        public JSONObject find4sStrore(String messageId, Integer type, Integer brandId) {
            //查询品牌
            //根据距离排序，接收品牌id，返回商户list
            JSONObject result = new JSONObject();
            //查询到具体经纬度
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

            if (brandId == null) {
                result.put("code", 407);
                result.put("message", "brandId为空");
                return result;
            }
            //查询到品牌名称
            AppCarBrandEntity appCarBrandEntity = appCarBrandEntityMapper.selectById(brandId);
            String brandName = appCarBrandEntity.getName();


            AccidentRecordEntity accident = new AccidentRecordEntity();
            //1 pb  2 sos
            if (type == 1) {
                PushBillEntity pushBillEntity = pushBillService.selectById(messageId);
                if (pushBillEntity == null) {
                    result.put("code", 407);
                    result.put("message", "未查询到信息");
                    return result;
                }
                //查询到信息
                if (pushBillEntity.getSource() == 1) {
                    //app
                    AccidentRecordEntity accidentRecordEntity = accidentRecordService.selectById(pushBillEntity.getAccid());
                    accident.setLat(accidentRecordEntity.getLat());
                    accident.setLng(accidentRecordEntity.getLng());
                } else {
                    BizAccidentEntity bizAccidentEntity = bizAccidentService.findById(pushBillEntity.getAccid());
                    accident.setLng(bizAccidentEntity.getLng().doubleValue());
                    accident.setLat(bizAccidentEntity.getLat().doubleValue());
                }
            } else {
                AppSendOutSheetEntity appSendOutSheetEntity = appSendOutSheetService.selectById(messageId);
                if (appSendOutSheetEntity == null) {
                    result.put("code", 407);
                    result.put("message", "未查询到信息");
                    return result;
                }
                accident.setLat(appSendOutSheetEntity.getLat().doubleValue());
                accident.setLng(appSendOutSheetEntity.getLng().doubleValue());

            }

            List<TableMess> tableMesses = appSendOutSheetService.find4sStore(accident, brandName, brandId);


            result.put("code", 200);
            result.put("data", tableMesses);
            return result;
        }


        //第一次保存后生成id关联sos表或pb表 id用逗号隔开,
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
            Integer userid = currentLoginUser.getAppUserEntity().getId();
            //有id就全部修改，没有id就全部新增加

            //事故类型
            Integer accidentType = in.getInteger("accidentType");
            String voice = in.getString("voice");

    //        Integer type1 = in.getInteger("type");
    //        if (type1 == null) {
    //            result.put("code", 407);
    //            result.put("message", "type为空");
    //            return result;
    //        }

            String creat = "无";
            String messageId = in.getString("messageId");

            Integer type = in.getInteger("type");

            //logger.error("接受参数 messageId={};type={}", messageId,type);
            if (type == 1) {
                PushBillEntity pushBillEntity = pushBillService.selectById(messageId);
                if (pushBillEntity == null) {
                    result.put("code", 407);
                    result.put("message", "未查询到信息");
                    return result;
                } else {
                    if (StringUtils.isNotEmpty(voice)) {
                        pushBillEntity.setVoice(voice);
                    }
                }
                pushBillService.updateById(pushBillEntity);
            } else {
                AppSendOutSheetEntity appSendOutSheetEntity = appSendOutSheetService.selectById(messageId);
                if (appSendOutSheetEntity == null) {
                    result.put("code", 407);
                    result.put("message", "未查询到信息");
                    return result;
                } else {
                    if (StringUtils.isNotEmpty(voice))
                        appSendOutSheetEntity.setVoice(voice);
                }
                appSendOutSheetService.updateById(appSendOutSheetEntity);
            }

            JSONObject json = JSONObject.parseObject(String.valueOf(in));
            //车损照片
            JSONArray vehiclePhotoImgList = (JSONArray) json.get("vehiclePhotoImgList");

            if (vehiclePhotoImgList != null) {
                //先删除
                //查询到原本图片
                EntityWrapper<AppMessageUrlEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("message_id", in.getString("messageId"))
                        .eq("type", 1)
                        .eq("source", in.getInteger("type"));
                appMessageUrlMapper.delete(wrapper);

                List<String> vehicleList = JSONArray.parseArray(vehiclePhotoImgList.toString(), String.class);
                for (int i = 0; i < vehicleList.size(); i++) {
                    AppMessageUrlEntity appMessageUrlEntity = new AppMessageUrlEntity();
                    appMessageUrlEntity.setMessageId(in.getString("messageId"));
                    appMessageUrlEntity.setIndex(i + 1);
                    appMessageUrlEntity.setType(1);
                    appMessageUrlEntity.setSource(type);
                    appMessageUrlEntity.setUrl(vehicleList.get(i));
                    appMessageUrlEntity.setCreateTime(new Date());
                    appMessageUrlEntity.setUpdateTime(new Date());
                    appMessageUrlMapper.insert(appMessageUrlEntity);
                }
            }


            //转成list
            JSONArray carMessageList = (JSONArray) json.get("carMessageList");
            if (carMessageList != null) {
                List<AppMessageCarEntity> appMessageCarEntities = JSONArray.parseArray(carMessageList.toString(), AppMessageCarEntity.class);
                if (!appMessageCarEntities.isEmpty()) {
                    //有id就是新增，没有id就是修改
                    for (AppMessageCarEntity messageCarEntity : appMessageCarEntities) {
                        Long id = messageCarEntity.getId();
                        if (id == null) {
                            type = in.getInteger("type");

    //            if (StringUtils.isEmpty(in.getString("brandId"))) {
    //                result.put("code", 407);
    //                result.put("message", "品牌id为空");
    //                return result;
    //            }

                            PushBillEntity pushBillEntity = new PushBillEntity();
                            AppSendOutSheetEntity appSendOutSheetEntity = new AppSendOutSheetEntity();
                            if (type == 1) {
                                pushBillEntity = pushBillService.selectById(messageId);
                                if (pushBillEntity == null) {
                                    result.put("code", 407);
                                    result.put("message", "未查询到信息");
                                    return result;
                                }
                            } else {
                                appSendOutSheetEntity = appSendOutSheetService.selectById(messageId);
                                if (appSendOutSheetEntity == null) {
                                    result.put("code", 407);
                                    result.put("message", "未查询到信息");
                                    return result;
                                }
                            }
                            Integer vehicleResults = in.getInteger("vehicleResults");
                            if (vehicleResults != null && type == 1) {
                                pushBillEntity.setTrackState(3);
                            } else if (vehicleResults != null && type == 2) {
                                appSendOutSheetEntity.setTrackState(3);
                            }

                            if (vehicleResults != null && vehicleResults == 2) {
                                //成功。创建新订单。
                                creat = "创建";
                                //判断userid,图片是否为空
                                String userBId = messageCarEntity.getUserBId();
                                List<String> credentialsImgList = messageCarEntity.getCredentialsImgList();
                                if (StringUtils.isEmpty(userBId) || credentialsImgList.isEmpty()) {
                                    result.put("code", 407);
                                    result.put("message", "创建订单userbid和图片不能为空");
                                    return result;
                                }
                            }

                            String carid = addCarMessage.addCarMessage(messageCarEntity, type, messageId);
                            if (type == 1) {
                                String messageCarId = pushBillEntity.getMessageCarId();
                                if (StringUtils.isNotEmpty(messageCarId)) {
                                    String messageCarId1 = pushBillEntity.getMessageCarId();
                                    pushBillEntity.setMessageCarId(messageCarId1.concat(carid + ","));
                                } else {
                                    pushBillEntity.setMessageCarId(carid + ",");
                                }
                                if (accidentType != null) {
                                    pushBillEntity.setAccidentType(accidentType);
                                }
                                if (StringUtils.isNotEmpty(voice)) {
                                    pushBillEntity.setVoice(voice);
                                }
                                pushBillService.updateById(pushBillEntity);
                            } else {
                                if (StringUtils.isNotEmpty(appSendOutSheetEntity.getMessageCarId())) {
                                    String messageCarId = appSendOutSheetEntity.getMessageCarId();
                                    appSendOutSheetEntity.setMessageCarId(messageCarId.concat(carid + ","));
                                } else {
                                    appSendOutSheetEntity.setMessageCarId(carid + ",");
                                }
                                if (accidentType != null) {
                                    appSendOutSheetEntity.setAccidentType(accidentType);
                                }
                                if (StringUtils.isNotEmpty(voice)) {
                                    appSendOutSheetEntity.setVoice(voice);
                                }
                                appSendOutSheetService.updateById(appSendOutSheetEntity);
                            }

    //                        logger.error("###  查看参数 ###  creat={},indetnId={}", creat,messageCarEntity.getIndentId());
    //                        if (creat.equals("创建") &&  StringUtils.isEmpty(messageCarEntity.getIndentId())) {
    //                            logger.error("###  新订单开始创建订单 ###  creat={},indetnId={}", creat,messageCarEntity.getIndentId());
    //                            String indetnId = indentService.createNewIndent(messageCarEntity, userid);
    //                            messageCarEntity.setIndentId(indetnId);
    //                            appMessageCarService.updateById(messageCarEntity);
    //                        }
                            result.put("code", 200);
                            result.put("mesg", "成功");
                            result.put("data", carid);
                            return result;
                        } else {
                            //修改
                            AppMessageCarEntity appMessageCarEntity = addCarMessage.updateCarMessage(messageCarEntity, id.toString());
                            //  logger.error("###  查看参数 ###  indentId={},vehicleResults={}", messageCarEntity.getIndentId(),messageCarEntity.getVehicleResults());
                            if (appMessageCarEntity.getVehicleResults() != null && appMessageCarEntity.getVehicleResults() == 2 && StringUtils.isEmpty(appMessageCarEntity.getIndentId())) {
                                //成功。创建新订单。
                                //判断userid,图片是否为空
                                logger.error("###  修改开始创建订单 ###  indentId={},vehicleResults={}", messageCarEntity.getIndentId(), messageCarEntity.getVehicleResults());
                                String userBId = appMessageCarEntity.getUserBId();
                                List<String> credentialsImgList = messageCarEntity.getCredentialsImgList();
                                if (StringUtils.isNotEmpty(userBId) && !credentialsImgList.isEmpty()) {
                                    String indetnId = indentService.createNewIndent(appMessageCarEntity, userid, credentialsImgList);
                                    messageCarEntity.setIndentId(indetnId);
                                    appMessageCarService.updateById(messageCarEntity);
                                }
                            }
                            result.put("code", 200);
                            result.put("mesg", "成功");
                            result.put("data", id);
                            return result;
                        }
                    }
                }
            }
            result.put("code", 200);
            result.put("mesg", "成功");
            return result;
        }


        //车辆订单id


        //新增
        //关联数据
        //返回id
        //推修结果 修改订单状态

        //没有参数就为空


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


            AppMessageCarEntity appMessageCar = appMessageCarService.selectById(messageCarId);
            if (appMessageCar == null) {
                result.put("code", 407);
                result.put("msg", "未查询到车辆信息");
                return result;
            }
            //返回数据
            JSONObject object = new JSONObject();
            object.put("licensePlate", appMessageCar.getLicensePlate());
            object.put("brandId", appMessageCar.getBrandId());
            object.put("localInsurance", appMessageCar.getLocalInsurance());
            object.put("customerName", appMessageCar.getCustomerName());
            object.put("phone", appMessageCar.getPhone());
            object.put("casualties", appMessageCar.getCasualties());
            object.put("financialLoss", appMessageCar.getFinancialLoss());
            object.put("buyCar", appMessageCar.getBuyCar());
            object.put("channelsIns", appMessageCar.getChannelsIns());
            object.put("leaveMessage", appMessageCar.getLeaveMessage());
            object.put("vehicleResults", appMessageCar.getVehicleResults());
            //查询商户名称
            String userId = appMessageCar.getUserBId();
            if (StringUtils.isNotEmpty(userId)) {
                if (userId.endsWith("Z")) {
                    //upmerchants商户
                    userId = userId.substring(0, userId.length() - 1);
                    //修改已读未读
                    AppUpMerchantsEntity appUpMerchantsEntity = appUpMerchantsService.selectById(userId);
                    object.put("merchantsName", appUpMerchantsEntity.getName());
                } else {
                    BUserEntity bUserEntity = bUserService.selectById(userBId);
                    if (bUserEntity == null) {
                        result.put("code", 407);
                        result.put("msg", "未查询到该商户");
                        return result;
                    }
                    object.put("merchantsName", bUserEntity.getMerchantsName());

                }
            }


            //三组图片
    //        EntityWrapper<AppMessageUrlEntity> wrapper1 = new EntityWrapper<>();
    //        wrapper1.eq("type", 1)
    //                .eq("message_id", appMessageCar.getId());
    //        List<AppMessageUrlEntity> vehiclePhotoImgList = appMessageUrlService.selectList(wrapper1);
    //        object.put("vehiclePhotoImgList", vehiclePhotoImgList);
    //
    //        EntityWrapper<AppMessageUrlEntity> wrapper2 = new EntityWrapper<>();
    //        wrapper2.eq("type", 2)
    //                .eq("message_id", appMessageCar.getId());
    //        List<AppMessageUrlEntity> claimPhotoImgList = appMessageUrlService.selectList(wrapper2);
    //        object.put("claimPhotoImgList", claimPhotoImgList);

            EntityWrapper<AppMessageUrlEntity> wrapper3 = new EntityWrapper<>();
            wrapper3.eq("type", 3)
                    .eq("message_id", appMessageCar.getId());
            List<AppMessageUrlEntity> credentialsImgList = appMessageUrlService.selectList(wrapper3);
            object.put("credentialsImgList", credentialsImgList);

    //
    //        EntityWrapper<AppMessageUrlEntity> wrapper4 = new EntityWrapper<>();
    //        wrapper4.eq("type", 4)
    //                .eq("message_id", appMessageCar.getId());
    //        List<AppMessageUrlEntity> mainCarLicense = appMessageUrlService.selectList(wrapper4);
    //        object.put("mainCarLicense", mainCarLicense);
    //
    //
    //        EntityWrapper<AppMessageUrlEntity> wrapper5 = new EntityWrapper<>();
    //        wrapper5.eq("type", 5)
    //                .eq("message_id", appMessageCar.getId());
    //        List<AppMessageUrlEntity> insuredList = appMessageUrlService.selectList(wrapper5);
    //        object.put("insuredList", insuredList);
    //
    //
            EntityWrapper<AppPushMessEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("user_b_id", userBId)
                    .eq("message_car_id", messageCarId);
            AppPushMessEntity appPushMess = appPushMessService.selectOne(wrapper);
            if (appPushMess != null) {
                object.put("isPushMer", 1);
            } else {
                object.put("isPushMer", 0);
            }

            result.put("code", 200);
            result.put("data", object);
            return result;
        }


        //品牌列表
        @ApiOperation(value = "查询所有品牌和推荐")
        @RequestMapping(value = "/allBrandAndRecommd", method = RequestMethod.GET)
        public JSONObject allBrandAndRecommd() {
            JSONObject result = new JSONObject();

            String s = stringRedisTemplate.opsForValue().get(RedisConstant.SYS_ALL_BRAND);
            String s1 = stringRedisTemplate.opsForValue().get(RedisConstant.SYS_PUSH_BRAND);
            if (StringUtils.isNotEmpty(s) && StringUtils.isNotEmpty(s1)) {

                JSONObject object = new JSONObject();
                JSONArray objects = JSONArray.parseArray(s);
                JSONArray objects1 = JSONArray.parseArray(s1);
                object.put("allBrand", objects);
                object.put("pushBrand", objects1);

                result.put("code", 200);
                result.put("data", object);
                return result;
            }

            JSONObject object = new JSONObject();

            EntityWrapper<AppCarBrandEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("status", ConsEnum.CAR_BRAND_OK.getCode());
            wrapper.orderBy("initials");
            List<AppCarBrandEntity> appCarBrandEntities = appCarBrandEntityMapper.selectList(wrapper);
            stringRedisTemplate.opsForValue().set(RedisConstant.SYS_ALL_BRAND, JSONObject.toJSONString(appCarBrandEntities));
            object.put("allBrand", appCarBrandEntities);


            EntityWrapper<AppCarBrandEntity> wrapper1 = new EntityWrapper<>();
            wrapper1.eq("status", ConsEnum.CAR_BRAND_OK.getCode())
                    .eq("is_hot", 1)
                    .orderBy("initials");
            List<AppCarBrandEntity> appCarBrandEntities1 = appCarBrandEntityMapper.selectList(wrapper1);
            stringRedisTemplate.opsForValue().set(RedisConstant.SYS_PUSH_BRAND, JSONObject.toJSONString(appCarBrandEntities1), 60 * 5, TimeUnit.SECONDS);

            object.put("pushBrand", appCarBrandEntities1);


            result.put("code", 200);
            result.put("data", object);
            return result;
        }


        //推送商户
        @ApiOperation(value = "推送商户" +
                "407 数据有误，" +
                "408 已经推送给商户")
        @ApiImplicitParams({
                @ApiImplicitParam(paramType = "query", name = "messageCarId", value = "信息id", required = true, dataType = "Integer"),
                @ApiImplicitParam(paramType = "query", name = "userBId", value = "商户Id", required = true, dataType = "Integer"),
                @ApiImplicitParam(paramType = "query", name = "voice", value = "录音", required = false, dataType = "String"),
        })
        @RequestMapping(value = "/pushMerchants", method = RequestMethod.GET)
        public JSONObject pushMerchants(HttpServletRequest request, Integer messageCarId, Integer userBId, String voice) {
            JSONObject result = new JSONObject();
            TokenPojo currentLoginUser = getCurrentLoginUser(request);
            if (currentLoginUser == null) {
                result.put("code", 530);
                result.put("msg", "用户未登录");
                return result;
            }

            //新增数据
            if (messageCarId == null) {
                result.put("code", 407);
                result.put("msg", "messageCarId为空");
                return result;
            }

            if (userBId == null) {
                result.put("code", 407);
                result.put("msg", "userBId为空");
                return result;
            }

            AppMessageCarEntity appMessageCarEntity = appMessageCarService.selectById(messageCarId);
            if (appMessageCarEntity == null) {
                result.put("code", 407);
                result.put("msg", "未查询到车辆订单数据");
                return result;
            }

            BUserEntity bUserEntity = bUserService.selectById(userBId);
            if (bUserEntity == null) {
                result.put("code", 407);
                result.put("msg", "未查询到商户数据");
                return result;
            }


            EntityWrapper<AppPushMessEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("user_b_id", userBId)
                    .eq("message_car_id", messageCarId);
            AppPushMessEntity appPushMess = appPushMessService.selectOne(wrapper);
            if (appPushMess != null) {
                result.put("code", 408);
                result.put("msg", "已经推送给商户，请勿重复推送");
                return result;
            }

    //        appMessageCarEntity.setUserBId(userBId.toString());
    //        appMessageCarService.updateById(appMessageCarEntity);


            //新增数据
            AppPushMessEntity appPushMessEntity = new AppPushMessEntity();
            appPushMessEntity.setVoice(voice);
            appPushMessEntity.setUserBId(userBId);
            appPushMessEntity.setMessageCarId(messageCarId);
            appPushMessEntity.setCreateTime(new Date());
            appPushMessEntity.setUpdateTime(new Date());
            appPushMessService.insert(appPushMessEntity); //了事

            //推送商户
            result.put("code", 200);
            result.put("msg", "成功");
            return result;

    }


    //判断是否又权限进入信息页面
    @ApiOperation(value = "是否能进入信息页面")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/isPutMessage", method = RequestMethod.GET)
    public JSONObject isPutMessage(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
//        UserEntity userEntity = userService.selectById(id);
//        String username = userEntity.getUsername();
//        String s = bUserService.selectByUsername(username);
//        if (StringUtils.isEmpty(s) && id != 56) {
//            result.put("code", 408);
//            result.put("msg", "暂无b端账号，请注册");
//            return result;
//        }
        EntityWrapper<AppClaimTeacherEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", id);
        AppClaimTeacherEntity appClaimTeacher = appClaimTeacherService.selectOne(wrapper);
        if (appClaimTeacher == null) {
            result.put("code", 408);
            result.put("msg", "请联系管理员");
            return result;
        }
        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }


    @ApiOperation(value = "时实更新位置")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "经度", value = "lng", required = true, dataType = "Bigdecimal"),
            @ApiImplicitParam(paramType = "query", name = "纬度", value = "lat", required = true, dataType = "Bigdecimal"),
            @ApiImplicitParam(paramType = "query", name = "定位参数", value = "isLocation", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/updateLocatioin", method = RequestMethod.GET)
    public JSONObject updateLocatioin(HttpServletRequest request, BigDecimal lng, BigDecimal lat, Integer
            isLocation) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        UserEntity userEntity = userService.selectById(id);
        //查询是否添加到了理赔老师表
        EntityWrapper<AppClaimTeacherEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", id);
        AppClaimTeacherEntity appClaimTeacher = appClaimTeacherService.selectOne(wrapper);
        //判断理赔老师才能进入。必不为空
        if (isLocation == 1) {
            userEntity.setIsLocation(1);
            JSONObject in = new JSONObject();
            in.put("id", id);
            in.put("merchantsName", userEntity.getName());
            in.put("lat", lat);
            in.put("lng", lng);
            String add = getAdd(lng.toString(), lat.toString());
            in.put("address", add);

            // 1.理赔老师，2.4s店，3.修理厂
            if (appClaimTeacher.getType() == 1) {
                in.put("type", 7);
                if (appClaimTeacher.getCurrentPosition() != null) {
                    try {
                        stringRedisTemplate.opsForGeo().remove(RedisConstant.CLAIM_TEACHER_ADD, appClaimTeacher.getCurrentPosition());
                    } catch (Exception e) {
                        result.put("code", 200);
                        result.put("msg", "成功");
                        return result;
                    }
                    //添加
                    stringRedisTemplate.opsForGeo().add(RedisConstant.CLAIM_TEACHER_ADD, new Point(lng.doubleValue(), lat.doubleValue()), in.toJSONString());
                }
                appClaimTeacher.setCurrentPosition(in.toJSONString());
                appClaimTeacherService.updateById(appClaimTeacher);
            } else if (appClaimTeacher.getType() == 2) {
                //先删再加
                String brand = appClaimTeacher.getBrand();
                if (StringUtils.isNotEmpty(brand)) {
                    in.put("type", brand);
                    if (appClaimTeacher.getCurrentPosition() != null) {
                        try {
                            stringRedisTemplate.opsForGeo().remove(RedisConstant.MERCHANTS_GEO + brand, appClaimTeacher.getCurrentPosition());
                        } catch (Exception e) {
                            result.put("code", 200);
                            result.put("msg", "成功");
                            return result;
                        }
                    }
                    //添加
                    stringRedisTemplate.opsForGeo().add(RedisConstant.MERCHANTS_GEO + brand, new Point(lng.doubleValue(), lat.doubleValue()), in.toJSONString());
                }
                appClaimTeacher.setCurrentPosition(in.toJSONString());
                appClaimTeacherService.updateById(appClaimTeacher);
            } else if (appClaimTeacher.getType() == 4) {
                //运营商
                in.put("type", 4);
                if (appClaimTeacher.getCurrentPosition() != null) {
                    try {
                        stringRedisTemplate.opsForGeo().remove(RedisConstant.CLAIM_TEACHER_MER, appClaimTeacher.getCurrentPosition());
                    } catch (Exception e) {
                        result.put("code", 200);
                        result.put("msg", "成功");
                        return result;
                    }
                }
                //添加
                stringRedisTemplate.opsForGeo().add(RedisConstant.CLAIM_TEACHER_MER, new Point(lng.doubleValue(), lat.doubleValue()), in.toJSONString());
                appClaimTeacher.setCurrentPosition(in.toJSONString());
                appClaimTeacherService.updateById(appClaimTeacher);
            } else {
                //修理厂
                in.put("type", 1);
                //先删再加
                if (appClaimTeacher.getCurrentPosition() != null) {
                    try {
                        stringRedisTemplate.opsForGeo().remove(RedisConstant.MERCHANTS_GEO + 2, appClaimTeacher.getCurrentPosition());
                    } catch (Exception e) {
                        result.put("code", 200);
                        result.put("msg", "成功");
                        return result;
                    }
                }
                //添加
                stringRedisTemplate.opsForGeo().add(RedisConstant.MERCHANTS_GEO + 2, new Point(lng.doubleValue(), lat.doubleValue()), in.toJSONString());
            }
            appClaimTeacher.setCurrentPosition(in.toJSONString());
            appClaimTeacherService.updateById(appClaimTeacher);
        } else {
            // 1.理赔老师，2.4s店，3，修理厂
            if (appClaimTeacher.getType() == 1) {
                stringRedisTemplate.opsForGeo().remove(RedisConstant.CLAIM_TEACHER_ADD, appClaimTeacher.getCurrentPosition());
                userEntity.setIsLocation(0);
            } else if (appClaimTeacher.getType() == 2) {
                String brand = appClaimTeacher.getBrand();
                stringRedisTemplate.opsForGeo().remove(RedisConstant.MERCHANTS_GEO + brand, appClaimTeacher.getCurrentPosition());
                userEntity.setIsLocation(0);
            } else if (appClaimTeacher.getType() == 4) {
                stringRedisTemplate.opsForGeo().remove(RedisConstant.CLAIM_TEACHER_MER, appClaimTeacher.getCurrentPosition());
                userEntity.setIsLocation(0);
            } else {
                stringRedisTemplate.opsForGeo().remove(RedisConstant.MERCHANTS_GEO + 2, appClaimTeacher.getCurrentPosition());
                userEntity.setIsLocation(0);
            }
        }
        userService.updateById(userEntity);

//        String username = userEntity.getUsername();
//        String s = bUserService.selectByUsername(username);
//        //查询信息
//        BUserEntity bUserEntity = bUserService.selectById(s);
//
//        List<String> brand = bUserService.findBrand(s);
//
//
//        if (isLocation != null) {
//            if (isLocation == 1) {
//                userEntity.setIsLocation(1);
//                JSONObject in = new JSONObject();
//                in.put("id", bUserEntity.getId());
//                in.put("merchantsName", bUserEntity.getMerchantsName());
//                in.put("lat", lat);
//                in.put("lng", lng);
//                in.put("address", bUserEntity.getAddress());
//                in.put("type", bUserEntity.getType());
//                in.put("brandList", brand);
//
//                //先删再加
//                if (bUserEntity.getRealLocation() != null) {
//                    stringRedisTemplate.opsForGeo().remove(RedisConstant.MERCHANTS_GEO_REA + s, bUserEntity.getRealLocation());
//                }
//                //添加
//                stringRedisTemplate.opsForGeo().add(RedisConstant.MERCHANTS_GEO_REA + s, new Point(lng.doubleValue(), lat.doubleValue()), in.toJSONString());
//                bUserEntity.setRealLocation(in.toJSONString());
//                bUserService.updateById(bUserEntity);
//            } else {
//                userEntity.setIsLocation(0);
//            }
//            userService.updateById(userEntity);
//        }

        result.put("code", 200);
        result.put("msg", "成功");
        return result;

    }


    public String getAdd(String lng, String lat) {
        //lat 小  log  大
        //参数解释: 纬度,经度 采用高德API可参考高德文档https://lbs.amap.com/
        String urlString = "https://restapi.amap.com/v3/geocode/regeo?location=" + lat + "," + lng + "&extensions=base&batch=false&roadlevel=0&key=" + key;
        String res = "";
        try {
            URL url = new URL(urlString);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                res += line + "\n";
            }
            in.close();
            //解析结果
            JSONObject jsonObject = JSONObject.parseObject(res);
            //logger.info(jsonObject.toJSONString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("regeocode");
            res = jsonObject1.getString("formatted_address");
        } catch (Exception e) {
            logger.error("获取地址信息异常{}", e.getMessage());
            return null;
        }
        return res;
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
        Integer id = currentLoginUser.getAppUserEntity().getId();

        appSendOutSheetService.addAccidentSer(in, id);

        try {
            AccidentRecordEntityService.send();
        } catch (Exception e) {
            logger.error("往管理端发送长连接通知失败！# error:{}", e.getStackTrace());
        }
        result.put("code", 200);
        result.put("msg", "成功");
        return result;

    }


    //查询商户接口
    @ApiOperation(value = "查询商户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "brandId", value = "品牌id", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/findPushMerchants", method = RequestMethod.GET)
    public JSONObject findPushMerchants(String brandId) {
        //
        JSONObject result = new JSONObject();
        if (StringUtils.isEmpty(brandId)) {
            result.put("code", 407);
            result.put("msg", "brandId为空");
            return result;
        }
        List<ListMessage> listByStores = bUserService.findListByfoursStoresBrand(Integer.valueOf("028"), brandId);

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", listByStores);
        return result;

    }


}
