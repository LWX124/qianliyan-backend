/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.dao.AppAccidentRecordDao;
import com.jeesite.modules.app.entity.*;
import com.jeesite.modules.util2.GaoDeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * app上报事故信息表Service
 *
 * @author zcq
 * @version 2019-08-29
 */
@Service
@Transactional(readOnly = true)
public class AppAccidentRecordService extends CrudService<AppAccidentRecordDao, AppAccidentRecord> {
    @Resource
    private AppAccidentRecordDao appAccidentRecordDao;
    @Resource
    private AppVideoService appVideoService;
    @Resource
    private AppUserService appUserService;
    @Resource
    private AppPushBillService appPushBillService;
    @Resource
    private AppBUserService appBUserService;
    @Resource
    private AppUserBehaviorService appUserBehaviorService;
    @Resource
    private AppSendOutSheetService appSendOutSheetService;
    @Resource
    private AppPayAmountRecordService appPayAmountRecordService;
    @Resource
    private BizAccidentService bizAccidentService;
    @Resource
    private PayWxAmountService payWxAmountService;
    @Resource
    private AppSendUrlService appSendUrlService;

    /**
     * 获取单条数据
     *
     * @param appAccidentRecord
     * @return
     */
    @Override
    public AppAccidentRecord get(AppAccidentRecord appAccidentRecord) {
        return super.get(appAccidentRecord);
    }

