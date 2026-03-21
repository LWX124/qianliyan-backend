package com.cheji.b.modular.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.cheji.b.modular.domain.*;
import com.cheji.b.modular.dto.MineDto;
import com.cheji.b.modular.dto.TestDto;
import com.cheji.b.modular.dto.TrackStateNumber;
import com.cheji.b.modular.mapper.AppClaimTeacherMapper;
import com.cheji.b.modular.mapper.AppUserAccountRecordMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 理赔老师表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2022-03-28
 */
@Service
public class AppClaimTeacherService extends ServiceImpl<AppClaimTeacherMapper, AppClaimTeacherEntity> implements IService<AppClaimTeacherEntity> {

    @Resource
    private AppClaimTeacherMapper appClaimTeacherMapper;

    @Resource
    private AccidentRecordService accidentRecordService;

    @Resource
    private BizAccidentService bizAccidentService;

    @Resource
    private AppUserAccountRecordMapper appUserAccountRecordMapper;

    @Resource
    private UserService userService;

    @Resource
    private PushBillService pushBillService;

    @Resource
    private IndentService indentService;

    public JSONObject findClaimTeaMessage(Integer id, String year, String month) {
        JSONObject object = new JSONObject();
        //事故线索   查询到一共所有的线索条数
        Integer allMessCount = appClaimTeacherMapper.findAllMesCount(id, year, month);
        object.put("allMessCount", allMessCount);
        //接单抢单
        Integer goMess = appClaimTeacherMapper.findGoMess(id, year, month);
        object.put("goMss", goMess);
        //车辆总数   source 事故来源(1,app,2.小程序)
        List<PushBillEntity> pbId = appClaimTeacherMapper.findCarCount(id, year, month);
        //查询到对应车辆信息
        Integer allCarCount = 0;
        Integer allRisk = 0;
        for (PushBillEntity pushBillEntity : pbId) {
            Integer carCount = 0;
            String lossVehicle = "暂无";
            String riskFactor = "0";
            Integer source = pushBillEntity.getSource();
            if (source == 1) {
                //app
                AccidentRecord accidentRecordEntity = accidentRecordService.selectById(pushBillEntity.getAccid());
                if (accidentRecordEntity != null) {
                    lossVehicle = accidentRecordEntity.getLossVehicle();
                    //事故维修产值
                    riskFactor = accidentRecordEntity.getRiskFactor();
                }
            } else {
                BizAccident bizAccidentEntity = bizAccidentService.selectById(pushBillEntity.getAccid());
                if (bizAccidentEntity != null) {
                    lossVehicle = bizAccidentEntity.getLossVehicle();
                    riskFactor = bizAccidentEntity.getRiskFactor();

                }
            }
            if (StringUtils.isEmpty(lossVehicle)) {
                lossVehicle = "暂无";
            }
            if (lossVehicle.equals("一车")) {
                carCount = 1;
            } else if (lossVehicle.equals("两车")) {
                carCount = 2;
            } else if (lossVehicle.equals("三车")) {
                carCount = 3;
            } else if (lossVehicle.equals("四车及以上")) {
                carCount = 4;
            }
            allCarCount += carCount;


            if (StringUtils.isEmpty(riskFactor)) {
                riskFactor = "暂无";
            }
            if (riskFactor.endsWith("0")) {
                //如果是0结尾就进入
                Integer i = Integer.parseInt(riskFactor);
                allRisk += i;
            }

        }
        object.put("allCarCount", allCarCount);
        //维修产值
        object.put("maintenanceValue", allRisk);
        //现场签到的个数
        Integer checkMess = appClaimTeacherMapper.findCheckMessage(id, year, month);
        object.put("checkMess", checkMess);
        //成交台词
        Integer dealCount = appClaimTeacherMapper.findDealCount(id, year, month);
        object.put("delCount", dealCount);
        //成交率   成交台词/接单数量
        BigDecimal closing = new BigDecimal("0");
        if (goMess == 0) {
            object.put("closing", "0");
        } else {
            closing = new BigDecimal(dealCount.toString()).divide(new BigDecimal(goMess.toString()), 2, BigDecimal.ROUND_HALF_UP);
            object.put("closing", closing);
        }
        //成交产值
        BigDecimal dealOutput = appClaimTeacherMapper.findDealOutput(id, year, month);
        object.put("dealOutPut", dealOutput);

        //接单数量，接单比例
        object.put("orderNumber", goMess);
        //接单比例   接单量除以信息数量
        if (allMessCount == 0) {
            object.put("orderRatio", "0");
        } else {
            BigDecimal orderRatio = new BigDecimal(goMess.toString()).divide(new BigDecimal(allMessCount.toString()), 2, BigDecimal.ROUND_HALF_UP);
            object.put("orderRatio", orderRatio);
        }
        //签到数量， 签到数量/接单数量  签到率
        object.put("checkNumber", checkMess);
        if (goMess == 0) {
            object.put("checkRatio", "0");
        } else {
            BigDecimal checkRatio = new BigDecimal(checkMess.toString()).divide(new BigDecimal(goMess.toString()), 2, BigDecimal.ROUND_HALF_UP);
            object.put("checkRatio", checkRatio);
        }
        //完善订单数量，完善订单比例  接单数量/完善的订单数量
        Integer findMess = appClaimTeacherMapper.findFinshMess(id, year, month);
        object.put("completeMessNumber", findMess);
        if (goMess == 0) {
            object.put("completeMessRatio", new BigDecimal("0"));
        } else {
            BigDecimal completeRatio = new BigDecimal(findMess.toString()).divide(new BigDecimal(goMess.toString()), 2, BigDecimal.ROUND_HALF_UP);
            object.put("completeMessRatio", completeRatio);
        }
        //成交数量（订单数量），成交比例(成交率)
        object.put("clinchNumber", dealCount);
        object.put("clinchRatio", closing);
        //产值，
        object.put("outPutNumber", dealOutput);
        //接单标准
        object.put("getStand", new BigDecimal("0.95"));
        //签到标准
        object.put("checkStand", new BigDecimal("1"));
        //完单标准
        object.put("completeStand", new BigDecimal("0.95"));
        //成交标准
        object.put("clinchStand", new BigDecimal("0.3"));
        //产值标准
        object.put("outPutStand", new BigDecimal("400000"));
        //查询佣金
        object.put("commission", "0.05");
        //查询本月收入
        BigDecimal income = appClaimTeacherMapper.findAllIncome(id, month, year);
        object.put("income", income);

        AppUserEntity appUserEntity = userService.selectById(id);
        Integer isLocation = appUserEntity.getIsLocation();
        if (isLocation == null) {
            isLocation = 0;
        }
        object.put("isLocation", isLocation);
        return object;
    }

