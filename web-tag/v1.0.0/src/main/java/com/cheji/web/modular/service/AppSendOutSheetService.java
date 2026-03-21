package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.cwork.CheckImgList;
import com.cheji.web.modular.cwork.TableMess;
import com.cheji.web.modular.cwork.Voice;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.mapper.AppSendOutSheetMapper;
import com.cheji.web.util.MapNavUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
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
 * web派单记录表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2021-01-11
 */
@Service
public class AppSendOutSheetService extends ServiceImpl<AppSendOutSheetMapper, AppSendOutSheetEntity> implements IService<AppSendOutSheetEntity> {

    //    private Logger logger = LoggerFactory.getLogger(AppSendOutSheetService.class);
    @Resource
    private AccidentRecordService accidentRecordService;

    @Resource
    private UserService userService;

    @Resource
    private PushBillService pushBillService;

    @Resource
    private AppSendOutSheetMapper appSendOutSheetMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AppMessageCarService appMessageCarService;

    @Resource
    private BUserService bUserService;

    @Resource
    private CarBrandService carBrandService;

    @Resource
    private AppSendUrlService appSendUrlService;

    @Resource
    private AppMessageUrlService appMessageUrlService;

    private static final String key = "a11e3020a4b82ce9390044286910f02f";


    public List<String> findIndex1Img(Long id) {
        return appSendOutSheetMapper.findIndex1Img(id);
    }

    public List<CheckImgList> selectSenceImg(Long id) {
        return appSendOutSheetMapper.selectSenceImg(id);
    }

    public List<TableMess> find4sStore(AccidentRecordEntity accident, String brandName, Integer brandId) {
        List<TableMess> tableMesses = new ArrayList<>();
        //根据id查询到店铺，遍历店铺是否上架，确定是上架之后就添加信息
        List<Integer> userIdList = appSendOutSheetMapper.findBrandMerIdList(brandId);

        //遍历查询
        for (Integer userBid : userIdList) {
            BUserEntity bUserEntity = bUserService.selectById(userBid);
            if (bUserEntity != null) {
                //查询该店铺是否上架
                if (bUserEntity.getState() == 1) {
                    //是已经上架的商户
                    //查询距离 添加信息
                    TableMess tableMess = new TableMess();
                    tableMess.setId(userBid.toString());
                    tableMess.setName(bUserEntity.getMerchantsName());
                    tableMess.setBrandId(brandId.toString());
                    //查询品牌照片
                    CarBrandEntity carBrandEntity = carBrandService.selectById(brandId);
                    tableMess.setBrandUrl(carBrandEntity.getPicUrl());

                    MapNavUtil mapNavUtil = new MapNavUtil(accident.getLng() + "," + accident.getLat(), bUserEntity.getLng() + "," + bUserEntity.getLat(), key, 1, "JSON");
                    Long results = mapNavUtil.getResults();
                    BigDecimal distance3 = new BigDecimal(results);
                    distance3 = distance3.divide(new BigDecimal("1000"), 2, BigDecimal.ROUND_HALF_UP);

                    tableMess.setLeave(distance3 + "km");
                    tableMess.setBrandName(brandName);
                    tableMess.setIsPush(0);
                    tableMess.setMerLat(bUserEntity.getLat());
                    tableMess.setMerLng(bUserEntity.getLng());
                    tableMess.setMerAddress(bUserEntity.getAddress());
                    tableMesses.add(tableMess);
                }
            }
        }
        return tableMesses;


    }

