package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.mapper.AppClaimTeacherMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.mapper.AppUserAccountRecordMapper;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 理赔老师表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2021-03-01
 */
@Service
public class AppClaimTeacherService extends ServiceImpl<AppClaimTeacherMapper, AppClaimTeacherEntity> implements IService<AppClaimTeacherEntity> {


    @Resource
    private AppClaimTeacherMapper appClaimTeacherMapper;

    @Resource
    private AppUserAccountRecordMapper appUserAccountRecordMapper;

    @Resource
    private PushBillService pushBillService;

    @Resource
    private AccidentRecordService accidentRecordService;

    @Resource
    private BizAccidentService bizAccidentService;

    @Resource
    private UserService userService;


    //新增一个理赔老师
    private void addNewClaimTeacher(Integer userId) {
        //新增到理赔老师表格
        AppClaimTeacherEntity claimTeacherEntity = new AppClaimTeacherEntity();
        claimTeacherEntity.setLevel("D");
        claimTeacherEntity.setUserId(userId);
        claimTeacherEntity.setState(1);
        claimTeacherEntity.setOnLion(2);
        claimTeacherEntity.setCreateTime(new Date());
        claimTeacherEntity.setUpdateTime(new Date());
        appClaimTeacherMapper.insert(claimTeacherEntity);
    }

    public JSONArray findAccidMessBill(Integer id, Integer pagesize) {
        //查询到扣费数据
        JSONArray array = new JSONArray();
        pagesize = (pagesize - 1) * 20;
        List<String> accidList = appUserAccountRecordMapper.findAccidMessBill(id, pagesize);
        //根据扣费id查询到pushbill
        if (accidList.isEmpty()) {
            return null;
        } else {
            for (String s : accidList) {
                PushBillEntity pushList = pushBillService.findAccident(id, s);
                //查询到pushbill明细
                //查询到详情参数和金额
                JSONObject object = new JSONObject();
                //地址，金额，messageId，type，
                BigDecimal deduction = pushList.getDeduction();
                //根据来源查询到地址  1,app,2.小程序
                Integer source = pushList.getSource();
                Integer accid = pushList.getAccid();
                if (source == 1) {
                    //app
                    AccidentRecordEntity accidentRecord = accidentRecordService.selectById(accid);
                    String address = accidentRecord.getAddress();
                    if (StringUtils.isEmpty(address)) {
                        address = "未查询到";
                    }
                    object.put("address", address);
                } else {
                    BizAccidentEntity bizAccidentEntity = bizAccidentService.selectById(accid);
                    String address = bizAccidentEntity.getAddress();
                    if (StringUtils.isEmpty(address)) {
                        address = "未查询到";
                    }
                    object.put("address", address);
                }
                object.put("amount", deduction);
                object.put("messageId", pushList.getId());
                object.put("type", 1);
                Date createTime = pushList.getCreateTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(createTime);
                object.put("date", dateString);
                array.add(object);
            }
            return array;
        }
    }

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
                AccidentRecordEntity accidentRecordEntity = accidentRecordService.selectById(pushBillEntity.getAccid());
                if (accidentRecordEntity != null) {
                    lossVehicle = accidentRecordEntity.getLossVehicle();
                    //事故维修产值
                    riskFactor = accidentRecordEntity.getRiskFactor();
                }
            } else {
                BizAccidentEntity bizAccidentEntity = bizAccidentService.selectById(pushBillEntity.getAccid());
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
        object.put("commission","0.05");
        //查询本月收入
        BigDecimal income = appClaimTeacherMapper.findAllIncome(id,month,year);
        object.put("income",income);

        UserEntity userEntity = userService.selectById(id);
        Integer isLocation = userEntity.getIsLocation();
        if (isLocation == null) {
            isLocation = 0;
        }
        object.put("isLocation",isLocation);
        return object;
    }
}
