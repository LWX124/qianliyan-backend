package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.cwork.AccidentDetails;
import com.cheji.web.modular.cwork.AccidentList;
import com.cheji.web.modular.cwork.AccidentReward;
import com.cheji.web.modular.cwork.WorkList;
import com.cheji.web.modular.domain.AccidentRecordEntity;
import com.cheji.web.modular.domain.AppRepeatAccidentEntity;
import com.cheji.web.modular.domain.PushRecordEntity;
import com.cheji.web.modular.domain.UserEntity;
import com.cheji.web.modular.mapper.AccidentRecordMapper;
import com.cheji.web.modular.mapper.VideoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.swing.UIManager.getString;

/**
 * <p>
 * app上报事故信息表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-08-28
 */
@Service
public class AccidentRecordService extends ServiceImpl<AccidentRecordMapper, AccidentRecordEntity> implements IService<AccidentRecordEntity> {

    @Resource
    private UserService userService;

    @Resource
    private VideoMapper videoMapper;

    @Resource
    private AccidentRecordMapper AccidentRecordEntityMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private VideoCommentsService videoCommentsService;

    @Resource
    private AppRepeatAccidentService appRepeatAccidentService;

    @Resource
    private RestTemplate restTemplate;


    private static String sendme = "http://localhost:9001/jeesite/api/socket/sendMsg";


    //拿到最近得理赔员
  /*  public UserEntity getColseUser(Double lng, Double lat){
        UserEntity userEntity = null;
        Circle circle = new Circle(lng,lat, Metrics.KILOMETERS.getMultiplier());
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeCoordinates().includeCoordinates().sortAscending().limit(5);
        //根据给定的经纬度，返回半径不超过指定距离的元素,时间复杂度为O(N+log(M))，N为指定半径范围内的元素个数，M为要返回的个数
        //找到理赔员                                                                              理赔员位置信息key
        GeoResults<RedisGeoCommands.GeoLocation<String>> List = redisTemplate.opsForGeo().radius("push", circle, args);
        Iterator<GeoResult<RedisGeoCommands.GeoLocation<String>>> iterator = List.iterator();
        while (iterator.hasNext()){
            GeoResult<RedisGeoCommands.GeoLocation<String>> next = iterator.next();
            //System.out.println(next.getDistance().getValue());
            //System.out.println(next.getContent().getName());
            String[] split = next.getContent().getName().split("#");
            //System.out.println(Arrays.toString(split));
            //split[0].equals(carid) &&
            //获取到1.5之内得第一个理赔员，把消息推送给他
            if(next.getDistance().getValue()<1.5){
                //获取到 geopos push ‘test5’
                String name = next.getContent().getName();
                //根据名字找到User
                EntityWrapper<UserEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("id", name);
                List<UserEntity> userEntities = userService.selectList(wrapper);
                if (userEntities ==null){
                    return null;
                }
                for (int i = 0; i < userEntities.size(); i++) {
                    userEntity = userEntities.get(0);
                    String blackFalg = userEntity.getBlackFalg();
                    if ("Y".equals(blackFalg)) {
                        userEntity = null;
                        continue;
                    }else {
                        return userEntity;
                    }
                }
            }
        }
        return null;
    }*/
    //根据推送记录的事故id拿到事故表中id，用state状态来判断
    public List<AccidentList> findAccidByPush(List<PushRecordEntity> pushRecordEntities, Integer state) {
        List<AccidentList> accidentlists = new ArrayList<>();
        //先拿到list中的事故id
        for (PushRecordEntity pushRecordEntity : pushRecordEntities) {
            Integer accid = pushRecordEntity.getAccid();
            //通过accid查询到事故信息
            if (null == state) {
                state = -1;
            }

            AccidentList accident = AccidentRecordEntityMapper.findAccMesById(accid, state);
            if (null != accident)
                accidentlists.add(accident);
        }
        return accidentlists;
    }

    public AccidentDetails findDeatilsByAccid(String accid) {
        return AccidentRecordEntityMapper.findDeatilsByAccid(accid);
    }

