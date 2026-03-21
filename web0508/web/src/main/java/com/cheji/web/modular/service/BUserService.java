package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.cwork.*;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.mapper.AppUserAccountRecordMapper;
import com.cheji.web.modular.mapper.BUserMapper;
import com.cheji.web.modular.mapper.LableMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-10-10
 */
@Service
public class BUserService extends ServiceImpl<BUserMapper, BUserEntity> implements IService<BUserEntity> {

    private Logger logger = LoggerFactory.getLogger(BUserService.class);

    @Resource
    private AppBusinessConfirmService appBusinessConfirmService;

    @Resource
    private BUserMapper bUserMapper;

    @Resource
    private MerchantsInfoBannerService infoBannerService;

    @Resource
    private MerchantsServicerService servicerService;

    @Resource
    private ImgService ImgEntityService;

    @Resource
    private MerchantsCommentsTreeService commentsTreeService;

    @Resource
    private InsuranceMerchantsService insuranceMerchantsService;

    @Resource
    private LableMapper lableMapper;

    @Resource
    private CarBrandService carBrandService;

    @Resource
    private LableDetailsReviewTreeService lableDetailsReviewTreeService;

    @Resource
    private MerchantsBrandService merchantsBrandService;

    @Resource
    private AppUserAccountRecordMapper appUserAccountRecordMapper;

    @Resource
    private CleanIndetService cleanIndetService;

    @Resource
    private AppUpMerchantsService appUpMerchantsService;

    @Resource
    private AppPhotoMerService appPhotoMerService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public List<ListMessage> findAllGarage(Integer city, String brand, String lable, String level) {
        Integer county = null;
        if (city.toString().length() > 5) {
            county = city;
            city = null;
        }
        List<ListMessage> trees = bUserMapper.findAllGarage(city, county, brand, lable, level);
        if (trees.isEmpty()) {
            return trees;
        }
        return setDate(trees, 2);
    }


    public List<ListMessage> findListByCounty(Integer city) {
        List<ListMessage> trees = bUserMapper.findListByCounty(city);
        if (trees.isEmpty()) {
            return trees;
        }
        return setDate(trees, 1);
    }

    //根据所在城市拿到商户
    public List<ListMessage> findListAll(Integer city) {
        List<ListMessage> trees = bUserMapper.findList(city);
        if (trees.isEmpty()) {
            return trees;
        }
        return setDate(trees, 1);
    }

    //修理厂和城市
    public List<ListMessage> findLisgarage(Integer city) {
        Integer county = null;
        if (city.toString().length() > 5) {
            county = city;
            city = null;
        }
        List<ListMessage> list = bUserMapper.findGarage(city, county);
        if (list.isEmpty()) {
            return list;
        }
        return setDate(list, 2);
    }

    //根据品牌来筛选修理厂
    public List<ListMessage> findListByBrand(Integer city, String brand) {
        List<ListMessage> brandgran = bUserMapper.findByBrand(city, brand);
        if (brandgran.isEmpty()) {
            return brandgran;
        }
        return setDate(brandgran, 2);
    }

    //4s店和城市
    public List<ListMessage> findListByStores(Integer city) {
        Integer county = null;
        if (city.toString().length() > 5) {
            county = city;
            city = null;
        }
        List<ListMessage> stores = bUserMapper.findByStores(city, county);
        if (stores.isEmpty()) {
            return stores;
        }
        return setDate(stores, 1);
    }

    //4s店和品牌
    public List<ListMessage> findListByfoursStoresBrand(Integer city, String brand) {
        Integer county = null;
        if (city.toString().length() > 5) {
            county = city;
            city = null;
        }
        List<ListMessage> storesbrand = bUserMapper.findByfoursStoresBrand(city, county, brand);
        if (storesbrand.isEmpty()) {
            return storesbrand;
        }
        return setDate(storesbrand, 1);
    }

    //找到城市品牌标签查询修理厂
    public List<ListMessage> findgarageByBrandAndLable(Integer city, String brand, String lable) {
        List<ListMessage> brandAndLable = bUserMapper.findGarByBrandAndLable(city, brand, lable);
        if (brandAndLable.isEmpty()) {
            return brandAndLable;
        }
        return setDate(brandAndLable, 2);
    }

