package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.ImgEntity;
import com.cheji.web.modular.mapper.ImgMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 图片表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-08-23
 */
@Service
public class ImgService extends ServiceImpl<ImgMapper, ImgEntity> implements IService<ImgEntity> {
    @Resource
    private ImgMapper ImgMapper;

    public List<ImgEntity> getImgEntityByMerchentsAndType(String commentsCode) {
        return ImgMapper.getImgEntityByMerchantsAndType(commentsCode);

    }

    //保存订单图片
    public void save(String[] ImgEntity, Long id) {
        int i = 0;
        if (ImgEntity!=null&&ImgEntity.length!=0){
            for (String s : ImgEntity) {
                ImgEntity imgEntity = new ImgEntity();
                imgEntity.setType(2);
                imgEntity.setUrl(s);
                i++;
                imgEntity.setIndex(i);
                imgEntity.setKeyId(String.valueOf(id));
                imgEntity.setCreatTime(new Date());
                imgEntity.setUpdateTime(new Date());
                insert(imgEntity);
            }
        }
    }

    //保存商户评论图片
    public void saveComments(String [] img,Long id){
        int i = 0;
        if (img!=null&&img.length!=0){
            for (String s : img) {
                ImgEntity imgEntity = new ImgEntity();
                imgEntity.setType(1);
                imgEntity.setUrl(s);
                i++;
                imgEntity.setIndex(i);
                imgEntity.setKeyId(String.valueOf(id));
                imgEntity.setCreatTime(new Date());
                imgEntity.setUpdateTime(new Date());
                insert(imgEntity);

            }
        }
    }


    //查询订单图片
    public List<ImgEntity> findImgByIndentid(String IndentId){
        return ImgMapper.findImgByndentId(IndentId);
    }

    public List<String> findImgByIndentidndIndex(String indentid) {
        return ImgMapper.findImgByIndentidndIndex(indentid);
    }
}
