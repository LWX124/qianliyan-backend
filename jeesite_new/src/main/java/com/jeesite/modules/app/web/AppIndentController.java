/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.entity.*;
import com.jeesite.modules.app.service.*;
import com.jeesite.modules.file.entity.FileUpload;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.util2.FileWithExcelUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单信息表Controller
 *
 * @author zcq
 * @version 2019-08-05
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appIndent")
public class AppIndentController extends BaseController {

    @Autowired
    private AppIndentService appIndentService;

    @Resource
    private AppUserService appUserService;

    @Resource
    private AppBUserService appBUserService;

    @Resource
    private AppClaimTeacherService appClaimTeacherService;

    @Resource
    private AppImgService appImgService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public AppIndent get(Long id, boolean isNewRecord) {
        return appIndentService.get(String.valueOf(id), isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("app:appIndent:view")
    @RequestMapping(value = {"list", ""})
    public String list(AppIndent appIndent, Model model) {

        //新订单
        Integer newIndentNumber = appIndentService.findinewIndentNumber();
        if (newIndentNumber == null) {
            newIndentNumber = 0;
        }
        appIndent.setNewIndentNumber(newIndentNumber);

        //服务中
        Integer inServiceNumer = appIndentService.findInServiceNumer();
        if (inServiceNumer == null) {
            inServiceNumer = 0;
        }
        appIndent.setInServiceNumber(inServiceNumer);

        //已交车
        Integer forwardNumber = appIndentService.findforwardNumber();
        if (forwardNumber == null) {
            forwardNumber = 0;
        }
        appIndent.setForwardNumber(forwardNumber);

        //已结算
        Integer settleNumer = appIndentService.findsettleNumer();
        if (settleNumer == null) {
            settleNumer = 0;
        }
        appIndent.setSettledNumber(settleNumer);

        //已完成
        Integer finishNumber = appIndentService.findfinishNumber();
        if (finishNumber == null) {
            finishNumber = 0;
        }
        appIndent.setFinishNumber(finishNumber);

        //新订单 预估总金额。
        BigDecimal inNew = appIndentService.findEstimatedAmountINew();
        if (inNew == null) {
            inNew = BigDecimal.ZERO;
        }
        appIndent.setEstimatedAmountNew(inNew);

        // 服务中 预估总金额。
        BigDecimal inSer = appIndentService.findEstimatedAmountInSer();
        if (inSer == null) {
            inSer = BigDecimal.ZERO;
        }
        appIndent.setEstimatedAmountInSer(inSer);

        // 结算总金额
        BigDecimal allAmount = appIndentService.findAllSettleAmount();
        if (allAmount == null) {
            allAmount = BigDecimal.ZERO;
        }
        appIndent.setAllSettlementAmount(allAmount);


        model.addAttribute("appIndent", appIndent);
        return "modules/app/appIndentList";
    }


    @RequestMapping(value = "mess")
    public String messList(AppIndent appIndent, Model model) {

        String id = appIndent.getId();
        String upId = null;
        String userBId = null;
        String name = null;
        Integer type = appIndent.getType();
        AppIndent appIndent1 = new AppIndent();
        if (type == 1) {
            //图片上架
            upId = id;
            appIndent1.setUpId(upId);
             name = appIndentService.findMerchantsName(upId);
        } else {
            userBId = id;
            appIndent1.setUserBId(Integer.valueOf(userBId));
             name = appIndentService.appIndentUsBName(userBId);
        }

        appIndent1.setName(name);

        Integer umb = appIndentService.findByState(upId, userBId, 1);
        //新订单
        appIndent1.setNewIndentNumber(umb);
        //服务中
        Integer inServiceNumer = appIndentService.findByState(upId, userBId, 3);
        appIndent1.setInServiceNumber(inServiceNumer);
        //已交车
        Integer forwardNumber = appIndentService.findByState(upId, userBId, 4);
        appIndent1.setForwardNumber(forwardNumber);
        //已结算
        Integer settleNumer = appIndentService.findByState(upId, userBId, 5);
        appIndent1.setSettledNumber(settleNumer);
        //已完成
        Integer finishNumber = appIndentService.findByState(upId, userBId, 6);
        appIndent1.setFinishNumber(finishNumber);
        //新订单 预估总金额。
        BigDecimal inNew = appIndentService.findByFixuser(upId, userBId, 1);
        appIndent1.setEstimatedAmountNew(inNew);
        // 服务中 预估总金额。
        BigDecimal inSer = appIndentService.findByFixuser(upId, userBId, 3);
        appIndent1.setEstimatedAmountInSer(inSer);
        // 结算总金额
        BigDecimal allAmount = appIndentService.findByUser(upId, userBId, 5);
        appIndent1.setAllSettlementAmount(allAmount);

        model.addAttribute("appIndent1", appIndent1);
        return "modules/app/appIndentList111";
    }

    @RequestMapping(value = "listData111")
    @ResponseBody
    public Page<AppIndent> listData111(AppIndent appIndent1, HttpServletRequest request, HttpServletResponse response) {
        appIndent1.setPage(new Page<>(request, response));
        appIndent1.setLicensePlate(null);
        Page<AppIndent> page = appIndentService.findPage(appIndent1);
        return page;
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("app:appIndent:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<AppIndent> listData(AppIndent appIndent, HttpServletRequest request, HttpServletResponse response) {
        appIndent.setPage(new Page<>(request, response));
        Page<AppIndent> page = appIndentService.findPage(appIndent);
        if(StringUtils.isNotEmpty(appIndent.getRemake())){
            Page<AppIndent> remake = appIndentService.findRemake(appIndent);
            return remake;
        }
        return page;
    }

    //    professionalListData
    @RequiresPermissions("app:appIndent:view")
    @RequestMapping(value = "professionalListData")
    @ResponseBody
    public Page<AppIndent> professionalListData(AppIndent appIndent, HttpServletRequest request, HttpServletResponse response) {
        appIndent.setPage(new Page<>(request, response));
        Page<AppIndent> page = appIndentService.findprofessionalPage(appIndent);
        return page;
    }


    @RequiresPermissions("app:appIndent:view")
    @RequestMapping(value = "professionalWorkList")
    public String substituteDirveMerchants(AppIndent appIndent, Model model) {
        model.addAttribute("appIndent", appIndent);
        return "modules/app/professionalWorkList";
    }

    @RequestMapping(value = "fincAllFixuser")
    public JSONObject fincAllFixuser(String username, String plan) {
        JSONObject reslut = new JSONObject();
        if (StringUtils.isNotEmpty(username) || StringUtils.isNotEmpty(plan)) {
            BigDecimal allFix = appIndentService.findAllFix(username, plan);
            reslut.put("data", allFix);
        } else {
            reslut.put("data", "暂无,请重新查询");
        }
        return reslut;
    }


    /**
     * 查看编辑表单
     */
    @RequiresPermissions("app:appIndent:view")
    @RequestMapping(value = "form")
    public String form(AppIndent appIndent, Model model) {
        //查询比例
        BigDecimal ratio = new BigDecimal("0");
        Integer userId = appIndent.getUserId();
        AppClaimTeacher level = appClaimTeacherService.findClaimByUserId(userId);
        if (level != null) {
            if (level.getLevel().equals("D")) {
                ratio = new BigDecimal("0.05");
            } else if (level.getLevel().equals("C")) {
                ratio = new BigDecimal("0.06");
            } else if (level.getLevel().equals("B")) {
                ratio = new BigDecimal("0.07");
            }
            BigDecimal dealOutput = appClaimTeacherService.findDealOutput(userId);
            if (dealOutput.compareTo(new BigDecimal("300000")) == 1) { //大于于
                BigDecimal subtract = dealOutput.subtract(new BigDecimal("300000"));
                BigDecimal divide = subtract.divide(new BigDecimal("50000"), 0, BigDecimal.ROUND_HALF_UP);
                BigDecimal add = divide.add(new BigDecimal("1"));
                BigDecimal divide1 = add.divide(new BigDecimal("1000"), 0, BigDecimal.ROUND_HALF_UP);
                ratio = ratio.add(divide1);
            }

            appIndent.setSettlementRatio(ratio);
        } else {
            appIndent.setSettlementRatio(new BigDecimal("0"));
        }

        model.addAttribute("appIndent", appIndent);
        return "modules/app/appIndentForm";
    }

    /**
     * 保存订单信息表
     */
    @RequiresPermissions("app:appIndent:edit")
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated AppIndent appIndent) {

        //判断是否是修改
//        if (appIndent.getId()==null){
//            //查询到图片，并且删除
//            List<Integer> imgidList = appImgService.findAllImg(appIndent.getId());
//            if (!imgidList.isEmpty()){
//                for (Integer integer : imgidList) {
//                    appImgService.delete(appImgService.get(integer.toString()));
//                }
//            }
//
//            FileUploadUtils.saveFileUpload(appIndent.getId(), "indent_img");
//            List<FileUpload> fileUpload = FileUploadUtils.findFileUpload(appIndent.getId(), "indent_img");
//            for (int i = 0; i < fileUpload.size(); i++) {
//                FileUpload file = fileUpload.get(i);
//                String uploadId = file.getId();
//                String newUrl = appBUserService.findNewUrl(uploadId);
//                AppImg appImg = new AppImg();
//                appImg.setKeyId(Long.valueOf(appIndent.getId()));
//                Integer index= i+1;
//                appImg.setIndex(index.longValue());
//                appImg.setType(3L);
//                appImg.setUrl(newUrl);
//                appImg.setCreatTime(new Date());
//                appImg.setUpdateTime(new Date());
//                appImgService.insertNew(appImg);
//            }
//
//
//        }
        appIndentService.save(appIndent);
        return renderResult(Global.TRUE, text("保存订单信息表成功！"));
    }

    /**
     * 删除订单信息表
     */
    @RequiresPermissions("app:appIndent:edit")
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(AppIndent appIndent) {
        appIndentService.delete(appIndent);
        return renderResult(Global.TRUE, text("删除订单信息表成功！"));
    }

    @RequestMapping(value = "settlement")
    @ResponseBody
    public JSONObject settlement(String id) {
        JSONObject reslut = new JSONObject();
        //根据id查询到对应的订单，

        //判断是否是公司员工订单，如果是就不结算金额


        AppIndent appIndent = appIndentService.get(id);
        Integer state = appIndent.getState();
        if (state != 5) {
            //不能处理
            reslut.put("msg", false);
            reslut.put("data", "订单状态不能操作");
            return reslut;
        }

        //根据佣金比例和结算金额结算到用户金额
//        BigDecimal settleAccounts = appIndent.getSettleAccounts();
        BigDecimal commissionRate = appIndent.getCommissionRate();
        //4s结算公司金额
        BigDecimal settleFoursCompany = appIndent.getSettleFoursCompany();
        //4s结算公司比例
//        BigDecimal settleFoursCompanyRate = appIndent.getSettleFoursCompanyRate();
//
        BigDecimal fixlossUser = appIndent.getFixlossUser();

        //给用户添加余额,还有plus会员
        Integer userId = appIndent.getUserId();
        AppUser appUser = appUserService.get(String.valueOf(userId));
        //获取到用户数据
        //查询plus会员id
        Integer parentId = appUser.getParentId();

        if (fixlossUser == null) {
            reslut.put("msg", false);
            reslut.put("data", "没有结算给用户金额");
            return reslut;
        }
        //有父级用户
//        if (parentId != 0) {
//            //佣金比例减去百分之2
//            BigDecimal subtract = commissionRate.subtract(new BigDecimal(Double.toString(0.02)));
//            //结算金额乘 减去得佣金比例
//            appUserService.addAmount4Parent(userId, settleFoursCompany.multiply(subtract), parentId, id, appIndent);
//        } else {
        BigDecimal subtract = commissionRate.subtract(new BigDecimal(Double.toString(0.01)));
        appUserService.addAmount4User(userId, fixlossUser, id, appIndent);
        //平台抽成  settleFoursCompany.multiply(subtract)
//        AppUserAccountRecord appUserAcount = new AppUserAccountRecord();
		/*private Integer momey;		// 金额
			private Long userId;		// 用户id
			private Integer type;		// 操作类型 1：提现  2：提现失败回滚 3:发送红包 4:订单结算
			private Date createTime;		// 发生时间
			private Integer addFlag;		// 1加钱  2 减钱
			private Integer source;		// 1:c端  2：b端
			private Long bussinessId;		// 业务id*/

//        BigDecimal i = fixlossUser.multiply(new BigDecimal("0.9"));
//        appUserAcount.setMomey(i);
//        appUserAcount.setUserId(0L);
//        appUserAcount.setType(-1);          //平台抽成
//
//        appUserAcount.setCreateTime(new Date());
//        appUserAcount.setAddFlag(1);
//        appUserAcount.setSource(1);
//        appUserAcount.setBusinessId(id);
//        appUserAccountRecordDao.insertCus(appUserAcount);


        //添加极光土推送。订单结算，推送给B端和C端
        /*private String source;		// 来源（C端，B端）
	private String ispush;		// 是否推送（0，没有，1，有）
	private String type;		// 操作类型()
	private String userId;		// C端用户id
	private String userBId;		// B端用户id
	private Date createTime;		// 创建时间
	private Date updateTime;		// 修改时间*/
//        try {
//            AppJgPush appJgPush = new AppJgPush();
//            appJgPush.setSource("C");
//            appJgPush.setIspush("0");
//            appJgPush.setType("2");
//            appJgPush.setUserId(String.valueOf(userId));
//            appJgPush.setCreateTime(new Date());
//            appJgPush.setUpdateTime(new Date());
//            appJgPushService.insertJpush(appJgPush);
//            String userId1 = appJgPush.getUserId();
//            String userBId = appJgPush.getUserBId();
//            if (StringUtils.isEmpty(userId1)) {
//                userId1 = "0";
//            }
//            if (StringUtils.isEmpty(userBId)) {
//                userBId = "0";
//            }
//            Producer.productSend(appJgPush.getSource(), appJgPush.getType(), userId1, userBId);
//            appJgPush.setSource("B");
//            appJgPush.setUserBId(String.valueOf(appIndent.getUserBId()));
//            appJgPush.setUserId(null);
//            appJgPushService.insertJpush(appJgPush);
//            String userId2 = appJgPush.getUserId();
//            String userBId2 = appJgPush.getUserBId();
//            if (StringUtils.isEmpty(userId2)) {
//                userId2 = "0";
//            }
//            if (StringUtils.isEmpty(userBId2)) {
//                userBId2 = "0";
//            }
//            Producer.productSend(appJgPush.getSource(), appJgPush.getType(), userId2, userBId2);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        reslut.put("msg", true);
        reslut.put("data", "操作成功");
        return reslut;

    }

    /**
     * 统计
     */
    @RequestMapping(value = "/countData")
    @ResponseBody
    public JSONObject countData(Integer val) {
        return appIndentService.countData();
    }

    @RequestMapping(value = "/updateNoCome")
    @ResponseBody
    public String updateNoCome(String id) {

        AppIndent appIndent = appIndentService.get(id);
        appIndent.setState(2);
        appIndentService.update(appIndent);
        return renderResult(Global.TRUE, text("修改车辆状态成功！"));

    }


    @RequestMapping("/export")
    public String export(HttpServletResponse response, String beginTime, String lastTime, String dealTime,
                         String messageSource, String sendPeople, String sendUnit, String state, String account, String sendBack,
                         String orderNumber, String licensePlate, Integer userBId,
                         String lastUpdateTime, String beginUpdateTime) {
        if (StringUtils.isEmpty(beginTime)) {
            beginTime = "2018-02-16 16:24";
        }
        if (StringUtils.isEmpty(lastTime)) {
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            lastTime = formatter.format(date);
        }

        if (StringUtils.isNotEmpty(beginUpdateTime) && StringUtils.isEmpty(lastUpdateTime)) {
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            lastUpdateTime = formatter.format(date);
        }

        if (StringUtils.isNotEmpty(lastUpdateTime) && StringUtils.isEmpty(beginUpdateTime)) {
            beginUpdateTime = "2018-02-16 16:24";
        }

        try {
            //模拟从数据库获取需要导出的数据
            List<AppExcel> indentList = appIndentService.findExcelMesg(beginTime, lastTime, dealTime,
                    messageSource, sendPeople, sendUnit, state, account,sendBack, orderNumber, licensePlate, userBId,
                    lastUpdateTime, beginUpdateTime);
            FileWithExcelUtil.exportExcel(indentList, "订单信息表", "订单表", AppExcel.class, "订单表.xls", response);
            return renderResult(Global.TRUE, text("导出订单信息表成功！"));
        } catch (Exception e) {
            logger.info("getCustomerPage", e);
            return renderResult(Global.FALSE, text("导出订单信息表失败，请联系管理员！"));
            // TODO: handle exception
        }
    }

    @RequestMapping("/addPeople")
    public String addPeople() {
        return "modules/app/addPeople";
    }

    @RequestMapping("/findIsCompany")
    @ResponseBody
    public JSONObject findIsCompany() {
        JSONObject result = new JSONObject();
        List<AppBUser> appBUser = appBUserService.findIsCompany();
        if (appBUser.isEmpty()) {
            result.put("code", 0);
            result.put("msg", "暂无数据");
            result.put("count", "");
            result.put("data", "");
        }
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", appBUser.size());
        result.put("data", appBUser);
        return result;
    }

    @RequestMapping("/removeCompany")
    @ResponseBody
    public JSONObject removeCompany(String userBId) {
        JSONObject result = new JSONObject();
        //修改userbid  isCompany
        AppBUser appBUser = appBUserService.get(userBId);
        appBUser.setIsCompany(0);
        appBUserService.update(appBUser);
        result.put("stat", "success");
        return result;
    }

    //添加数据
    @RequestMapping("/addisCompany")
    @ResponseBody
    public JSONObject addisCompany(String phone) {
        JSONObject data = new JSONObject();

        //根据手机查询到商户
        AppBUser appBUser = appBUserService.findBByPhone(phone);

        if (appBUser == null) {
            data.put("stat", "false");
            return data;
        }
        if (appBUser.getState() != 1) {
            data.put("stat", "innuable");
            return data;
        }
        appBUser.setIsCompany(1);
        appBUserService.update(appBUser);
        data.put("stat", "success");
        return data;
    }

    @RequestMapping("/addMore")
    public ModelAndView addMore(String id) {
        ModelAndView mv = new ModelAndView("modules/app/settlementClaims");
        mv.addObject("id", id);
        return mv;
    }

    //查询出所有有用户id的理赔老师的id·
    @RequestMapping("/findClaimsTeacher")
    public JSONObject findClaimsTeacher(Integer name) {
        List<AppLeague> userList = appIndentService.findClaimsTeacher();
        List<ClaimsTeacher> claimsTeacherList = new ArrayList<>();
        for (AppLeague appLeague : userList) {
            ClaimsTeacher claimsTeacher = new ClaimsTeacher();
            claimsTeacher.setName(appLeague.getName());
            claimsTeacher.setValue(appLeague.getValue());
            claimsTeacher.setIndentId(name);
            claimsTeacherList.add(claimsTeacher);
        }
        JSONObject data = new JSONObject();
        data.put("data", claimsTeacherList);
        return data;

    }


    @RequestMapping(value = "/getClaimsTeacher", method = RequestMethod.POST)
    public JSONObject getClaimsTeacher(@RequestBody List<AppLeague> selectArr) {
        JSONObject result = new JSONObject();
        //遍历添加数据到新表种
        //System.out.println(selectArr);
        return result;
    }


    @Resource
    private AppLableDetailsReviewTreeService appLableDetailsReviewTreeService;

    @Resource
    private AppBusinessConfirmService appBusinessConfirmService;

    @RequestMapping(value = "/trys")
    public JSONObject trys() {
        JSONObject result = new JSONObject();
        //查询到所有带有事故的服务的商户，获取到一个商户idlist
        List<Integer> userBidList = appLableDetailsReviewTreeService.selectAllAccident();
        //根据商户id来操作
        for (Integer userBId : userBidList) {
            //查询下面所有的明细，拿到比例list，放到每个对应的比例中
            List<BigDecimal> reviewTreeList = appLableDetailsReviewTreeService.selectAllList(userBId);
            //遍历每个明细，放到每个数据中

            BigDecimal bigDecimal = reviewTreeList.get(0);
            BigDecimal bigDecimal1 = reviewTreeList.get(1);
            BigDecimal bigDecimal2 = reviewTreeList.get(2);
            BigDecimal bigDecimal3 = reviewTreeList.get(3);
            BigDecimal bigDecimal4 = reviewTreeList.get(4);
            BigDecimal bigDecimal5 = reviewTreeList.get(5);
            BigDecimal bigDecimal6 = reviewTreeList.get(6);
            BigDecimal bigDecimal7 = reviewTreeList.get(7);

            AppBusinessConfirm appBusinessConfirm1 = new AppBusinessConfirm();
            appBusinessConfirm1.setBusinessConfirm("店内保险");
            appBusinessConfirm1.setAccidentReponsibility("全责");
            appBusinessConfirm1.setCustomersHave(bigDecimal);
            appBusinessConfirm1.setNotUnitedCustomer(bigDecimal1);
            appBusinessConfirm1.setCharterShop(bigDecimal2);
            appBusinessConfirm1.setUserBId(userBId);
            appBusinessConfirm1.setCreateTime(new Date());
            appBusinessConfirm1.setUpdateTime(new Date());
            appBusinessConfirmService.insertCus(appBusinessConfirm1);

            AppBusinessConfirm appBusinessConfirm2 = new AppBusinessConfirm();
            appBusinessConfirm2.setBusinessConfirm("店内保险");
            appBusinessConfirm2.setAccidentReponsibility("三责");
            appBusinessConfirm2.setCustomersHave(bigDecimal3);
            appBusinessConfirm2.setNotUnitedCustomer(bigDecimal4);
            appBusinessConfirm2.setCharterShop(bigDecimal5);
            appBusinessConfirm2.setUserBId(userBId);
            appBusinessConfirm2.setCreateTime(new Date());
            appBusinessConfirm2.setUpdateTime(new Date());
            appBusinessConfirmService.insertCus(appBusinessConfirm2);

            AppBusinessConfirm appBusinessConfirm3 = new AppBusinessConfirm();
            appBusinessConfirm3.setBusinessConfirm("店外保险");
            appBusinessConfirm3.setAccidentReponsibility("包含");
            appBusinessConfirm3.setCustomersHave(bigDecimal6);
            appBusinessConfirm3.setNotUnitedCustomer(bigDecimal7);
            appBusinessConfirm3.setCharterShop(bigDecimal7);
            appBusinessConfirm3.setUserBId(userBId);
            appBusinessConfirm3.setCreateTime(new Date());
            appBusinessConfirm3.setUpdateTime(new Date());
            appBusinessConfirmService.insertCus(appBusinessConfirm3);

        }
        return result;
    }


    //提交到后台


    @RequestMapping(value = "/findIndentImg", method = RequestMethod.GET)
    public JSONObject findIndentImg(String indentId) {
        JSONObject result = new JSONObject();
        //遍历添加数据到新表种
        List<String> imgs = appIndentService.findIndentImgs(indentId);
        result.put("imgList", imgs);
        return result;
    }


    @RequestMapping(value = "/recIndent")
    @ResponseBody
    public String recIndent(String ids) {
        //遍历添加数据到新表种
        AppIndent appIndent = appIndentService.get(ids);
        appIndent.setIsRec(1);
        appIndent.setCollateTime(new Date());
        appIndentService.update(appIndent);
        return renderResult(Global.TRUE, text("操作成功！"));
    }


}