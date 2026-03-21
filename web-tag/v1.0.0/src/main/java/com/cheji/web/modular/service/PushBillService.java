package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.cwork.BillList;
import com.cheji.web.modular.cwork.BillListDetail;
import com.cheji.web.modular.cwork.CheckImgList;
import com.cheji.web.modular.cwork.TrackStateNumber;
import com.cheji.web.modular.domain.AccidentRecordEntity;
import com.cheji.web.modular.domain.BizAccidentEntity;
import com.cheji.web.modular.domain.PushBillEntity;
import com.cheji.web.modular.mapper.PushBillMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 用户扣费记录表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-09-02
 */
@Service
public class PushBillService extends ServiceImpl<PushBillMapper, PushBillEntity> implements IService<PushBillEntity> {

    @Resource
    private PushBillMapper pushBillMapper;

    @Resource
    private PayAmountRecordService payAmountRecordService;

    @Resource
    private AccidentRecordService accidentRecordService;

    @Resource
    private BizAccidentService bizAccidentService;


    //根据id查询到bill列表
    public List<BillList> findBillListByid(String id, String date) {
        List<BillList> billLists = new ArrayList<>();
        if (null == date) {
            billLists = pushBillMapper.findBillListByid(id);
        } else {
            billLists = pushBillMapper.findBillListByidAndDate(id, date);
        }
        //如果账单表没有数据
        if (billLists.isEmpty() && date == null) {
            billLists = payAmountRecordService.findListByid(id);
        } else if (billLists.isEmpty()) {
            billLists = payAmountRecordService.findListByidAndDate(id, date);
        }
        //红包表也为空
        if (billLists.isEmpty()) {
            return null;
        }
        //拿到月份
        for (BillList billList : billLists) {
            String thisMouthAndYear = billList.getThisMouth();
            String thisMouth = thisMouthAndYear.substring(thisMouthAndYear.length() - 2, thisMouthAndYear.length());

            //先处理加减，红包表中是加，账单表里面是减
            //根据id查询红包表查询红包记录和事故记录
            List<BillListDetail> redWalletListDetails = payAmountRecordService.findRecordByid(id);

            //红包表加
            for (BillListDetail billListDetail : redWalletListDetails) {
                String money = billListDetail.getMoney();
                billListDetail.setMoney("+" + money);
            }
            //账单表查询账单记录
            List<BillListDetail> billListDetailList = pushBillMapper.findRecordByid(id);
            //账单表减
            for (BillListDetail billListDetail : billListDetailList) {
                String money = billListDetail.getMoney();
                billListDetail.setMoney("-" + money);
            }

            List<BillListDetail> allList = new ArrayList<>();
            //list合到一起
            allList.addAll(redWalletListDetails);
            allList.addAll(billListDetailList);

            //遍历数据和月份比较，放到对应的月份list中，
            List<BillListDetail> billListDetails = new ArrayList<>();
            for (BillListDetail alllist : allList) {
                String createTime = alllist.getCreateTime();
                DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date1 = format1.parse(createTime);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date1);
                    //拿到月份数据
                    int month = cal.get(Calendar.MONTH) + 1;
                    //月份相等就加入集合中

                    if (month == Integer.valueOf(thisMouth)) {
                        billListDetails.add(alllist);
                        billList.setBillListDetails(billListDetails);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            //转换成日历类根据时间类型来排序
            List<BillListDetail> billListDetailss = billList.getBillListDetails();
            if (null == billListDetailss) {
                continue;
            }
            ListSort(billListDetailss);
        }
        return billLists;
    }

    private static void ListSort(List<BillListDetail> list) {
        Collections.sort(list, new Comparator<BillListDetail>() {
            @Override
            public int compare(BillListDetail o1, BillListDetail o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date dt1 = format.parse(o1.getCreateTime());
                    Date dt2 = format.parse(o2.getCreateTime());
                    if (dt1.getTime() > dt2.getTime()) {
                        return 1;
                    } else if (dt1.getTime() < dt2.getTime()) {
                        return -1;
                    } else {
                        return 0;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }


    public List<PushBillEntity> findBillList(Integer id, Integer pagesize, Integer state) {
        //拿到列表数据
//        EntityWrapper<PushBillEntity> wrapper = new EntityWrapper<>();
//        wrapper.eq("user_id", id)
//                .orderBy("create_time")
//                .last("desc");
//        Page<PushBillEntity> page = new Page<>(pagesize, 20);
//        Page<PushBillEntity> pushBillEntityPage = selectPage(page, wrapper);
//        List<PushBillEntity> records = pushBillEntityPage.getRecords();
        //1.新任务。  2.到现场    3.未现场    4.无车辆
        //5.再次跟踪   6.拒绝到店，  7.同意到店    8.推修成功

        //1.新任务。  2.到现场    3.未现场    4.现场补贴
        //5.再次跟踪   6.推修失败，  7.推修成功    8.审核结果

        pagesize = (pagesize - 1) * 20;
        List<PushBillEntity> records = new ArrayList<>();
        //派单信息也插入其中
        if (state == 1 || state == 2 || state == 3 | state == 4) {
            records = pushBillMapper.selectAllBillList(id, pagesize, state);
        } else {
            if (state == 5) {
                state = 1;
            } else if (state == 6) {
                state = 2;
            } else if (state == 7) {
                state = 3;
            } else if (state == 8) {
                state = 4;
            }
            records = pushBillMapper.select234BillList(id, pagesize, state);
        }


        //获取到分页数据
        //遍历数据加上标识
//        for (int i = 0; i < records.size(); i++) {
//            //获取到最后一条数据
//            if (i==records.size()-1){
//                PushBillEntity lastMesg = records.get(i);
//                Date time = lastMesg.getCreateTime();
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String dateString = formatter.format(time);
//                lastMesg.setTime(dateString);
//                //查询到下个月第一条数据
////                Page<PushBillEntity> page1 = new Page<>(pagesize+1, 20);
////                Page<PushBillEntity> pushBillEntityPage1 = selectPage(page1, wrapper);
////                List<PushBillEntity> records1= pushBillEntityPage1.getRecords();
//                List<PushBillEntity> records1 = pushBillMapper.selectAllBillList(id,pagesize+1,state);
//
//
//                if (records1.isEmpty()){
//                    lastMesg.setLoge("1");
//                }else {
//                    PushBillEntity firstPushBill = records1.get(0);
//                    Date time1 = firstPushBill.getCreateTime();
//                    //当前时间
//                    int yearMonth = getYearMonth(time);
//                    //下一页第一条数据
//                    int yearMonth1 = getYearMonth(time1);
//                    if (yearMonth!=yearMonth1){
//                        lastMesg.setLoge("1");
//                    }
//                }
//            }
//            //先组装数据
//            PushBillEntity pushBillEntity = records.get(i);
//            if (!pushBillEntity.getType().equals("2")){
//                Integer accid = pushBillEntity.getAccid();
//                Integer source = pushBillEntity.getSource();
//                AccidentRecordEntity accidentRecord = new AccidentRecordEntity();
//                if (source==1){
//                    accidentRecord = accidentRecordService.selectById(accid);
//                }else {
//                    BizAccidentEntity bizAccident = bizAccidentService.findById(accid);
//                    accidentRecord.setThumbnailUrl(bizAccident.getThumbnailUrl());
//                    accidentRecord.setAddress(bizAccident.getAddress());
//                    accidentRecord.setVideo(bizAccident.getVideo());
//                }
//
//                String thumbnailUrl = accidentRecord.getThumbnailUrl();
//                String address = accidentRecord.getAddress();
//                pushBillEntity.setAddress(address);
//                pushBillEntity.setThumbnailUrl(thumbnailUrl);
//                pushBillEntity.setVideoUrl(accidentRecord.getVideo());
//            }
//            //获取到月份总额
////            BigDecimal payAmount = getPayAmount(pushBillEntity);
////            pushBillEntity.setPayAmount(payAmount);
//            //时间转成string
//            Date time1 = pushBillEntity.getCreateTime();
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String dateString = formatter.format(time1);
//            pushBillEntity.setTime(dateString);
//            //当前数据比较下一条数据
//            //比较月份时间，如果不一样肯定是当前月最后一条
//            if (i + 1 == records.size()) {
//                break;
//            }
//            PushBillEntity nextPushBillEntity = records.get(i + 1);
//            Date time = pushBillEntity.getCreateTime();
//            Date nextTime = nextPushBillEntity.getCreateTime();
//            int yearMonth = getYearMonth(time);
//            int nextYearMonth = getYearMonth(nextTime);
//            if (yearMonth != nextYearMonth) {
//                pushBillEntity.setLoge("1");
//            }
//        }
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


    private BigDecimal getPayAmount(PushBillEntity pushBillEntity) {
        //通过月份查询到总额
        Date time = pushBillEntity.getCreateTime();
        Long userId = pushBillEntity.getUserId();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String dateString = formatter.format(time);
        //获取到数据
        return pushBillMapper.findPayAmount(userId, dateString);
    }


    public TrackStateNumber selectTrackStateNumber(String bid) {
//        TrackStateNumber sosNumber = pushBillMapper.selectStateNumberBySOS(bid);
//        TrackStateNumber pbNumber = pushBillMapper.selectStateNumberByPB(bid);
//
//        if (sosNumber==null){
//            TrackStateNumber trackStateNumber = new TrackStateNumber();
//            trackStateNumber.setRefuse(0);
//            trackStateNumber.setAgree(0);
//            trackStateNumber.setTrackAgain(0);
//            trackStateNumber.setNewTask(0);
//            sosNumber = trackStateNumber;
//        }
//
//        if (pbNumber==null){
//            TrackStateNumber trackStateNumber = new TrackStateNumber();
//            trackStateNumber.setRefuse(0);
//            trackStateNumber.setAgree(0);
//            trackStateNumber.setTrackAgain(0);
//            trackStateNumber.setNewTask(0);
//            pbNumber = trackStateNumber;
//        }
//
//        sosNumber.setNewTask(sosNumber.getNewTask() + pbNumber.getNewTask());
//        sosNumber.setTrackAgain(sosNumber.getTrackAgain() + pbNumber.getTrackAgain());
//        sosNumber.setAgree(sosNumber.getAgree() + pbNumber.getAgree());
//        sosNumber.setRefuse(sosNumber.getRefuse() + pbNumber.getRefuse());


        //1.新任务。  2.到现场    3.未现场    4.无车辆
        //5.再次跟踪   6.拒绝到店，  7.同意到店    8.推修成功

        //1.新任务。  2.到现场    3.未现场    4.现场补贴
        //5.再次跟踪   6.推修失败，  7.推修成功    8.审核结果

        TrackStateNumber sosNumber = new TrackStateNumber();
        //1.同意 2.再次。3.拒绝"
        //查询到新订单的总数
        Integer newTask = pushBillMapper.findAllNewTask(bid);

        //到现场
        Integer comeScene = pushBillMapper.findComeScene(bid);

        //未现场
        Integer notToScene = pushBillMapper.findNotToScene(bid);

        //无车辆---->现场补助
        Integer noCar = pushBillMapper.findNoCar(bid);

        //查询到pb中的再次跟踪
        Integer pbTrackAgain = pushBillMapper.findPbAgree(bid);
        //pb中的拒绝到店
        Integer noToMer = pushBillMapper.findPbTrackAgain(bid);
        //pb中的同意到店---->推修成功
        Integer agree = pushBillMapper.findPushSuccess(bid);
        //pb中的推修成功---->审核结果
        Integer pushSuccess = pushBillMapper.findPbRefuse(bid);


//        //sos 再次跟踪
//        Integer sosTrackAgain = pushBillMapper.findSosAgree(bid);
//        //sos 拒绝到店
//        Integer sosNoToMer = pushBillMapper.findSosTrackAgain(bid);
//        //sos 同意到店
//        Integer sosAgree = pushBillMapper.findSosRefuse(bid);
//        //sos 推修成功
//        Integer sosPushSuccess = pushBillMapper.findSosPushSuccess(bid);

        sosNumber.setNewTask(newTask);
        sosNumber.setComeScene(comeScene);
        sosNumber.setNoToScene(notToScene);
        sosNumber.setNoCar(noCar);

        sosNumber.setTrackAgain(pbTrackAgain );
        sosNumber.setAgree(agree );
        sosNumber.setRefuse(noToMer);
        sosNumber.setPushSuccess(pushSuccess);

        return sosNumber;
    }

    public List<CheckImgList> selectSceneImg(Long id) {
        return pushBillMapper.selectSceneImg(id);
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
                AccidentRecordEntity accidentRecord = new AccidentRecordEntity();
                if (source == 1) {
                    accidentRecord = accidentRecordService.selectById(accid);
                } else {
                    BizAccidentEntity bizAccident = bizAccidentService.findById(accid);
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

    public PushBillEntity findAccident(Integer id, String s) {
        return pushBillMapper.findAccident(id,s);
    }
}
