package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.b.modular.domain.LableDetailsReviewTreeEntity;
import com.cheji.b.modular.domain.LableEntity;
import com.cheji.b.modular.mapper.LableMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-09-29
 */
@Service
public class LableService extends ServiceImpl<LableMapper, LableEntity> implements IService<LableEntity> {
    @Resource
    private LableDetailsReviewTreeService lableDetailsReviewTreeService;


    public List<LableEntity> findLable(Integer userBId) {
        //获取到标签列表
        EntityWrapper<LableEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("type", 1)
                .eq("state",1)
                .orderBy("`index`");
        List<LableEntity> lable = selectList(wrapper);
        //查询到，所有服务信息和图片
        //查询到审核表数据的对应服务项目状态
        //根据商户id查询到对应的服务功能和状态
        //根据商户名字查询到对应商户lablecode，
        //根据商户lablecode找到对应的lable标签
        EntityWrapper<LableDetailsReviewTreeEntity> detaulsWrapper = new EntityWrapper<>();
        detaulsWrapper.eq("user_b_id", userBId)
                .eq("`show`", 1);
        List<LableDetailsReviewTreeEntity> list = lableDetailsReviewTreeService.selectList(detaulsWrapper);

        //获取到已经添加的数据
        //遍历每一个标签
        for (LableEntity lableEntity : lable) {    //1,2,3,4,5,6,7
            //判断是否为空
            if (list.isEmpty()){
                lableEntity.setState("-1");
            }else {
                //遍历明细数据
                for (LableDetailsReviewTreeEntity lableDetails : list) {   //123
                    //获取到已经添加的数据中的状态
                    Long id = lableEntity.getId();
                    Integer lableId = lableDetails.getLableId();
                    //如果id相等就是同一个数据
                    if (id.intValue()==lableId){
                        Integer state = lableDetails.getState();
                        lableEntity.setState(state.toString());
                        break;
                    }else {
                        lableEntity.setState("-1");
                    }
                }
            }
        }
        return lable;
    }
}
