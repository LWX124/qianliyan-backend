package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.controller.MerchantsTreeController;
import com.cheji.b.modular.domain.AppCleanIndetEntity;
import com.cheji.b.modular.dto.CenterDetailsDto;
import com.cheji.b.modular.mapper.AppCleanIndetMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * 洗车订单表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-12-31
 */
@Service
public class AppCleanIndetService extends ServiceImpl<AppCleanIndetMapper, AppCleanIndetEntity> implements IService<AppCleanIndetEntity> {
    @Resource
    private AppCleanIndetMapper appCleanIndetMapper;

    private Logger logger = LoggerFactory.getLogger(AppCleanIndetService.class);


    public List<AppCleanIndetEntity> findCleanIndent(Integer id, Integer pagesize, String type) {
        pagesize = (pagesize - 1) * 20;
        List<AppCleanIndetEntity> cleanIndent = new ArrayList<>();
        if (type.equals("0")) {
            //全部数据
            type = null;
            cleanIndent = appCleanIndetMapper.findCleanIndent(id, pagesize, type);
        } else if (type.equals("1")) {
            //待服务
            type = "2";
            cleanIndent = appCleanIndetMapper.findCleanIndent(id, pagesize, type);
        } else if (type.equals("2")) {
            //已完成
            type = "3";
            cleanIndent = appCleanIndetMapper.findCleanIndent3(id, pagesize, type);
        } else if (type.equals("3")) {
            //已经取消
            type = "4";
            cleanIndent = appCleanIndetMapper.findCleanIndent(id, pagesize, type);
        }
        if (!cleanIndent.isEmpty()) {
            for (AppCleanIndetEntity appCleanIndetEntity : cleanIndent) {
                BigDecimal amount = appCleanIndetEntity.getAmount();
                if (amount == null) {
                    continue;
                }
                BigDecimal bigDecimal = appCleanIndetEntity.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
                appCleanIndetEntity.setAmount(bigDecimal);
                if (appCleanIndetEntity.getResource().equals("4") || appCleanIndetEntity.getResource().equals("5")) {
                    continue;
                }

                if (!appCleanIndetEntity.getResource().equals("3")) {
                    //救援不用设置图片
                    if (appCleanIndetEntity.getCleanName().equals("免费")) {
                        appCleanIndetEntity.setCarType(1);
                    }
                    String img = "";
                    String carName = appCleanIndetEntity.getCarName();
                    if (carName.equals("小型轿车")) {
                        appCleanIndetEntity.setCarType(1);
                    } else if (carName.equals("小型越野")) {
                        appCleanIndetEntity.setCarType(2);
                    } else if (carName.equals("大型越野")) {
                        appCleanIndetEntity.setCarType(3);
                    } else if (carName.equals("商务车")) {
                        appCleanIndetEntity.setCarType(4);
                    } else {
                        appCleanIndetEntity.setCarType(1);
                    }
                    // logger.error("订单详情 appCleanIndetEntity={}", appCleanIndetEntity);
                    if (appCleanIndetEntity.getResource().equals("1")) {
                        if (appCleanIndetEntity.getCleanName().equals("精洗")) {
                            //精洗车
                            img = appCleanIndetMapper.findCarImgJx(appCleanIndetEntity.getCarType());
                        } else {
                            img = appCleanIndetMapper.findCarImgPx(appCleanIndetEntity.getCarType());
                        }
                    } else {
                        img = appCleanIndetMapper.findBeautyImg(appCleanIndetEntity.getCleanType());
                    }
                    appCleanIndetEntity.setImgUrl(img);
                }
            }
        }
        return cleanIndent;
    }

