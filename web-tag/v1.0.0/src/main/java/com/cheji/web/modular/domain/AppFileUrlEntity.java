package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 * <p>
 * 拍卖上架
 * </p>
 *
 * @author yang
 */
@TableName("app_file_url")
@Data
public class AppFileUrlEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("file_id")
    private Long fileId;

    private String url;

}
