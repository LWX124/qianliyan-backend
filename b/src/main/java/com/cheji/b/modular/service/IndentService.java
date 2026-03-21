package com.cheji.b.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.*;
import com.cheji.b.modular.dto.*;
import com.cheji.b.modular.mapper.IndentMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.b.utils.GenerateDigitalUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单信息表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-09-25
 */
@Service
public class IndentService extends ServiceImpl<IndentMapper, IndentEntity> implements IService<IndentEntity> {

    @Resource
    private IndentMapper indentMapper;
    @Resource
    private ImgService imgService;
    @Resource
    private CarBrandService carBrandService;
    @Resource
    private AppUpMerchantsService appUpMerchantsService;
    @Resource
    private UserService userService;
    @Resource
    private CUserService cUserService;

    public List<EarningListDto> findTodayEarning(Integer userBId, Integer pagesize) {
        pagesize = pagesize * 20 - 20;
        List<EarningListDto> listByMerchants = indentMapper.findListByMerchants(userBId, pagesize);
        for (EarningListDto listByMerchant : listByMerchants) {
            String orderNumber = listByMerchant.getOrderNumber();
            String substring = orderNumber.substring(0, 2);
            if (!substring.equals("sh")) {
                //如果是洗车
                continue;
            }
            String indentId = listByMerchant.getIndentId();
            EntityWrapper<ImgEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("type", 2);
            wrapper.eq("`index`", 1);
            wrapper.eq("key_id", indentId);
            List<ImgEntity> imgEntities = imgService.selectList(wrapper);
            if (imgEntities != null) {
                for (ImgEntity imgEntity : imgEntities) {
                    String url = imgEntity.getUrl();
                    listByMerchant.setUrl(url);
                }
            }
        }
        return listByMerchants;
    }

    public List<EarningListDto> findEffectiveOrder(Integer userBId, Integer pagesize) {
        pagesize = pagesize * 20 - 20;
        List<EarningListDto> listEffectiveOrder = indentMapper.findListEffectiveOrder(userBId, pagesize);
        for (EarningListDto listOrder : listEffectiveOrder) {
            String orderNumber = listOrder.getOrderNumber();
            String substring = orderNumber.substring(0, 2);
            if (!substring.equals("sh")) {
                //如果是洗车
                continue;
            }
            String indentId = listOrder.getIndentId();
            EntityWrapper<ImgEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("type", 2);
            wrapper.eq("`index`", 1);
            wrapper.eq("key_id", indentId);
            List<ImgEntity> imgEntities = imgService.selectList(wrapper);
            if (imgEntities != null) {
                for (ImgEntity imgEntity : imgEntities) {
                    String url = imgEntity.getUrl();
                    listOrder.setUrl(url);
                }
            } else {
                listOrder.setUrl(null);
            }
        }

        return listEffectiveOrder;
    }

    public List<IndentListDto> findMesg(Integer userBId, Integer type, String searchText, Integer pageSize) {
        //查询到订单信息
        pageSize = (pageSize - 1) * 20;
        List<IndentListDto> mesg;
        if (type == 0) {
            type = null;
            mesg = indentMapper.findMesg(userBId, type, pageSize, searchText);
        } else if (type == 5) {
            type = 6;
            mesg = indentMapper.findMesg(userBId, type, pageSize, searchText);
        } else {
            mesg = indentMapper.findMesg(userBId, type, pageSize, searchText);//IndentListDto indentListDto : mesg
        }
        for (int i = 0; i < mesg.size(); i++) {
            String brand = mesg.get(i).getBrand();
            if (StringUtils.isEmpty(brand)) {
                mesg.get(i).setBrand("不限");
            }
            //拿到下一页第一条数据
            if (i == mesg.size() - 1) {
                IndentListDto indentListDto = mesg.get(i);
                String monthTime = indentListDto.getMonthTime();
                pageSize = pageSize + 20;
                List<IndentListDto> nextMesg;
                if (type == null) {
                    nextMesg = indentMapper.findMesg(userBId, type, pageSize, searchText);
                } else if (type == 6) {
                    nextMesg = indentMapper.findMesg(userBId, type, pageSize, searchText);
                } else {
                    nextMesg = indentMapper.findMesg(userBId, type, pageSize, searchText);//IndentListDto indentListDto : mesg
                }

                if (nextMesg.isEmpty()) {
                    indentListDto.setFinalMeg("1");
                } else {
                    IndentListDto nextFirstMes = nextMesg.get(0);
                    String nextTime = nextFirstMes.getMonthTime();
                    if (!monthTime.equals(nextTime)) {
                        indentListDto.setFinalMeg("1");
                    }
                }
            }
            //遍历数据查询月份
            String monthTime = mesg.get(i).getMonthTime();
            if (i + 1 == mesg.size()) {
                break;
            }
            IndentListDto indentListDto = mesg.get(i + 1);
            String nextOne = indentListDto.getMonthTime();
            if (!monthTime.equals(nextOne)) {
                indentListDto.setFinalMeg("1");
            }
        }
        return mesg;
    }

