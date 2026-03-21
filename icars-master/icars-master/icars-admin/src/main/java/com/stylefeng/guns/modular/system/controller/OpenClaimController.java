package com.stylefeng.guns.modular.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.modular.system.constant.ApiResponseEntity;
import com.stylefeng.guns.modular.system.constant.OpenClaimOrderStatus;
import com.stylefeng.guns.modular.system.constant.poimaps.POIExportTitleMaps;
import com.stylefeng.guns.modular.system.model.BizOpenClaimExReport;
import com.stylefeng.guns.modular.system.model.Dept;
import com.stylefeng.guns.modular.system.model.User;
import com.stylefeng.guns.modular.system.service.IBizOpenClaimExReportService;
import com.stylefeng.guns.modular.system.service.IDeptService;
import com.stylefeng.guns.modular.system.utils.POIUtils;
import com.stylefeng.guns.modular.system.warpper.OpenClaimWarpper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.OpenClaim;
import com.stylefeng.guns.modular.system.service.IOpenClaimService;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制器
 *
 * @author kosan
 * @Date 2018-09-04 09:13:01
 */
@Controller
@RequestMapping("/openClaim")
public class OpenClaimController extends BaseController {

    private String PREFIX = "/system/openClaim/";
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IOpenClaimService openClaimService;

    @Autowired
    private IDeptService deptService;

    @Autowired
    private IBizOpenClaimExReportService bizOpenClaimExReportService;


