package com.stylefeng.guns.modular.system.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.dao.DataCountMapper;
import com.stylefeng.guns.modular.system.model.Accident;
import com.stylefeng.guns.modular.system.model.WxCouponCountModel;
import com.stylefeng.guns.modular.system.service.DataCountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <ul>
 * <li>文件包名 : com.stylefeng.guns.modular.system.service.impl</li>
 * <li>创建时间 : 2019-03-14 16:21</li>
 * <li>修改记录 : 无</li>
 * </ul>
 * 类说明： 数据统计
 *
 * @author duanhong
 * @version 2.0.0
 */
@Service
public class DataCountServiceImpl implements DataCountService {

    @Resource
    private DataCountMapper dataCountMapper;

    @Override
    public List<WxCouponCountModel> countData(String startTime, String endTime) {
        String[] citys = {"成都", "武汉", "重庆","天津"};
        List<WxCouponCountModel> result = new ArrayList<>();
        //1.88元红包总计个数
        Integer oneTotalNum = 0;
        //5元红包总计个数
        Integer fiveTotalNum = 0;
        //10元红包总计个数
        Integer tenTotalNum = 0;
        //红包总金额
        Double sumMoney = 0d;
        for (String city : citys) {
            Map in = new HashMap();
            WxCouponCountModel wxCouponCountModel = new WxCouponCountModel();
            in.put("city", city);
            in.put("startTime", startTime);
            in.put("endTime", endTime);
            List<Map<String, Object>> singleResultList = dataCountMapper.countCoupon(in);

            if (singleResultList != null && singleResultList.size() != 0) {
                for (Map<String, Object> map : singleResultList) {
                    switch (String.valueOf(map.get("amount"))) {
                        case "1.88":
                            oneTotalNum += Integer.parseInt(String.valueOf(map.get("countNum")));
                            wxCouponCountModel.setOneAmountNumber(Integer.parseInt(String.valueOf(map.get("countNum"))));
                            break;
                        case "5.00":
                            fiveTotalNum += Integer.parseInt(String.valueOf(map.get("countNum")));
                            wxCouponCountModel.setFiveAmountNumber(Integer.parseInt(String.valueOf(map.get("countNum"))));
                            break;
                        case "10.00":
                            tenTotalNum += Integer.parseInt(String.valueOf(map.get("countNum")));
                            wxCouponCountModel.setTenAmountNumber(Integer.parseInt(String.valueOf(map.get("countNum"))));
                            break;
                    }
                }
                wxCouponCountModel.setSumNumber(wxCouponCountModel.getOneAmountNumber() + wxCouponCountModel.getFiveAmountNumber() + wxCouponCountModel.getTenAmountNumber());
                wxCouponCountModel.setSumMoney(wxCouponCountModel.getOneAmountNumber() * 1.88 + wxCouponCountModel.getFiveAmountNumber() * 5
                        + wxCouponCountModel.getTenAmountNumber() * 10);
            }
            wxCouponCountModel.setCityName(city);
            sumMoney += wxCouponCountModel.getSumMoney();
            result.add(wxCouponCountModel);
        }
        WxCouponCountModel wxCouponCountModel = new WxCouponCountModel();
        wxCouponCountModel.setCityName("总计");
        wxCouponCountModel.setSumNumber(oneTotalNum + fiveTotalNum + tenTotalNum);
        wxCouponCountModel.setOneAmountNumber(oneTotalNum);
        wxCouponCountModel.setFiveAmountNumber(fiveTotalNum);
        wxCouponCountModel.setTenAmountNumber(tenTotalNum);
        wxCouponCountModel.setPercentage(1);
        wxCouponCountModel.setSumMoney(sumMoney);
        for (WxCouponCountModel model : result) {
            if (model.getSumMoney() != 0) {
                model.setPercentage(model.getSumMoney() / sumMoney);
            }
        }
        result.add(wxCouponCountModel);
        return result;
    }

    @Override
    public List<Map> userCount(String startTime, String endTime, String name, String department) {
        //查找符合条件的理赔老师和4s店员
        List<Map> userList = dataCountMapper.countUser(startTime, endTime, name, department);
        for (Map map : userList) {
            BigDecimal couponCount = new BigDecimal("0");
            BigDecimal couponSum = new BigDecimal("0");
            BigDecimal amount188Num = new BigDecimal(map.get("amount188").toString());
            BigDecimal amount5 = new BigDecimal(map.get("amount5").toString());
            BigDecimal amount10 = new BigDecimal(map.get("amount10").toString());
            couponCount = couponCount.add(amount188Num).add(amount5).add(amount10);
            couponSum = amount188Num.multiply(new BigDecimal("1.88")).add(amount5.multiply(new BigDecimal("5"))).add(amount10.multiply(new BigDecimal("10")));
            map.put("couponCount", couponCount);
            map.put("couponSum", couponSum);

            BigDecimal openClaimNum = new BigDecimal(map.get("openClaimNum").toString());
            if (couponCount.equals(BigDecimal.ZERO)) {
                map.put("openClaimRate", "0");
            } else {
                map.put("openClaimRate", openClaimNum.divide(couponCount, 2, BigDecimal.ROUND_CEILING));
            }
        }

        return userList;
    }

    /**
     * 小程序新增用户统计
     * @param page 分页
     * @return 数据
     */
    @Override
    public Object xcxUserCount(Page page) {
        return dataCountMapper.xcxUserCount(page.getOffset(),page.getLimit());
    }
}