    //查询今日订单和全部订单
    public MineDto findIndentCount(Integer user_b_id) {
        return indentMapper.findIndentCount(user_b_id);
    }

    public List<EarningListDto> findConsumptionDeductions(Integer userBId, Integer pageSize) {
        pageSize = (pageSize - 1) * 20;
        List<EarningListDto> consumptionDeductions = indentMapper.findConsumptionDeductions(userBId, pageSize);
        //拿到图片数据
        for (EarningListDto deduction : consumptionDeductions) {
            String indentId = deduction.getIndentId();
            EntityWrapper<ImgEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("type", 2);
            wrapper.eq("`index`", 1);
            wrapper.eq("key_id", indentId);
            List<ImgEntity> imgEntities = imgService.selectList(wrapper);
            if (imgEntities != null) {
                for (ImgEntity imgEntity : imgEntities) {
                    String url = imgEntity.getUrl();
                    deduction.setUrl(url);
                }
            }
        }
        return consumptionDeductions;
    }

    public IndentDetailsDto findDetailsById(Integer indentId) {

        IndentDetailsDto detailsDto = indentMapper.findDetailsById(indentId);
        String brand = detailsDto.getBrand();
        if (StringUtils.isEmpty(brand)) {
            detailsDto.setBrand("不限");
        }
        //根据订单id查询到图片
        EntityWrapper<ImgEntity> imgWrapper = new EntityWrapper<>();
        imgWrapper.eq("key_id", indentId)
                .eq("type", 2);
        List<ImgEntity> imgEntityList = imgService.selectList(imgWrapper);
        detailsDto.setImgList(imgEntityList);
        return detailsDto;
    }

    public void updateIndent(Integer indentId, Integer state, String settleAccounts, String commissionRate) {
        IndentEntity indentEntity = selectById(indentId);
        //维修金额和佣金比例都为空
        if (StringUtils.isEmpty(settleAccounts) && StringUtils.isEmpty(commissionRate)) {
            //单纯的修改状态
            //根据indenid
            indentEntity.setState(state);
            updateById(indentEntity);
        } else if (StringUtils.isEmpty(settleAccounts) && StringUtils.isNotEmpty(commissionRate)) {
            //修改佣金比例和状态
            BigDecimal bd = new BigDecimal(commissionRate);
            //设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            indentEntity.setCommissionRate(bd);
            indentEntity.setState(state);
            updateById(indentEntity);
        } else if (StringUtils.isNotEmpty(settleAccounts) && StringUtils.isEmpty(commissionRate)) {
            BigDecimal bd = new BigDecimal(settleAccounts);
            //设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            indentEntity.setSettleAccounts(bd);
            indentEntity.setState(state);
            //算出结算到用户金额
            BigDecimal commissionRate1 = indentEntity.getCommissionRate();
            indentEntity.setFixlossUser(commissionRate1.multiply(bd));
            updateById(indentEntity);
        } else if (StringUtils.isEmpty(settleAccounts) && state == null && StringUtils.isNotEmpty(commissionRate)) {
            BigDecimal bd = new BigDecimal(commissionRate);
            //设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            indentEntity.setCommissionRate(bd);
            updateById(indentEntity);
        }
    }

