package com.stylefeng.guns.modular.system.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.base.tips.Tip;
import com.stylefeng.guns.core.common.annotion.Permission;
import com.stylefeng.guns.core.common.constant.Const;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.modular.system.model.Accident;
import com.stylefeng.guns.modular.system.model.BizWxpayBill;
import com.stylefeng.guns.modular.system.warpper.BizClaimWarpper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.BizClaim;
import com.stylefeng.guns.modular.system.service.IBizClaimService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 控制器
 *
 * @author kosan
 * @Date 2018-08-27 10:39:18
 */
@Controller
@RequestMapping("/bizClaim")
public class BizClaimController extends BaseController {

    private String PREFIX = "/system/bizClaim/";

    @Autowired
    private IBizClaimService bizClaimService;

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "bizClaim.html";
    }

    /**
     * 跳转到添加
     */
    @RequestMapping("/bizClaim_add")
    public String bizClaimAdd() {
        return PREFIX + "bizClaim_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/bizClaim_update/{bizClaimId}")
    public String bizClaimUpdate(@PathVariable Integer bizClaimId, Model model) {
        BizClaim bizClaim = bizClaimService.selectById(bizClaimId);
        model.addAttribute("item",bizClaim);
        LogObjectHolder.me().set(bizClaim);
        return PREFIX + "bizClaim_edit.html";
    }

    /**
     * 跳转到推送理赔顾问页面
     */
    @RequestMapping("/claims_push_claims")
    public String addPushClaimsView() {
        return PREFIX + "claims_push_claims.html";
    }

    /**
     * 获取列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(BizClaim bizClaim, @RequestParam(required = false) String startTime, @RequestParam(required = false) String endTime) {
        Page<BizClaim> page = new PageFactory<BizClaim>().defaultPage();
        List<Map<String, Object>> result = bizClaimService.selectListForPage(page, bizClaim, startTime, endTime);
        page.setRecords((List<BizClaim>) new BizClaimWarpper(result).warp());
        return super.packForBT(page);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(BizClaim bizClaim) {
        bizClaimService.insert(bizClaim);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer bizClaimId) {
        bizClaimService.deleteById(bizClaimId);
        return SUCCESS_TIP;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(BizClaim bizClaim) {
        bizClaimService.updateById(bizClaim);
        return SUCCESS_TIP;
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{bizClaimId}")
    @ResponseBody
    public Object detail(@PathVariable("bizClaimId") Integer bizClaimId) {
        return bizClaimService.selectById(bizClaimId);
    }

    /**
     * 事故推送理赔顾问
     */
    @RequestMapping("/pushClaims")
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Tip pushClaims(@RequestParam Integer id, @RequestParam String account) {
        if (ToolUtil.isEmpty(id) || ToolUtil.isEmpty(account)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        BizClaim bizClaim = bizClaimService.selectById(id);
        bizClaim.setClaimer(account);
        bizClaimService.updateById(bizClaim);
        return SUCCESS_TIP;
    }
}
