package com.cheji.web.modular.service;

import com.cheji.web.modular.domain.AppUserAccountRecordEntity;
import com.cheji.web.modular.domain.AppUserEntity;
import com.cheji.web.modular.domain.AppWxCashOutRecordEntity;
import com.cheji.web.modular.mapper.AppUserAccountRecordMapper;
import com.cheji.web.modular.mapper.AppUserMapper;
import com.cheji.web.modular.mapper.AppWxCashOutRecordEntityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * <p>
 *     退vip申请提现记录
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionCashOutService  {

    @Resource
    private AppUserMapper appUserMapper;

    @Resource
    private AppWxCashOutRecordEntityMapper appWxCashOutRecordEntityMapper;

    @Resource
    private AppUserAccountRecordMapper appUserAccountRecordMapper;
    /**
     * 增加提现记录
     *
     * @param amount 提现金额
     * @param userId 用户id
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String addCashOut(BigDecimal amount, Integer userId, AppWxCashOutRecordEntity appWxCashOutRecordEntity) {
        //修改用户余额
        AppUserEntity appUserEntity = appUserMapper.selectUser(userId);//锁行  事务提交之后自动解锁

        if (appUserEntity.getBalance().compareTo(amount) == -1) {
            return "";//用户账户不够扣
        }
        appWxCashOutRecordEntity.setPartnerTradeNo("BANK" + getOrderNo());

        //增加提现记录
        appWxCashOutRecordEntityMapper.insert(appWxCashOutRecordEntity);

        appUserEntity.setBalance(appUserEntity.getBalance().subtract(amount));
        appUserMapper.updateById(appUserEntity);

        AppUserAccountRecordEntity appUserAccountRecordEntity = new AppUserAccountRecordEntity();
        appUserAccountRecordEntity.setCreateTime(new Date());
        appUserAccountRecordEntity.setAddFlag(2);
        appUserAccountRecordEntity.setMomey(amount);
        appUserAccountRecordEntity.setSource(1);
        appUserAccountRecordEntity.setType(1);
        appUserAccountRecordEntity.setUserId(userId);
        appUserAccountRecordEntity.setBusinessId(appWxCashOutRecordEntity.getId() + "");
        appUserAccountRecordMapper.insert(appUserAccountRecordEntity);
        return appWxCashOutRecordEntity.getPartnerTradeNo();
    }


    public static String getOrderNo() {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        Random random = new Random();
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数
        return str + rannum;// 当前时间 + 系统5随机生成位数
    }

}
