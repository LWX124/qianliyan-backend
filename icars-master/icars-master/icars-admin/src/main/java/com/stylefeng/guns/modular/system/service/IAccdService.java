package com.stylefeng.guns.modular.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.stylefeng.guns.core.datascope.DataScope;
import com.stylefeng.guns.modular.system.model.Accident;
import com.stylefeng.guns.modular.system.vo.AccidentVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 部门服务
 *
 * @author kosan
 * @date 2017-04-27 17:00
 */
public interface IAccdService extends IService<Accident> {

    /**
     * 修改事故状态
     */
    int setStatus(@Param("accdId") Integer accdId, @Param("status") int status);

    /**
     * 修改事故状态
     */
    int setStatus(@Param("accdId") Integer accdId, @Param("status") int status, @Param("reason") String reason);


    /**
     * 根据条件查询事故列表  分页
     */
    List<Map<String, Object>> selectAccident(@Param("page") Page<Accident> page, @Param("dataScope") DataScope dataScope, @Param("openid") String openid, @Param("createStartTime") String createStartTime,
                                             @Param("createEndTime") String createEndTime, @Param("checkStartTime") String checkStartTime, @Param("checkStartTime") String checkEndTime,
                                             @Param("checkStatus") Integer checkStatus, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc, @Param("name") String name);

    BigDecimal countAccidentRedPack(@Param("openid") String openid, @Param("createStartTime") String createStartTime, @Param("createEndTime") String createEndTime, @Param("checkStartTime") String checkStartTime,
                                    @Param("checkEndTime") String checkEndTime, @Param("checkStatus") Integer checkStatus, @Param("pushStatus") Integer pushStatus, @Param("name") String name);

    List<Map<String, Object>> countAccidentRedPackByGroup(@Param("openid") String openid, @Param("createStartTime") String createStartTime, @Param("createEndTime") String createEndTime,
                                                          @Param("checkStartTime") String checkStartTime, @Param("checkEndTime") String checkEndTime, @Param("checkStatus") Integer checkStatus,
                                                          @Param("pushStatus") Integer pushStatus, @Param("name") String name);

    /**
     * 根据条件查询事故列表  分页
     */
    List<Map<String, Object>> selectAccident(@Param("page") Page<Accident> page, @Param("dataScope") DataScope dataScope, @Param("openid") String openid, @Param("createStartTime") String createStartTime,
                                             @Param("createEndTime") String createEndTime, @Param("checkStartTime") String checkStartTime, @Param("checkStartTime") String checkEndTime,
                                             @Param("checkStatus") Integer checkStatus, @Param("pushStatus") Integer pushStatus, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc, @Param("name") String name);

    /**
     * 根据条件查询事故列表  分页 api
     */
    List<Map<String, Object>> selectAccidentForApi(@Param("page") Page<Accident> page, @Param("dataScope") DataScope dataScope, @Param("openid") String openid, @Param("createStartTime") String createStartTime, @Param("createEndTime") String createEndTime, @Param("checkStartTime") String checkStartTime, @Param("checkStartTime") String checkEndTime, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);

    /**
     * 根据条件查询事故列表  不分页
     */
    List<Map<String, Object>> selectAccident(@Param("dataScope") DataScope dataScope, @Param("openid") String openid, @Param("createStartTime") String createStartTime, @Param("createEndTime") String createEndTime,
                                             @Param("checkStartTime") String checkStartTime, @Param("checkStartTime") String checkEndTime, @Param("name") String name);

    /**
     * 根据事故经纬度计算推送4S门店距离排序
     */
    List<Map<String, Object>> sortByDistanceAsc(Accident accident, List<Map<String, Object>> depts);

    /**
     * 新增事故上报
     *
     * @param file
     * @param accidentVo
     * @param thirdSessionKey
     * @return
     * @throws IOException
     */
    Integer add(MultipartFile file, AccidentVo accidentVo, String thirdSessionKey, Integer isImage) throws IOException;

    /**
     * 推送websocket事故消息
     *
     * @param accidentVo
     * @param
     */
    void push(AccidentVo accidentVo, List<String> thirdSessionKeys);

    /**
     * 推送websocket事故消息 给4S店
     *
     * @param accidentVo
     * @param
     */
    void pushFs(AccidentVo accidentVo, List<String> accounts);

    /**
     * 推送websocket 开放平台理赔单消息 给4S店，修理厂
     *
     * @param
     * @param
     */
    void pushOpenClaimToFS(Serializable object, List<String> accounts);

    /**
     * 推送websocket事故消息 给理赔顾问
     *
     * @param accidentVo
     * @param
     */
    void pushClaims(AccidentVo accidentVo, List<String> accounts);

    /**
     * 推送websocket事故消息
     *
     * @param accidentVo
     * @param
     */
    void autoPush(AccidentVo accidentVo);


    int selectsum(String account);

    /**
     * 获取上传视频缩略图
     * @param openId openId
     * @param accId 事故id
     * @return 事故缩略图url
     */
    String findUrlByAccIdAndOpenId(String openId, Integer accId);

    Integer newAdd(AccidentVo accidentVo, String thirdSessionKey);

    void addRedis();

}