    //通过城市品牌和等级来查询修理厂
    public List<ListMessage> findgarageByBrandAndLevel(Integer city, String brand) {
        List<ListMessage> brandAndLevel = bUserMapper.findGarByBrandAndLevel(city, brand);
        if (brandAndLevel.isEmpty()) {
            return brandAndLevel;
        }
        return setDate(brandAndLevel, 2);
    }

    //通过标签来查询修理厂
    public List<ListMessage> findgarageBylable(Integer city, String lable) {
        List<ListMessage> lablebrand = bUserMapper.findGarByLable(city, lable);
        if (lablebrand.isEmpty()) {
            return lablebrand;
        }
        return setDate(lablebrand, 2);
    }

    //通过级别来查询修理厂
    public List<ListMessage> findgarageBylevel(Integer city) {
        List<ListMessage> levelve = bUserMapper.findGarByLevel(city);
        if (levelve.isEmpty()) {
            return levelve;
        }
        return setDate(levelve, 2);
    }

    //通过品牌和标签来找到4s店和专修店
    public List<ListMessage> findStoresByBrandAndLable(Integer city, String brand, String lable) {
        List<ListMessage> stoBrandLable = bUserMapper.findStoByBrandAndLable(city, brand, lable);
        if (stoBrandLable.isEmpty()) {
            return stoBrandLable;
        }
        return setDate(stoBrandLable, 1);
    }

    //通过品牌和等级来找到4s店
    public List<ListMessage> findStoresByBrandAndLevel(Integer city, String brand) {
        List<ListMessage> stoBrandLevel = bUserMapper.findStoByBrandAndLevel(city, brand);
        if (stoBrandLevel.isEmpty()) {
            return stoBrandLevel;
        }
        return setDate(stoBrandLevel, 1);
    }

    //通过标签找到4s店和专修店
    public List<ListMessage> findStoresBylable(Integer city, String lable) {
        List<ListMessage> stolable = bUserMapper.findStoByLable(city, lable);
        if (stolable.isEmpty()) {
            return stolable;
        }
        return setDate(stolable, 1);
    }

    //通过级别来找到商户
    public List<ListMessage> findStoresBylevel(Integer city) {
        List<ListMessage> StoLevel = bUserMapper.findStoByLevel(city);
        if (StoLevel.isEmpty()) {
            return StoLevel;
        }
        return setDate(StoLevel, 2);
    }

