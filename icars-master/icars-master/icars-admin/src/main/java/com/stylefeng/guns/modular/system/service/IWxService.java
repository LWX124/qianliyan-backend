package com.stylefeng.guns.modular.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.stylefeng.guns.core.datascope.DataScope;
import com.stylefeng.guns.modular.system.model.Accident;
import com.stylefeng.guns.modular.system.vo.AccidentVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 腾讯小程序API接口
 *
 * @author kosan
 * @date 2017-04-27 17:00
 */
public interface IWxService {

    String getAccessToken();

    byte[] getMiniqrQr(String sceneStr);

}
