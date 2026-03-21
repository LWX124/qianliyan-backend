package com.stylefeng.guns.modular.system.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 事故推送记录
 * </p>
 *
 * @author kosans
 * @since 2018-07-26
 */
public class PushRecordVo{

    /**
     * 主键id
     */
    private Integer id;
    /**
     * 员工账号
     */
    private String account;
    /**
     * 事故主键id
     */
    private Integer accid;
    /**
     * 状态，0：未查看，1：已查看
     */
    private Integer status;
    /**
     * 推送时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date modifyTime;
    /**
     * 保留字段
     */
    private Integer version;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getAccid() {
        return accid;
    }

    public void setAccid(Integer accid) {
        this.accid = accid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "PushRecord{" +
        "id=" + id +
        ", account=" + account +
        ", accid=" + accid +
        ", status=" + status +
        ", createTime=" + createTime +
        ", modifyTime=" + modifyTime +
        ", version=" + version +
        "}";
    }
}
