package com.stylefeng.guns.modular.system.model;

import com.stylefeng.guns.modular.system.constant.*;
import com.stylefeng.guns.modular.system.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class ConstantEntity extends BaseController{
	private Logger log = LoggerFactory.getLogger(this.getClass());


	@RequestMapping(value = "/api/v1/wx/console/switch", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResponseEntity getList() {
		ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
		BussinessSwitch.businessSwitch = BussinessSwitch.businessSwitch == true ? false : true;
		apiResponseEntity.setErrorCode(0);
		apiResponseEntity.setData(BussinessSwitch.businessSwitch);
		return apiResponseEntity;
	}

	@RequestMapping(value = "/api/v1/wx/console/autoPush/switch", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResponseEntity autoPushSwitch() {
		ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
		BussinessSwitch.autoPushSwitch = BussinessSwitch.autoPushSwitch == true ? false : true;
		apiResponseEntity.setErrorCode(0);
		apiResponseEntity.setData(BussinessSwitch.autoPushSwitch);
		return apiResponseEntity;
	}

	@RequestMapping(value = "/api/v1/wx/console/autoPush/switch/get", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResponseEntity autoPushSwitchQuery() {
		ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
		apiResponseEntity.setErrorCode(0);
		apiResponseEntity.setData(BussinessSwitch.autoPushSwitch);
		return apiResponseEntity;
	}
}