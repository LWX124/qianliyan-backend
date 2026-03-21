package com.stylefeng.guns.modular.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.datascope.DataScope;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.GisUtil;
import com.stylefeng.guns.core.util.JedisUtil;
import com.stylefeng.guns.modular.system.constant.Location;
import com.stylefeng.guns.modular.system.constant.ThumbnailFlag;
import com.stylefeng.guns.modular.system.constant.WxSession;
import com.stylefeng.guns.modular.system.dao.AccdMapper;
import com.stylefeng.guns.modular.system.factory.AccidFactory;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.*;
import com.stylefeng.guns.modular.system.utils.GaoDeUtils;
import com.stylefeng.guns.modular.system.vo.AccidentVo;
import com.stylefeng.guns.netty.Constant;
import com.stylefeng.guns.netty.service.IWebSocketMessagePushService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Service
public class AccdServiceImpl extends ServiceImpl<AccdMapper, Accident> implements IAccdService {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AccdServiceImpl.class);

    @Value("${spring.wx.videoLocalPath}")
    private String videoLocalPath;

    @Value("${spring.wx.videoHost}")
    private String videoHost;

    @Resource(name = "styleFengWxService")
    private WxService wxService;

    @Resource
    private IBizWxUserService bizWxUserService;

    @Resource
    IWebSocketMessagePushService webSocketMessagePushService;

    @Resource
    IPushRecordService pushRecordService;

    @Resource
    IDictService dictService;

    @Resource
    private IUserService userService;

    @Resource
    private JedisUtil jedisUtil;

    @Transactional
    @Override
    public int setStatus(Integer accdId, int status) {
        return this.baseMapper.setStatus(accdId, status, "调度人员", new Date(), null);
    }

    @Override
    @Transactional
    public int setStatus(Integer accdId, int status, String reason) {
        return this.baseMapper.setStatus(accdId, status, "调度人员", new Date(), reason);
    }

    @Override
    public List<Map<String, Object>> selectAccident(Page<Accident> page, DataScope dataScope, String openid, String createStartTime, String createEndTime, String checkStartTime, String checkEndTime, Integer checkStatus,
                                                    String orderByField, boolean isAsc, String name) {
        return this.baseMapper.selectAccident(page, dataScope, openid, createStartTime, createEndTime, checkStartTime, checkEndTime, checkStatus, orderByField, isAsc, name);
    }

    @Override
    public BigDecimal countAccidentRedPack(String openid, String createStartTime, String createEndTime, String checkStartTime, String checkEndTime, Integer checkStatus, Integer pushStatus, String name) {
        return this.baseMapper.countAccidentRedPack(openid, createStartTime, createEndTime, checkStartTime, checkEndTime, checkStatus, pushStatus, name);
    }

    @Override
    public List<Map<String, Object>> countAccidentRedPackByGroup(String openid, String createStartTime, String createEndTime, String checkStartTime, String checkEndTime, Integer checkStatus, Integer pushStatus, String name) {
        return this.baseMapper.countAccidentRedPackByGroup(openid, createStartTime, createEndTime, checkStartTime, checkEndTime, checkStatus, pushStatus, name);
    }

    @Override
    public List<Map<String, Object>> selectAccident(Page<Accident> page, DataScope dataScope, String openid, String createStartTime, String createEndTime, String checkStartTime,
                                                    String checkEndTime, Integer checkStatus, Integer pushStatus, String orderByField, boolean isAsc, String name) {
        return this.baseMapper.selectAccident(page, dataScope, openid, createStartTime, createEndTime, checkStartTime, checkEndTime, checkStatus, pushStatus, orderByField, isAsc, name);
    }

    @Override
    public List<Map<String, Object>> selectAccidentForApi(Page<Accident> page, DataScope dataScope, String openid, String createStartTime, String createEndTime, String checkStartTime, String checkEndTime, String orderByField, boolean isAsc) {
        return this.baseMapper.selectAccidentForApi(page, dataScope, openid, createStartTime, createEndTime, checkStartTime, checkEndTime, orderByField, isAsc);
    }

    @Override
    public int selectsum(String account) {
        return this.baseMapper.selectsum(account);
    }

    @Override
    public String findUrlByAccIdAndOpenId(String openId, Integer accId) {
        return this.baseMapper.findUrlByAccIdAndOpenId(openId, accId);
    }

    @Transactional
    @Override
    public Integer newAdd(AccidentVo accidentVo, String thirdSessionKey) {
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            throw new GunsException(BizExceptionEnum.WX_SESSION_IS_NULL);
        }
        List<BizWxUser> wxUsers = bizWxUserService.selectByOpenid(wxSession.getOpenId());
//       List<BizWxUser> wxUsers = bizWxUserService.selectByOpenid("om2cE5lzjXOGVCGXpfvsRF_e3NW8");
        if (wxUsers == null || wxUsers.size() == 0) {
            BizWxUser bizWxUser = new BizWxUser();
            bizWxUser.setOpenid(accidentVo.getOpenid());
            bizWxUser.setType(1);
            bizWxUser.setCreateTime(new Date());
            bizWxUserService.insert(wxUsers.get(0));
        }
