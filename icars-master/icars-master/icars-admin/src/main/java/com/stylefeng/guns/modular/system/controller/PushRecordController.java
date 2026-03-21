package com.stylefeng.guns.modular.system.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.annotion.Permission;
import com.stylefeng.guns.core.common.constant.Const;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.modular.system.model.PushRecord;
import com.stylefeng.guns.modular.system.service.IPushRecordService;
import com.stylefeng.guns.modular.system.warpper.PushRecordWarpper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 控制器
 *
 * @author kosan
 * @Date 2018-07-26 15:06:11
 */
@Controller
@RequestMapping("/pushRecord")
public class PushRecordController extends BaseController {

    private String PREFIX = "/system/pushRecord/";

    @Autowired
    private IPushRecordService pushRecordService;

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "pushRecord.html";
    }

    /**
     * 跳转到4S查询页面
     */
    @RequestMapping("/fsMgr")
    public String addView() {
        return PREFIX + "pushRecord4s.html";
    }

    /**
     * 获取推送记录列表
     */
    @RequestMapping("/list")
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Object list(@RequestParam(required = false) String pushStartTime, @RequestParam(required = false) String pushEndTime, @RequestParam(required = false) String account) {
        Page<PushRecord> page = new PageFactory<PushRecord>().defaultPage();
        List<Map<String, Object>> result = pushRecordService.getPushRecords(page, pushStartTime, pushEndTime, account, page.getOrderByField(), page.isAsc());
        page.setRecords((List<PushRecord>) new PushRecordWarpper(result).warp());
        return super.packForBT(page);
    }

    /**
     * 获取推送4S记录列表
     */
    @RequestMapping("/pushFS/list")
    @ResponseBody
    public Object listFS(@RequestParam(required = false) String pushStartTime, @RequestParam(required = false) String pushEndTime) {
        Page<PushRecord> page = new PageFactory<PushRecord>().defaultPage();
        List<Map<String, Object>> result = pushRecordService.getPushFSRecords(page, pushStartTime, pushEndTime, ShiroKit.getUser().getDeptId(), page.getOrderByField(), page.isAsc());
        page.setRecords((List<PushRecord>) new PushRecordWarpper(result).warp());
        return super.packForBT(page);
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Object delete(@RequestParam Integer pushRecordId) {
        pushRecordService.deleteById(pushRecordId);
        return SUCCESS_TIP;
    }

    /**
     * 推送记录设置为已查看
     */
    @RequestMapping(value = "/changeStatus")
    @ResponseBody
    public Object changeStatus(@RequestParam Integer id) {
        pushRecordService.setStatus(id, 1, new Date());        return SUCCESS_TIP;
    }


}
