package com.jeesite.modules.job.service;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.app.dao.AppCleanIndetDao;
import com.jeesite.modules.app.dao.AppCleanPriceDetailDao;
import com.jeesite.modules.app.dao.AppOrderRollBackDao;
import com.jeesite.modules.app.entity.AppCleanIndet;
import com.jeesite.modules.app.entity.AppCleanPriceDetail;
import com.jeesite.modules.app.entity.AppOrderRollBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class OrderRollBackService {
    @Resource
    private AppCleanIndetDao appCleanIndetDao;

    @Resource
    private AppCleanPriceDetailDao appCleanPriceDetailDao;

    private final static Logger logger = LoggerFactory.getLogger(OrderRollBackService.class);

    @Resource
    private AppOrderRollBackDao appOrderRollBackDao;
    @Transactional(rollbackFor = Exception.class)
    public void rollBack(AppOrderRollBack appOrderRollBack) {
        //洗车
        AppCleanIndet appCleanIndetParamer = new AppCleanIndet();
        appCleanIndetParamer.setId(appOrderRollBack.getOrderId());
        AppCleanIndet byEntity = appCleanIndetDao.getByEntity(appCleanIndetParamer);
        if (byEntity == null) {
            logger.error("## 回滚合约订单库存出错  byEntity==null appOrderRollBack={} ", appOrderRollBack);
        }
        String bussinessId = byEntity.getBussinessId();
        if (StringUtils.isEmpty(bussinessId)) {
            logger.error("## 回滚合约订单库存出错  bussinessId==null appOrderRollBack={}；byEntity={} ", appOrderRollBack, byEntity);
        }
        AppCleanPriceDetail appCleanPriceDetail = appCleanPriceDetailDao.selectForUpdate(bussinessId);
        if (appCleanPriceDetail == null) {
            logger.error("## 回滚合约订单库存出错  appCleanPriceDetail==null  appOrderRollBack={}； appOrderRollBack={};bussinessId={} ", appOrderRollBack,
                    appOrderRollBack, bussinessId);
        }
        logger.info("###  回滚合约订单库存   回滚之前的数据={}", appCleanPriceDetail);
        appCleanPriceDetail.setResidueDegree(appCleanPriceDetail.getResidueDegree() + 1);//回滚次数加1
        appCleanPriceDetailDao.update(appCleanPriceDetail);

        appOrderRollBack.setOpsFlag(2);
        appOrderRollBackDao.update(appOrderRollBack);
        logger.info("###  回滚合约订单库存成功过  appCleanPriceDetail={}", appCleanPriceDetail);
    }
}