    /**
     * 跳转到合作门店权限首页
     */
    @RequestMapping("/partner")
    public String partnerIndex() {
        return PREFIX + "partnerOpenClaim.html";
    }

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "openClaim.html";
    }

    /**
     * 跳转到录入定损金额
     */
    @RequestMapping("/openClaim_addFixLoss/{id}")
    public String openClaimAddFixLoss(@PathVariable Integer id, Model model) {
        OpenClaim openClaim = openClaimService.selectById(id);
        openClaim.setFixloss(openClaim.getFixloss() == null ? null : new BigDecimal(openClaim.getFixloss().stripTrailingZeros().toPlainString()));
        model.addAttribute("openClaim", openClaim);
        return PREFIX + "openClaim_addFixLoss.html";
    }

    /**
     * 跳转到异常反馈界面
     */
    @RequestMapping("/openClaim_addReportEx/{id}")
    public String openClaimAddReportEx(@PathVariable Integer id, Model model) {
        OpenClaim openClaim = openClaimService.selectById(id);
        model.addAttribute("openClaim", openClaim);
        return PREFIX + "openClaim_addReportEx.html";
    }

    /**
     * 跳转到录入结算付款凭证界面
     */
    @RequestMapping("/openClaim_addPayBillNo/{id}")
    public String openClaimAddPayBillNo(@PathVariable Integer id, Model model) {
        OpenClaim openClaim = openClaimService.selectById(id);
        model.addAttribute("openClaim", openClaim);
        return PREFIX + "openClaim_addPayBillNo.html";
    }

    /**
     * 跳转到录入结算付款凭证界面
     */
    @RequestMapping("/openClaim_addPayBillNoForClaim/{id}")
    public String openClaimAddPayBillNoForClaim(@PathVariable Integer id, Model model) {
        OpenClaim openClaim = openClaimService.selectById(id);
        model.addAttribute("openClaim", openClaim);
        return PREFIX + "openClaim_addPayBillNoForClaim.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/openClaim_update/{openClaimId}")
    public String openClaimUpdate(@PathVariable Integer openClaimId, Model model) {
        OpenClaim openClaim = openClaimService.selectById(openClaimId);
        model.addAttribute("item",openClaim);
        LogObjectHolder.me().set(openClaim);
        return PREFIX + "openClaim_edit.html";
    }

    /**
     * 跳转到详情
     */
    @RequestMapping("/openClaim_detail/{orderNo}")
    public String openClaimDetail(@PathVariable String orderNo, Model model) {
        OpenClaim openClaim = new OpenClaim();
        openClaim.setOrderno(orderNo);
        Map<String, Object> res = openClaimService.selectMap(new EntityWrapper<OpenClaim>().eq("orderno", orderNo));
        res.put("statusName", ConstantFactory.me().getOpenClaimOrderStatusName((Integer) res.get("status")));
        res.put("claimImgList", res.get("claimImg") == null ? null : res.get("claimImg").toString().split("\\|"));
        res.put("detailImgList", res.get("detailImg") == null ? null : res.get("detailImg").toString().split("\\|"));
        model.addAttribute("item",res);
        LogObjectHolder.me().set(openClaim);
        return PREFIX + "openClaim_detail.html";
    }


    /**
     * 获取列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(OpenClaim openClaim, @RequestParam(required = false) String condition, @RequestParam(required = false) String createStartTime, @RequestParam(required = false) String createEndTime) {
        Page<OpenClaim> page = new PageFactory<OpenClaim>().defaultPage();
        List<Map<String, Object>> claims = openClaimService.selectByCondition(page,null, openClaim, condition, createStartTime, createEndTime, null,page.getOrderByField(), page.isAsc());
        page.setRecords((List<OpenClaim>) new OpenClaimWarpper(claims).warp());
        return super.packForBT(page);
    }

    /**
     * 导出列表
     */
    @RequestMapping(value = "/list/export")
    @ResponseBody
    public void listExport(OpenClaim openClaim, @RequestParam(required = false) String condition, @RequestParam(required = false) String createStartTime, @RequestParam(required = false) String createEndTime, HttpServletResponse response) {
        List<Map<String, Object>> claims = openClaimService.selectByConditionForExport(openClaim, condition, createStartTime, createEndTime, null);
        List<Map<String, Object>> wrapperRes = (List<Map<String, Object>>)(new OpenClaimWarpper(claims).warp());
        try {
            // 告诉浏览器用什么软件可以打开此文件
            response.setHeader("content-Type", "application/vnd.ms-excel");
            // 下载文件的默认名称
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode("理赔订单"+DateUtil.getDay()+"数据.xlsx", "utf-8"));
            POIUtils.exportExcel(wrapperRes, response.getOutputStream(), "理赔订单数据", POIExportTitleMaps.OPEN_CLAIM_EXPORT);
        } catch (Exception e) {
            log.error("开放平台订单导出异常：",e);
        }
    }

    /**
     * 4S导出理赔单列表
     */
    @RequestMapping(value = "/partner/list/export")
    @ResponseBody
    public void listExportForPartner(OpenClaim openClaim, @RequestParam(required = false) String condition, @RequestParam(required = false) String createStartTime, @RequestParam(required = false) String createEndTime, HttpServletResponse response) {
        openClaim.setDeptid(ShiroKit.getUser().getDeptId());
        List<Map<String, Object>> claims = openClaimService.selectByConditionForExport(openClaim, condition, createStartTime, createEndTime, null);
        List<Map<String, Object>> wrapperRes = (List<Map<String, Object>>)(new OpenClaimWarpper(claims).warp());
        try {
            // 告诉浏览器用什么软件可以打开此文件
            response.setHeader("content-Type", "application/vnd.ms-excel");
            // 下载文件的默认名称
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode("理赔订单"+DateUtil.getDay()+"数据.xlsx", "utf-8"));
            POIUtils.exportExcel(wrapperRes, response.getOutputStream(), "理赔订单数据", POIExportTitleMaps.OPEN_CLAIM_PARTNER_EXPORT);
        } catch (Exception e) {
            log.error("开放平台订单导出异常：",e);
        }
    }

    /**
     * 获取门店接车单列表
     */
    @RequestMapping(value = "/partner/list")
    @ResponseBody
    public Object partnerList(OpenClaim openClaim, @RequestParam(required = false) String condition, @RequestParam(required = false) String createStartTime, @RequestParam(required = false) String createEndTime) {
        Page<OpenClaim> page = new PageFactory<OpenClaim>().defaultPage();
        openClaim.setDeptid(ShiroKit.getUser().getDeptId());
        List<Map<String, Object>> claims = openClaimService.selectByCondition(page,null, openClaim, condition, createStartTime, createEndTime, null, page.getOrderByField(), page.isAsc());
        for(Map var1 : claims){
            var1.remove("claimerPhone");
            var1.remove("rebateForCompany");
            var1.remove("rebateForEmp");
        }
        page.setRecords((List<OpenClaim>) new OpenClaimWarpper(claims).warp());
        return super.packForBT(page);
    }

    /**
     * 门店获取查询条件返回集合的定损金额合计
     */
    @RequestMapping(value = "/partner/fixLossSum")
    @ResponseBody
    public Object getFixLossSum(OpenClaim openClaim, @RequestParam(required = false) String condition, @RequestParam(required = false) String createStartTime, @RequestParam(required = false) String createEndTime) {
        openClaim.setDeptid(ShiroKit.getUser().getDeptId());
        BigDecimal fixLossSum = openClaimService.queryFixLossSumByCondition(openClaim, condition, createStartTime, createEndTime, null);
        return fixLossSum == null ? BigDecimal.ZERO : fixLossSum;
    }

    /**
     * 集团获取查询条件返回集合的定损金额合计
     */
    @RequestMapping(value = "/fixLossSum")
    @ResponseBody
    public Object getFixLossSumForAll(OpenClaim openClaim, @RequestParam(required = false) String condition, @RequestParam(required = false) String createStartTime, @RequestParam(required = false) String createEndTime) {
        BigDecimal fixLossSum = openClaimService.queryFixLossSumByCondition(openClaim, condition, createStartTime, createEndTime, null);
        return fixLossSum == null ? BigDecimal.ZERO : fixLossSum;
    }

    /**
     * 4S店确认接车
     */
    @RequestMapping(value = "/partner/accept")
    @ResponseBody
    public Object carAccept(@RequestParam Integer id) {
        OpenClaim openClaim = new OpenClaim();
        openClaim.setId(id);
        openClaim.setStatus(OpenClaimOrderStatus.SERVICING.getCode());
        openClaimService.updateById(openClaim);
        return SUCCESS_TIP;
    }

    /**
     * 4S店确认交车
     */
    @RequestMapping(value = "/partner/placed")
    @ResponseBody
    public Object carPlaced(@RequestParam Integer id) {
        OpenClaim openClaim = new OpenClaim();
        openClaim.setId(id);
        openClaim.setStatus(OpenClaimOrderStatus.PLACED.getCode());
        openClaimService.updateById(openClaim);
        return SUCCESS_TIP;
    }

    /**
     * 录入定损金额
     */
    @RequestMapping(value = "/partner/addFixloss")
    @ResponseBody
    public Object addFixloss(OpenClaim openClaim) {
        if(openClaim.getId() == null || openClaim.getFixloss() == null){
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }

        Dept dept = deptService.selectById(ShiroKit.getUser().getDeptId());
        if(dept == null){
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        Double scaleForEmp = dept.getScaleForEmp();
        Double scaleForCompany = dept.getScaleForCompany();
        openClaim.setRebateForCompany(scaleForCompany == null ? BigDecimal.ZERO : openClaim.getFixloss().multiply(new BigDecimal(scaleForCompany)).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
        if(openClaim.getRebateForCompany() != null && openClaim.getRebateForCompany().compareTo(BigDecimal.ZERO) == 1){
            openClaim.setRebateForEmp(scaleForEmp == null ? BigDecimal.ZERO : openClaim.getRebateForCompany().multiply(new BigDecimal(scaleForEmp)).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
        }
        openClaimService.updateById(openClaim);
        return SUCCESS_TIP;
    }

    /**
     * 录入异常信息
     */
    @RequestMapping(value = "/partner/addExMsg")
    @ResponseBody
    public Object addExMsg(OpenClaim openClaim, @RequestParam(required = false) String exMgs, @RequestParam(required = false) String exImgUrls) {
        openClaimService.reportExMsg(openClaim, exMgs, exImgUrls);
        return SUCCESS_TIP;
    }

    /**
     * 录入结算付款凭证号
     */
    @RequestMapping(value = "/partner/addPayBillNo")
    @ResponseBody
    public Object addPayBillNo(OpenClaim openClaim) {
        if(openClaim.getId() == null){
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        openClaim.setStatus(OpenClaimOrderStatus.SETTLED.getCode());
        openClaimService.updateById(openClaim);
        return SUCCESS_TIP;
    }

    /**
     * 录入结算付款凭证号 针对理赔老师
     */
    @RequestMapping(value = "/partner/addPayBillNoForClaim")
    @ResponseBody
    public Object addPayBillNoForClaim(OpenClaim openClaim) {
        if(openClaim.getId() == null){
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        openClaim.setStatus(OpenClaimOrderStatus.TRANSED.getCode());
        openClaimService.updateById(openClaim);
        return SUCCESS_TIP;
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(OpenClaim openClaim) {
        openClaimService.insert(openClaim);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
//    @RequestMapping(value = "/delete")
//    @ResponseBody
//    public Object delete(@RequestParam Integer openClaimId) {
//        openClaimService.deleteById(openClaimId);
//        return SUCCESS_TIP;
//    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(OpenClaim openClaim) {
        OpenClaim openClaim1 = openClaimService.selectById(openClaim.getId());
        openClaim1.setStatus(openClaim.getStatus());
        openClaimService.update(openClaim1);
//        openClaimService.updateById(openClaim);
        return SUCCESS_TIP;
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{openClaimId}")
    @ResponseBody
    public Object detail(@PathVariable("openClaimId") Integer openClaimId) {
        return openClaimService.selectById(openClaimId);
    }

    /**
     * 统计
     */
    @RequestMapping(value = "/countData")
    @ResponseBody
    public Object countData() {
        return openClaimService.countData();
    }


}
