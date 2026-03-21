package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.cwork.*;
import com.cheji.web.modular.domain.BUserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-10-10
 */
public interface BUserMapper extends BaseMapper<BUserEntity> {
    //城市
    List<ListMessage> findList(@Param("city")Integer city);
    //修理厂
    List<ListMessage> findGarage(@Param("city")Integer city,@Param("county")Integer county);
    //根据品牌找修理厂
    List<ListMessage> findByBrand(@Param("city")Integer city, @Param("brand")String brand);
    //4s店和城市
    List<ListMessage> findByStores(@Param("city")Integer city,@Param("county")Integer county);
    //4s店和品牌
    List<ListMessage> findByfoursStoresBrand(@Param("city")Integer city,@Param("county")Integer county,@Param("brand") String brand);
    //根据城市品牌和标签来找商户
    List<ListMessage> findGarByBrandAndLable(@Param("city")Integer city, @Param("brand")String brand, @Param("lable")String lable);
    //根据城市品牌和级别级别来查询
    List<ListMessage> findGarByBrandAndLevel(@Param("city")Integer city, @Param("brand")String brande);
    //根据城市和标签来查询修理厂
    List<ListMessage> findGarByLable(@Param("city")Integer city,@Param("lable") String lable);
    //根据级别来找修理厂
    List<ListMessage> findGarByLevel(@Param("city")Integer city);
    //根据城市和品牌和标签来找到4s店和专修店
    List<ListMessage> findStoByBrandAndLable(@Param("city")Integer city,@Param("brand") String brand, @Param("lable")String lable);
    //根据城市和品牌等级来找到4s店和专修店
    List<ListMessage> findStoByBrandAndLevel(@Param("city")Integer city,@Param("brand") String brand);
    //根据城市和标签来找到4s店和专修店
    List<ListMessage> findStoByLable(@Param("city")Integer city,@Param("lable") String lable);
    //根据级别来找到4s店和专修店
    List<ListMessage> findStoByLevel(@Param("city")Integer city);

    List<ListMessage> findAllGarage( @Param("city") Integer city,@Param("county")Integer county, @Param("brand") String brand, @Param("lable") String lable, @Param("level") String level);

    //根据商户code查询到商户
    MerchantsDetails findMerchantsByCode(String merchantsCode);
    //查询订单需要的商户信息
    IndentAndMerchants indentMerchantsMes(String merchanstId);

    //空订单状态下的推荐4s店
    List<ListMessage> findRecommendMer(@Param("city")Integer city);

    String findDetailsLable(String s);

    //洗车列表查询
    CleanMerDto findMechantsByid(String userid);

    //查询门店洗车详情
    CleanMerDto findCleanDetails(String userBId);

    //修改余额操作
    BUserEntity updateBalance(String userBId);

    //查询店铺展示
    StoreDisplayDto findstoreDisplay(String userBId);

    //首页查询推荐商户
    List<ListMessage> findRecommendFirstMer(@Param("city")String city,@Param("pagesize")Integer pagesize);

    //查询喷漆商户数据
    SparyMerchantsDto findSprayMerchants(@Param("userBId") Integer userBId, @Param("cityCode") Integer cityCode, @Param("score") String score,@Param("pagesize") Integer pagesize);

    //查询b端id 通过c端电话
    String selectByUsername(String username);

    List<String> findBrand(String s);

    List<ListMessage> findListByCounty(Integer city);
}
