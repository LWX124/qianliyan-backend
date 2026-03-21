package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.util.DateUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 首页controller
 */
@RequestMapping("/index")
@RestController
public class IndexController {

    private Logger logger = LoggerFactory.getLogger(IndexController.class);


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    static String city = "{\n" +
            "  \"北京\":\"beijing\",\n" +
            "  \"天津\":\"tianjin\",\n" +
            "  \"杭州\":\"hangzhou\",\n" +
            "  \"成都\":\"chengdu\",\n" +
            "  \"兰州\":\"lanzhou\",\n" +
            "  \"贵阳\":\"guiyang\",\n" +
            "  \"南昌\":\"nanchang\",\n" +
            "  \"长春\":\"changchun\",\n" +
            "  \"哈尔滨\":\"haerbin\",\n" +
            "  \"上海\":\"shanghai\",\n" +
            "  \"深圳\":\"shenzhen\",\n" +
            "  \"重庆\":\"chongqing\",\n" +
            "  \"焦作\":\"jiaozuo\",\n" +
            "  \"漯河\":\"luohe\",\n" +
            "  \"周口\":\"zhoukou\",\n" +
            "  \"滑县\":\"huaxian\",\n" +
            "  \"安阳\":\"anyang\",\n" +
            "  \"鹤壁\":\"hebi\",\n" +
            "  \"商丘\":\"shangqiu\",\n" +
            "  \"三门峡\":\"sanmenxia\",\n" +
            "  \"信阳\":\"xinyang\",\n" +
            "  \"许昌\":\"xuchang\",\n" +
            "  \"濮阳\":\"puyang\",\n" +
            "  \"洛阳\":\"luoyang\",\n" +
            "  \"开封\":\"kaifeng\",\n" +
            "  \"济源\":\"jiyuan\",\n" +
            "  \"驻马店\":\"zhumadian\",\n" +
            "  \"邢台\":\"xingtai\",\n" +
            "  \"邯郸\":\"handan\",\n" +
            "  \"石家庄\":\"shijiazhuang\",\n" +
            "  \"秦皇岛\":\"qinhuangdao\",\n" +
            "  \"辛集\":\"xinji\",\n" +
            "  \"定州\":\"dingzhou\",\n" +
            "  \"沧州\":\"cangzhou\",\n" +
            "  \"保定\":\"baoding\",\n" +
            "  \"廊坊\":\"langfang\",\n" +
            "  \"唐山\":\"tangshan\",\n" +
            "  \"张家口\":\"zhangjiakou\",\n" +
            "  \"郑州\":\"zhengzhou\",\n" +
            "  \"西安\":\"xian\",\n" +
            "  \"长治\":\"changzhi\",\n" +
            "  \"大连\":\"dalian\",\n" +
            "  \"达州\":\"dazhou\",\n" +
            "  \"黄石\":\"huangshi\",\n" +
            "  \"临汾\":\"linfen\",\n" +
            "  \"柳州\":\"liuzhou\",\n" +
            "  \"南京\":\"nanjing\",\n" +
            "  \"南阳\":\"nanyang\",\n" +
            "  \"宁波\":\"ningbo\",\n" +
            "  \"咸阳\":\"xianyang\",\n" +
            "  \"新乡\":\"xinxiang\",\n" +
            "  \"忻州\":\"xinzhou\",\n" +
            "  \"徐州\":\"xuzhou\",\n" +
            "  \"银川\":\"yinchuan\",\n" +
            "  \"运城\":\"yuncheng\",\n" +
            "  \"阳泉\":\"yangquan\",\n" +
            "  \"太原\":\"taiyuan\",\n" +
            "  \"绵阳\":\"mianyang\",\n" +
            "  \"登封\":\"dengfeng\",\n" +
            "  \"武汉\":\"wuhan\"\n" +
            "}";

    static JSONObject cityJson;

    static {
        cityJson = JSONObject.parseObject(city);
    }

    final String url = "https://api.jisuapi.com/vehiclelimit/query?appkey=1cf0817c1b596b38&";

    @ApiOperation(value = "限行查询")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "cityString", value = "城市名字 （成都、武汉....）", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/getXXData", method = RequestMethod.GET)
    public JSONObject getXXData(String cityString) {
        JSONObject result = new JSONObject();
        if (StringUtils.isEmpty(cityString)) {
            result.put("code", 401);
            result.put("data", "入参错误");
            return result;
        }
        if (cityString.contains("市")) {
            cityString = cityString.replace("市", "");
        }

        String data = "";

        String cityData = cityJson.getString(cityString);
        String currentDate = DateUtils.getCurrentDate();
        data = stringRedisTemplate.opsForValue().get(RedisConstant.LIMIT_DATE_ROW + "_" + cityData + "_" + currentDate);
        if (StringUtils.isEmpty(data)) {
            //调接口
            RestTemplate restTemplate = new RestTemplate();
            String urlString = url + "city=" + cityData + "&date=" + currentDate;
            ResponseEntity<String> forEntity = restTemplate.getForEntity(urlString, String.class);
            String body = forEntity.getBody();
            JSONObject bodyObj = JSONObject.parseObject(body);
            logger.info("限行查询#############  bodyObj={}", bodyObj);
            Integer status = bodyObj.getInteger("status");
            logger.info("### urlString={};bodyObj={}", urlString, bodyObj);
            if (status == 0) {
                data = bodyObj.getJSONObject("result").getString("number");
                logger.info("### 限行插入redis的数据  key={} ;data={}", RedisConstant.LIMIT_DATE_ROW + "_" + cityData + "_" + currentDate, data);
                if (StringUtils.isEmpty(data)) {
                    data = "1";//弄个默认值，让下次进来不再调用接口
                }
                stringRedisTemplate.opsForValue().set(RedisConstant.LIMIT_DATE_ROW + "_" + cityData + "_" + currentDate, data, 60 * 60 * 24, TimeUnit.SECONDS);
            }
        }

        if (data == null || data.equals("1")) {
            data = "";//返回给前端空字符串
        }

        JSONObject object = new JSONObject();
        //查询
        String s1 = stringRedisTemplate.opsForValue().get(RedisConstant.SHARE_CONTENT + 12);
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(s1)) {
            String[] split = s1.split(",");
            List<String> strings = new ArrayList<>();
            for (String s : split) {
                strings.add(s);
            }
            object.put("urlList", strings);
            object.put("accUrl",strings);
            object.put("washUrl",strings);
        }
        object.put("data", data);
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.APP_VIDEO_URL);
        object.put("videoUrl",s);
        //
        object.put("phone","18181999916");

        result.put("code", 200);
        result.put("data", object);
        return result;
    }
}
