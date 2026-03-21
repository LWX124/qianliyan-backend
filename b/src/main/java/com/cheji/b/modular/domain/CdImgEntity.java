package com.cheji.b.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 车店图片表

 * </p>
 *
 * @author Ashes
 * @since 2022-01-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("cd_img")
public class CdImgEntity extends Model<CdImgEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 图片地址
     */
    private String img;

    /**
     * 图片类型
     */
    private Integer type;

    /**
     * 图片排序
     */
    private Integer index;


    @TableField("indent_id")
    private Integer indentId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Integer getIndentId() {
        return indentId;
    }

    public void setIndentId(Integer indentId) {
        this.indentId = indentId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
