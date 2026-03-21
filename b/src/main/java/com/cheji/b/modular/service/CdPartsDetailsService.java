package com.cheji.b.modular.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.*;
import com.cheji.b.modular.mapper.CdPartsDetailsMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.b.modular.mapper.CdRepairOrderMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 配件明细 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2022-01-19
 */
@Service
public class CdPartsDetailsService extends ServiceImpl<CdPartsDetailsMapper, CdPartsDetailsEntity> implements IService<CdPartsDetailsEntity> {

    @Resource
    private CdCarService cdCarService;

    @Resource
    private CdPartsDetailsMapper cdPartsDetailsMapper;

    @Resource
    private CdRepairOrderService cdRepairOrderService;

    @Resource
    private CdRepairOrderMapper cdRepairOrderMapper;

    @Resource
    private CdImgService cdImgService;

    @Resource
    private CdIndentService cdIndentService;

    @Resource
    private CarBrandService carBrandService;

    public JSONObject savePartsDetails(JSONObject in) {
        JSONObject result = new JSONObject();

        String indentId = in.getString("indentId");
        Integer type = in.getInteger("type");
        BigDecimal price = in.getBigDecimal("price");
        Integer partsId = in.getInteger("partsId");

        //保存数据
        if (StringUtils.isEmpty(indentId) || type == null || price == null || partsId == null) {
            result.put("code", 200);
            result.put("msg", "参数不能为空");
            return result;
        }

        //新增数据
        CdPartsDetailsEntity parts = new CdPartsDetailsEntity();
        parts.setIndentId(Integer.parseInt(indentId));
        parts.setPrice(price);
        parts.setType(type);
        parts.setPartsId(partsId);
        parts.setCreateTime(new Date());
        this.insert(parts);

        CdIndentEntity cdIndentEntity = cdIndentService.selectById(indentId);
        if (cdIndentEntity.getState() == 1)
            cdIndentEntity.setState(2);
        cdIndentService.updateById(cdIndentEntity);

        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }

