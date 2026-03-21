package com.cheji.b.modular.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.CdCarEntity;
import com.cheji.b.modular.dto.CdCarDto;
import com.cheji.b.modular.mapper.CdCarMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2022-01-17
 */
@Service
public class CdCarService extends ServiceImpl<CdCarMapper, CdCarEntity> implements IService<CdCarEntity> {

    @Resource
    private CdCarMapper cdCarMapper;

    public List<CdCarDto> findCarVehicle() {
        //查询第一级数据
        List<CdCarDto> list = cdCarMapper.findBylevelAndPar(1, null);
        for (CdCarDto cdCarEntity : list) {
            //遍历查询
            Integer id = cdCarEntity.getId();
            //查询到第一级的id
            List<CdCarDto> level2 = cdCarMapper.findBylevelAndPar(2, id);
            cdCarEntity.setCarEntities(level2);
            for (CdCarDto carEntity : level2) {
                Integer id2 = carEntity.getId();
                List<CdCarDto> level3 = cdCarMapper.findBylevelAndPar(3, id2);
                carEntity.setCarEntities(level3);
            }
        }
        return list;
    }


    public JSONObject addProduct(JSONObject in) {
        JSONObject result = new JSONObject();
        CdCarEntity cdCarEntity = new CdCarEntity();
        String name = in.getString("name");
        if (StringUtils.isEmpty(name)) {
            result.put("code", 407);
            result.put("msg", "配件名称为空");
            return result;
        }
        Integer parentId = in.getInteger("parentId");
        cdCarEntity.setName(name);
        if (parentId != null) {
            cdCarEntity.setParentId(parentId);
            CdCarEntity parCdCar = selectById(parentId);
            cdCarEntity.setHierarchy(parCdCar.getHierarchy() + 1);
        } else {
            cdCarEntity.setParentId(null);
            cdCarEntity.setHierarchy(1);
        }
        this.insert(cdCarEntity);

        result.put("code", 200);
        result.put("msg", "保存成功");
        return result;
    }


    public JSONObject saveAccess(JSONObject in) {
        JSONObject result = new JSONObject();
        Integer id = in.getInteger("id");
        BigDecimal commodityPrice = in.getBigDecimal("commodityPrice");
        String name = in.getString("name");
        BigDecimal litFixPrice = in.getBigDecimal("litFixPrice");
        BigDecimal minFixPrice = in.getBigDecimal("minFixPrice");
        BigDecimal bigFixPrice = in.getBigDecimal("bigFixPrice");
        BigDecimal mountingPriceOne = in.getBigDecimal("mountingPriceOne");
        BigDecimal mountingPriceAll = in.getBigDecimal("mountingPriceAll");
        BigDecimal halfSprayPrice = in.getBigDecimal("halfSprayPrice");
        BigDecimal allSprayPrice = in.getBigDecimal("allSprayPrice");

        if (commodityPrice == null || litFixPrice == null || minFixPrice == null || bigFixPrice == null
                || mountingPriceOne == null || mountingPriceAll == null || halfSprayPrice == null || allSprayPrice == null) {
            result.put("code", 407);
            result.put("msg", "参数不能为空");
            return result;
        }
        //保存数据
        CdCarEntity cdCar = selectById(id);
        cdCar.setName(name);
        cdCar.setCommodityPrice(commodityPrice);
        cdCar.setLitFixPrice(litFixPrice);
        cdCar.setMinFixPrice(minFixPrice);
        cdCar.setBigFixPrice(bigFixPrice);
        cdCar.setMountingPriceOne(mountingPriceOne);
        cdCar.setMountingPriceAll(mountingPriceAll);
        cdCar.setHalfSprayPrice(halfSprayPrice);
        cdCar.setAllSprayPrice(allSprayPrice);
        updateById(cdCar);

        result.put("code", 200);
        result.put("msg", "保存成功");
        return result;
    }


}
