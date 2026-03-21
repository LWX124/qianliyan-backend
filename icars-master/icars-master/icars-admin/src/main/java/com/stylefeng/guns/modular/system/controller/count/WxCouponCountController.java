package com.stylefeng.guns.modular.system.controller.count;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.system.model.AlipayActivity;
import com.stylefeng.guns.modular.system.model.BizWxpayBill;
import com.stylefeng.guns.modular.system.model.WxCouponCountModel;
import com.stylefeng.guns.modular.system.service.DataCountService;
import com.stylefeng.guns.modular.system.service.IBizWxpayBillService;
import com.stylefeng.guns.modular.system.warpper.AlipayActivityWarpper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 红包统计
 *
 * @author kosan
 * @Date 2019-03-14 14:57:25
 */
@Controller
@RequestMapping("/couponCount")
public class WxCouponCountController extends BaseController {

    private String PREFIX = "/system/countData/";

    @Autowired
    private DataCountService dataCountService;

    /**
     * 跳转到红包统计首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "couponCount.html";
    }

    /**
     * 获取coupon列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String startTime, String endTime) {
        return dataCountService.countData(startTime, endTime);
    }

//    /**
//     * 获取coupon列表
//     */
//    @RequestMapping(value = "/adjustersCount")
//    public String adjustersHome() {
//        return PREFIX + "adjustersCount.html";
//    }
//    /**
//     * 获取coupon列表
//     */
//    @RequestMapping(value = "/adjustersList")
//    @ResponseBody
//    public Object adjustersList(String startTime, String endTime,String name,String department) {
//        return dataCountService.userCount(startTime, endTime,name,department);
//    }


}