    public MerchantsDetails findZDetailts(String merchantsCode) {
        MerchantsDetails merchantsDetails = new MerchantsDetails();

        //去掉最后一个字符
        merchantsCode = merchantsCode.substring(0, merchantsCode.length() - 1);
//        id merchantsCode,
//        merchants_name merchantsName,
//        service_sorce serviceSorce,
//        effciency_score effciencyScore,
//        score score,
//        huanxin_user_name huanxinUserName,
//        lat lat,
//        lng lng,
//        address address,
//        merchants_phone merchantsPhone,
//        type type
        AppUpMerchantsEntity appUpMerchants = appUpMerchantsService.selectById(merchantsCode);
        merchantsDetails.setMerchantsCode(merchantsCode + "Z");
//        BUserEntity bUserEntity = bUserMapper.selectById("150");
        merchantsDetails.setMerchantsName(appUpMerchants.getName());
        merchantsDetails.setServiceSorce(5);
        merchantsDetails.setEffciencyScore(5);
        merchantsDetails.setScore(new BigDecimal(appUpMerchants.getScore()));
        merchantsDetails.setHuanxinUserName(appUpMerchants.getHuanxinUsername());
        merchantsDetails.setLng(appUpMerchants.getLng());
        merchantsDetails.setLat(appUpMerchants.getLat());
        merchantsDetails.setAddress(appUpMerchants.getAddress());
        merchantsDetails.setMerchantsPhone("13348880288");
        merchantsDetails.setType(7);
        List<AppBusinessConfirmEntity> appBusinessConfirmEntities = new ArrayList<>();
        merchantsDetails.setBusinessConfirmList(appBusinessConfirmEntities);
        String s1 = stringRedisTemplate.opsForValue().get(RedisConstant.SHARE_CONTENT + 11);
        merchantsDetails.setConfirmText(s1);

        List<String> strings = new ArrayList<>();
        //品牌
        if (StringUtils.isNotEmpty(appUpMerchants.getBrand())) {
            CarBrandEntity carBrandEntity = carBrandService.selectById(appUpMerchants.getBrand());
            strings.add(carBrandEntity.getPicUrl());
            merchantsDetails.setBrand(strings);
        }

        List<MerchantsInfoBannerEntity> bannerList = appPhotoMerService.selectBrannerList(merchantsCode);
        merchantsDetails.setMerchantsInfoBannerEntities(bannerList);

        List<MerchantsServicerEntity> merchantsServicerEntities = new ArrayList<>();
        MerchantsServicerEntity merchantsServicerEntity = new MerchantsServicerEntity();
        merchantsServicerEntity.setName("服务顾问");
        merchantsServicerEntity.setImgUrl("https://obs-4f4a.obs.cn-southwest-2.myhuaweicloud.com/img/u%3D4205134036%2C1641827673%26fm%3D26%26gp%3D0.jpg");
        merchantsServicerEntities.add(merchantsServicerEntity);
        merchantsDetails.setMerchantsServicerEntities(merchantsServicerEntities);

        List<InsuranceMerchantsEntity> insuranceMerchantsEntities = new ArrayList<>();
        InsuranceMerchantsEntity insuranceMerchantsEntity = new InsuranceMerchantsEntity();
        insuranceMerchantsEntity.setUrl("https://obs-4f4a.obs.cn-southwest-2.myhuaweicloud.com/img/insurance/zhongguorenbao.png");
        insuranceMerchantsEntities.add(insuranceMerchantsEntity);
        merchantsDetails.setInsuranceMerchantEntities(insuranceMerchantsEntities);

        ArrayList<AppBusinessConfirmEntity> appBusinessConfirmEntities1 = new ArrayList<>();
        if (StringUtils.isEmpty(appUpMerchants.getBrand())) {
            //修理厂
            for (int i = 0; i < 3; i++) {
                if (i == 0) {
                    AppBusinessConfirmEntity appBusinessConfirmEntity = new AppBusinessConfirmEntity();
                    appBusinessConfirmEntity.setBusinessConfirm("店内保险");
                    appBusinessConfirmEntity.setAccidentReponsibility("全责");
                    appBusinessConfirmEntity.setCustomersHave(new BigDecimal("0"));
                    appBusinessConfirmEntity.setNotUnitedCustomer(new BigDecimal("0"));
                    appBusinessConfirmEntity.setCharterShop(new BigDecimal("0.15"));
                    appBusinessConfirmEntities1.add(appBusinessConfirmEntity);
                } else if (i == 1) {
                    AppBusinessConfirmEntity appBusinessConfirmEntity = new AppBusinessConfirmEntity();
                    appBusinessConfirmEntity.setBusinessConfirm("店内保险");
                    appBusinessConfirmEntity.setAccidentReponsibility("三责");
                    appBusinessConfirmEntity.setCustomersHave(new BigDecimal("0"));
                    appBusinessConfirmEntity.setNotUnitedCustomer(new BigDecimal("0.15"));
                    appBusinessConfirmEntity.setCharterShop(new BigDecimal("0.15"));
                    appBusinessConfirmEntities1.add(appBusinessConfirmEntity);
                } else {
                    AppBusinessConfirmEntity appBusinessConfirmEntity = new AppBusinessConfirmEntity();
                    appBusinessConfirmEntity.setBusinessConfirm("店外保险");
                    appBusinessConfirmEntity.setAccidentReponsibility("不分");
                    appBusinessConfirmEntity.setCustomersHave(new BigDecimal("0.15"));
                    appBusinessConfirmEntity.setNotUnitedCustomer(new BigDecimal("0.15"));
                    appBusinessConfirmEntity.setCharterShop(new BigDecimal("0.15"));
                    appBusinessConfirmEntities1.add(appBusinessConfirmEntity);
                }
            }
            merchantsDetails.setBusinessConfirmList(appBusinessConfirmEntities1);
        } else {
            for (int i = 0; i < 3; i++) {
                if (i == 0) {
                    AppBusinessConfirmEntity appBusinessConfirmEntity = new AppBusinessConfirmEntity();
                    appBusinessConfirmEntity.setBusinessConfirm("店内保险");
                    appBusinessConfirmEntity.setAccidentReponsibility("全责");
                    appBusinessConfirmEntity.setCustomersHave(new BigDecimal("0"));
                    appBusinessConfirmEntity.setNotUnitedCustomer(new BigDecimal("0"));
                    appBusinessConfirmEntity.setCharterShop(new BigDecimal("0.07"));
                    appBusinessConfirmEntities1.add(appBusinessConfirmEntity);
                } else if (i == 1) {
                    AppBusinessConfirmEntity appBusinessConfirmEntity = new AppBusinessConfirmEntity();
                    appBusinessConfirmEntity.setBusinessConfirm("店内保险");
                    appBusinessConfirmEntity.setAccidentReponsibility("三责");
                    appBusinessConfirmEntity.setCustomersHave(new BigDecimal("0"));
                    appBusinessConfirmEntity.setNotUnitedCustomer(new BigDecimal("0.07"));
                    appBusinessConfirmEntity.setCharterShop(new BigDecimal("0.07"));
                    appBusinessConfirmEntities1.add(appBusinessConfirmEntity);
                } else {
                    AppBusinessConfirmEntity appBusinessConfirmEntity = new AppBusinessConfirmEntity();
                    appBusinessConfirmEntity.setBusinessConfirm("店外保险");
                    appBusinessConfirmEntity.setAccidentReponsibility("不分");
                    appBusinessConfirmEntity.setCustomersHave(new BigDecimal("0.07"));
                    appBusinessConfirmEntity.setNotUnitedCustomer(new BigDecimal("0.07"));
                    appBusinessConfirmEntity.setCharterShop(new BigDecimal("0.07"));
                    appBusinessConfirmEntities1.add(appBusinessConfirmEntity);
                }
            }
            merchantsDetails.setBusinessConfirmList(appBusinessConfirmEntities1);
        }

        if (appUpMerchants.getProportion() != null) {
            BigDecimal proportion = appUpMerchants.getProportion();
            for (int i = 0; i < 3; i++) {
                if (i == 0) {
                    AppBusinessConfirmEntity appBusinessConfirmEntity = new AppBusinessConfirmEntity();
                    appBusinessConfirmEntity.setBusinessConfirm("店内保险");
                    appBusinessConfirmEntity.setAccidentReponsibility("全责");
                    appBusinessConfirmEntity.setCustomersHave(proportion);
                    appBusinessConfirmEntity.setNotUnitedCustomer(proportion);
                    appBusinessConfirmEntity.setCharterShop(proportion);
                    appBusinessConfirmEntities1.add(appBusinessConfirmEntity);
                } else if (i == 1) {
                    AppBusinessConfirmEntity appBusinessConfirmEntity = new AppBusinessConfirmEntity();
                    appBusinessConfirmEntity.setBusinessConfirm("店内保险");
                    appBusinessConfirmEntity.setAccidentReponsibility("三责");
                    appBusinessConfirmEntity.setCustomersHave(proportion);
                    appBusinessConfirmEntity.setNotUnitedCustomer(proportion);
                    appBusinessConfirmEntity.setCharterShop(proportion);
                    appBusinessConfirmEntities1.add(appBusinessConfirmEntity);
                } else {
                    AppBusinessConfirmEntity appBusinessConfirmEntity = new AppBusinessConfirmEntity();
                    appBusinessConfirmEntity.setBusinessConfirm("店外保险");
                    appBusinessConfirmEntity.setAccidentReponsibility("不分");
                    appBusinessConfirmEntity.setCustomersHave(proportion);
                    appBusinessConfirmEntity.setNotUnitedCustomer(proportion);
                    appBusinessConfirmEntity.setCharterShop(proportion);
                    appBusinessConfirmEntities1.add(appBusinessConfirmEntity);
                }
            }
        }


        List<String> strings1 = new ArrayList<>();
        String lable = "事故";
        strings1.add(lable);
        merchantsDetails.setLableEntities(strings1);


        ArrayList<MerchantsLableEntity> merchantsLableEntities = new ArrayList<>();
        MerchantsLableEntity merchantsLableEntity = new MerchantsLableEntity();
        merchantsLableEntity.setUrl("https://obs-4f4a.obs.cn-southwest-2.myhuaweicloud.com/system/serviceproject/classification_accident.png");
        merchantsLableEntity.setRebates(new BigDecimal("0.05"));
        merchantsLableEntity.setLableName("事故");
        merchantsLableEntities.add(merchantsLableEntity);
        merchantsDetails.setMerchantsLableEntities(merchantsLableEntities);

        return merchantsDetails;
    }