    public BigDecimal findpayAmount(Integer id) {
        return indentMapper.findPayAmount(id);
    }

    public CenterDetailsDto selectIndentCenterMes(Integer userBId) {
        return indentMapper.selectIndentCenterMes(userBId);
    }

    public JSONObject findIndentListByUserid22(String id, String year, String month, String state, Integer pagesize, String upId) {
        pagesize = (pagesize - 1) * 20;
        JSONObject object = new JSONObject();
        if (state.equals("5")) {
            state = "6";
        } else if (state.equals("0")) {
            state = null;
        }

        List<IndentList> indentList = indentMapper.findIndentListByuseridAndState(id, year, month, state, pagesize, upId);
        if (indentList != null) {
            for (int i = 0; i < indentList.size(); i++) {
                //送修单位不为空
                IndentList indentI = indentList.get(i);
                if (StringUtils.isNotEmpty(indentI.getSendUnit())) {
                    indentI.setMerchantsName(indentI.getSendUnit());
                }
                String brand = indentI.getBrand();
                //根据品牌id获取到品牌
                if (brand.equals("0")) {
                    indentI.setBrand("不限");
                    indentI.setImgUrl("");
                } else {
                    CarBrandEntity carBrandEntity = carBrandService.selectById(brand);
                    String name = carBrandEntity.getName();
                    indentI.setBrand(name);
                    indentI.setImgUrl(carBrandEntity.getPicUrl());
                }
                //遍历每一次的数据，拿到对应的图片
                //拿到订单idh
                String indentid = indentI.getIndentid();

                //根据订单id查询到对应的图片
                List<String> imgByIndentid = imgService.findImgByIndentidndIndex(indentid);

                List<String> stringimg = new ArrayList<>();
                for (int j = 0; j < imgByIndentid.size(); j++) {
                    String s = imgByIndentid.get(j);
                    if (StringUtils.isEmpty(s)) {
                        continue;
                    }
                    stringimg.add(s);
                    if (j == 1) {
                        break;
                    }
                }
                indentI.setImgList(stringimg);

//                indentI.setImgUrl(imgByIndentid);
                //根本品牌拿到对应图片

                //拿到下一页第一条数据
                if (i == indentList.size() - 1) {
                    String monthTime = indentI.getMonthTime();
                    pagesize = pagesize + 20;
                    List<IndentList> nextMonthindentList = indentMapper.findIndentListByuseridAndState(id, year, month, state, pagesize, upId);
                    if (nextMonthindentList.isEmpty()) {
                        indentI.setFinalMeg("1");
                    } else {
                        IndentList nextListFirst = nextMonthindentList.get(0);
                        String nextListFirstTime = nextListFirst.getMonthTime();
                        if (!nextListFirstTime.equals(monthTime)) {
                            indentI.setFinalMeg("1");
                        }
                    }
                }
                String monthTime = indentI.getMonthTime();
                if (i + 1 == indentList.size()) {
                    break;
                }
                IndentList indent2 = indentList.get(i + 1);
                String monthTime2 = indent2.getMonthTime();
                if (!monthTime.equals(monthTime2)) {
                    indentI.setFinalMeg("1");
                }
                if (indentI.getFixMoney() != null) {
                    indentI.setMoney(indentI.getFixMoney());
                }
            }
        }
        object.put("indentList", indentList);
        //查询对应 每个月的信息  1.台词，2.预估，3.维修，4.收入
        //查询台词，下了好多订单这个月
        Integer newIndent = indentMapper.findNumByState(Integer.valueOf(id), upId, "1"/*, year, month*/);
        object.put("newIndent", newIndent);

        //预估
        Integer noInShop = indentMapper.findNumByState(Integer.valueOf(id), upId, "2"/*, year, month*/);
        object.put("noInShop", noInShop);

        //维修金额
        Integer inService = indentMapper.findNumByState(Integer.valueOf(id), upId, "3"/*, year, month*/);
        object.put("inService", inService);

        //收入
        Integer inSettlement = indentMapper.findNumByState(Integer.valueOf(id), upId, "4"/*, year, month*/);
        object.put("inSettlement", inSettlement);

        Integer settled = indentMapper.findNumByState(Integer.valueOf(id), upId, "6"/*, year, month*/);
        object.put("settled", settled);


        return object;
    }

