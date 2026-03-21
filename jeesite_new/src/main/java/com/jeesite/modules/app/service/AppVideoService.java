/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.excep.CusException;
import com.jeesite.modules.constant2.AppConstants;
import com.jeesite.modules.app.dao.AppVideoDao;
import com.jeesite.modules.app.entity.AppVideo;
import com.jeesite.modules.file.utils.FileUploadUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 视频信息表Service
 * @author zcq
 * @version 2019-07-31
 */
@Service
@Transactional(readOnly=true)
public class AppVideoService extends CrudService<AppVideoDao, AppVideo> {

	@Resource
	private RedisTemplate redisTemplate;

	@Autowired
	private AppVideoDao videoDao;

	public List<AppVideo> findredis(){
		 return videoDao.findReids();
	}

	public String findAllCo(){
		return videoDao.findAllCount();
	}


	//根据事故id来查询到对应的video数据
	public AppVideo findVideoByAccid(String accid){
		return videoDao.findVideoAccid(accid);
	}

	/**
	 * 获取单条数据
	 * @param appVideo
	 * @return
	 */
	@Override
	public AppVideo get(AppVideo appVideo) {
		return super.get(appVideo);
	}

	/**
	 * 查询分页数据
	 * @param appVideo 查询条件
	 * @return
	 */
	@Override
	public Page<AppVideo> findPage(AppVideo appVideo) {
		Page a = appVideo.getPage();
		int pageNo = a.getPageNo();//页数
		int pageSize = a.getPageSize();//条数
		int pageOffset = (pageNo - 1) * pageSize;
		long count = a.getCount();
		Integer state = appVideo.getState();
		String url = appVideo.getUrl();
		Date creat = appVideo.getCreatTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String format;
		if (creat == null){
			format = null;
		} else {
			format = simpleDateFormat.format(creat);
		}

		List<AppVideo> videoListDto = videoDao.findListDot(pageOffset,state,url,format);
		count = videoDao.findcount();
		a.setList(videoListDto);
		a.setCount(count);


	//	Page<AppVideo> page = super.findPage(appVideo);
		//拿到page中list
		//List<AppVideo> list = page.getList();
		//从缓存中拿数据
		Set range = redisTemplate.opsForZSet().range(AppConstants.APP_VIDEO_LIST, 0, -1);
		videoListDto= new ArrayList(range);

		if ( videoListDto.size() ==0 || null == videoListDto ){
			//如果reids为空，先从数据库获取数据，放到redis中，在进行查询
			for (AppVideo video : videoListDto) {
				Date creatTime = video.getCreatTime();
				long time = creatTime.getTime();
				JSONObject in = new JSONObject();
				in.put("videoId", video.getId());
				in.put("url", video.getUrl());
				in.put("userId", video.getUserId());
				in.put("count", video.getCount());
				in.put("share", video.getShare());
				in.put("appViewCounts", video.getAppViewCounts());
				in.put("accidentId", video.getAccidentId());
				in.put("creatTime", video.getCreatTime());
				in.put("name", video.getName());
				in.put("introduce", video.getIntroduce());
				in.put("avatar", video.getAvatar());
				in.put("address", video.getAddress());
				if (video.getAppShowFalg()==0){
					continue;
				}
					//                              key                           数据    时间分数
				redisTemplate.opsForZSet().add(AppConstants.APP_VIDEO_LIST,in.toJSONString(),time);
			}
			return a;
		}else {
			return a;
		}
	}

	/**
	 * 保存数据（插入或更新）
	 * @param appVideo
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppVideo appVideo) {
		//删除缓存数据
		//获取到对象的分值,根据分值来删除数据
		Date creatTime = appVideo.getCreatTime();
		long time = creatTime.getTime();
//		redisTemplate.opsForZSet().remove(AppConstants.APP_VIDEO_LIST, time);

		super.save(appVideo);
		// 保存上传附件
		FileUploadUtils.saveFileUpload(appVideo.getId(), "appVideo_file");
	}

	/**
	 * 设置该视频为首页视频
	 */
	public void upVideo(String id) throws CusException {
		AppVideo video = this.get(new AppVideo(id));
		if (video==null) {
			throw new CusException(401, "根据id找视频失败，请联系管理员");
		}
		JSONObject in = new JSONObject();
		in.put("videoId", video.getId());
		in.put("url", video.getUrl());
		in.put("userId", video.getUserId());
		in.put("count", video.getCount());
		in.put("share", video.getShare());
		in.put("appViewCounts", video.getAppViewCounts());
		in.put("accidentId", video.getAccidentId());
		in.put("creatTime", video.getCreatTime());
		in.put("name", video.getName());
		in.put("introduce", video.getIntroduce());
		in.put("avatar", video.getAvatar());
		in.put("address", video.getAddress());
		redisTemplate.opsForValue().set(AppConstants.APP_FIRST_VIDEO,in.toJSONString());
	}

	/**
	 * 更新状态
	 * @param appVideo
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppVideo appVideo) {
		super.updateStatus(appVideo);
	}

	/**
	 * 删除数据
	 * @param appVideo
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppVideo appVideo) {
		super.delete(appVideo);
	}


	public AppVideo findVideoByAccidAndType(String id,String source) {
		return videoDao.findVideoByAccidAndType(id,source);
	}
}