    //找到商户详情页面的数据
    public MerchantsDetails getMerchantsDetails(String merchantsCode) {
        //通过传过来的id查询数据
        //先查询到商户数据根据code
        MerchantsDetails merchantsDetails = bUserMapper.findMerchantsByCode(merchantsCode);
        if (merchantsDetails == null) {
            return null;
        }

//        BUserEntity bUserEntity2 = bUserMapper.selectById(56);
//        merchantsDetails.setHuanxinUserName(bUserEntity2.getHuanxinUserName());

        //查询数据商户id下所有
        EntityWrapper<AppBusinessConfirmEntity> appBusinessWrapper = new EntityWrapper<>();
        appBusinessWrapper.eq("user_b_id", merchantsCode);
        List<AppBusinessConfirmEntity> appBusinessConfirmEntities = appBusinessConfirmService.selectList(appBusinessWrapper);
        merchantsDetails.setBusinessConfirmList(appBusinessConfirmEntities);

        //查询文字
        String s1 = stringRedisTemplate.opsForValue().get(RedisConstant.SHARE_CONTENT + 11);
        merchantsDetails.setConfirmText(s1);


        EntityWrapper<BUserEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("id", merchantsCode);
        BUserEntity bUserEntity1 = selectOne(wrapper);
        //成都
        if (bUserEntity1.getCity().equals("028")) {
            merchantsDetails.setMerchantsPhone("13348880288");
            //德阳
        } else if (bUserEntity1.getCity().equals("0838")) {
            merchantsDetails.setMerchantsPhone("15682022247");
            //重庆
        } else if (bUserEntity1.getCity().equals("023")) {
            merchantsDetails.setMerchantsPhone("17388227710");
        }
        //根据商户id查询到品牌
        EntityWrapper<MerchantsBrandEntity> brandWrapper = new EntityWrapper<>();
        brandWrapper.eq("user_b_id", merchantsCode)
                .eq("state", 1);
        List<MerchantsBrandEntity> merchantsBrandEntities = merchantsBrandService.selectList(brandWrapper);
        /*if (merchantsDetails.getType()==1){
            MerchantsBrandEntity merchantsBrandEntity = merchantsBrandEntities.get(0);
            String brandId = merchantsBrandEntity.getBrandId();
            CarBrandEntity carBrandEntity = carBrandService.selectById(brandId);
            ArrayList<String > merchantsBrand = new ArrayList<>();
            merchantsBrand.add(carBrandEntity.getPicUrl());
            merchantsDetails.setBrand(merchantsBrand);
        }else*/
        if (merchantsDetails.getType() == 2) {
            merchantsDetails.setBrand(null);
        } else {
            ArrayList<String> strings = new ArrayList<>();
            for (MerchantsBrandEntity brandEntity : merchantsBrandEntities) {
                String brandId = brandEntity.getBrandId();
                CarBrandEntity carBrandEntity = carBrandService.selectById(brandId);
                strings.add(carBrandEntity.getPicUrl());
            }
            merchantsDetails.setBrand(strings);
        }

        //商户详情页面标签，
        List<String> lableEntities = new ArrayList<>();
        BUserEntity bUserEntity = bUserMapper.selectById(merchantsCode);
        String detailsLable = bUserEntity.getDetailsLable();
        if (StringUtils.isNotEmpty(detailsLable)) {
            String[] split = detailsLable.split(",");
            for (String s : split) {
                //根据id查询到标签详细信息
                String details = bUserMapper.findDetailsLable(s);
                lableEntities.add(details);
            }
            merchantsDetails.setLableEntities(lableEntities);
        }

        //对应服务项目和返点数
        //查询到对应商户的服务和返点数
        //先根据商户id查询到标签
        List<MerchantsLableEntity> lableList = lableDetailsReviewTreeService.findSerAndRebById(merchantsCode);
        if (lableList != null && lableList.size() != 0) {
            for (MerchantsLableEntity lableEntity : lableList) {
                Long id = lableEntity.getId();
                BigDecimal reba = BigDecimal.ZERO;
                if (lableEntity.getLableName().equals("事故")) {
                    //查询比例
                    reba = lableDetailsReviewTreeService.findHigeRebates(Long.valueOf(merchantsCode));

                } else {
                    reba = lableDetailsReviewTreeService.findrebates(id);
                }
                lableEntity.setRebates(reba);
            }
        }
        merchantsDetails.setMerchantsLableEntities(lableList);
        //拿到商户对应的banner图
        List<MerchantsInfoBannerEntity> bannerList = infoBannerService.getBannerById(merchantsCode);
        merchantsDetails.setMerchantsInfoBannerEntities(bannerList);
        //拿到商户对应的服务顾问
        EntityWrapper<MerchantsServicerEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_b_id", merchantsCode);
        List<MerchantsServicerEntity> merchantsServicerEntities = servicerService.selectList(entityWrapper);
        merchantsDetails.setMerchantsServicerEntities(merchantsServicerEntities);
        //拿到商户合作保险公司信息
        List<InsuranceMerchantsEntity> insuranceMerchantEntities = insuranceMerchantsService.getInsByMerchants(merchantsCode);
        merchantsDetails.setInsuranceMerchantEntities(insuranceMerchantEntities);

        return merchantsDetails;
    }

