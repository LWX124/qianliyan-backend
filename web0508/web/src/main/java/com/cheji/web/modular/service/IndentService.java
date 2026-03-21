package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.cwork.IndentDetails;
import com.cheji.web.modular.cwork.IndentList;
import com.cheji.web.modular.cwork.Personal;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.mapper.AppClaimTeacherMapper;
import com.cheji.web.modular.mapper.IndentMapper;
import com.cheji.web.util.GenerateDigitalUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单信息表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-08-26
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
    private PushBillService pushBillService;

    @Resource
    private BUserService bUserService;

    @Resource
    private UserService userService;

    @Resource
    private AppUpMerchantsService appUpMerchantsService;

    @Resource
    private ImgService ImgService;

    @Resource
    private AppClaimTeacherMapper AppClaimTeacherMapper;


    //订单列表
    public List<IndentList> findIndentListByUserid(String id, String year, String month, String state, Integer pagesize) {
        pagesize = (pagesize - 1) * 20;
        JSONObject object = new JSONObject();
        if (state.equals("5")) {
            state = "6";
        } else if (state.equals("0")) {
            state = null;
        }

        List<IndentList> indentList = indentMapper.findIndentListByuseridAndState(id, year, month, state, pagesize);
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
                    List<IndentList> nextMonthindentList = indentMapper.findIndentListByuseridAndState(id, year, month, state, pagesize);
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
//        object.put("indentList",indentList);
//        //查询对应 每个月的信息  1.台词，2.预估，3.维修，4.收入
//        //查询台词，下了好多订单这个月
//        Integer dealCount = AppClaimTeacherMapper.findDealCount(Integer.valueOf(id), year, month);
//        object.put("dealCount",dealCount);
//
//        //预估
//        BigDecimal dealOutput = AppClaimTeacherMapper.findDealOutput(Integer.valueOf(id), year, month);
//        object.put("estimatedAmount",dealOutput);
//
//        //维修金额
//        BigDecimal Output = AppClaimTeacherMapper.findOutput(Integer.valueOf(id), year, month);
//        object.put("maintenanceAmount",Output);
//
//        //收入
//        BigDecimal allIncome = AppClaimTeacherMapper.findAllIncome(Integer.parseInt(id), year, month);
//        object.put("income",allIncome);

        return indentList;


//        if (StringUtils.isEmpty(state)) {
//            List<IndentList> indentListByuserid = indentMapper.findIndentListByuserid(id,pagesize);
//
//            return indentListByuserid;
//        }else {
//            if (state.equals("5")){
//                state="6";
//            }
//            List<IndentList> indentListByuseridAndState = indentMapper.findIndentListByuseridAndState(id, state,pagesize);
//            if (state.equals("4")){
//                indentListByuseridAndState = indentMapper.findFourAndFive(id,pagesize);
//            }
//            if (indentListByuseridAndState!=null){
//                for (IndentList indentList : indentListByuseridAndState) {
//                    if (StringUtils.isNotEmpty(indentList.getSendUnit())){
//                        indentList.setMerchantsName(indentList.getSendUnit());
//                    }
//                    String brand = indentList.getBrand();
//                    //根据品牌id获取到品牌
//                    if (brand.equals("0")){
//                        indentList.setBrand("不限");
//                    }else {
//                        CarBrandEntity carBrandEntity = carBrandService.selectById(brand);
//                        String name = carBrandEntity.getName();
//                        indentList.setBrand(name);
//                    }
//                    String indentid = indentList.getIndentid();
//                    String imgByIndentidndIndex = imgService.findImgByIndentidndIndex(indentid);
//                    indentList.setImgUrl(imgByIndentidndIndex);
//                }
//            }
//            return indentListByuseridAndState;
//        }
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

    //根据用户id找到销售收入
    public String findFixlossUserByuserId(String userId) {

        return indentMapper.findFixlossUserByuserId(userId);
    }

    public Personal findIndentCount(String userId) {
        return indentMapper.findIndentCount(userId);
    }

    //根据订单编号查询到订单
    public IndentEntity findIndentByOrderNum(String indentOrder) {
        EntityWrapper<IndentEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("order_number", indentOrder);
        //查询到订单
        List<IndentEntity> indentEntities = selectList(wrapper);
        //根据订单查询到图片
        if (indentEntities != null && indentEntities.size() != 0) {
            for (IndentEntity indent : indentEntities) {
                Integer brandId = indent.getBrandId();
                if (brandId == 0) {
                    indent.setBrandName("品牌不限");
                } else {
                    CarBrandEntity carBrandEntity = carBrandService.selectById(brandId);
                    indent.setBrandName(carBrandEntity.getName());
                }
                Long id = indent.getId();
                List<ImgEntity> imgByIndentid = imgService.findImgByIndentid(String.valueOf(id));
                indent.setConfirmImg(imgByIndentid);
                return indent;
            }
        }
        return null;
    }

    public List<String> findSendUnit() {
        return indentMapper.findSendUnit();
    }

    public List<String> findEmployee() {
        return indentMapper.findEmployee();
    }

    public String createNewIndent(AppMessageCarEntity messageCarEntity, Integer userid, List<String> credentialsImgList) {
        IndentEntity indentEntity = new IndentEntity();
        String userBId = messageCarEntity.getUserBId();
        if (StringUtils.isEmpty(userBId)) {
            return null;
        }
        if (userBId.endsWith("Z")) {
            //车己汽车
            indentEntity.setUserBId("150");
            //截取最后一位
            userBId = userBId.substring(0, userBId.length() - 1);
            AppUpMerchantsEntity appUpMerchantsEntity = appUpMerchantsService.selectById(userBId);
            indentEntity.setSendUnit(appUpMerchantsEntity.getName());
        } else {
            indentEntity.setUserBId(userBId);
            BUserEntity bUserEntity = bUserService.selectById(userBId);
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
        indentEntity.setFixloss(amount.toString());
        indentEntity.setLicensePlate(messageCarEntity.getLicensePlate());
        String brandId = messageCarEntity.getBrandId();
        if (StringUtils.isEmpty(brandId)) {
            brandId = "0";
        }
        indentEntity.setBrandId(Integer.parseInt(brandId));
        indentEntity.setMeansPayments("车辆到店 按预估维修金额结款");
        UserEntity userEntity = userService.selectById(userBId);
        indentEntity.setSendPeople(userEntity.getName());
        indentEntity.setMessageSource(1);
        indentEntity.setDealTime(1);
        this.insert(indentEntity);

        String[] strings = credentialsImgList.toArray(new String[credentialsImgList.size()]);
        Long id = indentEntity.getId();
        ImgService.save(strings, id);
        return id.toString();
    }

    public IndentEntity findTime(String dateString1, Integer userId) {
        return indentMapper.findTime(dateString1, userId);
    }


    //订单列表
    public JSONObject findIndentListByUserid22(String id, String year, String month, String state, Integer pagesize) {
        pagesize = (pagesize - 1) * 20;
        JSONObject object = new JSONObject();
        if (state.equals("5")) {
            state = "6";
        } else if (state.equals("0")) {
            state = null;
        }

        List<IndentList> indentList = indentMapper.findIndentListByuseridAndState(id, year, month, state, pagesize);
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
                    List<IndentList> nextMonthindentList = indentMapper.findIndentListByuseridAndState(id, year, month, state, pagesize);
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
        Integer dealCount = AppClaimTeacherMapper.findDealCount(Integer.valueOf(id), year, month);
        object.put("dealCount", dealCount);

        //预估
        BigDecimal dealOutput = AppClaimTeacherMapper.findDealOutput(Integer.valueOf(id), year, month);
        object.put("estimatedAmount", dealOutput);

        //维修金额
        BigDecimal Output = AppClaimTeacherMapper.findOutput(Integer.valueOf(id), year, month);
        object.put("maintenanceAmount", Output);

        //收入
        BigDecimal allIncome = AppClaimTeacherMapper.findAllIncome(Integer.parseInt(id), year, month);
        object.put("income", allIncome);

        return object;
    }

    public JSONObject selectCarType(JSONObject res, String body) {
        String host = "https://ocrcars.market.alicloudapi.com"; // 【1】请求地址 支持http 和 https 及 WEBSOCKET
        String path = "/OCRcar";  // 【2】后缀
        String appcode = "321589b1df7348a5a66e37c4a0eccd3d"; // 【3】开通服务后 买家中心-查看AppCode
        body = "image="+body;//obs-4f4a.obs.cn-southwest-2.myhuaweicloud.com/img/decf667d58555fadb756d55f063eeca.jpg"; // 【4】请求参数，详见文档描述
        // 或者base64
        // String body = "image=data:image/jpeg;base64,/9j/4A......";
        String urlSend = host + path; // 【5】拼接请求链接
        String exception = "异常，联系管理员";
        try {
            URL url = new URL(urlSend);
            HttpURLConnection httpURLCon = (HttpURLConnection) url.openConnection();
            httpURLCon.setRequestMethod("POST");
            httpURLCon.setRequestProperty("Authorization", "APPCODE " + appcode);// 格式Authorization:APPCODE
            // (中间是英文空格)
            StringBuilder postData = new StringBuilder(body);

            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            httpURLCon.setDoOutput(true);
            OutputStream out = httpURLCon.getOutputStream();
            out.write(postDataBytes);
            out.close();
            int httpCode = httpURLCon.getResponseCode();
            if (httpCode == 200) {
                String json = read(httpURLCon.getInputStream());
//                System.out.println("正常请求计费(其他均不计费)");
////                System.out.println("获取返回的json:");
////                System.out.print(json);
                JSONObject object = JSONObject.parseObject(json);
                JSONObject result = object.getJSONObject("result");
                String carType = result.getString("carType");
//                System.out.println(carType);
                //根据名字查询id
                Integer brandid = carBrandService.findId(carType);
                res.put("code", 200);
                res.put("msg", "成功");
                res.put("data", brandid);
                return res;
            } else {
                Map<String, List<String>> map = httpURLCon.getHeaderFields();
                String error = map.get("X-Ca-Error-Message").get(0);
                if (httpCode == 400 && error.equals("Invalid AppCode")) {
                    exception = "AppCode错误 ";
                } else if (httpCode == 400 && error.equals("Invalid Url")) {
                    exception = "请求的 Method、Path 或者环境错误";
                } else if (httpCode == 400 && error.equals("Invalid Param Location")) {
                    exception = "参数错误";
                } else if (httpCode == 403 && error.equals("Unauthorized")) {
                    exception = "服务未被授权（或URL和Path不正确）";
                } else if (httpCode == 403 && error.equals("Quota Exhausted")) {
                    exception = "套餐包次数用完 ";
                } else if (httpCode == 403 && error.equals("Api Market Subscription quota exhausted")) {
                    exception = "套餐包次数用完，请续购套餐";
                } else {
                    exception = "参数名错误 或 其他错误";
                }
                res.put("code", 407);
                res.put("msg", exception);
                return res;
            }

        } catch (MalformedURLException | UnknownHostException e) {
            exception = "URL地址错误";
        } catch (Exception e) {
            // 打开注释查看详细报错异常信息
            // e.printStackTrace();
        }
        res.put("code", 407);
        res.put("msg", exception);
        return res;

    }

    /*
     * 读取返回结果
     */
    private static String read(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = br.readLine()) != null) {
            line = new String(line.getBytes(), "utf-8");
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
}
