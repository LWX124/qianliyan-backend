package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.b.constant.RedisConstant;
import com.cheji.b.modular.domain.*;
import com.cheji.b.modular.dto.*;
import com.cheji.b.modular.mapper.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-08-26
 */
@Service
public class UserService extends ServiceImpl<AppUserMapper, AppUserEntity> implements IService<AppUserEntity> {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource
    private HuanXinService huanXinService;

    @Resource
    private AppUserMapper appUserMapper;

    @Resource
    private MerchantsInfoBannerService merchantsInfoBannerService;

    @Resource
    private MerchantsBrandService merchantsBrandService;

    @Resource
    private CarBrandService carBrandService;

    @Resource
    private LableService lableService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IndentService indentService;

    @Resource
    private PushBillService pushBillService;

    @Resource
    private MerchantsCommentsTreeService merchantsCommentsTreeService;

    @Resource
    private LableDetailsReviewTreeService lableDetailsReviewTreeService;

    @Resource
    private InsuranceMerchantsService insuranceMerchantsService;

    @Resource
    private BizInsuranceCompanyService bizInsuranceCompanyService;

    @Resource
    private MerchantsServicerService merchantsServicerService;

    @Resource
    private AppUserBankMapper appUserBankMapper;

    @Resource
    private MerchantsCommentsTreeMapper merchantsCommentsTreeMapper;

    @Resource
    private AppUserBMessageService appUserBMessageService;

    @Resource
    private AppWxCashOutRecordEntityMapper appWxCashOutRecordEntityMapper;

    @Resource
    private AppUserAccountRecordMapper appUserAccountRecordMapper;

    @Resource
    private AppCleanIndetService appCleanIndetService;


