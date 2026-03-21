package com.cheji.b.modular.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.b.modular.domain.*;
import com.cheji.b.modular.dto.CheckImgList;
import com.cheji.b.modular.dto.Voice;
import com.cheji.b.modular.mapper.AppMessageUrlMapper;
import com.cheji.b.modular.mapper.CdImgMapper;
import com.cheji.b.utils.MapNavUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 车店图片表
 * 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2022-01-12
 */
@Service
public class CdImgService extends ServiceImpl<CdImgMapper, CdImgEntity> implements IService<CdImgEntity> {

    private static Logger logger = LoggerFactory.getLogger(CdImgService.class);


    @Resource
    private AccidentRecordService accidentRecordService;

    @Resource
    private BizAccidentService bizAccidentService;

    @Resource
    private UserService userService;

    @Resource
    private PushBillService pushBillService;

    @Resource
    private CdImgMapper cdImgMapper;

    @Resource
    private AppMessageUrlService appMessageUrlService;

    @Resource
    private AppSendUrlService appSendUrlService;

    @Resource
    private AppMessageCarService appMessageCarService;

    @Resource
    private CarBrandService carBrandService;

    @Resource
    private AppMessageUrlMapper appMessageUrlMapper;

    @Resource
    private IndentService indentService;

    @Resource
    private AppUpMerchantsService appUpMerchantsService;

    @Resource
    private AppPushMessService appPushMessService;

    @Resource
    private CUserService cUserService;

    private static final String key = "a11e3020a4b82ce9390044286910f02f";

    public  JSONObject findSendSheentList(Integer userId, Integer state, Integer pagesize) {

        pagesize = (pagesize - 1) * 20;
        //先查询数据
        JSONObject object = new JSONObject();
        //等待服务，电话联系，现场签到，录音音频
        //再次跟踪，同意到店，拒绝回店，服务评价
        //1,待服务单 ，派单之后没有人点击接受的订单
        //teackState  = 1
        Integer waitPb = cdImgMapper.findWaitPB(userId);
        object.put("waitPb", waitPb);

        //2,电话联系，电话不为空的订单
        Integer findPhone = cdImgMapper.findPhone(userId);
        object.put("findPhone", findPhone);

        //3.现场签到，签到时间不为空的订单
        Integer noCheckTime = cdImgMapper.findCheckTime(userId);
        object.put("haCheckTime", noCheckTime);

        //4.录音音频，录音音频不为空的订单
        Integer haveVioce = cdImgMapper.findHaveVioce(userId);
        object.put("haveVioce", haveVioce);

        //5,再次跟踪  订单状态修改为再次跟踪的订单
        Integer again = appMessageCarService.findAgain(userId);
        object.put("again", again);

        //6,同意到店
        Integer agree = appMessageCarService.findAgree(userId);
        object.put("agree", agree);

        //7. 拒绝回店
        Integer refused = appMessageCarService.findRefused(userId);
        object.put("refused", refused);

        //8,服务评价，所有填写了最终状态的订单
        Integer finish = appMessageCarService.findFinish(userId);
        object.put("finish", finish);

        //查询列表数据
        List<AccidentRecord> accident = cdImgMapper.findAccidentList(userId, state, pagesize);

        if (accident.isEmpty()) {
            object.put("list", accident);
            return object;
        }

        //查询数据  品牌和图片  理赔老师 图片
        JSONArray array = new JSONArray();
        for (AccidentRecord accidentRecordEntity : accident) {
            JSONObject lis = new JSONObject();
            lis.put("id", accidentRecordEntity.getId());

            if (StringUtils.isEmpty(accidentRecordEntity.getCid())) {
                lis.put("cid", "");
            } else {
                lis.put("cid", accidentRecordEntity.getCid());
            }

            if (StringUtils.isEmpty(accidentRecordEntity.getPlate())) {
                lis.put("plate", "暂无");
            } else {
                lis.put("plate", accidentRecordEntity.getPlate());
            }

            if (StringUtils.isEmpty(accidentRecordEntity.getName())) {
                lis.put("name", "暂无");
            } else {
                lis.put("name", accidentRecordEntity.getName());
            }

            if (StringUtils.isEmpty(accidentRecordEntity.getPhone())) {
                lis.put("phone", "暂无");
            } else {
                String phone = accidentRecordEntity.getPhone();
                lis.put("phone", phone);
            }

            if (StringUtils.isEmpty(accidentRecordEntity.getCompany())) {
                lis.put("company", "暂无");
            } else {
                lis.put("company", accidentRecordEntity.getCompany());
            }

            if (StringUtils.isEmpty(accidentRecordEntity.getRemark())) {
                lis.put("remark", "暂无");
            } else {
                lis.put("remark", accidentRecordEntity.getRemark());
            }

            if (StringUtils.isEmpty(accidentRecordEntity.getLossVehicle())) {
                lis.put("lossVehicle", "暂无");
            } else {
                lis.put("lossVehicle", accidentRecordEntity.getLossVehicle());
            }

            if (StringUtils.isEmpty(accidentRecordEntity.getPbid())) {
                lis.put("serviceName", "暂无");
            } else {
                String pbid = accidentRecordEntity.getPbid();
                PushBillEntity pushBillEntity = pushBillService.selectById(pbid);
                Long userId1 = pushBillEntity.getUserId();
                CUserEntity userEntity = cUserService.selectById(userId1);
                lis.put("serviceName", userEntity.getName());
            }

            if (StringUtils.isEmpty(accidentRecordEntity.getBrand())) {
                lis.put("brand", "暂无");
                lis.put("brandImg", "");
            } else {
                String brandId = accidentRecordEntity.getBrand();
                if (StringUtils.isEmpty(brandId)) {
                    lis.put("brand", "暂无");
                    lis.put("brandImg", "");
                } else {
                    CarBrandEntity carBrandEntity = carBrandService.selectById(brandId);
                    lis.put("brand", carBrandEntity.getName());
                    lis.put("brandImg", carBrandEntity.getPicUrl());
                }
            }

            //查询图片
            String[] arr = appSendUrlService.findAccidentImg(accidentRecordEntity.getId());
            lis.put("imgList", arr);
            array.add(lis);
        }
        object.put("list", array);
        return object;
    }

