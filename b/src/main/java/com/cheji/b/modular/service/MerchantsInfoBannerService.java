package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.MerchantsInfoBannerEntity;
import com.cheji.b.modular.dto.ImgSaveDto;
import com.cheji.b.modular.mapper.MerchantsInfoBannerMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商户详情banner图 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-09-28
 */
@Service
public class MerchantsInfoBannerService extends ServiceImpl<MerchantsInfoBannerMapper, MerchantsInfoBannerEntity> implements IService<MerchantsInfoBannerEntity> {

    @Resource
    private MerchantsInfoBannerMapper merchantsInfoBannerMapper;

    //保存图片
    public void addImg(ImgSaveDto imgSaveDto, Integer userBId) {
        List<MerchantsInfoBannerEntity> otherImg = imgSaveDto.getOtherImg();
        for (MerchantsInfoBannerEntity infoBannerEntity : otherImg) {
            EntityWrapper<MerchantsInfoBannerEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("user_b_id",userBId)
                    .eq("`index`",infoBannerEntity.getIndex());
            List<MerchantsInfoBannerEntity> bannerEntityList = selectList(wrapper);
            if (!bannerEntityList.isEmpty()){
                for (MerchantsInfoBannerEntity entity : bannerEntityList) {
                    entity.setUrl(infoBannerEntity.getUrl());
                    updateById(entity);
                }
                continue;
            }
            MerchantsInfoBannerEntity bannerEntity = new MerchantsInfoBannerEntity();
            bannerEntity.setIndex(infoBannerEntity.getIndex());
            bannerEntity.setUserBId(userBId);
            bannerEntity.setCreatTime(new Date());
            bannerEntity.setUpdateTime(new Date());
            bannerEntity.setUrl(infoBannerEntity.getUrl());
            insert(bannerEntity);
        }
        /*MerchantsInfoBannerEntity masterImg = imgSaveDto.getMasterImg();
        if (masterImg!=null){
            Long id = masterImg.getId();
            if (id==null){
                //新增
                MerchantsInfoBannerEntity merchantsInfoBannerEntity = new MerchantsInfoBannerEntity();
                merchantsInfoBannerEntity.setUserBId(userBId);
                merchantsInfoBannerEntity.setUrl(masterImg.getUrl());
                merchantsInfoBannerEntity.setIndex(1);
                merchantsInfoBannerEntity.setCreatTime(new Date());
                merchantsInfoBannerEntity.setUpdateTime(new Date());
                insert(merchantsInfoBannerEntity);
            }else {
                MerchantsInfoBannerEntity merchantsInfoBanner = selectById(id);
                merchantsInfoBanner.setUrl(masterImg.getUrl());
                merchantsInfoBanner.setUpdateTime(new Date());
                updateById(merchantsInfoBanner);
            }
        }
        for (MerchantsInfoBannerEntity infoBanner : imgSaveDto.getOhterImg()) {
            infoBanner.setUserBId(userBId);
            infoBanner.setUrl(infoBanner.getUrl());
            infoBanner.setIndex(infoBanner.getIndex());
            infoBanner.setCreatTime(new Date());
            infoBanner.setUpdateTime(new Date());
            insert(infoBanner);
        }*/
    }

    public String findUpmerImg(String member) {
        return merchantsInfoBannerMapper.findUpMerImg(member);
    }
}