    public JSONObject offerDetails(Integer indentId, Integer type) {
        JSONObject result = new JSONObject();
        //根据id查询 列表  配件类型 1.换配件，2.拆装，3.修复，4.喷漆
        //查询到总数
        JSONObject object = new JSONObject();
        EntityWrapper<CdPartsDetailsEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("indent_id", indentId);
        List<CdPartsDetailsEntity> cdPartsEntities = this.selectList(wrapper);
        BigDecimal allprice = BigDecimal.ZERO;
        BigDecimal peijianPrice = BigDecimal.ZERO;
        BigDecimal caizhuangPrice = BigDecimal.ZERO;
        BigDecimal xiufuPrice = BigDecimal.ZERO;
        BigDecimal penqiPrice = BigDecimal.ZERO;
        int peijianNumber = 0;
        int caizhuangNumber = 0;
        int xiufuNumber = 0;
        int penqiNumber = 0;
        String plan = "暂无";
        JSONArray jsonArray = new JSONArray();
        for (CdPartsDetailsEntity cdPartsEntity : cdPartsEntities) {
            BigDecimal price = cdPartsEntity.getPrice();
            allprice = allprice.add(price);
            Integer type1 = cdPartsEntity.getType();
            //换配件数量和价格 1.换配件，2.拆装，3.修复，4.喷漆
            if (type1 == 1) {
                peijianNumber = peijianNumber + 1;
                peijianPrice = peijianPrice.add(price);
                plan = "配件";
            } else if (type1 == 2) {
                caizhuangNumber = caizhuangNumber + 1;
                caizhuangPrice = caizhuangPrice.add(price);
                plan = "拆装";
            } else if (type1 == 3) {
                xiufuNumber = xiufuNumber + 1;
                xiufuPrice = xiufuPrice.add(price);
                plan = "修复";
            } else if (type1 == 4) {
                penqiNumber = penqiNumber + 1;
                penqiPrice = penqiPrice.add(price);
                plan = "喷漆";
            }

            //添加到json  1.全部，2.配件，3.拆装，4.修复，5.喷漆
            JSONObject parts = new JSONObject();
            Integer partsId = cdPartsEntity.getPartsId();
            String name = cdCarService.selectById(partsId).getName();
            parts.put("id", cdPartsEntity.getId());
            parts.put("name", name);
            parts.put("number", 1);
            parts.put("plan", plan);
            parts.put("paice", price);
            parts.put("state", cdPartsEntity.getState());
            if (type == 1) {
                //全部
                jsonArray.add(parts);
            } else if (type == 2 && type1 == 1) { //2.配件
                jsonArray.add(parts);
            } else if (type == 3 && type1 == 2) {//拆装
                jsonArray.add(parts);
            } else if (type == 4 && type1 == 3) {
                jsonArray.add(parts);
            } else if (type == 5 && type1 == 4) {
                jsonArray.add(parts);
            }
        }
        //总报价
        object.put("allPrice", allprice);
        int size = cdPartsEntities.size();
        object.put("baojiaNumber", size + "/项");
        object.put("peijianNumber", peijianNumber + "/件");
        object.put("peijianPrice", peijianPrice);
        object.put("caizhuangNumber", caizhuangNumber + "/样");
        object.put("caizhuangPrice", caizhuangPrice);
        object.put("xiufuNumber", xiufuNumber + "/样");
        object.put("xiufuPrice", xiufuPrice);
        object.put("penqiNumber", penqiNumber + "/面");
        object.put("penqiPrice", penqiPrice);

        JSONObject is = new JSONObject();
        is.put("data", object);

        CdIndentEntity cdIndentEntity = cdIndentService.selectById(indentId);
        BigDecimal repairQuotation = cdIndentEntity.getRepairQuotation();
        if (repairQuotation == null) {
            repairQuotation = BigDecimal.ZERO;
            if (allprice.compareTo(repairQuotation) != 0) {
                cdIndentEntity.setRepairQuotation(allprice);
            }
        }
        BigDecimal accessoriesAmount = cdIndentEntity.getAccessoriesAmount();
        if (accessoriesAmount == null) {
            accessoriesAmount = BigDecimal.ZERO;
            if (peijianPrice.compareTo(accessoriesAmount) != 0) {
                cdIndentEntity.setAccessoriesAmount(peijianPrice);
            }
        }
        cdIndentService.updateById(cdIndentEntity);

        //查询详情
        is.put("list", jsonArray);


        result.put("code", 200);
        result.put("data", is);
        return result;
    }

    public JSONObject deleParts(JSONObject in) {
        JSONObject result = new JSONObject();
        Integer id = in.getInteger("id");
        if (id == null) {
            result.put("code", 407);
            result.put("msg", "id不能为空");
            return result;
        }

        //删除配件订单
        deleteById(id);

        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }

    public JSONObject addProcurement(JSONObject in) {
        JSONObject result = new JSONObject();
        Integer indentId = in.getInteger("indentId");
        if (indentId == null) {
            result.put("code", 407);
            result.put("msg", "id不能为空");
            return result;
        }
        //查询到所有配件列表
        List<CdPartsDetailsEntity> list = cdPartsDetailsMapper.addProcurementAndType(indentId, 1);
        JSONArray jsonArray = new JSONArray();
        BigDecimal allprice = new BigDecimal("0");
        for (CdPartsDetailsEntity partsDetails : list) {
            JSONObject object = new JSONObject();
            object.put("id", partsDetails.getId());
            object.put("name", cdCarService.selectById(partsDetails.getPartsId()).getName());
            object.put("number", 1);
            object.put("plan", "更换");
            object.put("price", partsDetails.getPrice());
            jsonArray.add(object);
            //查询金额总数
            BigDecimal price = partsDetails.getPrice();
            allprice = allprice.add(price);
        }
        JSONObject object = new JSONObject();
        object.put("list", jsonArray);
        object.put("number", list.size());
        object.put("price", allprice);

        result.put("code", 200);
        result.put("data", object);
        return result;
    }