    //订单页面要拿到的商户信息
    public IndentAndMerchants indentMerchants(String merchanstId) {
        //拿到商户信息
        IndentAndMerchants indentAndMerchants = bUserMapper.indentMerchantsMes(merchanstId);
        String imgurl = indentAndMerchants.getImgurl();

        //拿到图片信息
        EntityWrapper<MerchantsInfoBannerEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_b_id", merchanstId);
        List<MerchantsInfoBannerEntity> merchantsInfoBannerEntities = infoBannerService.selectList(wrapper);
        if (merchantsInfoBannerEntities != null && merchantsInfoBannerEntities.size() != 0) {
            for (MerchantsInfoBannerEntity merchantsInfoBannerEntity : merchantsInfoBannerEntities) {
                if (merchantsInfoBannerEntity.getIndex() == 1) {
                    imgurl = merchantsInfoBannerEntity.getUrl();
                }
                indentAndMerchants.setImgurl(imgurl);
            }
        }
        //拿到对应的标签信息
        String lableid = indentAndMerchants.getLableid();
        if (StringUtils.isNotEmpty(lableid)) {
            String[] split = lableid.split(",");
            List<LableEntity> lablesses = new ArrayList<>();
            for (String s : split) {
                LableEntity lableEntity = lableMapper.findLable(Integer.valueOf(s));
                lablesses.add(lableEntity);
            }
            indentAndMerchants.setLableEntities(lablesses);
        }
        return indentAndMerchants;
    }