    //根据订单id查询订单详情
    public IndentDetails findIndentDetilsByIndentId(String indentId) {
        //拿到对应的订单数据
        //拿到标签数据
        IndentDetails indent = indentMapper.findIndentDetilsByIndentId(indentId);
        if (indent.getSettleAccounts() != null && indent.getFixlossUser() != null) {
            if (indent.getSettleAccounts().compareTo(new BigDecimal("0")) == 0) {
                indent.setCommissionRate(new BigDecimal("0"));
            } else {
                indent.setCommissionRate(indent.getFixlossUser().divide(indent.getSettleAccounts(), 2, BigDecimal.ROUND_HALF_UP));
            }
        }

        //拿到品牌
        String brand = indent.getBrand();
        if (brand.equals("0")) {
            indent.setBrand("不限");
        } else {
            CarBrandEntity carBrandEntity = carBrandService.selectById(brand);
            indent.setBrand(carBrandEntity.getName());
        }

        //根据订单id拿到图片资料
        List<ImgEntity> imgByIndentid = imgService.findImgByIndentid(indentId);
        indent.setImgEntityList(imgByIndentid);

        //查询到订单信息订单对象
        IndentEntity indentEntity = selectById(indentId);
        String orderNumber = indentEntity.getOrderNumber();
        Date creatTime = indentEntity.getCreatTime();

        IndentEntity indentEntity1 = new IndentEntity();
        indentEntity1.setOrderNumber(orderNumber);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(creatTime);
        indentEntity1.setTime(format);

        indent.setIndentEntity(indentEntity1);
        if (indent.getSettleFoursCompanyRate() == null) {
            indent.setSettleFoursCompanyRate(new BigDecimal("0"));
        }
        return indent;
    }


    public JSONObject findIndentListByUserid33(String id, String year, String month, Integer pagesize, String upId) {
        pagesize = (pagesize - 1) * 20;
        JSONObject object = new JSONObject();

        List<IndentList> indentList = indentMapper.findIndentListByuseridAndState(id, year, month, null, pagesize, upId);
        if (indentList != null) {
            for (int i = 0; i < indentList.size(); i++) {
                //送修单位不为空
                IndentList indentI = indentList.get(i);
                if (StringUtils.isNotEmpty(indentI.getSendUnit())) {
                    indentI.setMerchantsName(indentI.getSendUnit());
                }
                String brand = indentI.getBrand();
                //根据品牌id获取到品牌
                if (brand.equals("0")) {
                    indentI.setBrand("不限");
                    indentI.setImgUrl("");
                } else {
                    CarBrandEntity carBrandEntity = carBrandService.selectById(brand);
                    String name = carBrandEntity.getName();
                    indentI.setBrand(name);
                    indentI.setImgUrl(carBrandEntity.getPicUrl());
                }
                //遍历每一次的数据，拿到对应的图片
                //拿到订单idh
                String indentid = indentI.getIndentid();

                //根据订单id查询到对应的图片
                List<String> imgByIndentid = imgService.findImgByIndentidndIndex(indentid);

                List<String> stringimg = new ArrayList<>();
                for (int j = 0; j < imgByIndentid.size(); j++) {
                    String s = imgByIndentid.get(j);
                    if (StringUtils.isEmpty(s)) {
                        continue;
                    }
                    stringimg.add(s);
                    if (j == 1) {
                        break;
                    }
                }
                indentI.setImgList(stringimg);

//                indentI.setImgUrl(imgByIndentid);
                //根本品牌拿到对应图片
                //拿到下一页第一条数据
                if (i == indentList.size() - 1) {
                    String monthTime = indentI.getMonthTime();
                    pagesize = pagesize + 20;
                    List<IndentList> nextMonthindentList = indentMapper.findIndentListByuseridAndState(id, year, month, null, pagesize, upId);
                    if (nextMonthindentList.isEmpty()) {
                        indentI.setFinalMeg("1");
                    } else {
                        IndentList nextListFirst = nextMonthindentList.get(0);
                        String nextListFirstTime = nextListFirst.getMonthTime();
                        if (!nextListFirstTime.equals(monthTime)) {
                            indentI.setFinalMeg("1");
                        }
                    }
                }
                String monthTime = indentI.getMonthTime();
                if (i + 1 == indentList.size()) {
                    break;
                }
                IndentList indent2 = indentList.get(i + 1);
                String monthTime2 = indent2.getMonthTime();
                if (!monthTime.equals(monthTime2)) {
                    indentI.setFinalMeg("1");
                }
                if (indentI.getFixMoney() != null) {
                    indentI.setMoney(indentI.getFixMoney());
                }
            }
        }
        object.put("indentList", indentList);
        //进店台词，  预估产值，实际产值，支付佣金
        Integer allIndent = indentMapper.selectAllIndent(id, upId);
        object.put("allIndent", allIndent);

        Integer estiOut = indentMapper.selectEstimatedOutput(id, upId);
        object.put("estimatedOutput", estiOut);

        Integer trueAmount = indentMapper.selectTrueAmount(id, upId);
        object.put("trueAmount", trueAmount);

        Integer payCommission = indentMapper.selectPayCommission(id, upId);
        object.put("payCommission", payCommission);

        return object;
    }

