package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 拍卖上架
 * </p>
 *
 * @author yang
 */
@TableName("js_sys_file_upload")
@Data
public class FileUploadEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("file_id")
    private String fileId;
    @TableField("file_name")
    private String fileName;
    @TableField("file_type")
    private String fileType;
    //主键ID(拍卖车辆ID)
    @TableField("biz_key")
    private String bizKey;
    //状态（0正常 1删除 2停用）
    private Character status;

    @TableField("create_date")
    private Date createDate;

}