    public List<TableMess> find4sStorebyAppsend(AccidentRecordEntity accident, String brandName, String brandId, AppMessageCarEntity appMessageCarEntity) {
        List<TableMess> tableMesses = new ArrayList<>();
        List<Integer> userIdList = appSendOutSheetMapper.findBrandMerIdList(Integer.parseInt(brandId));

        //遍历查询
        for (Integer userBid : userIdList) {
            BUserEntity bUserEntity = bUserService.selectById(userBid);
            if (bUserEntity != null) {
                //查询该店铺是否上架
                if (bUserEntity.getState() == 1) {
                    //是已经上架的商户
                    //查询距离 添加信息
                    TableMess tableMess = new TableMess();
                    tableMess.setId(userBid.toString());
                    tableMess.setName(bUserEntity.getMerchantsName());
                    tableMess.setBrandId(brandId);
                    //查询品牌照片
                    CarBrandEntity carBrandEntity = carBrandService.selectById(brandId);
                    tableMess.setBrandUrl(carBrandEntity.getPicUrl());

                    MapNavUtil mapNavUtil = new MapNavUtil(accident.getLng() + "," + accident.getLat(), bUserEntity.getLng() + "," + bUserEntity.getLat(), key, 1, "JSON");
                    Long results = mapNavUtil.getResults();
                    BigDecimal distance3 = new BigDecimal(results);
                    distance3 = distance3.divide(new BigDecimal("1000"), 2, BigDecimal.ROUND_HALF_UP);

                    tableMess.setLeave(distance3 + "km");
                    tableMess.setBrandName(brandName);
                    tableMess.setCustomerName(appMessageCarEntity.getCustomerName());
                    tableMess.setPhone(appMessageCarEntity.getPhone());
                    tableMess.setLicensePlate(appMessageCarEntity.getLicensePlate());
                    tableMess.setCarMessageId(appMessageCarEntity.getId());
                    //查询是否推送过商户
                    List<Integer> ids = appMessageCarService.findIsPush(userBid.toString(), appMessageCarEntity.getMessId());
                    if (ids.isEmpty()) {
                        tableMess.setIsPush(0);
                    } else {
                        tableMess.setIsPush(1);
                    }
                    tableMess.setMerLat(bUserEntity.getLat());
                    tableMess.setMerLng(bUserEntity.getLng());
                    tableMess.setMerAddress(bUserEntity.getAddress());
                    tableMesses.add(tableMess);
                }
            }
        }
        return tableMesses;
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
        AccidentRecordEntity appAccidentRecord = new AccidentRecordEntity();
        //添加一个默认图片
//        https://obs-4f4a.obs.cn-southwest-2.myhuaweicloud.com/img/4bb8e7002e4ca33cea8863ebeba7183.png
        appAccidentRecord.setLng(lng.doubleValue());
        appAccidentRecord.setLat(lat.doubleValue());
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

    public JSONObject findSendSheentList(Integer userId, Integer state, Integer pagesize) {

        pagesize = (pagesize - 1) * 20;
        //先查询数据
        JSONObject object = new JSONObject();
        //等待服务，电话联系，现场签到，录音音频
        //再次跟踪，同意到店，拒绝回店，服务评价
        //1,待服务单 ，派单之后没有人点击接受的订单
        //teackState  = 1
        Integer waitPb = appSendOutSheetMapper.findWaitPB(userId);
        object.put("waitPb", waitPb);

        //2,电话联系，电话不为空的订单
        Integer findPhone = appSendOutSheetMapper.findPhone(userId);
        object.put("findPhone", findPhone);

        //3.现场签到，签到时间不为空的订单
        Integer noCheckTime = appSendOutSheetMapper.findCheckTime(userId);
        object.put("haCheckTime", noCheckTime);

        //4.录音音频，录音音频不为空的订单
        Integer haveVioce = appSendOutSheetMapper.findHaveVioce(userId);
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
        List<AccidentRecordEntity> accident = appSendOutSheetMapper.findAccidentList(userId, state, pagesize);

        if (accident.isEmpty()) {
            object.put("list", accident);
            return object;
        }

        //查询数据  品牌和图片  理赔老师 图片
        JSONArray array = new JSONArray();
        for (AccidentRecordEntity accidentRecordEntity : accident) {
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
                UserEntity userEntity = userService.selectById(userId1);
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

    public JSONObject findAccidentDetails(Integer id, String cid) {
        //事故id
        JSONObject result = new JSONObject();
        //根据事故id查询到具体数据
        AccidentRecordEntity accident = accidentRecordService.selectById(id);
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
        PushBillEntity pbList = appSendOutSheetMapper.findPbList(id);
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
            UserEntity userEntity = userService.selectById(userId);
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

    public JSONObject findEvaluationDetails(String cid) {
        JSONObject object = new JSONObject();
        AppMessageCarEntity appMessageCar = appMessageCarService.selectById(cid);
        Integer vehicleResults = appMessageCar.getVehicleResults();
        if (vehicleResults == null) {
            object.put("vehicleResult", "暂无");
        } else {
            if (vehicleResults == 1) {
                object.put("vehicleResult", "再次跟踪");
            } else if (vehicleResults == 2) {
                object.put("vehicleResult", "拒绝到店");
            } else if (vehicleResults == 3) {
                object.put("vehicleResult", "同意到店");
            } else if (vehicleResults == 4) {
                object.put("vehicleResult", "推修成功");
            }
        }


        object.put("plate", appMessageCar.getLicensePlate());

        String brandId = appMessageCar.getBrandId();
        if (StringUtils.isEmpty(brandId)) {
            object.put("brandImg", "");
        } else {
            CarBrandEntity carBrandEntity = carBrandService.selectById(brandId);
            object.put("brandImg", carBrandEntity.getPicUrl());
        }

        String leaveMessage = appMessageCar.getLeaveMessage();
        object.put("leaveMessage", leaveMessage);

        //推修结果图片
        List<String> list = appMessageUrlService.selectUrlPhotos(Integer.valueOf(cid), 3, 1);
        object.put("credentialsImg", list);

        //评价
        Integer source = appMessageCar.getSource();
        object.put("source", source);
        //是否正装
        Integer suit = appMessageCar.getSuit();
        object.put("suit", suit);
        return object;
    }

    public void updateMessageCar(String cid, Integer source, Integer suit) {
        AppMessageCarEntity appMessageCar = appMessageCarService.selectById(cid);
        if (source != null) {
            appMessageCar.setSource(source);
        }

        if (suit != null) {
            appMessageCar.setSuit(suit);
        }

        appMessageCarService.updateById(appMessageCar);
    }
}
