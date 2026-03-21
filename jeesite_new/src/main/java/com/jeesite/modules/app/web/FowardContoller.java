package com.jeesite.modules.app.web;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.modules.app.entity.AppAccidentRecord;
import com.jeesite.modules.app.entity.AppRepeatAccident;
import com.jeesite.modules.app.entity.BizAccident;
import com.jeesite.modules.app.service.AppAccidentRecordService;
import com.jeesite.modules.app.service.AppRepeatAccidentService;
import com.jeesite.modules.app.service.BizAccidentService;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(value = "${adminPath}/app/forward")
public class FowardContoller {
    @Resource
    private AppAccidentRecordService appAccidentRecordService;

    @Resource
    private AppRepeatAccidentService appRepeatAccidentService;

    @Resource
    private BizAccidentService bizAccidentService;


    @GetMapping(value = "/test")
    public String repaly(Model model, String accId, String type) {
        //查询到重复事故的url
        //List<AppAccidentRecord> stringList =  appAccidentRecordService.selectRepart(accId,type);
        AppAccidentRecord appAccidentRecord = new AppAccidentRecord();
        appAccidentRecord.setId(accId);
        appAccidentRecord.setType(Integer.valueOf(type));
        model.addAttribute("appAccidentRecord", appAccidentRecord);
        return "modules/app/test";
    }


    @GetMapping(value = "/findVideo")
    @ResponseBody
    public JSONObject findVideo(String accId, String type) {
        JSONObject result = new JSONObject();
        // System.out.println(accId);
        // System.out.println(type);
        if (type.equals("3")){
            type="2";
        }
       // List<AppAccidentRecord> stringList = appAccidentRecordService.selectRepart(accId, type);
//        for (AppAccidentRecord appAccidentRecord : stringList) {
//            System.out.println(appAccidentRecord);
//        }
        ArrayList<AppAccidentRecord> appAccidentRecords = new ArrayList<>();
        List<AppRepeatAccident> repeatAccidentList =  appRepeatAccidentService.selectAllrepeat(accId,type);
        for (AppRepeatAccident appRepeatAccident : repeatAccidentList) {
            //查询重复事故的信息
            Integer repeatSrouce = appRepeatAccident.getRepeatSrouce();
            if(repeatSrouce==1){
                //app
                String repeatId = appRepeatAccident.getRepeatId();
                AppAccidentRecord appAccidentRecord = appAccidentRecordService.get(repeatId);
                appAccidentRecord.setId(appRepeatAccident.getId());
                appAccidentRecord.setType(Integer.valueOf(appRepeatAccident.getState()));
                if (appRepeatAccident.getState().equals("0")){
                    appAccidentRecord.setReason("未判断重复");
                }else {
                    appAccidentRecord.setReason("标记重复视频");
                }
                appAccidentRecords.add(appAccidentRecord);
            }else {
                //小程序
                String repeatId = appRepeatAccident.getRepeatId();
                BizAccident bizAccident = bizAccidentService.get(repeatId);
                String video = bizAccident.getVideo();
                AppAccidentRecord record = new AppAccidentRecord();
                record.setVideo(video);
                record.setId(appRepeatAccident.getId());
                record.setType(Integer.valueOf(appRepeatAccident.getState()));
                if (appRepeatAccident.getState().equals("0")){
                    record.setReason("未判断重复");
                }else {
                    record.setReason("标记重复视频");
                }
                appAccidentRecords.add(record);
            }
        }
        result.put("data", appAccidentRecords);
        for (AppAccidentRecord record : appAccidentRecords) {
            System.out.println(record);
        }
        return result;

    }


    @GetMapping(value = "/justJudge")
    @ResponseBody
    public JSONObject justJudge(String accId, String type) {
        JSONObject result = new JSONObject();
        // System.out.println(accId);
        // System.out.println(type);
        if (type.equals("3")){
            type= "2";
            List<BizAccident> bizAccidentList = bizAccidentService.selectRepart(accId,type);
            if (bizAccidentList.isEmpty()) {
                result.put("mesg", "false");
            } else {
                result.put("mesg", "success");
            }
        }else {
            List<AppAccidentRecord> stringList = appAccidentRecordService.selectRepart(accId, type);
            if (stringList.isEmpty()) {
                result.put("mesg", "false");
            } else {
                result.put("mesg", "success");
            }
        }
        return result;
    }


    //修改事故状态id
    @RequestMapping(value = "/updateRepear")
    @ResponseBody
    public JSONObject updateRepear(String id, String type) {
       // System.out.println(id+"    "+type+"   "+accId+"   "+sour);
        JSONObject resutl = new JSONObject();
        if (type.equals("0")){
            //未判断重复视频
            //修改为重复
            AppRepeatAccident appRepeatAccident = appRepeatAccidentService.get(id);
            appRepeatAccident.setState("1");
            appRepeatAccidentService.update(appRepeatAccident);
        }else {
            //修改为不重复
            AppRepeatAccident appRepeatAccident = appRepeatAccidentService.get(id);
            appRepeatAccident.setState("0");
            appRepeatAccidentService.update(appRepeatAccident);
        }
        try {
            resutl.put("mesg", "success");
            return resutl;
        } catch (Exception e) {
            e.printStackTrace();
            resutl.put("mesg", "false");
            return resutl;
        }
    }
}