    public void updateState(Integer id, Integer realness) {
        AccidentRecordEntity AccidentRecordEntity = selectById(id);
        AccidentRecordEntity.setRealness(realness);
        updateById(AccidentRecordEntity);
    }

    //查询当天的事故信息和支付记录
    public List<AccidentList> findAccidListByIdAndDate(Integer userid, Integer pagesize) {
        pagesize = (pagesize - 1) * 20;
        return AccidentRecordEntityMapper.findAccidListByIdAndDate(userid, pagesize);
    }

    //查询的事故信息和支付记录总数
    public List<AccidentList> findAccidByUserid(Integer userid, Integer pagesize) {
        pagesize = (pagesize - 1) * 20;
        return AccidentRecordEntityMapper.findAccidByUserid(userid, pagesize);
    }

    //根据用户查询到上传得作品
    public List<WorkList> findWorksByUserId(String userId, Integer pagesize) {
        pagesize = (pagesize - 1) * 20;
        List<WorkList> workByUserId = AccidentRecordEntityMapper.findWorkByUserId(Integer.valueOf(userId), pagesize);
        UserEntity userEntity = userService.selectById(userId);
        for (WorkList list : workByUserId) {
            //遍历上传的数据，没有videoid的就是 审核未通过的视频，就没有点赞数据和评论数据
            if (null == list.getVideoId()) {
                if (list.getReason() != null) {
                    list.setIntroduce(list.getReason());
                }
                //没有点赞和评论数据
                String avatar = userEntity.getAvatar();
                String name = userEntity.getName();
                list.setAvatar(avatar);
                list.setName(name);
                list.setIsPass("N");
            } else {
                //有video就是审核通过的视频，审核通过了就需要点赞数据和评论数据
                String avatar = userEntity.getAvatar();
                String name = userEntity.getName();
                list.setAvatar(avatar);
                list.setName(name);
                list.setIsPass("Y");
                //获取到点赞数和评论数据
                //视频的点赞数量从redis中获取到
                String videoId = list.getVideoId();
                //查询是否点了赞
                Double score = stringRedisTemplate.opsForZSet().score(RedisConstant.LIKE_USER_THUMBS_VIDEO + userId, videoId);
                if (score == null || score == 0D) {
                    list.setLikeFlag(0);
                } else {
                    list.setLikeFlag(1);
                }
                //分享次数
                String s = (String) stringRedisTemplate.opsForHash().get(RedisConstant.VIDEO_SHARE, videoId);
                if (StringUtils.isEmpty(s)) {
                    list.setShareCount(0L);
                } else {
                    list.setShareCount(Long.valueOf(s));
                }
                Long size = stringRedisTemplate.opsForZSet().size(RedisConstant.SET_VIDEO_THUMBS + videoId);
                list.setThumbsCount(size);
                list.setCount(size);
                //获取到视频评论数量
                Long videoCommentCount = videoCommentsService.findVideoCommentCount(Long.valueOf(videoId), 1);
                list.setCommentsCount(videoCommentCount);
            }

            //查询红包是否有红包记录
            BigDecimal amount = AccidentRecordEntityMapper.selectPayRecord(list.getId());
            if (amount == null) {
                amount = BigDecimal.ZERO;
            }
            list.setAmount(amount);
        }
        return workByUserId;
    }

    public List<WorkList> findWorksByUserIdAndType(String userId, Integer type) {
        return AccidentRecordEntityMapper.findWorkByUserIdAndType(userId, type);
    }

    //查询他点赞的视频
    public List<WorkList> findVideoByUserid(Integer userId) {
        return AccidentRecordEntityMapper.findVideoByUserid(userId);
    }

    //保存用户上传的视频
    public AccidentRecordEntity save(AccidentRecordEntity accidentRecordEntity, Integer id) {
        accidentRecordEntity.setUserId(String.valueOf(id));
        accidentRecordEntity.setIsaddvideo(0);
        Date date = new Date();
        accidentRecordEntity.setCreateTime(date);
        insert(accidentRecordEntity);
        EntityWrapper<AccidentRecordEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("create_time", date);
        List<AccidentRecordEntity> accidentRecordEntities = selectList(wrapper);
        for (AccidentRecordEntity recordEntity : accidentRecordEntities) {
            //Integer accid = recordEntity.getId();
            return recordEntity;
        }
        return null;
    }