    //操作数据
    private List<ListMessage> setDate(List<ListMessage> list, int isType) {
        //遍历拿到商户数据
        for (ListMessage tree : list) {
            //拿到标签数据和返点数据
            if (tree.getType() == 7) {
                ArrayList<LableEntity> lableEntities = new ArrayList<>();
                LableEntity lableEntity = new LableEntity();
                lableEntity.setLabel("事故");
                lableEntities.add(lableEntity);
                tree.setLable(lableEntities);
                tree.setMerchantsCode(tree.getMerchantsCode() + "Z");
                tree.setRebates(new BigDecimal("0.07"));
                if (isType == 2) {
                    tree.setRebates(new BigDecimal("0.15"));
                }

                //设置图片
                String id = tree.getMerchantsCode();
                AppUpMerchantsEntity appUpMerchantsEntity = appUpMerchantsService.selectById(id);
                if (appUpMerchantsEntity.getProportion() != null) {
                    tree.setRebates(appUpMerchantsEntity.getProportion());
                }
                //判断品牌
                String brand = appUpMerchantsEntity.getBrand();
                if (StringUtils.isNotEmpty(brand)) {
                    CarBrandEntity carBrandEntity = carBrandService.selectById(brand);
                    tree.setBrandName(carBrandEntity.getName());
                    tree.setBrand(brand);
                    List<String> strings = new ArrayList<>();
                    strings.add(carBrandEntity.getPicUrl());
                    tree.setBrandUrl(strings);
                }
                EntityWrapper<AppPhotoMerEntity> appPhotoWrapper = new EntityWrapper<>();
                appPhotoWrapper.eq("up_id", id)
                        .eq("`index`", 1);
                AppPhotoMerEntity appPhotoMer = appPhotoMerService.selectOne(appPhotoWrapper);
                tree.setUrl(appPhotoMer.getUrl());
                tree.setToShop("0");
            } else {

                List<LableEntity> lableEntity = lableDetailsReviewTreeService.findLableName(tree.getMerchantsCode());
                //拿到标签数据
                tree.setLable(lableEntity);
                //拿到brand图片
                //根据商户id查询到中间表数据，中间表数据查询到url
                List<String> strings = new ArrayList<>();
                EntityWrapper<MerchantsBrandEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("user_b_id", tree.getMerchantsCode());
                List<MerchantsBrandEntity> merchantsBrandlist = merchantsBrandService.selectList(wrapper);
                String brandName = "";
                if (merchantsBrandlist != null && merchantsBrandlist.size() != 0) {
                    for (MerchantsBrandEntity brandEntity : merchantsBrandlist) {
                        String brandId = brandEntity.getBrandId();
                        CarBrandEntity carBrandEntity = carBrandService.selectById(brandId);
                        String picUrl = carBrandEntity.getPicUrl();
                        String name = carBrandEntity.getName();
                        brandName += name + ",";
                        tree.setBrandName(brandName);
                        strings.add(picUrl);
                        if (tree.getType() == 2) {
                            strings = null;
                            break;
                        }
                    }
                }
                tree.setBrandUrl(strings);

                //拿到轮播图
                //根据商户id拿到轮播图第一张
                List<MerchantsInfoBannerEntity> bannerById = infoBannerService.getBannerById(tree.getMerchantsCode());
                for (int i = 0; i < bannerById.size(); i++) {
                    tree.setUrl(bannerById.get(i).getUrl());
                    break;
                }
                //返点数据
//            ArrayList<BigDecimal> bigDecimals = new ArrayList<>();
//            List<MerchantsLableEntity> serAndRebById = lableDetailsReviewTreeService.findSerAndRebById(tree.getMerchantsCode());
//            if (serAndRebById!=null&&serAndRebById.size()!=0){
//                for (MerchantsLableEntity serAnd : serAndRebById) {
//                    Long id = serAnd.getId();
//                    //根据id查询到rebate
//                    List<MerchantsLableEntity> rebate = lableDetailsReviewTreeService.findallRebates(id);
//                    BigDecimal aa = BigDecimal.ZERO;
//                    for (MerchantsLableEntity merchantsLableEntity : rebate) {
//                        BigDecimal rebates = merchantsLableEntity.getRebates();
//                        aa=aa.add(rebates);
//                    }
//                    BigDecimal divide = aa.divide(BigDecimal.valueOf(rebate.size()),2,BigDecimal.ROUND_HALF_UP);
//                    bigDecimals.add(divide);
//                }
//            }
//            BigDecimal zero = BigDecimal.ZERO;
//            if (bigDecimals.size()!=0){
//                for (BigDecimal bigDecimal : bigDecimals) {
//                    zero = zero.add(bigDecimal);
//                }
//                BigDecimal divide = zero.divide(BigDecimal.valueOf(bigDecimals.size()),2,BigDecimal.ROUND_HALF_UP);
//                tree.setRebates(divide);
//            }
                BigDecimal mixRebates = lableDetailsReviewTreeService.findHigeRebates(Long.valueOf(tree.getMerchantsCode()));
                if (mixRebates == null) {
                    tree.setRebates(new BigDecimal(0));
                }
                tree.setRebates(mixRebates);

                //是否到店结款标签
                BUserEntity bUserEntity = selectById(tree.getMerchantsCode());
                String detailsLable = bUserEntity.getDetailsLable();
                if (StringUtils.isEmpty(detailsLable)) {
                    tree.setToShop("0");
                } else {
                    if (detailsLable.contains("4")) {
                        tree.setToShop("1");
                    } else {
                        tree.setToShop("0");
                    }
                }
                //查询到保存保险公司
                String names = insuranceMerchantsService.getCompanyName(tree.getMerchantsCode());
                tree.setInsurance(names);
            }
        }
        return list;
    }


