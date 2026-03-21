package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.AccidentRecord;
import com.cheji.b.modular.domain.BizAccident;
import com.cheji.b.modular.domain.PushBillEntity;
import com.cheji.b.modular.dto.CheckImgList;
import com.cheji.b.modular.dto.MineDto;
import com.cheji.b.modular.dto.TrackStateNumber;
import com.cheji.b.modular.mapper.PushBillMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
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
 * 用户扣费记录表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-09-25
 */
@Service
public class PushBillService extends ServiceImpl<PushBillMapper, PushBillEntity> implements IService<PushBillEntity> {
    @Resource
    private AccidentRecordService accidentRecordService;
    @Resource
    private PushBillMapper pushBillMapper;
    @Resource
    private BizAccidentService bizAccidentService;

    public List<PushBillEntity> findBillList(Integer id, Integer pagesize) {
        //拿到列表数据
//        EntityWrapper<PushBillEntity> wrapper = new EntityWrapper<>();
//        wrapper.eq("user_id", id)
//                .orderBy("create_time")
//                .last("desc");
//        Page<PushBillEntity> page = new Page<>(pagesize, 20);
//        Page<PushBillEntity> pushBillEntityPage = selectPage(page, wrapper);
//        List<PushBillEntity> records = pushBillEntityPage.getRecords();


        pagesize = (pagesize - 1) * 20;
        //派单信息也插入其中
        List<PushBillEntity> records = pushBillMapper.selectAllBillList(id, pagesize);


        //获取到分页数据
        //遍历数据加上标识
        for (int i = 0; i < records.size(); i++) {
            //获取到最后一条数据
            if (i == records.size() - 1) {
                PushBillEntity lastMesg = records.get(i);
                Date time = lastMesg.getCreateTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(time);
                lastMesg.setTime(dateString);
                //查询到下个月第一条数据
//                Page<PushBillEntity> page1 = new Page<>(pagesize+1, 20);
//                Page<PushBillEntity> pushBillEntityPage1 = selectPage(page1, wrapper);
//                List<PushBillEntity> records1= pushBillEntityPage1.getRecords();
                List<PushBillEntity> records1 = pushBillMapper.selectAllBillList(id, pagesize + 1);


                if (records1.isEmpty()) {
                    lastMesg.setLoge("1");
                } else {
                    PushBillEntity firstPushBill = records1.get(0);
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
            PushBillEntity pushBillEntity = records.get(i);
            if (!pushBillEntity.getType().equals("2")) {
                Integer accid = pushBillEntity.getAccid();
                Integer source = pushBillEntity.getSource();
                AccidentRecord accidentRecord = new AccidentRecord();
                if (source == 1) {
                    accidentRecord = accidentRecordService.selectById(accid);
                } else {
                    BizAccident bizAccident = bizAccidentService.selectById(accid);
                    accidentRecord.setThumbnailUrl(bizAccident.getThumbnailUrl());
                    accidentRecord.setAddress(bizAccident.getAddress());
                }
                String thumbnailUrl = accidentRecord.getThumbnailUrl();
                String address = accidentRecord.getAddress();
                pushBillEntity.setAddress(address);
                pushBillEntity.setThumbnailUrl(thumbnailUrl);
            }
            //获取到月份总额
            BigDecimal payAmount = getPayAmount(pushBillEntity);
            pushBillEntity.setPayAmount(payAmount);
            //时间转成string
            Date time1 = pushBillEntity.getCreateTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(time1);
            pushBillEntity.setTime(dateString);
            //当前数据比较下一条数据
            //比较月份时间，如果不一样肯定是当前月最后一条
            if (i + 1 == records.size()) {
                break;
            }
            PushBillEntity nextPushBillEntity = records.get(i + 1);
            Date time = pushBillEntity.getCreateTime();
            Date nextTime = nextPushBillEntity.getCreateTime();
            int yearMonth = getYearMonth(time);
            int nextYearMonth = getYearMonth(nextTime);
            if (yearMonth != nextYearMonth) {
                pushBillEntity.setLoge("1");
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

    public List<PushBillEntity> findScreenBill(Integer id, Integer pagesize, String date) {
        //拿到对应月份得数据
        pagesize = (pagesize - 1) * 20;
        List<PushBillEntity> list = pushBillMapper.findScreenBill(id, pagesize, date);
        for (PushBillEntity pushBillEntity : list) {
            Integer accid = pushBillEntity.getAccid();
            Integer source = pushBillEntity.getSource();
            AccidentRecord accidentRecord = new AccidentRecord();

            if (source == 1) {
                accidentRecord = accidentRecordService.selectById(accid);
            } else {
                BizAccident bizAccident = bizAccidentService.selectById(accid);
                accidentRecord.setThumbnailUrl(bizAccident.getThumbnailUrl());
                accidentRecord.setAddress(bizAccident.getAddress());
            }
            String address = accidentRecord.getAddress();
            String thumbnailUrl = accidentRecord.getThumbnailUrl();
            pushBillEntity.setThumbnailUrl(thumbnailUrl);
            pushBillEntity.setAddress(address);
            BigDecimal payAmount = getPayAmount(pushBillEntity);
            pushBillEntity.setPayAmount(payAmount);
        }
        return list;
    }

    private BigDecimal getPayAmount(PushBillEntity pushBillEntity) {
        //通过月份查询到总额
        Date time = pushBillEntity.getCreateTime();
        Long userId = pushBillEntity.getUserId();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String dateString = formatter.format(time);
        //获取到数据
        return pushBillMapper.findPayAmount(userId, dateString);
    }

    public PushBillEntity findAccident(Integer id, String s) {
        return pushBillMapper.findAccident(id, s);
    }


    public List<PushBillEntity> findBillList(Integer id, Integer pagesize, Integer state) {
        pagesize = (pagesize - 1) * 20;
        List<PushBillEntity> records = new ArrayList<>();
        //派单信息也插入其中
        //新任务，现场签到，服务失败，服务成功
        //    1,     2,    6,      7
        //    1     2       3       4

        if (state == 1 || state == 2) {
            records = pushBillMapper.selectAllBillList2(id, pagesize, state);
        } else {
            if (state == 3) {
                state = 2;
            } else if (state == 4){
                state = 3;
            }
            records = pushBillMapper.select234BillList(id, pagesize, state);
        }
        return records;
    }

    public TrackStateNumber selectTrackStateNumber(String id) {

        TrackStateNumber sosNumber = new TrackStateNumber();

        Integer newTask = pushBillMapper.findAllNewTask(id);
        //到现场
        Integer comeScene = pushBillMapper.findComeScene(id);

        Integer noToMer = pushBillMapper.findPbTrackAgain(id);
        //pb中的同意到店---->推修成功
        Integer agree = pushBillMapper.findPushSuccess(id);

        sosNumber.setNewTask(newTask);
        sosNumber.setComeScene(comeScene);
        sosNumber.setAgree(agree );
        sosNumber.setRefuse(noToMer);
        return sosNumber;
    }

    public List<PushBillEntity> findBillListBySreach(Integer id, Integer pagesize, String text) {
        pagesize = (pagesize - 1) * 20;
        //派单信息也插入其中
        List<PushBillEntity> records = pushBillMapper.selectAllBillListbySearch(id, pagesize, text);


        //获取到分页数据
        //遍历数据加上标识
        for (int i = 0; i < records.size(); i++) {
            //获取到最后一条数据
            if (i == records.size() - 1) {
                PushBillEntity lastMesg = records.get(i);
                Date time = lastMesg.getCreateTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(time);
                lastMesg.setTime(dateString);
                //查询到下个月第一条数据
//                Page<PushBillEntity> page1 = new Page<>(pagesize+1, 20);
//                Page<PushBillEntity> pushBillEntityPage1 = selectPage(page1, wrapper);
//                List<PushBillEntity> records1= pushBillEntityPage1.getRecords();
                List<PushBillEntity> records1 = pushBillMapper.selectAllBillListbySearch(id, pagesize + 1, text);


                if (records1.isEmpty()) {
                    lastMesg.setLoge("1");
                } else {
                    PushBillEntity firstPushBill = records1.get(0);
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
            PushBillEntity pushBillEntity = records.get(i);
            if (!pushBillEntity.getType().equals("2")) {
                Integer accid = pushBillEntity.getAccid();
                Integer source = pushBillEntity.getSource();
                AccidentRecord accidentRecord = new AccidentRecord();
                if (source == 1) {
                    accidentRecord = accidentRecordService.selectById(accid);
                } else {
                    BizAccident bizAccident = bizAccidentService.findById(accid);
                    accidentRecord.setThumbnailUrl(bizAccident.getThumbnailUrl());
                    accidentRecord.setAddress(bizAccident.getAddress());
                    accidentRecord.setVideo(bizAccident.getVideo());
                }

                String thumbnailUrl = accidentRecord.getThumbnailUrl();
                String address = accidentRecord.getAddress();
                pushBillEntity.setAddress(address);
                pushBillEntity.setThumbnailUrl(thumbnailUrl);
                pushBillEntity.setVideoUrl(accidentRecord.getVideo());
            }
            //获取到月份总额
//            BigDecimal payAmount = getPayAmount(pushBillEntity);
//            pushBillEntity.setPayAmount(payAmount);
            //时间转成string
            Date time1 = pushBillEntity.getCreateTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(time1);
            pushBillEntity.setTime(dateString);
            //当前数据比较下一条数据
            //比较月份时间，如果不一样肯定是当前月最后一条
            if (i + 1 == records.size()) {
                break;
            }
            PushBillEntity nextPushBillEntity = records.get(i + 1);
            Date time = pushBillEntity.getCreateTime();
            Date nextTime = nextPushBillEntity.getCreateTime();
            int yearMonth = getYearMonth(time);
            int nextYearMonth = getYearMonth(nextTime);
            if (yearMonth != nextYearMonth) {
                pushBillEntity.setLoge("1");
            }
        }
        return records;
    }

    public List<CheckImgList> selectSceneImg(Long id) {
        return pushBillMapper.selectSceneImg(id);
    }

    public MineDto findAllCount(Integer id, String year, String month) {
        return pushBillMapper.findAllCount(id,year,month);
    }
}