//        String random = RandomStringUtils.randomAlphabetic(16);
//        String fileName;
//        if (isImage == 1) {
//            fileName = random.concat(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length()));
//        } else {
//            fileName = random.concat(".mp4");
//        }
//        String videoUrl = null;
//        File newfile = new File(videoLocalPath, fileName);
//        if (!newfile.getParentFile().exists()) {
//            newfile.getParentFile().mkdirs();
//        }
//        FileCopyUtils.copy(file.getBytes(), newfile);
//        videoUrl = videoHost.concat(fileName);
        String gdAddress = null;
        try {
            gdAddress = GaoDeUtils.getGDAddress(accidentVo.getLat().toString(), accidentVo.getLng().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        accidentVo.setOpenid(wxSession.getOpenId());
        accidentVo.setStatus(1);
        accidentVo.setAddress(gdAddress);
        accidentVo.setCreateTime(new Date());
        accidentVo.setVideo(accidentVo.getUrl());
        Accident accident = AccidFactory.createAccid(accidentVo);
        //设置Thumbnail为未处理，通过task自动任务截取视频缩略图
        accident.setThumbnailFlag(ThumbnailFlag.UNTREATED);
        this.baseMapper.insertAccid(accident);
        return accident.getId().intValue();
    }

    @Override
    public void addRedis() {
        int getAccidentRedisKey = jedisUtil.setEx("GET_ACCIDENT_REDIS_KEY", "1", 60 * 60);
        System.out.println("设置redis：" + getAccidentRedisKey);
    }


    @Override
    public List<Map<String, Object>> selectAccident(DataScope dataScope, String openid, String createStartTime, String createEndTime, String checkStartTime, String checkEndTime, String name) {
        return this.baseMapper.selectAccident(dataScope, openid, createStartTime, createEndTime, checkStartTime, checkEndTime, name);
    }

    @Transactional
    @Override
    public Integer add(MultipartFile file, AccidentVo accidentVo, String thirdSessionKey, Integer isImage) throws IOException {
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            throw new GunsException(BizExceptionEnum.WX_SESSION_IS_NULL);
        }
        List<BizWxUser> wxUsers = bizWxUserService.selectByOpenid(wxSession.getOpenId());
//       List<BizWxUser> wxUsers = bizWxUserService.selectByOpenid("om2cE5lzjXOGVCGXpfvsRF_e3NW8");
        if (wxUsers == null || wxUsers.size() == 0) {
            BizWxUser bizWxUser = new BizWxUser();
            bizWxUser.setOpenid(accidentVo.getOpenid());
            bizWxUser.setType(1);
            bizWxUser.setCreateTime(new Date());
            bizWxUserService.insert(wxUsers.get(0));
        }
        String random = RandomStringUtils.randomAlphabetic(16);
        String fileName;
        if (isImage == 1) {
            fileName = random.concat(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length()));
        } else {
            fileName = random.concat(".mp4");
        }
        String videoUrl = null;
        File newfile = new File(videoLocalPath, fileName);
        if (!newfile.getParentFile().exists()) {
            newfile.getParentFile().mkdirs();
        }
        FileCopyUtils.copy(file.getBytes(), newfile);
        videoUrl = videoHost.concat(fileName);

        accidentVo.setOpenid(wxSession.getOpenId());
        accidentVo.setStatus(1);
        accidentVo.setCreateTime(new Date());
        accidentVo.setVideo(videoUrl);
        Accident accident = AccidFactory.createAccid(accidentVo);
        //设置Thumbnail为未处理，通过task自动任务截取视频缩略图
        accident.setThumbnailFlag(ThumbnailFlag.UNTREATED);
        this.baseMapper.insertAccid(accident);
        return accident.getId().intValue();
    }

    @Override
    public void push(AccidentVo accidentVo, List<String> thirdSessionKeys) {
        for (String thirdSessionKey : thirdSessionKeys) {
            webSocketMessagePushService.push(accidentVo, thirdSessionKey);
        }
    }

    @Override
    public void pushFs(AccidentVo accidentVo, List<String> accounts) {
        for (String account : accounts) {
            webSocketMessagePushService.pushFS(accidentVo, account);
        }
    }

    @Override
    public void pushOpenClaimToFS(Serializable obj, List<String> accounts) {
        for (String account : accounts) {
            webSocketMessagePushService.pushOpenClaimToFS(obj, account);
        }
    }

    @Override
    public void pushClaims(AccidentVo accidentVo, List<String> accounts) {
        for (String account : accounts) {
            webSocketMessagePushService.pushClaims(accidentVo, account);
        }
    }

    @Override
    public void autoPush(AccidentVo accidentVo) {
        List<Location> list = new ArrayList<>();
        for (Location var : Constant.gisLocation.values()) {
            try {
                list.add(var.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        Location lo = new Location();
        lo.setLat(accidentVo.getLat());
        lo.setLng(accidentVo.getLng());
        list = getSortList(lo, list);
        if (list != null && list.size() > 0) {
            LOGGER.info("事故id: " + accidentVo.getId() + ",推送至：" + list.get(0).getAccount() + ",距离：" + list.get(0).getDistance());
            //推送websocket消息至业务员
            webSocketMessagePushService.push(accidentVo, list.get(0).getThirdSessionKey());
            WxSession wxSession = wxService.getWxSession(list.get(0).getThirdSessionKey());

            PushRecord var1 = pushRecordService.selectOne(new EntityWrapper<PushRecord>().eq("accid", accidentVo.getId()).eq("account", wxSession.getUser().getAccount()));
            if (var1 != null) {
                PushRecord pushRecord = new PushRecord();
                pushRecord.setModifyTime(new Date());
                pushRecord.setStatus(0);
                pushRecord.setId(var1.getId());
                //已有记录，更新推送时间
                pushRecordService.updateById(pushRecord);
                webSocketMessagePushService.pushClaims(accidentVo, wxSession.getUser().getAccount());
            } else {
                PushRecord pushRecord = new PushRecord();
                pushRecord.setAccid(accidentVo.getId());
                pushRecord.setAddress(accidentVo.getAddress());
                pushRecord.setVideo(accidentVo.getVideo());
                pushRecord.setCreateTime(new Date());
                pushRecord.setStatus(0);
                pushRecord.setDeptid(wxSession.getUser().getDeptid());
                pushRecord.setAccount(wxSession.getUser().getAccount());
                //添加事故推送记录
                pushRecordService.insert(pushRecord);
                webSocketMessagePushService.pushClaims(accidentVo, wxSession.getUser().getAccount());
            }
        } else {
            LOGGER.info("事故id: " + accidentVo.getId() + " 未匹配到目标");
        }
    }

    @Override
    public List<Map<String, Object>> sortByDistanceAsc(Accident accident, List<Map<String, Object>> depts) {
        Location lo = new Location();
        lo.setLng(accident.getLng());
        lo.setLat(accident.getLat());

        List<Map<String, Object>> sortedMap = new ArrayList<>();

        List<Location> locations = new ArrayList<>();
        for (Map<String, Object> map : depts) {
            if (map.get("lng") != null && map.get("lat") != null && map.get("id") != null) {
                locations.add(new Location((BigDecimal) map.get("lat"), (BigDecimal) map.get("lng"), map.get("fullname").toString(), map.get("tips").toString(), Integer.parseInt(map.get("id").toString())));
            }
        }
        locations = GisUtil.getSortList(lo, locations);
        if (locations != null && locations.size() > 5) {
            locations = locations.subList(0, 5);
        }
        for (Location var1 : locations) {
            Map<String, Object> var2 = new HashMap<>();
            var2.put("id", var1.getId());
            var2.put("name", var1.getFullName());
            var2.put("distance", var1.getDistance());
            var2.put("tips", var1.getTips());
            sortedMap.add(var2);
        }
        return sortedMap;
    }

    /**
     * 根据经纬度
     *
     * @param location1
     * @param locations
     * @return
     */
    private List<Location> getSortList(Location location1, List<Location> locations) {
        Dict dict = dictService.selectOne(new EntityWrapper<Dict>().eq("name", "事故推送距离限制"));
        double distanceLimit = 20;
        try {
            distanceLimit = Double.parseDouble(dict.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Location lo = new Location();
        Iterator<Location> it = locations.iterator();
        while (it.hasNext()) {
            Location item = it.next();
            User query = new User();
            query.setAccount(item.getAccount());
            List<Map<String, Object>> userMap = userService.selectUsersForPush(query);
            if (userMap == null || userMap.size() == 0) {
                LOGGER.info("未查找到被推送人员信息");
                continue;
            }
            BigDecimal balance = userMap.get(0).get("balance") == null ? BigDecimal.ZERO : (BigDecimal) (userMap.get(0).get("balance"));
            boolean sfkf = (userMap.get(0).get("sfkf") == null || userMap.get(0).get("sfkf").equals("Y")) ? true : false;
            if (sfkf && balance.compareTo(BigDecimal.ZERO) != 1) {
                LOGGER.info("用户账号: " + item.getAccount() + ",事故推送扣费状态：" + sfkf + ",预存款余额：" + balance.doubleValue() + ",不予推送");
                it.remove();
                continue;
            } else {
                LOGGER.info("用户账号: " + item.getAccount() + ",事故推送扣费状态：" + sfkf + ",预存款余额：" + balance.doubleValue() + ",可以推送");
            }
            lo.lat = item.lat;
            lo.lng = item.lng;
            item.distance = GisUtil.getDistance(location1, lo);
            if (item.distance > distanceLimit) {
                LOGGER.info("推送距离超限，不予推送。距离(km)：" + item.distance.doubleValue());
                it.remove();
            }
        }
        locations.sort(new Comparator<Location>() {
            @Override
            public int compare(Location o1, Location o2) {
                if (o1.getDistance() >= o2.getDistance()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        return locations;
    }
}
