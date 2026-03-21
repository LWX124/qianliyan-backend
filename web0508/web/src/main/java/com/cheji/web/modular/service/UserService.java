package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.cwork.*;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.mapper.*;
import com.cheji.web.util.PasswordUtil;
import com.cheji.web.util.ShareCodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-08-26
 */
@Service
public class UserService extends ServiceImpl<UserMapper, UserEntity> implements IService<UserEntity> {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private IndentService indentService;

    @Resource
    private PayAmountRecordService payAmountRecordService;

    @Resource
    private UserBankService userBankService;

    @Resource
    private AppWxCashOutRecordEntityMapper appWxCashOutRecordEntityMapper;

    @Resource
    private AppUserMapper appUserMapper;

    @Resource
    private AppUserPlusMapper appUserPlusMapper;

    @Resource
    private AppUserAccountRecordMapper appUserAccountRecordMapper;

    @Resource
    private AppChangeAccountRecordMapper appChangeAccountRecordMapper;

    @Resource
    private AppUserBankMapper appUserBankMapper;

    @Resource
    private HuanXinService huanXinService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AppWxBankMapper appWxBankMapper;

    @Resource
    private AppUserPlusService appUserPlusService;

    @Resource
    private CleanIndetService cleanIndetService;

    @Resource
    private AppSprayPaintIndentService appSprayPaintIndentService;

    @Resource
    private AppYearCheckIndentService appYearCheckIndentService;

    @Resource
    private AppSubstituteDrivingIndentService appSubstituteDrivingIndentService;

    @Resource
    private AppUserCouponService appUserCouponService;

    @Resource
    private AppClaimTeacherService appClaimTeacherService;

    /**
     * 增加提现记录
     *
     * @param amount 提现金额
     * @param userId 用户id
     */
    @Transactional
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


    //获取到用户信息
    public IndentAndMerchants userMerchants(String userId) {
        //获取到团队人数
        Integer teamCount = userMapper.findTeamCount(userId);
        //通过id查询到用户

        IndentAndMerchants indentAndMerchants = userMapper.userMerchantsById(userId);
        List<MyTeam> myTeamListById = userMapper.findMyTeamListById(userId);
        //根据团队列表来算出具体标签
        int i = myTeamListById.size();

        //得到团队人数
        //级别：初遇车己1-300、
        // 理赔顾问1-800、
        // 理赔经理1-1500、
        // 区域经理1-2200、
        // 城市经理1-3000
        if (i <= 300) {
            indentAndMerchants.setLevelName("初遇车己");
        } else if (i <= 800) {
            indentAndMerchants.setLevelName("理赔顾问");
        } else if (i <= 1500) {
            indentAndMerchants.setLevelName("理赔经理");
        } else if (i <= 2200) {
            indentAndMerchants.setLevelName("区域经理");
        } else {
            indentAndMerchants.setLevelName("城市经理");
        }
        indentAndMerchants.setCounts(teamCount);
        return indentAndMerchants;
    }


    //查询用户对应的粉丝数
    public List<UserEntity> findFansByUserid(Integer userid, Integer pagesize) {
        pagesize = (pagesize - 1) * 20;
        return userMapper.findFansByUserid(userid, pagesize);
    }

    public JSONObject update(UserEntity userEntity) {
        JSONObject result = new JSONObject();
        //Newpassword新密码  password旧密码
        //如果先判断旧密码是否一致 如果新密码不为空
        System.out.println(userEntity);
        if (StringUtils.isNotEmpty(userEntity.getPassword())) {
            //根据id获取到密码
            UserEntity user = selectById(userEntity.getId());
            String salt = user.getSalt();
            //判断旧密码是否一致
            String password = userEntity.getPassword();
            if (PasswordUtil.isPasswordValid(user.getPassword(), password, salt)) {
                //如果一致就修改密码
                //新密码设置为密码
                String encode = PasswordUtil.encode(userEntity.getNewPassword(), salt);
                userEntity.setPassword(encode);
                updateById(userEntity);
                result.put("code", 200);
                result.put("msg", "修改密码成功");
                dissOldUser(userEntity.getId());
                return result;
            } else {
                result.put("code", 430);
                result.put("msg", "旧密码输入有误");
                return result;
            }
        }
        //没有密码就直接保存
        if (userEntity.getName()!=null){
            AppClaimTeacherEntity appClaimTeacherEntity = appClaimTeacherService.selectById(userEntity.getId());
            if (appClaimTeacherEntity != null) {
                result.put("code", 409);
                result.put("msg", "理赔业务修改名称请联系管理员");
                return result;
            }
        }

        updateById(userEntity);
        result.put("code", 200);
        result.put("msg", "修改成功");
        return result;
    }

