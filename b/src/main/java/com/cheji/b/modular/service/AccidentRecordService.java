package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.AccidentRecord;
import com.cheji.b.modular.domain.BizAccident;
import com.cheji.b.modular.dto.ListDetailsDto;
import com.cheji.b.modular.mapper.AccidentRecordMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * <p>
 * app上报事故信息表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-09-26
 */
@Service
public class AccidentRecordService extends ServiceImpl<AccidentRecordMapper, AccidentRecord> implements IService<AccidentRecord> {
    @Resource
    private AccidentRecordMapper accidentRecordMapper;

    @Resource
    private BizAccidentService bizAccidentService;

    public ListDetailsDto findAccidentDetails(String accid,String source) {
        if (source.equals("1")){
            return accidentRecordMapper.findAccidentDetails(accid);
        }else {
            ListDetailsDto bizAccident = bizAccidentService.findBizAccident(accid);
            String address = bizAccident.getAddress();
            String replace = address.replace("& #40;", "(").replace("& #41;", ")");
            bizAccident.setAddress(replace);
            String name = bizAccident.getName();
            if (StringUtils.isNotEmpty(name)){
                int i  = name.indexOf("%");
                if (i!=-1){
                    try {
                        name = java.net.URLDecoder.decode(name, "UTF-8");
                        bizAccident.setName(name);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            return bizAccident;
        }
    }

    public void updateRealness(String accid, Integer realness,String source) {
        if (source.equals("1")){
            AccidentRecord accidentRecord = selectById(accid);
            accidentRecord.setRealness(realness);
            updateById(accidentRecord);
        }else {
            BizAccident bizAccident = bizAccidentService.selectById(accid);
            if (realness==0){
                bizAccident.setRealness(realness);
            }else {
                bizAccident.setRealness(1);
            }
            bizAccidentService.updateById(bizAccident);
        }
    }

    public ListDetailsDto findAccidentDetail(Integer accid) {
        return accidentRecordMapper.findAccidentDetail(accid);
    }
}
