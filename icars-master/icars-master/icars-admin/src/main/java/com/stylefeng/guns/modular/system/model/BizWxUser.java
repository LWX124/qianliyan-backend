package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 微信用户信息表
 * </p>
 *
 * @author kosans
 * @since 2018-07-24
 */
@TableName("biz_wx_user")
public class BizWxUser extends Model<BizWxUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * openid
     */
    private String openid;

    private String unionId;

    @TableField("third_openid")
    private String thirdOpenId;
    /**
     * 用户来源0：公众号，1：小程序
     */
    private Integer type;
    /**
     * 支付宝账户
     */
    @TableField("alipay_account")
    private String alipayAccount;

    /**
     * 手机号
     */
    private String phone;
    /**
     * 推广员工账号
     */
    private String extensionAccount;
    /**
     * 是否为黑名单用户
     */
    private Integer blackList;
    /**
     * 微信头像链接
     */
    private String headImg;
    /**
     * 微信昵称
     */
    private String wxname;
    /**
     * 绑定的4S门店登陆账号
     */
    private String bindAccount;
    /**
     * 上报时间
     */
    private Date createTime;
    /**
     * 保留字段
     */
    private Integer version;

    /**
     * 分享发起次数（乐观计数）
     */
    @TableField("share_count")
    private Integer shareCount;

    /**
     * 分享被他人打开次数（精确计数）
     */
    @TableField("share_open_count")
    private Integer shareOpenCount;

    /**
     * 用户类型  0 -> 普通用户  1 -> 内部员工
     */
    @TableField(exist = false)
    private Integer userType;

    /**
     * 内部员工账号
     */
    @TableField(exist = false)
    private String account;

    /**
     * 银行卡号
     */
    @TableField(exist = false)
    private String bankcard;
    /**
     * 开户银行
     */
    @TableField(exist = false)
    private String bankName;
    /**
     * 开户支行名称
     */
    @TableField(exist = false)
    private String bankSecondName;
    /**
     * 身份证
     */
    @TableField(exist = false)
    private String idcard;

    /**
     * 银行卡户名
     */
    @TableField(exist = false)
    private String bankUserName;

    @Override
    public String toString() {
        return "BizWxUser{" +
                "id=" + id +
                ", openid='" + openid + '\'' +
                ", unionId='" + unionId + '\'' +
                ", thirdOpenId='" + thirdOpenId + '\'' +
                ", type=" + type +
                ", alipayAccount='" + alipayAccount + '\'' +
                ", phone='" + phone + '\'' +
                ", extensionAccount='" + extensionAccount + '\'' +
                ", blackList=" + blackList +
                ", headImg='" + headImg + '\'' +
                ", wxname='" + wxname + '\'' +
                ", bindAccount='" + bindAccount + '\'' +
                ", createTime=" + createTime +
                ", version=" + version +
                ", shareCount=" + shareCount +
                ", shareOpenCount=" + shareOpenCount +
                ", userType=" + userType +
                ", account='" + account + '\'' +
                ", bankcard='" + bankcard + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankSecondName='" + bankSecondName + '\'' +
                ", idcard='" + idcard + '\'' +
                ", bankUserName='" + bankUserName + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getThirdOpenId() {
        return thirdOpenId;
    }

    public void setThirdOpenId(String thirdOpenId) {
        this.thirdOpenId = thirdOpenId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getExtensionAccount() {
        return extensionAccount;
    }

    public void setExtensionAccount(String extensionAccount) {
        this.extensionAccount = extensionAccount;
    }

    public Integer getBlackList() {
        return blackList;
    }

    public void setBlackList(Integer blackList) {
        this.blackList = blackList;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getWxname() {
        return wxname;
    }

    public void setWxname(String wxname) {
        this.wxname = wxname;
    }

    public String getBindAccount() {
        return bindAccount;
    }

    public void setBindAccount(String bindAccount) {
        this.bindAccount = bindAccount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBankcard() {
        return bankcard;
    }

    public void setBankcard(String bankcard) {
        this.bankcard = bankcard;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankSecondName() {
        return bankSecondName;
    }

    public void setBankSecondName(String bankSecondName) {
        this.bankSecondName = bankSecondName;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getBankUserName() {
        return bankUserName;
    }

    public void setBankUserName(String bankUserName) {
        this.bankUserName = bankUserName;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public Integer getShareOpenCount() {
        return shareOpenCount;
    }

    public void setShareOpenCount(Integer shareOpenCount) {
        this.shareOpenCount = shareOpenCount;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }
}

