package com.stylefeng.guns.modular.system.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.datascope.DataScope;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.modular.system.dao.OpenClaimMapper;
import com.stylefeng.guns.modular.system.model.BizOpenClaimExReport;
import com.stylefeng.guns.modular.system.model.OpenClaim;
import com.stylefeng.guns.modular.system.service.IBizOpenClaimExReportService;
import com.stylefeng.guns.modular.system.service.IOpenClaimService;
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
 * 开放平台理赔单 服务实现类
 * </p>
 *
 * @author kosans
 * @since 2018-09-04
 */
@Service
@Transactional
public class OpenClaimServiceImpl extends ServiceImpl<OpenClaimMapper, OpenClaim> implements IOpenClaimService {
    @Autowired
    private IBizOpenClaimExReportService bizOpenClaimExReportService;

    @Override
    public String getOpenClaimOrderNo() {
        return this.baseMapper.getOpenClaimOrderNo();
    }

    @Override
    public List<Map<String, Object>> selectByCondition(Page<OpenClaim> page, DataScope dataScope, OpenClaim openClaim, String condition, String createStartTime, String createEndTime, Integer isComplete, String orderByField, boolean isAsc) {
        return this.baseMapper.selectByCondition(page, dataScope, openClaim, condition, createStartTime, createEndTime, isComplete, orderByField, isAsc);
    }
    @Override
    public List<Map<String, Object>> selectByCondition2(Page<OpenClaim> page, DataScope dataScope, OpenClaim openClaim, String condition, String createStartTime, String createEndTime, Integer isComplete, String orderByField, boolean isAsc) {
        return this.baseMapper.selectByCondition2(page, dataScope, openClaim, condition, createStartTime, createEndTime, isComplete, orderByField, isAsc);
    }



    @Override
    public List<Map<String, Object>> selectByConditionForRest(Page<OpenClaim> page, DataScope dataScope, OpenClaim openClaim, String condition, String createStartTime, String createEndTime, Integer isComplete, String orderByField, boolean isAsc) {
        return this.baseMapper.selectByConditionForRest(page, dataScope, openClaim, condition, createStartTime, createEndTime, isComplete, orderByField, isAsc);
    }

    @Override
    public List<Map<String, Object>> selectByConditionForExport(OpenClaim openClaim, String condition, String createStartTime, String createEndTime, Integer isComplete) {
        return this.baseMapper.selectByConditionForExport(openClaim, condition, createStartTime, createEndTime, isComplete);
    }

    @Override
    public BigDecimal queryFixLossSumByCondition(OpenClaim openClaim, String condition, String createStartTime, String createEndTime, Integer isComplete) {
        return this.baseMapper.queryFixLossSumByCondition(openClaim, condition, createStartTime, createEndTime, isComplete);
    }

    @Override
    public long countByCondition(OpenClaim openClaim, String condition, String createStartTime, String createEndTime, Integer isComplete) {
        return this.baseMapper.countByCondition(openClaim, condition, createStartTime, createEndTime, isComplete);
    }


    @Override
    public    List<Map<String, Object>>  selectCountByStatus(int deptId) {
        return this.baseMapper.selectCountByStatus(deptId);
    }



    @Override
    public BigDecimal getOpenClaimOrdersIncome(String openid) {
        return baseMapper.getOpenClaimOrdersIncome(openid);
    }

    @Override
    public BigDecimal getOpenClaimOrdersIncomeByAccount(String account) {
        return baseMapper.getOpenClaimOrdersIncomeByAccount(account);
    }







    @Override
    @Transactional
    public void reportExMsg(OpenClaim openClaim, String exMgs, String exImgUrls) {
        if (openClaim.getId() == null) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        OpenClaim var1 = this.selectById(openClaim.getId());
        String name = ShiroKit.getUser().getName();
        String account = ShiroKit.getUser().getAccount();
        BizOpenClaimExReport exReport = new BizOpenClaimExReport();
        exReport.setCreateTime(new Date());
        exReport.setOrderno(var1.getOrderno());
        exReport.setOperator(account);
        exReport.setName(name);
        exReport.setMessage(exMgs);
        exReport.setExImgUrls(exImgUrls);
        bizOpenClaimExReportService.insert(exReport);
        openClaim.setHasException(1);
        this.updateById(openClaim);
    }

    @Override
    public Object countData() {
        Map map = new HashMap();
        map.put("openOrder", baseMapper.selectCountOpenOrder(-1));
        map.put("receipt", baseMapper.selectCountOpenOrder(1));
        map.put("money", baseMapper.selectCountMoney());
        map.put("crosstown", baseMapper.selectCountOpenOrder(2));
        map.put("settlement", baseMapper.selectCountOpenOrder(3));
        return map;
    }

    @Override
    public void update(OpenClaim openClaim1) {
        baseMapper.updateStatusById(openClaim1.getId(), openClaim1.getStatus());
    }
}
