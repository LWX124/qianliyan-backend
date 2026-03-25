package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("app_upload_record")
public class AppUploadRecordEntity extends Model<AppUploadRecordEntity> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Integer userId;
    private String fileUrl;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String thumbnailUrl;
    private Date createTime;

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
