package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.InsuranceMerchantsEntity;
import com.cheji.b.modular.mapper.InsuranceMerchantsMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 保险和商户关联表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-10-09
 */
@Service
public class InsuranceMerchantsService extends ServiceImpl<InsuranceMerchantsMapper, InsuranceMerchantsEntity> implements IService<InsuranceMerchantsEntity> {

    //添加修改保险数据
    public void update(Integer userBId, Integer[] id) {
        //根据商户id查询到对应的保险信息
        EntityWrapper<InsuranceMerchantsEntity> insuranceWrapper = new EntityWrapper<>();
        insuranceWrapper.eq("user_b_id", userBId);
        List<InsuranceMerchantsEntity> insuranceMerchantsList = selectList(insuranceWrapper);
        //查询为空第一次直接添加
        if (insuranceMerchantsList.isEmpty()){
            for (Integer integer : id) {
                InsuranceMerchantsEntity insuranceMerchants = new InsuranceMerchantsEntity();
                insuranceMerchants.setState(1);
                insuranceMerchants.setInsuranceId(integer);
                insuranceMerchants.setUserBId(userBId);
                insuranceMerchants.setCreateTime(new Date());
                insuranceMerchants.setUpdateTime(new Date());
                insert(insuranceMerchants);
            }
        }else {
            //不是空就判断
            flag: for (Integer integer : id) {                                                     //1,2,3,4
                for (InsuranceMerchantsEntity merchantsEntity : insuranceMerchantsList) {       //2，3
                    Integer insuranceId = merchantsEntity.getInsuranceId();
                    if (integer==insuranceId&&merchantsEntity.getState()==1){
                        continue flag;
                    }else if (integer==insuranceId&&merchantsEntity.getState()==2){
                        merchantsEntity.setState(1);
                        updateById(merchantsEntity);
                        continue flag;
                    }
                }
                //添加数据
                InsuranceMerchantsEntity insuranceMerchantsEntity = new InsuranceMerchantsEntity();
                insuranceMerchantsEntity.setState(1);
                insuranceMerchantsEntity.setInsuranceId(integer);
                insuranceMerchantsEntity.setUserBId(userBId);
                insuranceMerchantsEntity.setCreateTime(new Date());
                insuranceMerchantsEntity.setUpdateTime(new Date());
                insert(insuranceMerchantsEntity);
            }

            flag: for (InsuranceMerchantsEntity entity : insuranceMerchantsList) {//23
                Integer insuranceId = entity.getInsuranceId();
                for (Integer integer : id) {                        //1234
                    if (insuranceId==integer){
                    continue flag;
                    }
                }
                entity.setState(2);
                updateById(entity);
            }
        }
    }
}