    public List<String> selectByInAndTy(Integer indentId, int i) {
        return cdImgMapper.selectByInAdnTy(indentId, i);
    }


    public JSONObject findMessageDetails(JSONObject result, String messageId, Integer type, String id) {
        JSONObject messageDetails = new JSONObject();
        String messageCarId;
        //查询到详情
        //创建时间，地点，经纬度，备注，图片或者视频，理赔老师，签到时间，现场照片
        //1 pb  2 sos
        PushBillEntity pushBillEntity = new PushBillEntity();
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
            BizAccident bizAccidentEntity = bizAccidentService.findById(pushBillEntity.getAccid());
            messageDetails.put("address", bizAccidentEntity.getAddress());
            messageDetails.put("lng", bizAccidentEntity.getLng());
            messageDetails.put("lat", bizAccidentEntity.getLat());
            messageDetails.put("remark", "车辆事故");
            //视频
            messageDetails.put("url", bizAccidentEntity.getThumbnailUrl());
            messageDetails.put("videoUrl", bizAccidentEntity.getVideo());
            String openid = bizAccidentEntity.getOpenid();
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


        messageDetails.put("taskSource", "公司信息");

        AppUserEntity userEntity = userService.selectById(id);
        messageDetails.put("claimsConsultant", userEntity.getName());

        messageDetails.put("merchants", new JSONArray());


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
                    CarBrandEntity appCarBrandEntity = carBrandService.selectById(appMessageCar.getBrandId());
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

//                String userId = appMessageCar.getUserBId();
//                if (StringUtils.isNotEmpty(userId)) {
//                    if (userId.endsWith("Z")) {
//                        //upmerchants商户
//                        userId = userId.substring(0, userId.length() - 1);
//                        //修改已读未读
//                        AppUpMerchantsEntity appUpMerchantsEntity = appUpMerchantsService.selectById(userId);
//                        object.put("merchantsName", appUpMerchantsEntity.getName());
//                        object.put("userBId", "150");
//                    } else {
//                        BUserEntity bUserEntity = bUserService.selectById(userId);
//                        if (bUserEntity == null) {
//                            result.put("code", 407);
//                            result.put("msg", "未查询到该商户");
//                            return result;
//                        }
//                        object.put("merchantsName", bUserEntity.getMerchantsName());
//                        object.put("userBId", userId);
//                    }
//                } else {
//                    object.put("merchantsName", "暂无");
//                }

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

    public JSONObject saveCarMess(JSONObject result, JSONObject in, String userid) {
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

                        PushBillEntity pushBillEntity1 = new PushBillEntity();
                        AppSendOutSheetEntity appSendOutSheetEntity = new AppSendOutSheetEntity();
                        if (type == 1) {
                            pushBillEntity1 = pushBillService.selectById(messageId);
                            if (pushBillEntity1 == null) {
                                result.put("code", 407);
                                result.put("message", "未查询到信息");
                                return result;
                            }
                        } else {
                            if (appSendOutSheetEntity == null) {
                                result.put("code", 407);
                                result.put("message", "未查询到信息");
                                return result;
                            }
                        }
                        Integer vehicleResults = in.getInteger("vehicleResults");
                        if (vehicleResults != null && type == 1) {
                            pushBillEntity1.setTrackState(3);
                        } else if (vehicleResults != null && type == 2) {

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

                        String carid = appMessageCarService.addCarMessage(messageCarEntity, type, messageId);
                        if (type == 1) {
                            String messageCarId = pushBillEntity1.getMessageCarId();
                            if (StringUtils.isNotEmpty(messageCarId)) {
                                String messageCarId1 = pushBillEntity1.getMessageCarId();
                                pushBillEntity1.setMessageCarId(messageCarId1.concat(carid + ","));
                            } else {
                                pushBillEntity1.setMessageCarId(carid + ",");
                            }
                            if (accidentType != null) {
                                pushBillEntity1.setAccidentType(accidentType);
                            }
                            if (StringUtils.isNotEmpty(voice)) {
                                pushBillEntity1.setVoice(voice);
                            }
                            pushBillService.updateById(pushBillEntity1);
                        }

                        result.put("code", 200);
                        result.put("mesg", "成功");
                        result.put("data", carid);
                        return result;
                    } else {
                        //修改
                        AppMessageCarEntity appMessageCarEntity = appMessageCarService.updateCarMessage(messageCarEntity, id.toString());
                        //  logger.error("###  查看参数 ###  indentId={},vehicleResults={}", messageCarEntity.getIndentId(),messageCarEntity.getVehicleResults());
                        if (appMessageCarEntity.getVehicleResults() != null && appMessageCarEntity.getVehicleResults() == 2 && StringUtils.isEmpty(appMessageCarEntity.getIndentId())) {
                            //成功。创建新订单。
                            //判断userid,图片是否为空
                            logger.error("###  修改开始创建订单 ###  indentId={},vehicleResults={}", messageCarEntity.getIndentId(), messageCarEntity.getVehicleResults());
                            String userBId = appMessageCarEntity.getUserBId();
                            List<String> credentialsImgList = messageCarEntity.getCredentialsImgList();
                            if (StringUtils.isNotEmpty(userBId) && !credentialsImgList.isEmpty()) {
                                String indetnId = indentService.createNewIndent(appMessageCarEntity, Integer.valueOf(userid), credentialsImgList);
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

    public JSONObject checkSign(JSONObject result, JSONObject in) {
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
                AccidentRecord accidentApp = accidentRecordService.selectById(pushBillEntity.getAccid());
                inlng = accidentApp.getLng();
                inlat = accidentApp.getLat();
            } else {
                BizAccident bizAccidentWx = bizAccidentService.findById(pushBillEntity.getAccid());
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

        }

        result.put("code", 200);
        result.put("message", "成功");
        result.put("checkTime", time);
        return result;
    }

    public JSONObject findMessageCar(JSONObject result,Integer messageCarId, Integer userBId) {
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
                AppUserEntity bUserEntity = userService.selectById(userBId);
                if (bUserEntity == null) {
                    result.put("code", 407);
                    result.put("msg", "未查询到该商户");
                    return result;
                }
                object.put("merchantsName", bUserEntity.getMerchantsName());

            }
        }


        EntityWrapper<AppMessageUrlEntity> wrapper3 = new EntityWrapper<>();
        wrapper3.eq("type", 3)
                .eq("message_id", appMessageCar.getId());
        List<AppMessageUrlEntity> credentialsImgList = appMessageUrlService.selectList(wrapper3);
        object.put("credentialsImgList", credentialsImgList);
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

    public void addAccidentSer(JSONObject in, Integer id) {
        BigDecimal lng = in.getBigDecimal("lng");
        BigDecimal lat = in.getBigDecimal("lat");
        String address = in.getString("address");
        String plate = in.getString("plate");
        String name = in.getString("name");
        String phone = in.getString("phone");
        String company = in.getString("company");
        String remark = in.getString("remark");
        String brand = in.getString("brand");
//        AppSendOutSheetEntity appSendOutSheet = new AppSendOutSheetEntity();
//        appSendOutSheet.setRemark(remark);
//        appSendOutSheet.setUserId(id);
//        appSendOutSheet.setLng(lng);
//        appSendOutSheet.setLat(lat);
//        appSendOutSheet.setAdress(address);
//        appSendOutSheet.setPlateNumber(plate);
//        appSendOutSheet.setName(name);
//        appSendOutSheet.setPhone(phone);
//        appSendOutSheet.setCompany(company);
//        appSendOutSheet.setCreateTime(new Date());
//        appSendOutSheet.setUpdateTime(new Date());
//        appSendOutSheetMapper.insert(appSendOutSheet);
        AccidentRecord appAccidentRecord = new AccidentRecord();
        //添加一个默认图片
//        https://obs-4f4a.obs.cn-southwest-2.myhuaweicloud.com/img/4bb8e7002e4ca33cea8863ebeba7183.png
        appAccidentRecord.setLng(lng);
        appAccidentRecord.setLat(lat);
        appAccidentRecord.setName(name);
        appAccidentRecord.setPlate(plate);
        appAccidentRecord.setPhone(phone);
        appAccidentRecord.setCompany(company);
        appAccidentRecord.setRemark(remark);
        appAccidentRecord.setBrand(brand);
        appAccidentRecord.setCheckid("调度");
        appAccidentRecord.setStatuse(2);
        appAccidentRecord.setAddress(address);
        appAccidentRecord.setType(1);
        appAccidentRecord.setIsaddvideo(0);
        appAccidentRecord.setDelgeo("1");
        appAccidentRecord.setCreateTime(new Date());
        appAccidentRecord.setUpdateTime(new Date());
        appAccidentRecord.setCheckTime(new Date());
        appAccidentRecord.setUserId(id.toString());
        accidentRecordService.insert(appAccidentRecord);


        JSONObject json = JSONObject.parseObject(String.valueOf(in));
        //车损照片
        JSONArray urlList = (JSONArray) json.get("urlList");
        if (urlList != null) {
            List<String> vehicleList = JSONArray.parseArray(urlList.toString(), String.class);
            String s = vehicleList.get(0);
            if (StringUtils.isNotEmpty(s)) {
                appAccidentRecord.setImgUrl(s);
            } else {
                appAccidentRecord.setImgUrl("https://obs-4f4a.obs.cn-southwest-2.myhuaweicloud.com/img/4bb8e7002e4ca33cea8863ebeba7183.png");
            }
            accidentRecordService.updateById(appAccidentRecord);
            for (int i = 0; i < vehicleList.size(); i++) {
                AppSendUrlEntity appSendUrlEntity = new AppSendUrlEntity();
                appSendUrlEntity.setIndex(i + 1);
                appSendUrlEntity.setSource(2);
                appSendUrlEntity.setSendId(appAccidentRecord.getId().toString());
                appSendUrlEntity.setUrl(vehicleList.get(i));
                appSendUrlEntity.setType(1);
                appSendUrlEntity.setCreateTime(new Date());
                appSendUrlEntity.setUpdateTime(new Date());
                appSendUrlService.insert(appSendUrlEntity);
            }
        }
    }

    public JSONObject findAccidentDetails(Integer id, String cid) {
        //事故id
        JSONObject result = new JSONObject();
        //根据事故id查询到具体数据
        AccidentRecord accident = accidentRecordService.selectById(id);
        if (StringUtils.isEmpty(accident.getPlate())) {
            result.put("plate", "暂无");
        } else {
            result.put("plate", accident.getPlate());
        }


        if (StringUtils.isEmpty(accident.getName())) {
            result.put("name", "暂无");
        } else {
            result.put("name", accident.getName());
        }

        if (StringUtils.isEmpty(accident.getPhone())) {
            result.put("customerPhone", "暂无");
        } else {
            result.put("customerPhone", accident.getPhone());
        }

        if (StringUtils.isEmpty(accident.getLossVehicle())) {
            result.put("lossVehicle", "暂无");
        } else {
            result.put("lossVehicle", accident.getLossVehicle());
        }

        if (StringUtils.isEmpty(accident.getAddress())) {
            result.put("address", "暂无");
        } else {
            result.put("address", accident.getAddress());
        }

        result.put("lng", accident.getLng());
        result.put("lat", accident.getLat());
        //事故照片
        String[] arr = appSendUrlService.findAccidentImg(id);
        result.put("accidentImgList", arr);

        //单位 备注
        if (StringUtils.isEmpty(accident.getCompany())) {
            result.put("company", "个人");
        } else {
            result.put("company", accident.getCompany());
        }

        if (StringUtils.isEmpty(accident.getRemark())) {
            result.put("remark", "暂无");
        } else {
            result.put("remark", accident.getRemark());
        }


        if (StringUtils.isEmpty(accident.getBrand())) {
            result.put("brand", "暂无");
            result.put("brandImg", "");
        } else {
            CarBrandEntity carBrandEntity = carBrandService.selectById(accident.getBrand());
            result.put("brandImg", carBrandEntity.getPicUrl());
            result.put("brand", accident.getName());
        }

        //服务顾问
        //根据accid查询pbid
        PushBillEntity pbList = cdImgMapper.findPbList(id);
        if (pbList == null) {
            result.put("serviceName", "暂无");
            result.put("servicePhone", "暂无");
            result.put("checkTime", "暂无");
            result.put("checkAddress", "暂无");
            result.put("checkImg", "");
            String[] a = new String[0];
            result.put("nowImg", a);
            List<Voice> voices = new ArrayList<>();
            result.put("nowVoice", voices);
        } else {
            Long userId = pbList.getUserId();
            CUserEntity userEntity = cUserService.selectById(userId);
            result.put("serviceName", userEntity.getName());
            result.put("servicePhone", userEntity.getUsername());
            if (pbList.getCheckTime() == null) {
                result.put("checkTime", "暂无");
                result.put("checkAddress", "暂无");
            } else {
                Date checkTime = pbList.getCheckTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String endTime = format.format(checkTime);
                result.put("checkTime", endTime);
                result.put("checkAddress", pbList.getCheckAddress());
            }
            //查询签到照片
            String url = appSendUrlService.findCheckImg(pbList.getId());
            result.put("checkImg", url);


            //现场照片
            List<String> stringList = appMessageUrlService.selectScenePhotos(pbList.getId(), 1);
            stringList.removeAll(Collections.singleton(null));
            result.put("nowImg", stringList);


            List<Voice> strings = new ArrayList<>();

            Voice voice1 = new Voice();
            String time;
            String voice;
            if (StringUtils.isNotEmpty(pbList.getVoice())) {
                voice = pbList.getVoice();
                voice1.setVoice(voice);
                Date checkTime = pbList.getCheckTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                time = format.format(checkTime);
                voice1.setVoiceTime(time);
                strings.add(voice1);
                result.put("nowVoice",strings);
            }else {
                result.put("nowVoice",strings);
            }

        }
        List<Voice> voices = new ArrayList<>();
        result.put("phoneVoice", voices);

        if (StringUtils.isNotEmpty(cid)) {
            AppMessageCarEntity appMessageCar = appMessageCarService.selectById(cid);
            Integer vehicleResults = appMessageCar.getVehicleResults();
            if (vehicleResults == null) {
                result.put("vehicleResult", "暂无");
            } else {
                if (vehicleResults == 1) {
                    result.put("vehicleResult", "再次跟踪");
                } else if (vehicleResults == 2) {
                    result.put("vehicleResult", "拒绝到店");
                } else if (vehicleResults == 3) {
                    result.put("vehicleResult", "同意到店");
                } else if (vehicleResults == 4) {
                    result.put("vehicleResult", "推修成功");
                }
            }

            String leaveMessage = appMessageCar.getLeaveMessage();
            result.put("leaveMessage", leaveMessage);

            //推修结果图片
            List<String> list = appMessageUrlService.selectUrlPhotos(Integer.valueOf(cid), 3, 1);
            list.removeAll(Collections.singleton(null));
            result.put("credentialsImg", list);

            //评价
            Integer source = appMessageCar.getSource();
            result.put("source", source);
            //是否正装
            Integer suit = appMessageCar.getSuit();
            result.put("suit", suit);
        } else {
            result.put("vehicleResult", "暂无");
            result.put("leaveMessage", "");
            String[] d = new String[0];
            result.put("credentialsImg", d);
            result.put("source", "");
            result.put("suit", "");

        }

        return result;
    }
}