    /**
     * 查询分页数据
     *
     * @param appAccidentRecord      查询条件
     * @param appAccidentRecord.page 分页对象
     * @return
     */
    @Override
    public Page<AppAccidentRecord> findPage(AppAccidentRecord appAccidentRecord) {
        Page a = appAccidentRecord.getPage();
        int pageNo = a.getPageNo();//页数
        int pageSize = a.getPageSize();//条数
        int pageoffset = (pageNo - 1) * pageSize;
        long count = a.getCount();
        Integer statuse = appAccidentRecord.getStatuse();
        Integer realness = appAccidentRecord.getRealness();
        String phone = appAccidentRecord.getPhone();
        Date time = appAccidentRecord.getCreateTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format;
        if (time == null) {
            format = null;
        } else {
            format = simpleDateFormat.format(time);
        }

        List<AppAccidentRecord> otherList = appAccidentRecordDao.findmessage(pageoffset, statuse, realness, format, phone);
        count = appAccidentRecordDao.findcount();
        //判断，还要查询条数
        for (AppAccidentRecord accidentRecord : otherList) {
            //处理经纬度
            if (StringUtils.isEmpty(accidentRecord.getAddress())) {
                //地址为空，查询地址，地址添加进去。修改表数据
                Double lng = accidentRecord.getLng();
                Double lat = accidentRecord.getLat();
                String gdAddress = null;
                try {
                    gdAddress = GaoDeUtils.getGDAddress(lat.toString(), lng.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //修改
                accidentRecord.setAddress(gdAddress);
            }

            Integer type = accidentRecord.getType();
            String id = accidentRecord.getId();
            //根据type和id查询推送了的商户
            if (type == 3) {   //微信事故表
                type = 2;
            } else if (type == 2) {
                type = 1;       //app事故表
            }

            if (type == 4) {
                //是web派单   查询 web派单sos表 查询理赔老师
                //查询到推送人员
                AppSendOutSheet sendOutSheet = appSendOutSheetService.get(accidentRecord.getId());
                Integer userId = sendOutSheet.getUserId();
                if (userId == null) {
                    accidentRecord.setPushMerchants("暂无");
                    accidentRecord.setAmount("0");
                } else {
                    AppUser appUser = appUserService.get(userId.toString());
                    String name = appUser.getName();
                    accidentRecord.setPushMerchants(name);
                }

                //查询到用户名称
                // AppBUser appBUser = appBUserService.get(userbid);
//                if (appBUser != null) {
//                    accidentRecord.setPushMerchants(appBUser.getMerchantsName());
//                }else {
//                    accidentRecord.setPushMerchants("暂无");
//                }

//                accidentRecord.setSlight(0);
//                accidentRecord.setOnGround(0);
//                accidentRecord.setNearCar(0);
//                accidentRecord.setRepeat(0);
//                accidentRecord.setPassNumber(0);
                //设置用户行为
            } else {    //pb表查询理赔老师
                List<Integer> userBids = appPushBillService.selectPushMerchants(id, type);
                //查询到pb
                //查询到app红包金额    //根据事故id查询到红包金额
                if (type == 1) {
                    BigDecimal amount = appPayAmountRecordService.findAllPayAmount(id);
                    if (amount == null) {
                        accidentRecord.setAmount("暂无");
                    } else {
                        accidentRecord.setAmount(amount.toString());
                    }
                } else {
                    accidentRecord.setAmount("小程序");
                }

                //查询到app收到的消息
                //查询出来推送的数据，找一个签到时间，签到图片，车损照片，现场录音
                //查询推送数据
                List<AppPushBill> pushBills = appPushBillService.findBySourceAccid(id, type);
                if (!pushBills.isEmpty()) {
                    AppPushBill appPushBill = pushBills.get(0);
                    String voice = appPushBill.getVoice();
                    if (StringUtils.isNotEmpty(voice)) {
                        accidentRecord.setIsVoice("有");
                    }else {
                        accidentRecord.setIsVoice("无");
                    }

                    Date checkTime = appPushBill.getCheckTime();
                    accidentRecord.setUpdateTime(checkTime);
                    //查询到图片
                    String pushBillId = appPushBill.getId();
                    //根据pushillid查询到签到图片
                    List<String> checkList = appSendUrlService.findImg(pushBillId, 1, 2);
                    //用逗号拼接
                    if (!checkList.isEmpty()) {
                        String checkLiss = "";
                        if (checkList.size()==1){
                            checkLiss = checkList.get(0);
                        }else {
                            for (int i = 0; i < checkList.size(); i++) {
                                if (i >= 2) {
                                    break;
                                }
                                checkLiss += checkList.get(i) + ",";
                            }
                        }
                        accidentRecord.setCheckImg(checkLiss);
                    }
                    //查询到事故图片
                    List<String> carimg = appSendUrlService.findcarImg(pushBillId, 1, 1);
                    if (!carimg.isEmpty()) {
                        String carimgList = "";
                        if (carimg.size()==1){
                            carimgList = carimg.get(0);
                        }else {
                            for (int i = 0; i < carimg.size(); i++) {
                                if (i >= 2) {
                                    break;
                                }
                                carimgList += carimg.get(i) + ",";
                            }
                        }
                        accidentRecord.setCarImg(carimgList);
                    }
                }




                if (userBids.isEmpty()) {
                    accidentRecord.setPushMerchants("无");
                } else {
                    String names = "";
                    for (Integer userBiId : userBids) {
                        AppUser appUser = appUserService.get(String.valueOf(userBiId));
                        if (appUser != null) {
                            String merchantsName = appUser.getName();
                            names += merchantsName + ",";
                        }
                    }
                    accidentRecord.setPushMerchants(names);
                }


                //获取到id
//                String useropenId = accidentRecord.getUseropenId();
//                AppUserBehavior appUserBehavior = appUserBehaviorService.selectByUserId(useropenId);
//                if (appUserBehavior != null) {
//                    if (appUserBehavior.getSlight() == null) {
//                        appUserBehavior.setSlight(0);
//                    }
//                    if (appUserBehavior.getOnGround() == null) {
//                        appUserBehavior.setOnGround(0);
//                    }
//                    if (appUserBehavior.getNearCar() == null) {
//                        appUserBehavior.setNearCar(0);
//                    }
//                    if (appUserBehavior.getRepeat() == null) {
//                        appUserBehavior.setRepeat(0);
//                    }
//                    if (appUserBehavior.getPassNumber() == null) {
//                        appUserBehavior.setPassNumber(0);
//                    }
////                    accidentRecord.setSlight(appUserBehavior.getSlight());
////                    accidentRecord.setOnGround(appUserBehavior.getOnGround());
////                    accidentRecord.setNearCar(appUserBehavior.getNearCar());
////                    accidentRecord.setRepeat(appUserBehavior.getRepeat());
////                    accidentRecord.setPassNumber(appUserBehavior.getPassNumber());
//                }
            }
        }

        a.setList(otherList);
        a.setCount(count);
        return a;

//		Page<AppAccidentRecord> page = super.findPage(appAccidentRecord);
//        List<AppAccidentRecord> otherList = appAccidentRecordDao.findOtherList();
//        page.setList(otherList);
//        return super.findPage(appAccidentRecord);
//		return page;
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param appAccidentRecord
     */
    @Override
    @Transactional(readOnly = false)
    public void save(AppAccidentRecord appAccidentRecord) {
        super.save(appAccidentRecord);
    }

    /**
     * 更新状态
     *
     * @param appAccidentRecord
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(AppAccidentRecord appAccidentRecord) {
        super.updateStatus(appAccidentRecord);
    }

    /**
     * 删除数据
     *
     * @param appAccidentRecord
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(AppAccidentRecord appAccidentRecord) {
        super.delete(appAccidentRecord);
    }

    public List<AppAccidentRecord> findAllList() {
        return appAccidentRecordDao.findAllList();
    }

    @Transactional(readOnly = false)
    public void saveReason(AppAccidentRecord appAccidentRecord) {
        //保存修改数据
        update(appAccidentRecord);
    }

    /**
     * 把事故视频展示到app表中
     *
     * @param id app_accident_record 的id
     * @return
     */
    @Transactional(readOnly = false)
    public AppVideo saveApp(String id) {
        //通过事故id查询到具体信息
        AppAccidentRecord AccidentRecord = get(id);
        String video = AccidentRecord.getVideo();
        String introduce = AccidentRecord.getIntroduce();
        String userId = AccidentRecord.getUserId();
        String address = AccidentRecord.getAddress();

        //根据userid查询到名字
        AppUser appUser = appUserService.get(userId);
        String avatar = appUser.getAvatar();
        String name = appUser.getName();
        String thumbnailUrl = AccidentRecord.getThumbnailUrl();
        AccidentRecord.setIsaddvideo(1);

        //把对应信息放到app表中
        AppVideo appVideo = new AppVideo();
        if (AccidentRecord.getStatuse() == 2) {
            appVideo.setIsPay(1);
        }
        appVideo.setUrl(video);
        //https://www.hw.chejiqiche.com/video/361571710380539516.mp4 
        if (StringUtils.isNotEmpty(video)) {
            if (video.contains("%")) {
                String str1 = video.substring(0, video.indexOf("/video"));
                String sg = video.substring(str1.length() + 1, video.length());
                String str3 = "http://www.hw.chejiqiche.com/" + sg;
                // sg = "http://www.hw.chejiqiche.com" + str1;
                appVideo.setUrl(str3);
            } else {
                //app上传
                String replace = video.replace("https://obs-4f4a.obs.cn-southwest-2.myhuaweicloud.com/video/", "http://www.hw.chejiqiche.com/video/");
                logger.info("### app上传 cdn地址 # replace={}", replace);
                appVideo.setUrl(replace);
            }
        }

//        appVideo.setUrl(video);
        appVideo.setUserId(userId);
        appVideo.setSource(1);
        appVideo.setCount(0);
        appVideo.setShare(0L);
        appVideo.setAppViewCounts(0L);
        appVideo.setAppShowFalg(1);
        appVideo.setAccidentId(Long.valueOf(id));
        appVideo.setThumbnailUrl(thumbnailUrl);
        Date date = new Date();
        appVideo.setCreatTime(date);
        appVideo.setUpdateTime(date);
        appVideo.setName(name);
        appVideo.setAvatar(avatar);
        appVideo.setIntroduce(introduce);
        appVideo.setAddress(address);
        appVideoService.insert(appVideo);

        String id1 = appVideo.getId();
        AccidentRecord.setVideoId(Long.valueOf(id1));
        update(AccidentRecord);

        return appVideo;
    }

    public Integer findCommonappCount() {
        return appAccidentRecordDao.findCommonappCount();
    }

    public Integer findWxCommonCount() {
        return appAccidentRecordDao.findWxCommonCount();
    }

    public BigDecimal findCommonAppAmount() {
        return appAccidentRecordDao.findCommonAppAmount();
    }

    public BigDecimal findWxAmount() {
        return appAccidentRecordDao.findWxAmount();
    }

    public BigDecimal findPlusAmount() {
        return appAccidentRecordDao.findPlusAmount();
    }

    public Integer findPlusCount() {
        return appAccidentRecordDao.findPlusCount();
    }

    public Integer findTodayCount() {
        return appAccidentRecordDao.findTodayCount();
    }

    public Integer findTodayWxCount() {
        return appAccidentRecordDao.findTodayWxCount();
    }

    public Integer findTodayAppCount() {
        return appAccidentRecordDao.findTodayAppCount();
    }

    public Integer findTodayAppPushCount() {
        return appAccidentRecordDao.findTodayAppPushCount();
    }

    public List<AppAccidentRecord> selectNotDelGeo() {
        return appAccidentRecordDao.selectNotDelGeo();
    }

    public List<AppAccidentRecord> selectRepart(String accId, String type) {
        if (type.equals("3")) {
            type = "2";
        }
        return appAccidentRecordDao.selectRepart(accId, type);
    }

    public Integer findBlackNumber() {
        return appAccidentRecordDao.findBlackNumber();
    }

    public List<AppAccidentRecord> findAccbyUserid(String id) {
        return appAccidentRecordDao.findAccbyUserid(id);
    }

    public Integer findGreenNumber() {
        return appAccidentRecordDao.findGreenNumber();
    }

    public Integer findTowRecord(String id) {
        return appAccidentRecordDao.findTowRecord(id);
    }

    public List<String> findgoingIndent(String id) {
        return appAccidentRecordDao.findgoingIndent(id);
    }

    @Transactional(readOnly = false)
    public JSONObject accidentTag(String id, String type, String genre) {
        JSONObject result = new JSONObject();
        try {
            String userId;
            if (type.equals("1")) {
                //app上传
                AppAccidentRecord appAccidentRecord = this.get(id);
                userId = appAccidentRecord.getUserId();
                //找到表里面是否有用户存在

            } else {
                //小程序上传
                BizAccident bizAccident = bizAccidentService.get(id);
                userId = bizAccident.getOpenid();
                //找到表里面是否有用户存在
            }
            AppUserBehavior appUserBehavior = appUserBehaviorService.selectByUserId(userId);

            //不存在就新增一个数据
            if (appUserBehavior == null) {
                AppUserBehavior addUserBehavior = new AppUserBehavior();
                addUserBehavior.setUserId(userId);
                if (type.equals("3")) {
                    type = "2";
                }
                addUserBehavior.setUserSource(type);
                addUserBehavior.setCreateTime(new Date());
                addUserBehavior.setUpdateTime(new Date());
                appUserBehaviorService.insert(addUserBehavior);
                appUserBehaviorService.addScount(addUserBehavior, genre);
            } else {
                //存在就在对应数据上加一
                appUserBehaviorService.addScount(appUserBehavior, genre);
            }
        } catch (Exception e) {
            logger.error("标记事故失败！# error:{}", e.getStackTrace());
            // e.getStackTrace();
            result.put("stat", "false");
            result.put("mes", "审核失败！");
            return result;
        }
        result.put("stat", "success");
        result.put("mes", "审核成功！");
        return result;
    }

    @Transactional(readOnly = false)
    public JSONObject failure(String id, String reason, String type) {
        JSONObject result = new JSONObject();
        //微信审核不通过
        if (type.equals("3")) {
            try {
                payWxAmountService.falied(id, reason);
            } catch (Exception e) {
                result.put("stat", "false");
                result.put("mes", "审核失败！");
                return result;
            }
        }

        //先拿事故到状态判断是否修改
        try {
            AppAccidentRecord appAccidentRecord1 = this.get(id);
//            if (appAccidentRecord1==null){
//                return null;
//            }
            Integer statuse = appAccidentRecord1.getStatuse();

            if (statuse == 3 || statuse == 2) {
                result.put("stat", "false");
                result.put("mes", "审核失败！");
                return result;
            }

            //接收到事故id和不通过原因
            //保存不通过原因到事故表
            AppAccidentRecord appAccidentRecord = new AppAccidentRecord();
            //状态(1：未审核  2：审核通过  3：审核失败）
            appAccidentRecord.setStatuse(3);
            appAccidentRecord.setCheckTime(new Date());
            appAccidentRecord.setId(id);
            appAccidentRecord.setReason(reason);
            saveReason(appAccidentRecord);
            if (reason.equals("重复事故")) {

                //重复数据加一
                String userId;
                if (type.equals("1")) {
                    //app上传
                    userId = appAccidentRecord1.getUserId();
                    //找到表里面是否有用户存在
                } else {
                    //小程序上传
                    BizAccident bizAccident = bizAccidentService.get(id);
                    userId = bizAccident.getOpenid();
                    //找到表里面是否有用户存在
                }
                AppUserBehavior appUserBehavior = appUserBehaviorService.selectByUserId(userId);

                if (appUserBehavior == null) {
                    AppUserBehavior addUserBehavior = new AppUserBehavior();
                    addUserBehavior.setUserId(userId);
                    if (type.equals("3")) {
                        type = "2";
                    }
                    addUserBehavior.setUserSource(type);
                    addUserBehavior.setCreateTime(new Date());
                    addUserBehavior.setUpdateTime(new Date());
                    appUserBehaviorService.insert(addUserBehavior);
                    appUserBehaviorService.addScount(addUserBehavior, "4");
                } else {
                    //存在就在对应数据上加一
                    appUserBehaviorService.addScount(appUserBehavior, "4");
                }
            } else if (reason.equals("没有损失")) {
                //重复数据加一
                String userId;
                if (type.equals("1")) {
                    //app上传
                    userId = appAccidentRecord1.getUserId();
                    //找到表里面是否有用户存在
                } else {
                    //小程序上传
                    BizAccident bizAccident = bizAccidentService.get(id);
                    userId = bizAccident.getOpenid();
                    //找到表里面是否有用户存在
                }
                AppUserBehavior appUserBehavior = appUserBehaviorService.selectByUserId(userId);

                if (appUserBehavior == null) {
                    AppUserBehavior addUserBehavior = new AppUserBehavior();
                    addUserBehavior.setUserId(userId);
                    if (type.equals("3")) {
                        type = "2";
                    }
                    addUserBehavior.setUserSource(type);
                    addUserBehavior.setCreateTime(new Date());
                    addUserBehavior.setUpdateTime(new Date());
                    appUserBehaviorService.insert(addUserBehavior);
                    appUserBehaviorService.addScount(addUserBehavior, "1");
                } else {
                    //存在就在对应数据上加一
                    appUserBehaviorService.addScount(appUserBehavior, "1");
                }
            }

        } catch (Exception e) {
            logger.error("### 报错 标记失败 # e={}", e);
        }
        result.put("stat", "success");
        result.put("mes", "审核成功！");
        return result;
    }

    @Transactional(readOnly = false)
    public void insetNewAcc(AppAccidentRecord appAccidentRecord) {
        appAccidentRecordDao.insertNewAcc(appAccidentRecord);
    }

    public Page<AppAccidentRecord> findTwoPage(AppAccidentRecord appAccidentRecord) {
        Page a = appAccidentRecord.getPage();
        int pageNo = a.getPageNo();//页数
        int pageSize = a.getPageSize();//条数
        int pageoffset = (pageNo - 1) * pageSize;
        long count = a.getCount();
        Integer statuse = appAccidentRecord.getStatuse();
        Integer realness = appAccidentRecord.getRealness();
        String phone = appAccidentRecord.getPhone();
        Date time = appAccidentRecord.getCreateTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format;
        if (time == null) {
            format = null;
        } else {
            format = simpleDateFormat.format(time);
        }

        List<AppAccidentRecord> otherList = appAccidentRecordDao.findTwomessage(pageoffset, statuse, realness, format, phone);
        count = appAccidentRecordDao.findcount();
        //判断，还要查询条数
        for (AppAccidentRecord accidentRecord : otherList) {
            //处理经纬度
            if (StringUtils.isEmpty(accidentRecord.getAddress())) {
                //地址为空，查询地址，地址添加进去。修改表数据
                Double lng = accidentRecord.getLng();
                Double lat = accidentRecord.getLat();
                String gdAddress = null;
                try {
                    gdAddress = GaoDeUtils.getGDAddress(lat.toString(), lng.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //修改
                accidentRecord.setAddress(gdAddress);
            }

            Integer type = accidentRecord.getType();
            String id = accidentRecord.getId();
            //根据type和id查询推送了的商户
            if (type == 3) {   //微信事故表
                type = 2;
            } else if (type == 2) {
                type = 1;       //app事故表
            }

            if (type == 4) {
                //是web派单   查询 web派单sos表 查询理赔老师
                //查询到推送人员
                AppSendOutSheet sendOutSheet = appSendOutSheetService.get(accidentRecord.getId());
                Integer userId = sendOutSheet.getUserId();
                if (userId == null) {
                    accidentRecord.setPushMerchants("暂无");
                    accidentRecord.setAmount("0");
                } else {
                    AppUser appUser = appUserService.get(userId.toString());
                    String name = appUser.getName();
                    accidentRecord.setPushMerchants(name);
                }

                //查询到用户名称
                // AppBUser appBUser = appBUserService.get(userbid);
//                if (appBUser != null) {
//                    accidentRecord.setPushMerchants(appBUser.getMerchantsName());
//                }else {
//                    accidentRecord.setPushMerchants("暂无");
//                }

//                accidentRecord.setSlight(0);
//                accidentRecord.setOnGround(0);
//                accidentRecord.setNearCar(0);
//                accidentRecord.setRepeat(0);
//                accidentRecord.setPassNumber(0);
                //设置用户行为
            } else {    //pb表查询理赔老师
                List<Integer> userBids = appPushBillService.selectPushMerchants(id, type);
                //查询到pb
                //查询到app红包金额    //根据事故id查询到红包金额
                if (type == 1) {
                    BigDecimal amount = appPayAmountRecordService.findAllPayAmount(id);
                    if (amount == null) {
                        accidentRecord.setAmount("暂无");
                    } else {
                        accidentRecord.setAmount(amount.toString());
                    }
                } else {
                    accidentRecord.setAmount("小程序");
                }

                //查询到app收到的消息
                //查询出来推送的数据，找一个签到时间，签到图片，车损照片，现场录音
                //查询推送数据
                List<AppPushBill> pushBills = appPushBillService.findBySourceAccid(id, type);
                if (!pushBills.isEmpty()) {
                    AppPushBill appPushBill = pushBills.get(0);
                    String voice = appPushBill.getVoice();
                    if (StringUtils.isNotEmpty(voice)) {
                        accidentRecord.setIsVoice("有");
                    }else {
                        accidentRecord.setIsVoice("无");
                    }

                    Date checkTime = appPushBill.getCheckTime();
                    accidentRecord.setUpdateTime(checkTime);
                    //查询到图片
                    String pushBillId = appPushBill.getId();
                    //根据pushillid查询到签到图片
                    List<String> checkList = appSendUrlService.findImg(pushBillId, 1, 2);
                    //用逗号拼接
                    if (!checkList.isEmpty()) {
                        String checkLiss = "";
                        for (int i = 0; i < checkList.size(); i++) {
                            if (i >= 2) {
                                break;
                            }
                            checkLiss += checkList.get(i) + ",";
                        }
                        accidentRecord.setCheckImg(checkLiss);
                    }
                    //查询到事故图片
                    List<String> carimg = appSendUrlService.findcarImg(pushBillId, 1, 1);
                    if (!carimg.isEmpty()) {
                        String carimgList = "";
                        for (int i = 0; i < carimg.size(); i++) {
                            if (i >= 2) {
                                break;
                            }
                            carimgList += carimg.get(i) + ",";
                        }
                        accidentRecord.setCarImg(carimgList);
                    }
                }




                if (userBids.isEmpty()) {
                    accidentRecord.setPushMerchants("无");
                } else {
                    String names = "";
                    for (Integer userBiId : userBids) {
                        AppUser appUser = appUserService.get(String.valueOf(userBiId));
                        if (appUser != null) {
                            String merchantsName = appUser.getName();
                            names += merchantsName + ",";
                        }
                    }
                    accidentRecord.setPushMerchants(names);
                }


                //获取到id
//                String useropenId = accidentRecord.getUseropenId();
//                AppUserBehavior appUserBehavior = appUserBehaviorService.selectByUserId(useropenId);
//                if (appUserBehavior != null) {
//                    if (appUserBehavior.getSlight() == null) {
//                        appUserBehavior.setSlight(0);
//                    }
//                    if (appUserBehavior.getOnGround() == null) {
//                        appUserBehavior.setOnGround(0);
//                    }
//                    if (appUserBehavior.getNearCar() == null) {
//                        appUserBehavior.setNearCar(0);
//                    }
//                    if (appUserBehavior.getRepeat() == null) {
//                        appUserBehavior.setRepeat(0);
//                    }
//                    if (appUserBehavior.getPassNumber() == null) {
//                        appUserBehavior.setPassNumber(0);
//                    }
////                    accidentRecord.setSlight(appUserBehavior.getSlight());
////                    accidentRecord.setOnGround(appUserBehavior.getOnGround());
////                    accidentRecord.setNearCar(appUserBehavior.getNearCar());
////                    accidentRecord.setRepeat(appUserBehavior.getRepeat());
////                    accidentRecord.setPassNumber(appUserBehavior.getPassNumber());
//                }
            }
        }

        a.setList(otherList);
        a.setCount(count);
        return a;
    }

    public Integer findAudit() {
        return appAccidentRecordDao.findAudit();
    }

    public Integer findDayNumber() {
        return appAccidentRecordDao.findDayNumber();
    }

    public Integer findNightNumber() {
        return appAccidentRecordDao.findNightNumber();
    }
}