    public WorkList findVideoById(String member) {
        WorkList videoById = videoMapper.findVideoById(member);
        videoById.setAmount(BigDecimal.ZERO);
        UserEntity userEntity = new UserEntity();
        if (videoById.getIsPass().equals("2")) {
            String wxuserId = videoById.getUserId();
            userEntity = userService.selectWxuser(wxuserId);
        } else {
            String userId = videoById.getUserId();
            userEntity = userService.selectById(userId);
        }
        if (userEntity == null) {
            return null;
        }

        String avatar = userEntity.getAvatar();
        String name = userEntity.getName();
        if (StringUtils.isNotEmpty(name)) {
            int i = name.indexOf("%");
            if (i != -1) {
                try {
                    name = java.net.URLDecoder.decode(name, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        videoById.setAvatar(avatar);
        videoById.setName(name);
        videoById.setLikeFlag(1);
        videoById.setVideoId(member);

        //获取到点赞数量
        //获取到视频id

        String id = videoById.getId();
        Long size = stringRedisTemplate.opsForZSet().size(RedisConstant.SET_VIDEO_THUMBS + id);
        videoById.setThumbsCount(size);
        videoById.setCount(size);
        //获取到评论数量
        Long videoCommentCount = videoCommentsService.findVideoCommentCount(Long.valueOf(id), 1);
        videoById.setCommentsCount(videoCommentCount);

        //分享次数
        String s = (String) stringRedisTemplate.opsForHash().get(RedisConstant.VIDEO_SHARE, member);
        if (StringUtils.isEmpty(s)) {
            videoById.setShareCount(0L);
        } else {
            videoById.setShareCount(Long.valueOf(s));
        }
        return videoById;
    }

    public void send() {
        restTemplate.getForObject(sendme, String.class);
    }

    public List<AccidentRecordEntity> selectFiveAgo(Integer id) {
        return AccidentRecordEntityMapper.selectFiveAgo(id);
    }

    public List<AccidentRecordEntity> selectByuserIdAndLngLat(String userId, Double lng, Double lat) {
        return AccidentRecordEntityMapper.selectByUserIdAndLngLat(userId, lng, lat);
    }

    public void addGeoAndRepeat(AccidentRecordEntity save) {
        //先找再传
        //先查询GEO中数据是否有在500米内
        Circle circle = new Circle(save.getLng(), save.getLat(), 500);
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeCoordinates().includeCoordinates().sortAscending().limit(50);
        GeoResults<RedisGeoCommands.GeoLocation<String>> radius = stringRedisTemplate.opsForGeo().radius(RedisConstant.ACCIDENT_GEO, circle, args);
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = radius.getContent();
        for (GeoResult<RedisGeoCommands.GeoLocation<String>> geoLocationGeoResult : content) {
            RedisGeoCommands.GeoLocation<String> conten = geoLocationGeoResult.getContent();
            String name = conten.getName();
//            String  data3=name.replace("\\", "");
//            //去掉开头，结尾的引号
//            String data4 =  data3.substring(1, data3.length()-1);
            JSONObject jsonObject = JSONObject.parseObject(name);
            //geo中事故id
            Integer id = jsonObject.getInteger("id");
            //事故类型，1，派单视频，2.上传视频
            Integer type = jsonObject.getInteger("type");

            //获取到重复的事故id
            //获取到的事故id添加到表中
            AppRepeatAccidentEntity appRepeatAccident = new AppRepeatAccidentEntity();
            appRepeatAccident.setAccidentId(String.valueOf(save.getId()));
            appRepeatAccident.setAccidentSource(save.getType());
            appRepeatAccident.setRepeatId(String.valueOf(id));
            appRepeatAccident.setRepeatSrouce(type);
            appRepeatAccident.setCreateTime(new Date());
            appRepeatAccidentService.insert(appRepeatAccident);
        }


        //把上传的视频添加到geo中
        JSONObject in = new JSONObject();
        in.put("id", save.getId());
        in.put("type", save.getType());
        stringRedisTemplate.opsForGeo().add(RedisConstant.ACCIDENT_GEO, new Point(save.getLng(), save.getLat()), in.toJSONString());
        save.setGeoredis(in.toJSONString());
        updateById(save);


    }

    public BigDecimal selectPayRecord(String id) {
        return AccidentRecordEntityMapper.selectPayRecord(id);
    }

    public List<AccidentReward> findAccidentReward(Integer pagesize) {
        pagesize = (pagesize - 1) * 20;
        List<AccidentReward> accidentReward = AccidentRecordEntityMapper.findAccidentReward(pagesize);
        //查询到评论数量，红包金额，播放量
        for (AccidentReward accidentReward1 : accidentReward) {
            //事故id
            Integer id = accidentReward1.getId();
            //查询到评论数量，红包金额，播放量
            Long videoCommentCount = videoCommentsService.findVideoCommentCount(Long.valueOf(id), 2);
            accidentReward1.setCommentNumber(videoCommentCount);
            //根据事故id查询到红包金额
            List<BigDecimal> bigDecimals = AccidentRecordEntityMapper.findRedPayAmount(id.toString());
            if (!bigDecimals.isEmpty()){
                BigDecimal bigDecimal = bigDecimals.get(0);
                accidentReward1.setRedPay(bigDecimal);
            }else {
                accidentReward1.setRedPay(new BigDecimal("5"));
            }

            accidentReward1.setPlayNumber("99+");
        }
        return accidentReward;
    }

    public List<AccidentReward> findSeriousAcc(Integer pagesize) {
        //查询金额超过10块得事故
        pagesize = (pagesize - 1) * 20;
        List<Integer> accIdList = AccidentRecordEntityMapper.findSeriousAcc(pagesize);
        //遍历查询
        List<AccidentReward> accidentRewards = new ArrayList<>();
        for (Integer accid : accIdList) {
            AccidentReward seriousMes = AccidentRecordEntityMapper.findSeriousMes(accid);
            if (seriousMes==null){
                continue;
            }
            Integer id = seriousMes.getId();
            //查询到评论数量，红包金额，播放量
            Long videoCommentCount = videoCommentsService.findVideoCommentCount(Long.valueOf(id), 2);
            seriousMes.setCommentNumber(videoCommentCount);
            //根据事故id查询到红包金额
            List<BigDecimal> bigDecimals = AccidentRecordEntityMapper.findRedPayAmount(id.toString());
            BigDecimal bigDecimal = bigDecimals.get(0);

            seriousMes.setRedPay(bigDecimal);
            seriousMes.setPlayNumber("99+");
            accidentRewards.add(seriousMes);
        }
        return accidentRewards;
    }

    public List<AccidentReward> findAccYourself(Integer id, Integer pagesize) {
        //先查询到他自己收到得红包
        pagesize = (pagesize - 1) * 20;
        List<Integer> accidList =  AccidentRecordEntityMapper.findYourself(id,pagesize);
        //查询到具体信息
        List<AccidentReward> accidentRewards = new ArrayList<>();
        if (!accidList.isEmpty()){
            for (Integer accid : accidList) {
                AccidentReward accMes = AccidentRecordEntityMapper.findSeriousMes(accid);
                Integer id1 = accMes.getId();
                //查询到评论数量，红包金额，播放量
                Long videoCommentCount = videoCommentsService.findVideoCommentCount(Long.valueOf(id1), 2);
                accMes.setCommentNumber(videoCommentCount);
                //根据事故id查询到红包金额
                List<BigDecimal> bigDecimals = AccidentRecordEntityMapper.findRedPayAmountByHim(id1.toString(),id);
                BigDecimal bigDecimal = bigDecimals.get(0);
                accMes.setRedPay(bigDecimal);
                accMes.setPlayNumber("99+");
                accidentRewards.add(accMes);
            }
        }
        return accidentRewards;
    }
}