    //推荐的4s店列表
    public List<ListMessage> findRecommendMer(Integer city) {
        //根据城市查询到4s店列表
        List<ListMessage> recommendMer = bUserMapper.findRecommendMer(city);
        return setDate(recommendMer, 2);

    }

    //根据商户id查询到评论
    public List<MerchantsComment> findMerchantsCommentByid(String merchantsCode, Integer pagesize, Integer type, Integer genre) {
        pagesize = (pagesize - 1) * 20;
        //拿到对应的商户评论
        //拿到除了图片之外得数据
        List<MerchantsComment> commentsTrees = commentsTreeService.getComByMerId(merchantsCode, pagesize, type, genre);
        for (MerchantsComment comments : commentsTrees) {
            String commentsCode = comments.getCommentCode();
            BigDecimal score = comments.getScore();
            if (score.compareTo(new BigDecimal(5)) == 1) {
                score = score.divide(new BigDecimal(2), 2, BigDecimal.ROUND_HALF_UP);
                comments.setScore(score);
            }
            //查询到商户回复数据
            EntityWrapper<MerchantsCommentsTreeEntity> merchantsWrapper = new EntityWrapper<>();
            merchantsWrapper.eq("parent_code", commentsCode);
            List<MerchantsCommentsTreeEntity> commentsTreeEntities = commentsTreeService.selectList(merchantsWrapper);
            if (!commentsTreeEntities.isEmpty()) {
                for (MerchantsCommentsTreeEntity entity : commentsTreeEntities) {
                    comments.setCommentsTree(entity);
                }
            }
            //拿到商户评论图片数据
            List<ImgEntity> ImgEntityList = ImgEntityService.getImgEntityByMerchentsAndType(commentsCode);
            comments.setMerchantsCommont(ImgEntityList);
        }
        return commentsTrees;

    }