    /**
     * 把之前的用户挤下线
     */
    private void dissOldUser(long userId) {
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.USER_ID_TOKEN + userId);
        if (StringUtils.isNotEmpty(s)) {
            stringRedisTemplate.delete(RedisConstant.USER_TOKEN + s);
        }
    }

    public UserWallet findWalletById(String id) {
        //根据用户id查询到余额
        UserEntity userEntity = selectById(id);
        BigDecimal balance = userEntity.getBalance();
        UserWallet userWallet = new UserWallet();
        //给余额赋值
        userWallet.setBalance(balance);

        //根据用户id查询到收入
        //先根据id查询到订单收入
        EntityWrapper<IndentEntity> indentWrapper = new EntityWrapper<>();
        indentWrapper.eq("user_id", id);
        List<IndentEntity> indents = indentService.selectList(indentWrapper);
        BigDecimal indentIncome = BigDecimal.ZERO;
        for (IndentEntity indent : indents) {
            BigDecimal fixlossUser = indent.getFixlossUser();
            if (fixlossUser == null) {
                break;
            } else {
                indentIncome = indentIncome.add(fixlossUser);
            }
        }
        //根据id查询到红包收入
        EntityWrapper<PayAmountRecordEntity> warpper = new EntityWrapper<>();
        warpper.eq("user_id", id);
        List<PayAmountRecordEntity> payAmountRecordEntities = payAmountRecordService.selectList(warpper);
        //遍历拿到每一次红包推送得数据
        BigDecimal payAmountRecordsIncome = BigDecimal.ZERO;
        for (PayAmountRecordEntity payAmountRecordEntity : payAmountRecordEntities) {
            BigDecimal payAmount = payAmountRecordEntity.getPayAmount();
            payAmountRecordsIncome = payAmountRecordsIncome.add(payAmount);
        }
        //收入就等于订单收入加上红包收入
        // if (indentIncome.toString().equals("0.00"))
        userWallet.setIncome(indentIncome.add(payAmountRecordsIncome));

        //根据用户id查询到对应得银行卡和用户电话尾号
        List<BankCard> bankCards = userBankService.findBankAndTail(id);
        for (BankCard bankCard : bankCards) {
            String tail = bankCard.getTail();
            int n = 4;
            String substring = tail.substring(tail.length() - n);
            bankCard.setTail(substring);
        }
        userWallet.setBanks(bankCards);
        return userWallet;
    }


    //根据id查询到团队列表
    public MyTeam findTeamListById(String id) {
        MyTeam myTeam1 = new MyTeam();

        //查询到用户
        UserEntity userEntity = selectById(id);
        //获取到id，头像和名称
        Long id1 = userEntity.getId();
        String avatar = userEntity.getAvatar();
        String name = userEntity.getName();

        myTeam1.setId(id);
        myTeam1.setAvatar(avatar);
        myTeam1.setName(name);
        //获取到团队列表
        List<MyTeam> myTeamList = userMapper.findMyTeamListById(id);
        myTeam1.setMyTeams(myTeamList);
        //根据团队列表来算出具体标签
        int i = myTeamList.size();
        //得到团队人数
        myTeam1.setTeamCount(i);
        //级别：初遇车己1-300、
        // 理赔顾问1-800、
        // 理赔经理1-1500、
        // 区域经理1-2200、
        // 城市经理1-3000
        if (i <= 300) {
            myTeam1.setLabel("初遇车己");
        } else if (i <= 800) {
            myTeam1.setLabel("理赔顾问");
        } else if (i <= 1500) {
            myTeam1.setLabel("理赔经理");
        } else if (i <= 2200) {
            myTeam1.setLabel("区域经理");
        } else {
            myTeam1.setLabel("城市经理");
        }
        return myTeam1;
    }

    //根据用户id拿到个人数据
    public Personal findPersonalByUserId(String userId) {
        //拿到用户名字，理赔等级，头像
        UserEntity user = selectById(userId);
        Long id = user.getId();
        String name = user.getName();
        String avatar = user.getAvatar();
        String phoneNumber = user.getPhoneNumber();

        Personal r = cleanIndetService.findIndentCount(userId);
        //Personal ri = appRescueIndentService.findIndentCount(userId);
        Personal spIndent = appSprayPaintIndentService.findIndentCount(userId);
        Personal yckIndent = appYearCheckIndentService.findIndentCount(userId);
        Personal subIndent = appSubstituteDrivingIndentService.findIndentCount(userId);


        //查询到各个状态的订单数量
        Personal person = indentService.findIndentCount(userId);
        person.setUserid(String.valueOf(id));
        person.setUserName(name);
        person.setAvatar(avatar);
        person.setPhoneNumber(phoneNumber);

        //查询优惠卷
        Integer counpon = appUserCouponService.findNoUseCoupon(userId);
        person.setCoupons(counpon);

        //待收货和服务中使用一样的参数
        person.setWaitReceiving(r.getInServiceing() + spIndent.getInServiceing() + yckIndent.getInServiceing() + subIndent.getWaitReceiving());
        person.setInServiceing(r.getInServiceing() + spIndent.getInServiceing() + yckIndent.getInServiceing() + subIndent.getInServiceing());
        person.setToEvaluate(r.getToEvaluate() + spIndent.getToEvaluate() + subIndent.getToEvaluate());
        //拿到订单表销售收入数据
        String fixlossUser = indentService.findFixlossUserByuserId(userId);
        person.setSalesRevenue(fixlossUser);
        //查询到余额
        String balance = userMapper.findWallet(userId);
        person.setWallet(balance);

        List<MyTeam> myTeamListById = userMapper.findMyTeamListById(userId);
        //根据团队列表来算出具体标签
        int i = myTeamListById.size();

        //得到团队人数
        if (i <= 10) {
            person.setLevelName("热心群众");
        } else if (i <= 50) {
            person.setLevelName("初遇车己");
        } else {
            person.setLevelName("车己达人");
        }

        //判断是否开通了plus会员
        AppUserPlusEntity plusUserByid = appUserPlusService.findPlusUserByid(userId);
        Date date = new Date();
        if (plusUserByid == null) {
            //没开通
            person.setPlusMember("1");
            person.setInviteCode("请先成为plus会员");
        } else if (plusUserByid.getInvalidTimeEnd().before(date)) {
            //过期
            person.setPlusMember("2");
            person.setInviteCode(plusUserByid.getInviteCode());
        } else {
            //已开通且未过期
            person.setPlusMember("3");
            person.setInviteCode(plusUserByid.getInviteCode());
        }

        //是否是商户
        EntityWrapper<AppClaimTeacherEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", id);
        AppClaimTeacherEntity appClaimTeacher = appClaimTeacherService.selectOne(wrapper);
        if (appClaimTeacher == null) {
            person.setIsMerchants(0);
        } else {
            if (appClaimTeacher.getType() == 1 || appClaimTeacher.getType() == 4) {
                person.setIsMerchants(0);
            } else {
                person.setIsMerchants(1);
            }
        }

        return person;
    }

    public List<Map> findBankList(Integer id) {
//        EntityWrapper<AppUserBankEntity> wrapper = new EntityWrapper<>();
//        wrapper.eq("user_id", id);
        return appUserBankMapper.selectListById(id);
    }

    /**
     * 注册环信好友
     */
    @Transactional
    public void registerHuanxin(AppUserEntity appUserEntity) {
        Random random = new Random();
        String userName = appUserEntity.getId() + appUserEntity.getUsername() + ((int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000) + "";
        String passWord = ((int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000) + "";
        String nikeName = "默认用户" + ((int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000) + "";
        huanXinService.singleRegister(userName, passWord, nikeName, false);

        appUserEntity.setHuanxinUserName(userName);
        appUserEntity.setHuanxinPassword(passWord);
        appUserMapper.updateById(appUserEntity);

        //------免费开通plus会员
        //判断免费开关是否打开
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.PLUS_MEMBER_FREE_FLAG);
        if ("true".equals(s)) {
            logger.info("###  免费开通plus会员 开关已打开 ");
            //计算一年后的今天
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, 1);
            Date time = calendar.getTime();

            AppUserPlusEntity appUserPlus = new AppUserPlusEntity();
            appUserPlus.setCreateTime(new Date());
            appUserPlus.setUserId(appUserEntity.getId());
            appUserPlus.setInvalidTimeStart(new Date());
            appUserPlus.setInvalidTimeEnd(time);//设置过期时间
            appUserPlus.setInviteCode(ShareCodeUtil.toSerialCode(appUserEntity.getId()));

            appUserPlusMapper.insert(appUserPlus);
        } else {
            logger.info("###  免费开通plus会员 开关已关闭 ");
        }
    }

    public static void main(String[] args) {
        Random random = new Random();
        System.out.println((int) (random.nextDouble() * 6));
    }

/*
    private static void ListSort(List<ChangeList> list) {
        Collections.sort(list, new Comparator<ChangeList>() {
            @Override
            public int compare(ChangeList o1, ChangeList o2) {
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
    }*/

    //根据提现id查询到提现详情
    public WithdDetails findWithdrawalDetails(String partnerTradeNo) {
        WithdDetails detailsByid = appWxCashOutRecordEntityMapper.findDetailsByPartnerTradeNo(partnerTradeNo);
        //获取到银行名称
        String bankName = detailsByid.getBankName();
        String iconUrl1 = detailsByid.getIconUrl();

        //获取到银行卡号后四位
        String bankTail = detailsByid.getBankTail();
        //截取后四位
        String substring = bankTail.substring(bankTail.length() - 4);
        detailsByid.setBankTail(substring);

        EntityWrapper<AppWxBankEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("bank_code", bankName);

        List<AppWxBankEntity> appWxBankEntities = appWxBankMapper.selectList(wrapper);
        for (AppWxBankEntity appWxBankEntity : appWxBankEntities) {
            bankName = appWxBankEntity.getBankName();
            iconUrl1 = appWxBankEntity.getIconUrl();

        }
        detailsByid.setBankName(bankName);
        detailsByid.setIconUrl(iconUrl1);

        //获取到进度
        AppWxCashOutRecordEntity appWxCashOutRecordEntityParamer = new AppWxCashOutRecordEntity();
        appWxCashOutRecordEntityParamer.setPartnerTradeNo(partnerTradeNo);
        AppWxCashOutRecordEntity appWx = appWxCashOutRecordEntityMapper.selectOne(appWxCashOutRecordEntityParamer);
        String result = appWx.getResultInfo();

        List<Progress> progresses = new ArrayList<>();
        Progress progress1 = new Progress();
        progress1.setWorkName("发起提现");
        progress1.setTime(detailsByid.getCreateTime());
        progresses.add(progress1);
        Progress progress2 = new Progress();
        progress2.setWorkName("银行处理中");
        progress2.setTime(detailsByid.getCreateTime());
        progresses.add(progress2);

      /*  if (result.equals("FAIL") || result.equals("付款失败")) {
            Progress progres3 = new Progress();
            progres3.setWorkName("提现失败");
            progres3.setTime(detailsByid.getSuccessTime());
            progresses.add(progres3);
            detailsByid.setProgresses(progresses);
        } */
        if (result.equals("成功") || result.equals("提现成功")) {
            Progress progres3 = new Progress();
            progres3.setWorkName("提现成功");
            progres3.setTime(detailsByid.getSuccessTime());
            progresses.add(progres3);
            detailsByid.setProgresses(progresses);
        } else if (result.length() > 6) {
            Progress progres3 = new Progress();
            progres3.setWorkName("提现失败");
            progres3.setTime(detailsByid.getSuccessTime());
            progresses.add(progres3);
            detailsByid.setProgresses(progresses);
        } else {
            Progress progres3 = new Progress();
            progres3.setWorkName("提现中");
            progresses.add(progres3);
            detailsByid.setProgresses(progresses);
        }

        /*else {
            Progress progres3 = new Progress();
            progres3.setWorkName("到账");
            progresses.add(progres3);
            detailsByid.setProgresses(progresses);
        }*/

        return detailsByid;
    }


    /**
     * 换账号
     *
     * @param id
     */
    @Transactional
    public void convertPhone(Integer id, String phone) {
        AppUserEntity appUserEntity = appUserMapper.selectById(id);
        String oldPhone = appUserEntity.getUsername();

        appUserEntity.setUsername(phone);
        appUserEntity.setPhoneNumber(phone);
        appUserMapper.updateById(appUserEntity);

        AppChangeAccountRecord appChangeAccountRecord = new AppChangeAccountRecord();
        appChangeAccountRecord.setCreateTime(new Date());
        appChangeAccountRecord.setUserId(id);
        appChangeAccountRecord.setNewAccount(phone);
        appChangeAccountRecord.setOldAccount(oldPhone);

        appChangeAccountRecordMapper.insert(appChangeAccountRecord);

    }

    public List<AmountList> findChangeList(Integer id, Integer pagesize, String date) {
        pagesize = (pagesize - 1) * 20;
        //判断时间
        if (StringUtils.isEmpty(date)) {
            List<AmountList> changeList = appUserMapper.findChangeList(id, pagesize);
            //获取到每月的最后一条数据和收入总额和支出
            if (changeList.isEmpty()) {
                return null;
            }
            return workData(changeList, id, pagesize);
        } else {
            List<AmountList> changeListAndDate = appUserMapper.findChangeListAndDate(id, pagesize, date);
            if (changeListAndDate.isEmpty()) {
                return null;
            }
            return changeListAndDate;
        }
    }


    private List<AmountList> workData(List<AmountList> changeList, Integer id, Integer pagesize) {
        if (changeList.size() == 20) {
            AmountList amountList = changeList.get(changeList.size() - 1);
            String finalMonth = amountList.getOperationMonth();
            //获取到下个月第一条数据
            pagesize = pagesize + 20;
            List<AmountList> nextChangeList = appUserMapper.findChangeList(id, pagesize);
            if (!nextChangeList.isEmpty()) {
                AmountList amountList1 = nextChangeList.get(0);
                String operationMonth = amountList1.getOperationMonth();
                if (!operationMonth.equals(finalMonth)) {
                    amountList.setFinalMeg("1");
                }
            }
        }
        for (int i = 0; i < changeList.size(); i++) {
            //获取到最后一条数据

            AmountList amountList = changeList.get(i);
            BigDecimal income = appUserMapper.findIncome(amountList.getOperationMonth(), id);
            //BigDecimal spend = appUserMapper.findSpend(amountList.getOperationMonth(), id);
            amountList.setIncome(income);
            amountList.setSpend(new BigDecimal("0"));
            //为1就是支出
           /* if (amountList.getType().equals("1")) {
                String amount = amountList.getAmount();
                BigDecimal bd = new BigDecimal(amount);
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                spend = spend.add(bd);
            } else {
                String amount = amountList.getAmount();
                BigDecimal bd = new BigDecimal(amount);
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                income = income.add(bd);
            }*/
            //找到最后一个月
            //每个月最后一条数据添加标识
            if (i + 1 == changeList.size()) {
                break;
            }
            AmountList nextAmount = changeList.get(i + 1);
            String nextMonth = nextAmount.getOperationMonth();
            if (nextMonth == null) {
                amountList.setFinalMeg("1");
            } else {
                String operationMonth = amountList.getOperationMonth();
                if (!nextMonth.equals(operationMonth)) {
                    amountList.setFinalMeg("1");
                }
            }
        }
        return changeList;
    }

    public void removeCard(Integer id) {
        AppUserBankEntity appUserBankEntity = appUserBankMapper.selectById(id);
        appUserBankEntity.setStatus(3);
        appUserBankMapper.updateById(appUserBankEntity);
    }

    public UserEntity selectWxuser(String wxuserId) {
        return appUserMapper.selectWxuser(wxuserId);
    }

    public List<Integer> selectId() {
        return appUserMapper.selectId();
    }

    public AppUserPlusEntity addAmountToParent(Integer parentId) {
        return appUserPlusService.findPlusUserByid(String.valueOf(parentId));
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateBalance(Integer parentId, String cleanIndentNumber, BigDecimal amount) {
        //修改plus会员得金额
        AppUserEntity appUserEntity = findUserById(parentId);
        BigDecimal balance = appUserEntity.getBalance();
        //总价得百分之五
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.CAR_WASH_MEMBERSHIP_ROYALTY_RATES);
        BigDecimal plusAmount = amount.multiply(new BigDecimal(s));
        appUserEntity.setBalance(balance.add(plusAmount));
        appUserMapper.updateById(appUserEntity);

        //添加数据
        AppUserAccountRecordEntity accountRecordEntity = new AppUserAccountRecordEntity();
        accountRecordEntity.setMomey(plusAmount);
        accountRecordEntity.setUserId(parentId);
        EntityWrapper<CleanIndetEntity> indetWrapper = new EntityWrapper<>();
        indetWrapper.eq("clean_indent_number", cleanIndentNumber);
        CleanIndetEntity cleanIndetEntity = cleanIndetService.selectOne(indetWrapper);
        if (cleanIndetEntity.getResource().equals("1")) {
            accountRecordEntity.setType(12);
        } else if (cleanIndetEntity.getResource().equals("2")) {
            accountRecordEntity.setType(14);
        }
        accountRecordEntity.setCreateTime(new Date());
        accountRecordEntity.setAddFlag(1);
        accountRecordEntity.setSource(1);
        accountRecordEntity.setBusinessId(cleanIndentNumber);
        appUserAccountRecordMapper.insert(accountRecordEntity);


    }

    private AppUserEntity findUserById(Integer parentId) {
        return appUserMapper.findUserById(parentId);
    }

    public List<String> findFans(Integer id) {
        return appUserMapper.findFans(id);
    }

    public List<FansVideoDto> findVideo(List<String> users, Integer pagesize, String beginTime, String endTime) {
        pagesize = (pagesize - 1) * 20;
        List<FansVideoDto> video = appUserMapper.findVideo(users, pagesize, beginTime, endTime);
        //下一页有数据的情况
        if (video.size() == 20) {
            FansVideoDto amountList = video.get(video.size() - 1);
            String finalMonth = amountList.getCreateTime();
            //获取到下个月第一条数据
            pagesize = pagesize + 20;
            List<FansVideoDto> nextChangeList = appUserMapper.findVideo(users, pagesize, beginTime, endTime);
            if (!nextChangeList.isEmpty()) {
                FansVideoDto amountList1 = nextChangeList.get(0);
                String operationMonth = amountList1.getCreateTime();
                if (!operationMonth.equals(finalMonth)) {
                    amountList.setFinalMeg("1");
                }
            }
        }
        for (int i = 0; i < video.size(); i++) {
            if (i + 1 == video.size()) {
                break;
            }
            FansVideoDto amountList = video.get(i);
            FansVideoDto nextAmount = video.get(i + 1);
            String nextMonth = nextAmount.getCreateTime();
            String operationMonth = amountList.getCreateTime();
            if (!nextMonth.equals(operationMonth)) {
                amountList.setFinalMeg("1");
            }
        }
        return video;
    }


    public List<PromoteList> findPromote(Integer id, Integer pagesize) {
        pagesize = (pagesize - 1) * 20;
        List<PromoteList> promote = userMapper.findPromote(id, pagesize);
        //遍历查询
        for (PromoteList promoteList : promote) {
            promoteList.setState("开通");
            Integer id1 = promoteList.getId();
            //判断是否提报中
            //是否有给父id发钱的记录
            Integer num = userMapper.findAccState(id1);
            if (num == 1) {
                promoteList.setAccidentState("已通过");
                promoteList.setMoney(new BigDecimal("10"));
            } else {
                promoteList.setAccidentState("提报中");
                promoteList.setMoney(new BigDecimal("0"));
            }
        }
        return promote;
    }

    public Integer findProple(Integer id) {
        return userMapper.findProple(id);
    }

    public String findVip(Long id){return userMapper.findVip(id);}

    public void openVip(Integer userId, String vipLv) {
        userMapper.openVip(userId,vipLv);
    }
}
