package com.stylefeng.guns.modular.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.stylefeng.guns.modular.system.constant.BizYckCzmxStatus;
import com.stylefeng.guns.modular.system.dao.PushRecordMapper;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 事故推送记录 服务实现类
 * </p>
 *
 * @author kosans
 * @since 2018-07-26
 */
@Service
@Transactional
public class PushRecordServiceImpl extends ServiceImpl<PushRecordMapper, PushRecord> implements IPushRecordService {

    @Autowired
    IBizWxUserService bizWxUserService;

    @Autowired
    IUserService userService;

    @Autowired
    IBizYckBalanceService bizYckBalanceService;

    @Autowired
    IBizYckCzmxService bizYckCzmxService;

    @Override
    public List<Map<String, Object>> getPushRecords2(Page<PushRecord> page, int realness, String beginTime, String endTime, String account, String orderByField, boolean isAsc) {
        return this.baseMapper.getPushRecords2(page, realness, account);
    }

    @Override
    public List<Map<String, Object>> getPushRecords(Page<PushRecord> page, String beginTime, String endTime, String account, String orderByField, boolean isAsc) {
        return this.baseMapper.getPushRecords(page, beginTime, endTime, account, orderByField, isAsc);
    }

    @Override
    public Map<String, Object> getAppHomeAccident(String account) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> waitList = this.baseMapper.getAppHomeAccidentWait(account);
        List<Map<String, Object>> noWaitList = this.baseMapper.getAppHomeAccidentNoWait(account);
        map.put("wait", waitList);
        map.put("other", noWaitList);
        return map;
    }

    @Override
    public List<Map<String, Object>> getPushRecords3(int accid, String beginTime, String endTime, String account, String orderByField, boolean isAsc) {
        return this.baseMapper.getPushRecords3(accid, account);
    }


    @Override
    public List<Map<String, Object>> getPushFSRecords(Page<PushRecord> page, String beginTime, String endTime, Integer deptid, String orderByField, boolean isAsc) {
        return this.baseMapper.getPushFSRecords(page, beginTime, endTime, deptid, orderByField, isAsc);
    }

    @Override
    public List<Map<String, Object>> getPushRecordsByAccount(Page<PushRecord> page, String account, String orderByField, boolean isAsc) {

        return this.baseMapper.getPushRecordsByAccount(page, account, orderByField, isAsc);
    }

    @Override
    @Transactional
    public int setStatus(Integer id, int status, Date modifyTime) {
        return this.baseMapper.setStatus(id, status, modifyTime);
    }

    @Override
    @Transactional
    public void pushClaims(List<PushRecord> pushRecords, String account, BigDecimal accLevelValue) {
        User user = userService.getByAccount(account);
//        if(user.getDeptid() == null){
//            throw new GunsException(BizExceptionEnum.ACCID_DEPTID_NULL);
//        }

        BizWxUser bizWxUser = bizWxUserService.selectOne(new EntityWrapper<BizWxUser>().eq("phone", user.getPhone()));
        BizYckBalance bizYckBalance = bizYckBalanceService.selectOne(new EntityWrapper<BizYckBalance>().eq("account", user.getAccount()));
        //扣除金额大于0
        if (accLevelValue.compareTo(BigDecimal.ZERO) == 1) {
            //余额足够扣除
            if (bizYckBalance != null) {
                boolean flag = bizYckBalanceService.reduceBalance(accLevelValue, user.getAccount(), new Date());
                if (flag) {
                    if (accLevelValue.compareTo(BigDecimal.ZERO) == 1) {
                        // 记录扣款明细
                        BizYckCzmx bizYckCzmx = new BizYckCzmx();
                        bizYckCzmx.setAmount(accLevelValue.negate());
                        bizYckCzmx.setDetailType(BizYckCzmxStatus.EXPEND.getCode());
                        if (bizWxUser != null) {
                            bizYckCzmx.setOpenid(bizWxUser.getOpenid());
                        }

                        bizYckCzmx.setAccount(user.getAccount());
                        bizYckCzmx.setOperator("cheji");
                        bizYckCzmx.setCreateTime(new Date());
                        bizYckCzmx.setAccid(pushRecords.get(0).getAccid().intValue());
                        bizYckCzmxService.insert(bizYckCzmx);
                    }
                    //插入推送记录
                    this.insertOrUpdateBatch(pushRecords);
                }
            } else {
                List<BizYckBalance> account1 = bizYckBalanceService.selectList(new EntityWrapper<BizYckBalance>().eq("account", user.getAccount()));
                if (account1.size() == 0) {
                    BizYckBalance bizYckBalance1 = new BizYckBalance();
                    bizYckBalance1.setAccount(user.getAccount());
                    bizYckBalance1.setBalance(BigDecimal.ZERO);
                    bizYckBalance1.setCreateTime(new Date());
                    bizYckBalance1.setOpenid(user.getAccount());
                    bizYckBalanceService.insert(bizYckBalance1);
                }

                boolean flag = bizYckBalanceService.reduceBalance(accLevelValue, user.getAccount(), new Date());
//                throw new GunsException(BizExceptionEnum.ACCID_BALANCE_REDUCE_LIMIT);
                this.insertOrUpdateBatch(pushRecords);
            }
        } else {
            //插入推送记录
            this.insertOrUpdateBatch(pushRecords);
        }
    }
}