    public CleanMerDto findMerchantsByid(String userid) {
        return bUserMapper.findMechantsByid(userid);
    }

    public CleanMerDto findCleanDetails(String userBId) {
        return bUserMapper.findCleanDetails(userBId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateBalance(String userBId, BigDecimal merchantsAmount, String cleanIndentNumber) {
        //商户id，        到手价格、
        BUserEntity bUserEntity = bUserMapper.updateBalance(userBId);
        BigDecimal balance = bUserEntity.getBalance();
        //给商户余额加钱
        bUserEntity.setBalance(balance.add(merchantsAmount));
        updateById(bUserEntity);

        //新增数据      新增数据
        AppUserAccountRecordEntity accountRecordEntity = new AppUserAccountRecordEntity();
        accountRecordEntity.setMomey(merchantsAmount);
        accountRecordEntity.setUserId(Integer.valueOf(userBId));
        EntityWrapper<CleanIndetEntity> indetWrapper = new EntityWrapper<>();
        indetWrapper.eq("clean_indent_number", cleanIndentNumber);
        CleanIndetEntity cleanIndetEntity = cleanIndetService.selectOne(indetWrapper);
        if (cleanIndetEntity.getResource().equals("1")) {
            accountRecordEntity.setType(11);
        } else if (cleanIndetEntity.getResource().equals("2")) {
            accountRecordEntity.setType(13);
        }
        accountRecordEntity.setCreateTime(new Date());
        accountRecordEntity.setAddFlag(1);
        accountRecordEntity.setSource(1);
        accountRecordEntity.setBusinessId(cleanIndentNumber);
        appUserAccountRecordMapper.insert(accountRecordEntity);

    }

    public StoreDisplayDto findstoreDisplay(String userBId) {
        return bUserMapper.findstoreDisplay(userBId);
    }

    public List<ListMessage> findRecommendFirstMer(String city, Integer pagesize) {
        //推荐的4s店列表
        //根据城市查询到4s店列表
        pagesize = (pagesize - 1) * 10;
        List<ListMessage> recommendMer = bUserMapper.findRecommendFirstMer(city, pagesize);
        return setDate(recommendMer, 2);

    }

    //查询喷漆商户列表
    public SparyMerchantsDto findSprayMerchants(Integer userBId, Integer cityCode, String score, Integer pagesize) {
        pagesize = (pagesize - 1) * 20;
        return bUserMapper.findSprayMerchants(userBId, cityCode, score, pagesize);
    }

    public String selectByUsername(String username) {
        return bUserMapper.selectByUsername(username);
    }

    public List<String> findBrand(String s) {
        return bUserMapper.findBrand(s);
    }


}