    public JSONObject saveWorkOrder(JSONObject in) {

        JSONObject result = new JSONObject();
        Integer indentId = in.getInteger("indentId");
        String sheetMetal = in.getString("sheetMetal");
        String workPrice = in.getString("workPrice");
        String workTime = in.getString("workTime");
        Integer picking = in.getInteger("picking");
        Integer replenishment = in.getInteger("replenishment");
        Integer sales = in.getInteger("sales");
        String changeJob = in.getString("changeJob");
        Integer type = in.getInteger("type");

        //保存数据
        if (indentId == null || type == null) {
            result.put("code", 407);
            result.put("msg", "参数不能为空");
            return result;
        }

        JSONObject json = JSONObject.parseObject(String.valueOf(in));
        JSONArray imgList = (JSONArray) json.get("imgList");
        if (imgList != null) {
            //保存图片
            List<String> lossPhotos = JSONArray.parseArray(imgList.toString(), String.class);

            Integer imgtype = null;
            if (type == 1) {
                imgtype = 4;
            } else if (type == 2) {
                imgtype = 5;
            } else if (type == 3) {
                imgtype = 6;
            }

            //先删除
            EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("indent_id", indentId)
                    .eq("type", imgtype);

            cdImgService.delete(wrapper);

            for (int i = 0; i < lossPhotos.size(); i++) {
                //保存图片
                CdImgEntity cdImgEntity = new CdImgEntity();
                cdImgEntity.setType(imgtype);
                cdImgEntity.setImg(lossPhotos.get(i));
                cdImgEntity.setIndex(i + 1);
                cdImgEntity.setIndentId(indentId);
                cdImgService.insert(cdImgEntity);
            }
        }

        CdRepairOrderEntity cdRepairOrderEntity = cdRepairOrderMapper.selectIndentByType(indentId, type);
        if (cdRepairOrderEntity != null) {
            //修改数据
            if (StringUtils.isNotEmpty(sheetMetal))
                cdRepairOrderEntity.setSheetMetal(sheetMetal);

            if (StringUtils.isNotEmpty(workPrice))
                cdRepairOrderEntity.setWorkPrice(workPrice);

            if (StringUtils.isNotEmpty(workTime))
                cdRepairOrderEntity.setWorkTime(workTime);

            if (StringUtils.isNotEmpty(changeJob))
                cdRepairOrderEntity.setChangeJob(changeJob);

            if (picking != null)
                cdRepairOrderEntity.setPicking(picking);

            if (replenishment != null)
                cdRepairOrderEntity.setReplenishment(replenishment);

            if (sales != null)
                cdRepairOrderEntity.setSales(sales);

            cdRepairOrderService.updateById(cdRepairOrderEntity);
            result.put("code", 200);
            result.put("msg", "成功");
            return result;
        }
        if(!"0".equals(workPrice)){
            if (StringUtils.isEmpty(sheetMetal) || StringUtils.isEmpty(workPrice) || StringUtils.isEmpty(workTime)) {
                result.put("code", 407);
                result.put("msg", "参数不能为空");
                return result;
            }
        }

        CdRepairOrderEntity cdRepairOrder = new CdRepairOrderEntity();
        cdRepairOrder.setIndentId(indentId);
        cdRepairOrder.setSheetMetal(sheetMetal);
        cdRepairOrder.setWorkPrice(workPrice);
        cdRepairOrder.setChangeJob(changeJob);
        cdRepairOrder.setWorkTime(workTime);
        cdRepairOrder.setReplenishment(replenishment);
        cdRepairOrder.setSales(sales);
        cdRepairOrder.setType(type);
        cdRepairOrder.setPicking(picking);
        cdRepairOrder.setCreateTime(new Date());
        cdRepairOrderService.insert(cdRepairOrder);

        CdIndentEntity byId = cdIndentService.selectById(indentId);
        if (byId.getState() == 3) {
            byId.setState(4);
            cdIndentService.updateById(byId);
        }

        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }

