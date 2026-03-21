package com.stylefeng.guns.modular.system.controller;

import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public abstract class BaseController {

	private static ImmutableMap<String, String> errorCodeMap ;
//	private static Map<Integer,String> ERROR_CODE_MAP = new HashMap<Integer,String>();
//	static {
//		ERROR_CODE_MAP.put(50010,"登陆状态获取失败");
//		ERROR_CODE_MAP.put(50020,"微信状态鉴权失败");
//		ERROR_CODE_MAP.put(50021,"获取userInfo失败");
//		errorCodeMap = ImmutableMap.copyOf(ERROR_CODE_MAP);
//	}

	/**
	 * 接口数据返回
	 * @param errorCode
	 * @param data
	 * @return
	 */
	protected Map<String,Object> rtnParam(Integer errorCode,Object data) {
		//正常的业务逻辑
		return ImmutableMap.of("errorCode", errorCode,"data", (data == null)? new Object() : data);
	}


}
