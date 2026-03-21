package com.stylefeng.guns.modular.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.BizClaim;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 理赔单 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-27
 */
public interface IBizClaimService extends IService<BizClaim> {

    List<Map<String, Object>> selectListForPage(@Param("page") Page<BizClaim> page, @Param("bizClaim") BizClaim bizClaim, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<Map<String, Object>> selectListForPageByOpendId(@Param("page")Page<BizClaim> page,@Param("openId")  String openId,@Param("type")Integer type);

    void addClaimImage(@Param("id") Integer id, @Param("claimImg") String claimImg) throws IOException;

    List<String> uploadFile(List<MultipartFile> files) throws IOException;
}