    public JSONObject noHaveId() {
        JSONObject object = new JSONObject();
        object.put("allMessCount", "0");
        //接单抢单
        object.put("goMss", "0");
        object.put("allCarCount", "0");
        //维修产值
        object.put("maintenanceValue", "0");
        //现场签到的个数
        object.put("checkMess", "0");
        //成交台词
        object.put("delCount", "0");
        //成交率   成交台词/接单数量
        object.put("closing", "0");
        //成交产值
        object.put("dealOutPut", "0");

        //接单数量，接单比例
        object.put("orderNumber", "0");
        //接单比例   接单量除以信息数量
        object.put("orderRatio", "0");
        //签到数量， 签到数量/接单数量  签到率
        object.put("checkNumber", "0");
        object.put("checkRatio", "0");
        //完善订单数量，完善订单比例  接单数量/完善的订单数量
        object.put("completeMessNumber", "0");
        object.put("completeMessRatio", "0");
        //成交数量（订单数量），成交比例(成交率)
        object.put("clinchNumber", "0");
        object.put("clinchRatio", "0");
        //产值，
        object.put("outPutNumber", "0");
        //接单标准
        object.put("getStand", "0");
        //签到标准
        object.put("checkStand", "0");
        //完单标准
        object.put("completeStand", "0");
        //成交标准
        object.put("clinchStand", "0");
        //产值标准
        object.put("outPutStand", "0");
        //查询佣金
        object.put("commission", "0");
        //查询本月收入
        object.put("income", "0");

        object.put("isLocation", "0");
        return object;
    }

