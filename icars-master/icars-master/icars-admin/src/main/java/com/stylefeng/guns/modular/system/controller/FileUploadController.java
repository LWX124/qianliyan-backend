package com.stylefeng.guns.modular.system.controller;

import com.stylefeng.guns.modular.system.constant.ApiResponseEntity;
import com.stylefeng.guns.modular.system.service.IBizClaimService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * 微信用户认证相关
 * @author xiaoqiang
 *
 */
@RestController
public class FileUploadController extends BaseController{
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
    IBizClaimService bizClaimService;

	/**
	 * 上传文件
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/file/uploadfile", method = RequestMethod.POST, produces = "application/json")
	public ApiResponseEntity uploadFile(HttpServletRequest request) {
		ApiResponseEntity apiResponseEntity = new ApiResponseEntity();
		List<MultipartFile> files = ((MultipartHttpServletRequest) request)
				.getFiles("file");
		try {
			List<String> urls = bizClaimService.uploadFile(files);
			apiResponseEntity.setData(urls);
		} catch (IOException e) {
			log.error("上传文件异常",e);
			apiResponseEntity.setErrorCode(1002);
			apiResponseEntity.setErrorMsg("上传文件异常");
			return apiResponseEntity;
		}
		apiResponseEntity.setErrorCode(0);
		apiResponseEntity.setErrorMsg("");
		return apiResponseEntity;
	}

}