    public AppCleanIndetEntity findCleanIndentDetails(String cleanIndentNumber) {
        EntityWrapper<AppCleanIndetEntity> cleanIndetEntityWrapper = new EntityWrapper<>();
        cleanIndetEntityWrapper.eq("clean_indent_number", cleanIndentNumber);
        AppCleanIndetEntity cleanIndentDetails = selectOne(cleanIndetEntityWrapper);
        //美容
        if (cleanIndentDetails.getResource().equals("1")) {
            cleanIndentDetails = appCleanIndetMapper.findCleanIndentDetails(cleanIndentNumber);
            Integer carType = cleanIndentDetails.getCarType();
            if (carType == 0) {
                carType = 1;
            }
            String img = "";
            if (cleanIndentDetails.getCleanType() == 1) {
                //普洗
                img = appCleanIndetMapper.findCarImgPx(carType);
            } else {
                //精洗
                img = appCleanIndetMapper.findCarImgJx(carType);
            }
            cleanIndentDetails.setImgUrl(img);
        } else if (cleanIndentDetails.getResource().equals("2")) {
            //美容
            cleanIndentDetails = appCleanIndetMapper.findBeautyDetails(cleanIndentNumber);
            String img = appCleanIndetMapper.findBeautyImg(cleanIndentDetails.getCleanType());
            cleanIndentDetails.setImgUrl(img);
        }
        cleanIndentDetails.setPhoneTail(cleanIndentDetails.getPhoneTail().substring(cleanIndentDetails.getPhoneTail().length() - 4));
        Date time = cleanIndentDetails.getCreateTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(time);
        cleanIndentDetails.setTime(dateString);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
        time = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endTime = format.format(time);
        cleanIndentDetails.setEndTime(endTime);
        return cleanIndentDetails;
        //return null;
    }

    public Integer findTodayCleanIndent(Integer id) {
        return appCleanIndetMapper.findTodayCleanIndetn(id);
    }

    public CenterDetailsDto selectCleanCenterMes(Integer userBId) {
        return appCleanIndetMapper.selectCleanCenterMes(userBId);
    }

    public List<AppCleanIndetEntity> newCleanIndentList(Integer id, Integer pagesize, Integer type) {
        pagesize = (pagesize-1)*20;
        List<AppCleanIndetEntity> cleanIndent = appCleanIndetMapper.newCleanIndentList(id, pagesize, type);
        if (!cleanIndent.isEmpty()) {
            for (AppCleanIndetEntity appCleanIndetEntity : cleanIndent) {
                Date createTime = appCleanIndetEntity.getCreateTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String creat = format.format(createTime);
                appCleanIndetEntity.setTime(creat);
                BigDecimal amount = appCleanIndetEntity.getAmount();
                if (amount == null) {
                    continue;
                }
                BigDecimal bigDecimal = appCleanIndetEntity.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
                appCleanIndetEntity.setAmount(bigDecimal);

                if (appCleanIndetEntity.getCleanName().equals("免费")) {
                    appCleanIndetEntity.setCarType(1);
                }
                String img = "";
                String carName = appCleanIndetEntity.getCarName();
                if (carName.equals("小型轿车")) {
                    appCleanIndetEntity.setCarType(1);
                } else if (carName.equals("小型越野")) {
                    appCleanIndetEntity.setCarType(2);
                } else if (carName.equals("大型越野")) {
                    appCleanIndetEntity.setCarType(3);
                } else if (carName.equals("商务车")) {
                    appCleanIndetEntity.setCarType(4);
                } else {
                    appCleanIndetEntity.setCarType(1);
                }
                // logger.error("订单详情 appCleanIndetEntity={}", appCleanIndetEntity);
                if (appCleanIndetEntity.getResource().equals("1")) {
                    if (appCleanIndetEntity.getCleanName().equals("精洗")) {
                        //精洗车
                        img = appCleanIndetMapper.findCarImgJx(appCleanIndetEntity.getCarType());
                    } else {
                        img = appCleanIndetMapper.findCarImgPx(appCleanIndetEntity.getCarType());
                    }
                } else {
                    img = appCleanIndetMapper.findBeautyImg(appCleanIndetEntity.getCleanType());
                }
                appCleanIndetEntity.setImgUrl(img);
            }
        }
        return cleanIndent;
    }
}