    public BigDecimal findAllSerMoney(Integer id, String year, String month) {
        return indentMapper.findAllSerMoney(id, year, month);
    }

    public String createNewIndent(AppMessageCarEntity messageCarEntity, Integer userid, List<String> credentialsImgList) {
        IndentEntity indentEntity = new IndentEntity();
        String userBId = messageCarEntity.getUserBId();
        if (StringUtils.isEmpty(userBId)) {
            return null;
        }
        if (userBId.endsWith("Z")) {
            //车己汽车
            indentEntity.setUserBId(150);
            //截取最后一位
            userBId = userBId.substring(0, userBId.length() - 1);
            AppUpMerchantsEntity appUpMerchantsEntity = appUpMerchantsService.selectById(userBId);
            indentEntity.setSendUnit(appUpMerchantsEntity.getName());
        } else {
            indentEntity.setUserBId(Integer.valueOf(userBId));
            AppUserEntity bUserEntity = userService.selectById(userBId);
            if (bUserEntity == null) {
                indentEntity.setSendUnit("名称有误请重新填写");
            } else {
                indentEntity.setSendUnit(bUserEntity.getMerchantsName());
            }
        }
        String orderNo = GenerateDigitalUtil.getOrderNo();
        orderNo = "sh" + orderNo;
        indentEntity.setOrderNumber(orderNo);
        indentEntity.setUserId(userid);
        indentEntity.setCreatTime(new Date());
        indentEntity.setPlan("事故");
        indentEntity.setResponsibility(messageCarEntity.getAccidentResponsibility());
        indentEntity.setUsername(messageCarEntity.getCustomerName());
        indentEntity.setPhoneNumber(messageCarEntity.getPhone());
        indentEntity.setInsuranceCompany(messageCarEntity.getLocalInsurance());
        BigDecimal amount = messageCarEntity.getAmount();
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }
        indentEntity.setFixloss(amount);
        indentEntity.setLicensePlate(messageCarEntity.getLicensePlate());
        String brandId = messageCarEntity.getBrandId();
        if (StringUtils.isEmpty(brandId)) {
            brandId = "0";
        }
        indentEntity.setBrandId(Integer.parseInt(brandId));
        indentEntity.setMeansPayments("车辆到店 按预估维修金额结款");
        CUserEntity userEntity = cUserService.selectById(userid);
        indentEntity.setSendPeople(userEntity.getName());
        indentEntity.setMessageSource(1);
        indentEntity.setDealTime(1);
        this.insert(indentEntity);

        String[] strings = credentialsImgList.toArray(new String[credentialsImgList.size()]);
        Long id = indentEntity.getId();
        imgService.save(strings, id);
        return id.toString();
    }
}