    public JSONObject findAccidMessBill(Integer id, Integer pagesize,String year,String month) {
        //查询到扣费数据
        JSONObject result = new JSONObject();
        JSONArray objects = new JSONArray();
        pagesize = (pagesize - 1) * 20;
        List<String> accidList = appUserAccountRecordMapper.findAccidMessBill(id, pagesize,year,month);
        //根据扣费id查询到pushbill
        if (accidList.isEmpty()) {
            return null;
        } else {
            ArrayList<PushBillEntity> pushBillEntities = new ArrayList<>();
            for (String s : accidList) {
                PushBillEntity pushList = pushBillService.findAccident(id, s);
                pushBillEntities.add(pushList);
            }
            for (PushBillEntity pushBillEntity : pushBillEntities) {
                JSONObject object = new JSONObject();
                object.put("messageId", pushBillEntity.getId());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                object.put("trackState", pushBillEntity.getTrackState());
                String format = sdf.format(pushBillEntity.getCreateTime());
                object.put("time", format);
                object.put("type", pushBillEntity.getType());//1 pb  2 sos
                String address = "...";
                //type1，根据pb类型操作
                //是小程序或者app
                //查询具体的信息
                Integer source = pushBillEntity.getSource();
                //1,app,2.小程序
                if (source == 1) {
                    AccidentRecord accidentRecordEntity = accidentRecordService.selectById(pushBillEntity.getAccid());
                    address = accidentRecordEntity.getAddress();
                    object.put("url", accidentRecordEntity.getThumbnailUrl());
                    object.put("videoUrl", accidentRecordEntity.getVideo());
                    if (accidentRecordEntity.getVideo() == null) {
                        object.put("url", accidentRecordEntity.getImgUrl());
                    }
                    String phone = accidentRecordEntity.getPhone();
                    if (StringUtils.isEmpty(phone)){
                        object.put("isPhone", 0);
                    }else {
                        object.put("isPhone", 0);
                    }
                    pushBillEntity.setLat(accidentRecordEntity.getLat());
                    pushBillEntity.setLng(accidentRecordEntity.getLng());
                } else {
                    BizAccident bizAccidentEntity = bizAccidentService.findById(pushBillEntity.getAccid());
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
                int length = address.length();
                if (length > 6) {
                    String substring = address.substring(0, 6);
                    object.put("address", substring + "...");     //地址只展示6位 。。。
                } else {
                    object.put("address", address + "...");     //地址只展示6位
                }
                //查询距离
                object.put("lng", pushBillEntity.getLng());
                object.put("lat", pushBillEntity.getLat());
                objects.add(object);
            }

            //查询到线索条数，线索费用，服务产值
            MineDto num = pushBillService.findAllCount(id,year,month);
            Integer allbill = num.getAllbill();  //条数
            Integer allIndent = num.getAllIndent();//费用
            //产值
            BigDecimal serMoney = indentService.findAllSerMoney(id,year,month);
            result.put("billCount",allbill);
            result.put("AllMoney",allIndent);
            result.put("serMoney",serMoney);
            result.put("array",objects);
            return result;
        }
    }

    public JSONObject MessageList(Integer id, Integer state, Integer pagesize) {
        JSONObject result = new JSONObject();
        AppUserEntity userEntity = userService.selectById(id);
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
            //是小程序或者app
            //查询具体的信息
            Integer source = pushBillEntity.getSource();
            //1,app,2.小程序
            if (source == 1) {
                AccidentRecord accidentRecordEntity = accidentRecordService.selectById(pushBillEntity.getAccid());
                address = accidentRecordEntity.getAddress();
                object.put("url", accidentRecordEntity.getThumbnailUrl());
                object.put("videoUrl", accidentRecordEntity.getVideo());
                if (accidentRecordEntity.getVideo() == null) {
                    object.put("url", accidentRecordEntity.getImgUrl());
                }
                String phone = accidentRecordEntity.getPhone();
                if (StringUtils.isEmpty(phone)){
                    object.put("isPhone", 0);
                }else {
                    object.put("isPhone", 0);
                }
                pushBillEntity.setLat(accidentRecordEntity.getLat());
                pushBillEntity.setLng(accidentRecordEntity.getLng());
            } else {
                BizAccident bizAccidentEntity = bizAccidentService.findById(pushBillEntity.getAccid());
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
            int length = address.length();
            if (length > 6) {
                String substring = address.substring(0, 6);
                object.put("address", substring + "...");     //地址只展示6位 。。。
            } else {
                object.put("address", address + "...");     //地址只展示6位
            }
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

    public static void main(String[] args) {
        //20220401
        List<String> strings1 = new ArrayList<>();
        strings1.add("20220408");
        strings1.add("20220208");
        strings1.add("20220406");
        strings1.add("20220401");
        strings1.add("20220405");
        strings1.add("20220106");
        strings1.add("20220404");
        strings1.add("20220403");
        strings1.add("20220402");
        strings1.add("20220407");
        strings1.add("20220331");
        strings1.add("20220330");
        strings1.add("20220108");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Date> dates = new ArrayList<>();
        strings1.sort(Collections.reverseOrder());
        for (String string : strings1) {
            String year = string.substring(0, 4);
            String month = string.substring(4, 6);
            String day = string.substring(6, 8);
            String nrw = year+"-"+month+"-"+day;
            Date strToDate = new Date();
            try {
                strToDate = sdf.parse(nrw);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dates.add(strToDate);
        }
        int continuousDay = 1;
        boolean todaySignIn = false;
        Date today = new Date();
        for (int i = 0; i < dates.size(); i++) {
            int intervalDay = distanceDay(today, dates.get(i));
            //当天签到
            if (intervalDay == 0 && i == 0) {
                todaySignIn = true;
            }
            else if (intervalDay == continuousDay) {
                continuousDay++;
            }else {
                //不连续，终止判断
                break;
            }
        }
        if (!todaySignIn) {
            continuousDay--;
        }
        System.out.println("连续签到日期："+ continuousDay);
    }



    private static int distanceDay(Date largeDay, Date smallDay) {
        return (int) ((largeDay.getTime() - smallDay.getTime()) / (1000 * 60 * 60 * 24));
    }


}
