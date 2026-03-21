package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.*;
import com.cheji.b.modular.domain.AppSendOutSheetEntity;
import com.cheji.b.modular.dto.CheckImgList;
import com.cheji.b.modular.dto.TableMess;
import com.cheji.b.modular.mapper.AppSendOutSheetMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.b.utils.MapNavUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * web派单记录表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2020-10-14
 */
@Service
public class AppSendOutSheetService extends ServiceImpl<AppSendOutSheetMapper, AppSendOutSheetEntity> implements IService<AppSendOutSheetEntity> {

    @Resource
    private AppSendOutSheetMapper appSendOutSheetMapper;

    @Resource
    private AppSendUrlService appSendUrlService;

    @Resource
    private UserService userService;

    @Resource
    private CarBrandService carBrandService;

    @Resource
    private AppMessageCarService appMessageCarService;

    private static final String key = "a11e3020a4b82ce9390044286910f02f";


    public List<AppSendOutSheetEntity> findSendOutList(Integer id, Integer pagesize) {
        //拿到列表数据
        EntityWrapper<AppSendOutSheetEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_b_id", id)
                .orderBy("create_time")
                .last("desc");
        Page<AppSendOutSheetEntity> page = new Page<>(pagesize, 20);
        Page<AppSendOutSheetEntity> pushBillEntityPage = selectPage(page, wrapper);
        List<AppSendOutSheetEntity> records = pushBillEntityPage.getRecords();

        //派单信息也插入其中
        //获取到分页数据
        //遍历数据加上标识
        for (int i = 0; i < records.size(); i++) {
            //查询图片
            String url = appSendUrlService.findimgById(records.get(i).getId());
            records.get(i).setSrc(url);
            //获取到最后一条数据
            if (i == records.size() - 1) {
                AppSendOutSheetEntity lastMesg = records.get(i);
                Date time = lastMesg.getCreateTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(time);
                lastMesg.setTime(dateString);
                //查询到下个月第一条数据
                Page<AppSendOutSheetEntity> page1 = new Page<>(pagesize + 1, 20);
                Page<AppSendOutSheetEntity> pushBillEntityPage1 = selectPage(page1, wrapper);
                List<AppSendOutSheetEntity> records1 = pushBillEntityPage1.getRecords();
                if (records1.isEmpty()) {
                    lastMesg.setLoge("1");
                } else {
                    AppSendOutSheetEntity firstPushBill = records1.get(0);
                    Date time1 = firstPushBill.getCreateTime();
                    //当前时间
                    int yearMonth = getYearMonth(time);
                    //下一页第一条数据
                    int yearMonth1 = getYearMonth(time1);
                    if (yearMonth != yearMonth1) {
                        lastMesg.setLoge("1");
                    }
                }
            }
            //先组装数据
            AppSendOutSheetEntity AppSendOutSheetEntity = records.get(i);
            //时间转成string
            Date time1 = AppSendOutSheetEntity.getCreateTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(time1);
            AppSendOutSheetEntity.setTime(dateString);
            //当前数据比较下一条数据
            //比较月份时间，如果不一样肯定是当前月最后一条
            if (i + 1 == records.size()) {
                break;
            }
            AppSendOutSheetEntity nextPushBillEntity = records.get(i + 1);
            Date time = AppSendOutSheetEntity.getCreateTime();
            Date nextTime = nextPushBillEntity.getCreateTime();
            int yearMonth = getYearMonth(time);
            int nextYearMonth = getYearMonth(nextTime);
            if (yearMonth != nextYearMonth) {
                AppSendOutSheetEntity.setLoge("1");
            }
        }
        return records;
    }


    private int getYearMonth(Date date) {
        //传入日期
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH);
        //获取一个唯一得年月数值
        return year * 100 + month;
    }

    public List<String> findUrlbyid(Integer id) {
        return appSendOutSheetMapper.findUrlbyid(id);
    }

    public List<String> findIndex1Img(Integer id) {
        return appSendOutSheetMapper.findIndex1Img(id);
    }

    public List<CheckImgList> selectSenceImg(Integer id) {
        return appSendOutSheetMapper.selectSenceImg(id);
    }

    public List<TableMess> find4sStorebyAppsend(AccidentRecord accident, String brandName, String brandId, AppMessageCarEntity appMessageCarEntity) {
        List<TableMess> tableMesses = new ArrayList<>();
        List<Integer> userIdList = appSendOutSheetMapper.findBrandMerIdList(Integer.parseInt(brandId));

        //遍历查询
        for (Integer userBid : userIdList) {
            AppUserEntity bUserEntity = userService.selectById(userBid);
            if (bUserEntity != null) {
                //查询该店铺是否上架
                if (bUserEntity.getState() == 1) {
                    //是已经上架的商户
                    //查询距离 添加信息
                    TableMess tableMess = new TableMess();
                    tableMess.setId(userBid.toString());
                    tableMess.setName(bUserEntity.getMerchantsName());
                    tableMess.setBrandId(brandId);
                    //查询品牌照片
                    CarBrandEntity carBrandEntity = carBrandService.selectById(brandId);
                    tableMess.setBrandUrl(carBrandEntity.getPicUrl());

                    MapNavUtil mapNavUtil = new MapNavUtil(accident.getLng() + "," + accident.getLat(), bUserEntity.getLng() + "," + bUserEntity.getLat(), key, 1, "JSON");
                    Long results = mapNavUtil.getResults();
                    BigDecimal distance3 = new BigDecimal(results);
                    distance3 = distance3.divide(new BigDecimal("1000"), 2, BigDecimal.ROUND_HALF_UP);

                    tableMess.setLeave(distance3 + "km");
                    tableMess.setBrandName(brandName);
                    tableMess.setCustomerName(appMessageCarEntity.getCustomerName());
                    tableMess.setPhone(appMessageCarEntity.getPhone());
                    tableMess.setLicensePlate(appMessageCarEntity.getLicensePlate());
                    tableMess.setCarMessageId(appMessageCarEntity.getId());
                    //查询是否推送过商户
                    List<Integer> ids = appMessageCarService.findIsPush(userBid.toString(), appMessageCarEntity.getMessId());
                    if (ids.isEmpty()) {
                        tableMess.setIsPush(0);
                    } else {
                        tableMess.setIsPush(1);
                    }
                    tableMess.setMerLat(bUserEntity.getLat());
                    tableMess.setMerLng(bUserEntity.getLng());
                    tableMess.setMerAddress(bUserEntity.getAddress());
                    tableMesses.add(tableMess);
                }
            }
        }
        return tableMesses;
    }
}
