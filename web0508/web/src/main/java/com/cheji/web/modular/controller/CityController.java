package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.domain.CityEntity;
import com.cheji.web.modular.service.CityService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/city")
public class CityController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CityService cityService;

    //推荐城市接口
    @ApiOperation(value = "推荐城市接口")
    @RequestMapping(value = "/recommendCity", method = RequestMethod.GET)
    public JSONObject recommendCity() {
        JSONObject result = new JSONObject();

        //先从redis中取推荐城市
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.RECOMMEND_CITY);
//        String s = "";
        if (StringUtils.isEmpty(s)) {
            //查询到推荐城市，把数据返回去
            EntityWrapper<CityEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("recommend_city", 1);
            List<CityEntity> cityEntities = cityService.selectList(wrapper);
            //放到redis里面
            JSONArray array = new JSONArray();
            for (CityEntity cityEntity : cityEntities) {
                JSONObject object = new JSONObject();
                Integer citycode = cityEntity.getCitycode();
                String s1 = citycode.toString();
                if (s1.length() == 2) {
                    object.put("citycode", "0" + s1);
                } else {
                    object.put("citycode", s1);
                }
                object.put("cityName", cityEntity.getCityName());
                array.add(object);
            }
            stringRedisTemplate.opsForValue().set(RedisConstant.RECOMMEND_CITY, JSONArray.toJSONString(array), 60 * 60, TimeUnit.SECONDS);
            result.put("code", 200);
            result.put("msg", "成功");
            result.put("data", array);
            return result;
        } else {
            List<CityEntity> cityEntities = JSONArray.parseArray(s, CityEntity.class);

            result.put("code", 200);
            result.put("msg", "成功");
            result.put("data", cityEntities);
            return result;
        }


    }


    @ApiOperation(value = "推荐城市接口")
    @RequestMapping(value = "/recommendCity22", method = RequestMethod.GET)
    public JSONObject recommendCity22() {
        JSONObject result = new JSONObject();

        //先从redis中取推荐城市
        // String s = stringRedisTemplate.opsForValue().get(RedisConstant.RECOMMEND_CITY);
        String s = "";
        if (StringUtils.isEmpty(s)) {
            //查询到推荐城市，把数据返回去
            EntityWrapper<CityEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("recommend_city", 1);
            List<CityEntity> cityEntities = cityService.selectList(wrapper);
            //放到redis里面
            for (CityEntity cityEntity : cityEntities) {
                JSONArray array = new JSONArray();
                Integer citycode = cityEntity.getCitycode();
                //查询所有的citycode
                EntityWrapper<CityEntity> wrapper1 = new EntityWrapper<>();
                wrapper1.eq("citycode", citycode);
                List<CityEntity> citycodeEntities = cityService.selectList(wrapper1);

                for (CityEntity citycodeEntity : citycodeEntities) {
                    JSONObject citydo = new JSONObject();
                    if ( citycodeEntity.getRecommendCity()!=null) {
                        continue;
                    }
                    Integer adcode = citycodeEntity.getAdcode();
                    String s1 = adcode.toString();
                    if (s1.length() < 5) {
                        citydo.put("citycode", "0" + s1);
                    } else {
                        citydo.put("citycode", s1);
                    }
                    citydo.put("cityName", citycodeEntity.getCityName());
                    array.add(citydo);
                }
                cityEntity.setJsonArray(array);
            }
            // stringRedisTemplate.opsForValue().set(RedisConstant.RECOMMEND_CITY, JSONArray.toJSONString(array), 60 * 5, TimeUnit.SECONDS);
            result.put("code", 200);
            result.put("msg", "成功");
            result.put("data", cityEntities);
            return result;
        } else {
            List<CityEntity> cityEntities = JSONArray.parseArray(s, CityEntity.class);

            result.put("code", 200);
            result.put("msg", "成功");
            result.put("data", cityEntities);
            return result;
        }


    }
}
