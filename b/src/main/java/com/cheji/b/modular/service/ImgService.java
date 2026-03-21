package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.ImgEntity;
import com.cheji.b.modular.mapper.ImgMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
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
 * @since 2019-09-25
 */
@Service
public class ImgService extends ServiceImpl<ImgMapper, ImgEntity> implements IService<ImgEntity> {
    @Resource
    private ImgMapper imgMapper;

    public List<ImgEntity> findCommentsImg(String commentsCode) {
        return imgMapper.findCOmmentsImg(commentsCode);
    }

    public List<String> findImgByIndentidndIndex(String indentid) {
        return imgMapper.findImgByIndentidndIndex(indentid);
    }

    public List<ImgEntity> findImgByIndentid(String indentId) {
        return imgMapper.findImgByndentId(indentId);
    }

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
}
