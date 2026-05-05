package com.stylefeng.guns.modular.system.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.alipay.IAlipayService;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.base.tips.ErrorTip;
import com.stylefeng.guns.core.base.tips.Tip;
import com.stylefeng.guns.core.common.annotion.BussinessLog;
import com.stylefeng.guns.core.common.annotion.Permission;
import com.stylefeng.guns.core.common.constant.Const;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.modular.system.constant.dictmap.AccdDict;
import com.stylefeng.guns.modular.system.model.Accident;
import com.stylefeng.guns.modular.system.model.BizWxUser;
import com.stylefeng.guns.modular.system.service.IAccdService;
import com.stylefeng.guns.modular.system.service.IBizWxUserService;
import com.stylefeng.guns.modular.system.vo.BizAlipayBillVo;
import com.stylefeng.guns.modular.system.warpper.BizAlipayBillWarpper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.BizAlipayBill;
import com.stylefeng.guns.modular.system.service.IBizAlipayBillService;

import java.util.List;
import java.util.Map;

/**
 * 支付宝支付管理控制器
 *
 * @author kosan
 * @Date 2018-08-01 10:09:52
 */
@Controller
@RequestMapping("/bizAlipayBill")
public class BizAlipayBillController extends BaseController {

    private String PREFIX = "/system/bizAlipayBill/";

    @Autowired
    private IBizAlipayBillService bizAlipayBillService;

    @Autowired
    private IAlipayService alipayService;

    @Autowired
    private IBizWxUserService bizWxUserService;

    @Autowired
    private IAccdService accdService;

    /**
     * 跳转到支付宝支付管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "bizAlipayBill.html";
    }

    /**
     * 跳转到添加支付宝支付管理
     */
    @RequestMapping("/bizAlipayBill_add")
    public String bizAlipayBillAdd() {
        return PREFIX + "bizAlipayBill_add.html";
    }

//    /**
//     * 跳转到修改支付宝支付管理
//     */
//    @RequestMapping("/bizAlipayBill_update/{bizAlipayBillId}")
//    public String bizAlipayBillUpdate(@PathVariable Integer bizAlipayBillId, Model model) {
//        BizAlipayBill bizAlipayBill = bizAlipayBillService.selectById(bizAlipayBillId);
//        model.addAttribute("item",bizAlipayBill);
//        LogObjectHolder.me().set(bizAlipayBill);
//        return PREFIX + "bizAlipayBill_edit.html";
//    }

    /**
     * 获取支付宝支付管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = true) Integer payStatus, @RequestParam(required = true) String alipayAccount, @RequestParam(required = true) String startTime, @RequestParam(required = true) String endTime) {
        Page<BizAlipayBill> page = new PageFactory<BizAlipayBill>().defaultPage();
        List<Map<String, Object>> result = bizAlipayBillService.selectListForPage(page, payStatus, alipayAccount, startTime, endTime);
        page.setRecords((List<BizAlipayBill>) new BizAlipayBillWarpper(result).warp());
        return super.packForBT(page);
    }

    /**
     * 事故发起重新支付
     */
    @RequestMapping("/rePay")
    @BussinessLog(value = "事故发起重新支付", key = "accdId", dict = AccdDict.class)
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Tip rePay(@RequestParam Integer accdId) {
        if (ToolUtil.isEmpty(accdId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        Accident accident = accdService.selectById(accdId);
        if(accident != null && accident.getStatus() == 2){
            BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(accident.getOpenid(), null);
            if(bizWxUser != null){
                if(StringUtils.isNotEmpty(bizWxUser.getAlipayAccount())){
                    boolean triggerResult = alipayService.autoTrigger(bizWxUser.getAlipayAccount(), accident.getId().intValue());
                    if(triggerResult){
                        return SUCCESS_TIP;
                    }else{
                        return new ErrorTip(4001, "支付失败");
                    }
                } else {
                    return new ErrorTip(4001, "支付失败，用户未绑定支付宝");
                }
            }
        }
        return new ErrorTip(500,"操作失败，事故未通过审核");
    }

//    /**
//     * 新增支付宝支付管理
//     */
//    @RequestMapping(value = "/add")
//    @ResponseBody
//    public Object add(BizAlipayBill bizAlipayBill) {
//        bizAlipayBillService.insert(bizAlipayBill);
//        return SUCCESS_TIP;
//    }

//    /**
//     * 删除支付宝支付管理
//     */
//    @RequestMapping(value = "/delete")
//    @ResponseBody
//    public Object delete(@RequestParam Integer bizAlipayBillId) {
//        bizAlipayBillService.deleteById(bizAlipayBillId);
//        return SUCCESS_TIP;
//    }

//    /**
//     * 修改支付宝支付管理
//     */
//    @RequestMapping(value = "/update")
//    @ResponseBody
//    public Object update(BizAlipayBill bizAlipayBill) {
//        bizAlipayBillService.updateById(bizAlipayBill);
//        return SUCCESS_TIP;
//    }

//    /**
//     * 支付宝支付管理详情
//     */
//    @RequestMapping(value = "/detail/{bizAlipayBillId}")
//    @ResponseBody
//    public Object detail(@PathVariable("bizAlipayBillId") Integer bizAlipayBillId) {
//        return bizAlipayBillService.selectById(bizAlipayBillId);
//    }
}