    public JSONObject workOrDetails(JSONObject in) {
        //查询订单数据
        JSONObject result = new JSONObject();
        Integer indentId = in.getInteger("indentId");
        Integer type = in.getInteger("type");
        if (indentId == null || type == null) {
            result.put("code", 407);
            result.put("msg", "参数不能为空");
            return result;
        }


        //查询到详情
        CdRepairOrderEntity cdRepairOrder = cdRepairOrderService.selectIndentByType(indentId, type);
        if (cdRepairOrder == null) {
            result.put("code", 200);
            result.put("msg", "暂无工单");
            return result;
        }
        JSONObject object = new JSONObject();
        CdIndentEntity cdIndentEntity = cdIndentService.selectById(indentId);
        object.put("plate", cdIndentEntity.getPlate());
        object.put("brand", carBrandService.selectById(cdIndentEntity.getBrandId()).getName());
        object.put("plateImg", cdIndentEntity.getPlateImg());
        object.put("frameImg", cdIndentEntity.getFrameImg());
        object.put("sheetMetal", cdRepairOrder.getSheetMetal());
        object.put("workPrice", cdRepairOrder.getWorkPrice());
        object.put("workTime", cdRepairOrder.getWorkTime());
        object.put("picking", cdRepairOrder.getPicking());
        object.put("replenishment", cdRepairOrder.getReplenishment());
        object.put("sales", cdRepairOrder.getSales());
        object.put("changeJob", cdRepairOrder.getChangeJob());
        //查询图片
        EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("indent_id", indentId)
                .eq("type", type);
        List<CdImgEntity> cdImgEntities = cdImgService.selectList(wrapper);
        object.put("imgList", cdImgEntities);
        if (!cdImgEntities.isEmpty()) {
            List<String> strings = new ArrayList<>();
            for (CdImgEntity cdImgEntity : cdImgEntities) {
                String img = cdImgEntity.getImg();
                if (StringUtils.isNotEmpty(img)){
                    strings.add(img);
                }
            }
            object.put("imgList", strings);
        }

        result.put("code", 200);
        result.put("data", object);
        return result;
    }

    public JSONObject buyAcc(JSONObject in) {
        JSONObject result = new JSONObject();
        JSONObject json = JSONObject.parseObject(String.valueOf(in));
        JSONArray lossPhotosList = (JSONArray) json.get("idList");
        if (lossPhotosList == null) {
            result.put("code", 407);
            result.put("msg", "参数");
            return result;
        }
        List<String> idList = JSONArray.parseArray(lossPhotosList.toString(), String.class);
        //
        Integer indentId = null;
        for (String s : idList) {
            CdPartsDetailsEntity cdPartsDetailsEntity = this.selectById(s);
            indentId = cdPartsDetailsEntity.getIndentId();
            cdPartsDetailsEntity.setState(1);
            updateById(cdPartsDetailsEntity);
        }
        CdIndentEntity cdIndentEntity = cdIndentService.selectById(indentId);
        if (cdIndentEntity.getState() == 2) {
            cdIndentEntity.setState(3);
            cdIndentService.updateById(cdIndentEntity);
        }

        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }

    public JSONObject findPriceDetails(Integer id) {
        JSONObject result = new JSONObject();
        if (id == null) {
            result.put("code", 407);
            result.put("msg", "参数不能为空");
            return result;
        }
        CdCarEntity cdCarEntity = cdCarService.selectById(id);
        if (cdCarEntity == null) {
            result.put("code", 407);
            result.put("msg", "id有误");
            return result;
        }
        JSONObject object = new JSONObject();
        object.put("id", cdCarEntity.getId());
        object.put("name", cdCarEntity.getName());
        object.put("commodityPrice", cdCarEntity.getCommodityPrice());
        object.put("litFixPrice", cdCarEntity.getLitFixPrice());
        object.put("minFixPrice", cdCarEntity.getMinFixPrice());
        object.put("bigFixPrice", cdCarEntity.getBigFixPrice());
        object.put("mountingPriceOne", cdCarEntity.getMountingPriceOne());
        object.put("mountingPriceAll", cdCarEntity.getMountingPriceAll());
        object.put("halfSprayPrice", cdCarEntity.getHalfSprayPrice());
        object.put("allSprayPrice", cdCarEntity.getAllSprayPrice());

        result.put("code", 200);
        result.put("data", object);
        return result;
    }
}