    /**
     * 注册环信好友
     */
    @Transactional
    public void registerHuanxin(AppUserEntity appUserEntity) {
        Random random = new Random();
        String userName = appUserEntity.getId() + appUserEntity.getUsername() + ((int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000) + "b";
        String passWord = ((int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000) + "";
        String nikeName = "默认用户" + ((int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000) + "";
        logger.info("### 注册环信  userName ### userName={}", userName);
        huanXinService.singleRegister(userName, passWord, nikeName, false);

        appUserEntity.setHuanxinUserName(userName);
        appUserEntity.setHuanxinPassword(passWord);
        appUserMapper.updateById(appUserEntity);
    }

    /**
     * 增加提现记录
     *
     * @param amount 提现金额（已减去手续费）
     * @param userId 用户id
     */
    @Transactional
    public String addCashOut(BigDecimal amount, Integer userId, AppWxCashOutRecordEntity appWxCashOutRecordEntity) {
        //修改用户余额
        AppUserEntity appUserEntity = appUserMapper.selectUser(userId);//锁行  事务提交之后自动解锁

        if (appUserEntity.getBalance().compareTo(amount) == -1) {
            return "";//用户账户不够扣
        }
        appWxCashOutRecordEntity.setPartnerTradeNo("BBANK" + getOrderNo());

        //增加提现记录
        appWxCashOutRecordEntityMapper.insert(appWxCashOutRecordEntity);

        appUserEntity.setBalance(appUserEntity.getBalance().subtract(amount));
        appUserMapper.updateById(appUserEntity);

        AppUserAccountRecordEntity appUserAccountRecordEntity = new AppUserAccountRecordEntity();
        appUserAccountRecordEntity.setCreateTime(new Date());
        appUserAccountRecordEntity.setAddFlag(2);
        appUserAccountRecordEntity.setMomey(amount);
        appUserAccountRecordEntity.setSource(2);
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

    /**
     * 首页信息
     *
     * @param id
     * @return
     */
    public HomePageDto findHomePage(Integer id) {

        //查询技师id
        EntityWrapper<AppUserBMessageEntity> appUserBMessage = new EntityWrapper<>();
        appUserBMessage.eq("user_b_id",id)
                .eq("wrok_type",3);
        AppUserBMessageEntity bMessage = appUserBMessageService.selectOne(appUserBMessage);
        Integer bMessageid;
        if (bMessage==null){
             bMessageid = 0;
        }else {
             bMessageid = bMessage.getId();
        }
        //根据用户id查询到用户余额
        AppUserEntity appUserEntity = appUserMapper.selectById(id);
        //先查询到商户有效订单数量
        Integer indentCount = appUserMapper.findEarningsAndIndent(appUserEntity.getId());

        Integer cleanIndetnCount = appUserMapper.findTodayCleanIndent(id,bMessageid);

       // Integer sprayIndentCount = appUserMapper.findTodaySprayIndent(id);

        indentCount = indentCount + cleanIndetnCount ;

        //查询到今日收益
        BigDecimal todayEarning = appUserMapper.findTodayEarning(id);
        BigDecimal todayCleanEarning = appUserMapper.findTodayCleanEarning(id);
        BigDecimal todayRescueEarning = appUserMapper.findTodayRescueEarning(id);
        BigDecimal todaySprayPaintEarning = appUserMapper.findTodaySprayPaintEarning(bMessageid);
        if (todaySprayPaintEarning == null || todaySprayPaintEarning.equals(new BigDecimal(0))) {
            todaySprayPaintEarning = new BigDecimal(0);
        }
        if (todayEarning == null || todayEarning.equals(new BigDecimal(0))) {
            todayEarning = new BigDecimal(0);
        }
        if (todayCleanEarning == null || todayCleanEarning.equals(new BigDecimal(0))) {
            todayCleanEarning = new BigDecimal(0);
        }
        if (todayRescueEarning == null || todayRescueEarning.equals(new BigDecimal(0))) {
            todayRescueEarning = new BigDecimal(0);
        }
        todayEarning = todayEarning.add(todayCleanEarning);
        todayEarning = todayEarning.add(todayRescueEarning);
        todayEarning = todayEarning.add(todaySprayPaintEarning);
        BigDecimal balance = appUserEntity.getBalance();
        HomePageDto homePageDto = new HomePageDto();
        homePageDto.setIndentCount(indentCount);
        homePageDto.setMerchantsName(appUserEntity.getMerchantsName());
        homePageDto.setTodayEarnings(todayEarning);
        homePageDto.setWallet(balance);
        return homePageDto;
    }

    public EvaluationDto findEvaluation(Integer id) {
        //根据用户id查询到用户
        AppUserEntity resultAppUser = appUserMapper.selectById(id);
        String merchantsName = resultAppUser.getMerchantsName();
        BigDecimal score = resultAppUser.getScore();
        //查询评价总数和好评数量
        BigDecimal evaCount = merchantsCommentsTreeService.findEvaCount(id);
        BigDecimal praiseCount = merchantsCommentsTreeService.findhighPraiseCount(id);
        //算出好评百分比
        BigDecimal highPrase = BigDecimal.ZERO;
        BigDecimal subtract = BigDecimal.ZERO;
        if (evaCount.equals(BigDecimal.ZERO)) {
            evaCount = BigDecimal.ZERO;
        } else if (praiseCount.equals(BigDecimal.ZERO)) {
            praiseCount = BigDecimal.ZERO;
            subtract = BigDecimal.ONE;
        } else {
            highPrase = praiseCount.divide(evaCount, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal bigDecimal = new BigDecimal(BigInteger.ONE);
            //获取到差评百分比
            subtract = bigDecimal.subtract(highPrase);
        }

        //查询到回复总数，和中差评回复总数
        BigDecimal evaComment = merchantsCommentsTreeService.findevaComment(id);
        BigDecimal midBadComment = merchantsCommentsTreeService.findmidBadComment(id);
        BigDecimal commentResponse = BigDecimal.ZERO;
        BigDecimal poorEvaluationRes = BigDecimal.ZERO;
        if (evaComment.equals(BigDecimal.ZERO)) {
            evaComment = BigDecimal.ZERO;
        } else if (midBadComment.equals(BigDecimal.ZERO)) {
            commentResponse = evaComment.divide(evaCount, 2, BigDecimal.ROUND_HALF_UP);
        } else {
            //获取到评论回复率
            commentResponse = evaComment.divide(evaCount, 2, BigDecimal.ROUND_HALF_UP);
            //差评回复数量
            BigDecimal poorComments = evaCount.subtract(praiseCount);
            poorEvaluationRes = midBadComment.divide(poorComments, 2, BigDecimal.ROUND_HALF_UP);
        }
        //获取到中差评回复率
        EvaluationDto evaluationDto = merchantsCommentsTreeMapper.findallCount(id);
        if (evaluationDto == null) {
            evaluationDto = new EvaluationDto();
        }
        evaluationDto.setMerchantsName(merchantsName);
        evaluationDto.setScore(score);
        evaluationDto.setEvaluationCount(evaCount);
        evaluationDto.setHighPraise(highPrase);
        evaluationDto.setBadReview(subtract);
        evaluationDto.setEvaComment(commentResponse);
        evaluationDto.setMidBabComment(poorEvaluationRes);
        return evaluationDto;
    }

    //我的
    public MineDto findMine(Integer id) {
        //查询到头像和名字，
        //查询到今日访客，今日订单，全部订单，全部账单
        //查询到我的钱包，消费抵扣
        AppUserEntity appUserEntity = appUserMapper.selectById(id);
        //名字
        String name = appUserEntity.getName();

        String merchantsName = appUserEntity.getMerchantsName();
        //头像
        String avatar = appUserEntity.getAvatar();
        //钱包
        BigDecimal balance = appUserEntity.getBalance();
        //账号
        String username = appUserEntity.getUsername();
        //今日访客
        Long size = stringRedisTemplate.opsForZSet().size(RedisConstant.MERCHANTS_VISIT_COUNT + id);
        if (size==null){
            size = 0L;
        }
        //今日订单和全部订单和消费抵扣
        MineDto mineDto = indentService.findIndentCount(id);

        EntityWrapper<AppUserBMessageEntity> appUserBMessage = new EntityWrapper<>();
        appUserBMessage.eq("user_b_id",id)
                .eq("wrok_type",3);
        AppUserBMessageEntity bMessage = appUserBMessageService.selectOne(appUserBMessage);
        Integer bMessageid;
        if (bMessage==null){
            bMessageid = 0;
        }else {
            bMessageid = bMessage.getId();
        }
        //全部订单
        Integer cleanIndetnCount = appUserMapper.findTodayCleanIndent(id, bMessageid);
        mineDto.setAllIndent(mineDto.getAllIndent()+cleanIndetnCount);

        //查询今日清洗订单数
        Integer todayCleanIndent = appCleanIndetService.findTodayCleanIndent(id);
        mineDto.setTodayIndent(mineDto.getTodayIndent()+todayCleanIndent);

        BigDecimal decomne = indentService.findpayAmount(id);
        if (decomne == null) {
            decomne = BigDecimal.ZERO;
        }
        if (mineDto == null) {
            mineDto = new MineDto();
            mineDto.setAllIndent(0);
            mineDto.setTodayIndent(0);
        }
        mineDto.setConsumptionDeduction(decomne);
        //全部账单
        EntityWrapper<PushBillEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", id);
        List<PushBillEntity> list = pushBillService.selectList(wrapper);
        int size1 = list.size();

        mineDto.setAllbill(size1);
        mineDto.setName(name);
        mineDto.setAvatar(avatar);
        mineDto.setWallet(balance);
        mineDto.setVisiter(Math.toIntExact(size));
        mineDto.setUsername(username);
        mineDto.setMerchantsName(merchantsName);
        if(appUserEntity.getUpId() != null){
            mineDto.setPhoneOn(appUserEntity.getUpId());
        }

        return mineDto;
    }

    //服务页面
    public ServicDisplayDto getServiceDisplay(Integer userBId) {

        // 查询到图片list，商户名称，打分
        ServicDisplayDto displayDto = appUserMapper.getNameBrandScore(userBId);

        String type = displayDto.getType();
        if (StringUtils.isNotEmpty(type)) {
            if (1 == Integer.valueOf(type)) {
                displayDto.setType("4s店");
            } else if (2 == Integer.valueOf(type)) {
                displayDto.setType("修理厂");
            } else if (3 == Integer.valueOf(type)) {
                displayDto.setType("专修店");
            } else if (4 == Integer.valueOf(type)) {
                displayDto.setType("快修店");
            } else if (5 == Integer.valueOf(type)) {
                displayDto.setType("美容店");
            }
        }
        // 查询到banner图
        EntityWrapper<MerchantsInfoBannerEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_b_id", userBId)
                .orderBy("`index`");
        List<MerchantsInfoBannerEntity> bannerList = merchantsInfoBannerService.selectList(wrapper);

        //服务项目
        //根据id查询服务表中的数据
        EntityWrapper<LableDetailsReviewTreeEntity> lableWarrper = new EntityWrapper<>();
        lableWarrper.eq("user_b_id", userBId)
                .eq("`show`", 1)
                .eq("state", 1)
                .orderBy("`index`");
        List<LableDetailsReviewTreeEntity> list = lableDetailsReviewTreeService.selectList(lableWarrper);
        if (list.isEmpty()) {
            displayDto.setLableList(list);
        } else {
            for (LableDetailsReviewTreeEntity reviewTreeEntity : list) {
                Integer lableId = reviewTreeEntity.getLableId();
                LableEntity lableEntity = lableService.selectById(lableId);
                String url = lableEntity.getUrl();
                reviewTreeEntity.setUrl(url);
            }
            displayDto.setLableList(list);
        }

        EntityWrapper<MerchantsServicerEntity> merchantsServicerWrapper = new EntityWrapper<>();
        merchantsServicerWrapper.eq("user_b_id", userBId);
        List<MerchantsServicerEntity> merchantsServicerEntities = merchantsServicerService.selectList(merchantsServicerWrapper);
        displayDto.setServicerList(merchantsServicerEntities);

        displayDto.setBannerList(bannerList);
        return displayDto;
    }

    public StoresDto findStroeMessage(Integer userBId) {
        //查询到商户信息
        StoresDto storesDto = appUserMapper.findStore(userBId);
 /*       private List<CarBrandEntity> carBrandList;  //品牌列表
        private List<LableDetailsReviewTreeEntity> lableList;   //门店服务
        private List<InsuranceMerchantsEntity> insuranceList;   //合作保险
        private List<MerchantsServicerEntity> servicerList;     //服务顾问
        private String onlineCustomers;     //在线客户*/
        //查询品牌列表
        EntityWrapper<MerchantsBrandEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_b_id", userBId);
        List<MerchantsBrandEntity> merchantsBrandlist = merchantsBrandService.selectList(wrapper);
        List<CarBrandEntity> carBrandList = new ArrayList<>();
        if (merchantsBrandlist == null || merchantsBrandlist.size() == 0) {
            storesDto.setCarBrandList(carBrandList);
        } else {
            for (MerchantsBrandEntity brandEntity : merchantsBrandlist) {
                //修改为1的时候，不管有其他的都是只展示第一个
//                if (storesDto.getMerchantsType().equals("1")) {
//                    CarBrandEntity carBrandEntity = carBrandService.selectById(brandEntity.getBrandId());
//                    carBrandList.add(carBrandEntity);
//                    break;
//                }
                String brandId = brandEntity.getBrandId();
                CarBrandEntity carBrandEntity = carBrandService.selectById(brandId);
                carBrandList.add(carBrandEntity);
            }
            storesDto.setCarBrandList(carBrandList);
        }

        //查询所有图片
        EntityWrapper<MerchantsInfoBannerEntity> infoBannerWrapper = new EntityWrapper<>();
        infoBannerWrapper.eq("user_b_id", userBId);
        List<MerchantsInfoBannerEntity> bannerEntityList = merchantsInfoBannerService.selectList(infoBannerWrapper);
        ArrayList<MerchantsInfoBannerEntity> merchantsInfoBanner = new ArrayList<>(10);
        if (bannerEntityList.size() < 7) {
            flag:
            for (int i = 1; i < 8; i++) {              //  1234567
                for (MerchantsInfoBannerEntity infoBannerEntity : bannerEntityList) {  //13
                    Integer index = infoBannerEntity.getIndex();
                    if (index == i) {
                        merchantsInfoBanner.add(infoBannerEntity);
                        continue flag;
                    }
                }
                MerchantsInfoBannerEntity merchantsInfoBannerEntity = new MerchantsInfoBannerEntity();
                merchantsInfoBannerEntity.setIndex(i);
                merchantsInfoBanner.add(merchantsInfoBannerEntity);
                if (merchantsInfoBanner.size() == 7) {
                    break;
                }
            }
            storesDto.setMasterImg(merchantsInfoBanner);
        } else {
            storesDto.setMasterImg(bannerEntityList);
        }


        //查询到门店服务
        //通过商户id查询到门店服务
        EntityWrapper<LableDetailsReviewTreeEntity> Lablewrapper = new EntityWrapper<>();
        Lablewrapper.eq("user_b_id", userBId)
                .eq("`show`", 1)
                .eq("state", 1);
        List<LableDetailsReviewTreeEntity> list = lableDetailsReviewTreeService.selectList(Lablewrapper);
        storesDto.setLableList(list);

        //查询到所有的合作保险
        EntityWrapper<BizInsuranceCompanyEntity> bizInsuranceWrapper = new EntityWrapper<>();
        List<BizInsuranceCompanyEntity> bizInsuranceCompanyList = bizInsuranceCompanyService.selectList(bizInsuranceWrapper);
        //合作保险查询到所有合作保险给已选择的合作保险修改状态
        EntityWrapper<InsuranceMerchantsEntity> insurancewrapper = new EntityWrapper<>();
        insurancewrapper.eq("user_b_id", userBId)
                .eq("state", 1);
        List<InsuranceMerchantsEntity> insuranceMerchantslist = insuranceMerchantsService.selectList(insurancewrapper);
        //先遍历少的
        for (InsuranceMerchantsEntity merchantsEntity : insuranceMerchantslist) {
            for (BizInsuranceCompanyEntity insuranceCompanyEntity : bizInsuranceCompanyList) {
                if (merchantsEntity.getInsuranceId() == insuranceCompanyEntity.getId()) {
                    insuranceCompanyEntity.setState("1");
                }
            }
        }
        storesDto.setInsuranceList(bizInsuranceCompanyList);

        //服务顾问
        //根据商户id查询到对应得服务顾问
        EntityWrapper<MerchantsServicerEntity> servicerWrapper = new EntityWrapper<>();
        servicerWrapper.eq("user_b_id", userBId);
        List<MerchantsServicerEntity> servicerList = merchantsServicerService.selectList(servicerWrapper);
        storesDto.setServicerList(servicerList);

        return storesDto;
    }

    public List<AppUserBankEntity> findBankCard(Integer userBId) {
        List<AppUserBankEntity> bankCard = appUserBankMapper.findBankCard(userBId);
        for (AppUserBankEntity appUserBankEntity : bankCard) {
            String code = appUserBankEntity.getCode();
            int n = 4;
            String substring = code.substring(code.length() - n);
            appUserBankEntity.setCode(substring);
        }
        return bankCard;
    }

    public void removeBankCard(Integer id) {
        AppUserBankEntity appUserBankEntity = appUserBankMapper.selectById(id);
        appUserBankEntity.setStatus(3);
        appUserBankMapper.updateById(appUserBankEntity);
    }

    public List<ChangeListDto> findChangeList(Integer userBId, Integer pagesize, String date) {
        pagesize = (pagesize - 1) * 20;
        //通过用户id查询到零钱账单的提现和充值信息
        if (StringUtils.isEmpty(date)) {
            List<ChangeListDto> changeList = appUserMapper.findChangeList(userBId, pagesize);
            if (changeList.isEmpty()) {
                return null;
            }
            //查询到收入总数和支出总数
            return workData(changeList, userBId, pagesize);
        } else {
            //查询月份数据的条件
            List<ChangeListDto> changeList = appUserMapper.findChangeListAndMonth(userBId, pagesize, date);
            if (changeList.isEmpty()) {
                return null;
            }
            return changeList;
        }
    }

    //从充值订单中查询到详情
    public ChangeDetailsDto findChangeDetails(String changeId) {
        ChangeDetailsDto changeDetails = appWxCashOutRecordEntityMapper.findChangeDetails(changeId);

        //银行卡号后四位
        String bankCode = changeDetails.getBankTail();
        int n = 4;
        String substring = bankCode.substring(bankCode.length() - n);
        changeDetails.setBankTail(substring);

        String resultInfo = changeDetails.getResultInfo();
        ArrayList<ProgressDto> progressDtos = new ArrayList<>();
        ProgressDto progress1 = new ProgressDto();
        progress1.setWorkName("发起提现");
        progress1.setTime(changeDetails.getCreateTime());
        progressDtos.add(progress1);

        ProgressDto progress2 = new ProgressDto();
        progress2.setTime(changeDetails.getCreateTime());
        progress2.setWorkName("银行处理中");
        progressDtos.add(progress2);

        ProgressDto progress3 = new ProgressDto();
        if (resultInfo.equals("成功") || resultInfo.equals("提现成功")) {
            progress3.setWorkName("提现成功");
            progress3.setTime(changeDetails.getSuccessTime());
            progressDtos.add(progress3);
        } else if (resultInfo.length() > 6) {
            progress3.setWorkName("提现失败");
            progressDtos.add(progress3);
        } else {
            progress3.setWorkName("处理中");
            progressDtos.add(progress3);
        }
        changeDetails.setListProgress(progressDtos);
        return changeDetails;
    }


    private List<ChangeListDto> workData(List<ChangeListDto> changeList, Integer userBId, Integer pagesize) {
        for (int i = 0; i < changeList.size(); i++) {
            ChangeListDto changeListDto = changeList.get(i);
            String operationMonth = changeListDto.getOperationMonth();
            BigDecimal income = appUserMapper.findIncome(operationMonth, userBId);
            BigDecimal cleanIncome = appUserMapper.findCleanIncome(operationMonth, userBId);
           // BigDecimal rescueIncome = appUserMapper.findRescueIncome(operationMonth,userBId);
            if (income == null) {
                income = new BigDecimal(0);
            }
            if (cleanIncome == null) {
                cleanIncome = new BigDecimal(0);
            }
            income = income.add(cleanIncome);
            income = income.setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal spend = appUserMapper.findspennd(operationMonth, userBId);
            if (spend==null){
                spend = new BigDecimal(0);
            }
            spend = spend.setScale(2,BigDecimal.ROUND_HALF_UP);
            changeListDto.setIncome(income);
            changeListDto.setSpend(spend);
            //拿到下一页第一条数据
            if (i == changeList.size() - 1) {
                ChangeListDto finalMeg = changeList.get(i);
                String finalMonth = finalMeg.getOperationMonth();
                pagesize = pagesize + 20;
                List<ChangeListDto> nextChangelist = appUserMapper.findChangeList(userBId, pagesize);
                if (nextChangelist.isEmpty()) {
                    finalMeg.setFinalMeg("1");
                } else {
                    ChangeListDto firstMeg = nextChangelist.get(0);
                    String firstMonth = firstMeg.getOperationMonth();
                    if (!finalMonth.equals(firstMonth)) {
                        finalMeg.setFinalMeg("1");
                    }
                }
            }
            //每个月最后一条数据添加标识
            if (i + 1 == changeList.size()) {
                break;
            }
            ChangeListDto nextChangeDto = changeList.get(i + 1);
            String nextChangeDtoMonth = nextChangeDto.getOperationMonth();
            if (!operationMonth.equals(nextChangeDtoMonth)) {
                changeListDto.setFinalMeg("1");
            }
        }
        return changeList;
    }

    public String findAllMessage(Integer userBId) {
        //查询名称，品牌，地址，服务项目
        AppUserEntity appUserEntity = selectById(userBId);
        //名称
        String merchantsName = appUserEntity.getMerchantsName();
        //地址
        String address = appUserEntity.getAddress();
        //品牌
        EntityWrapper<MerchantsBrandEntity> BrandWrapper = new EntityWrapper<>();
        BrandWrapper.eq("user_b_id", userBId);
        List<MerchantsBrandEntity> merchantsBrandEntities = merchantsBrandService.selectList(BrandWrapper);
        //服务项目
        EntityWrapper<LableDetailsReviewTreeEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_b_id", userBId)
                .eq("`show`", 1);
        List<LableDetailsReviewTreeEntity> list = lableDetailsReviewTreeService.selectList(wrapper);

        //查询门店主图
        EntityWrapper<MerchantsInfoBannerEntity> bannerWrapper = new EntityWrapper<>();
        bannerWrapper.eq("user_b_id", userBId)
                .eq("`index`", 1);
        List<MerchantsInfoBannerEntity> bannerEntityList = merchantsInfoBannerService.selectList(bannerWrapper);

        if (StringUtils.isEmpty(merchantsName) || StringUtils.isEmpty(address) || merchantsBrandEntities.isEmpty()) {
            return "请先完善商户基本信息";
        } else if (list.isEmpty()) {
            return "请先添加服务项目";
        } else if (bannerEntityList.isEmpty()) {
            return "请先添加门店主图";
        } else {
            return "请耐心等待审核";
        }

    }

    public List<String> findBrand(Integer userBId) {
        return appUserMapper.findBrand(userBId);
    }

    public String findUpName(String member) {
        return appUserMapper.findUpName(member);
    }

    public AppUserEntity selectByIdupMerchats(String member) {
        return appUserMapper.selectByIdupMerchats(member);
    }

    public void updateUpMerMess(String userBId) {
        appUserMapper.updateUpMerMess(userBId);
    }

    public AppUserEntity selectWxuser(String wxuserId) {
        return appUserMapper.selectWxuser(wxuserId);

    }

//    public String huannxinUsername(String member) {
//        return appUserMapper.huannxinUsernaem(member);
//    }
//
//    public String huanxinPassward(String member) {
//        return appUserMapper.huanxinPassward(member);
//    }
//
//    public Integer unreadMess(String member) {
//        return appUserMapper.unreadMess(member);
//    }
}
