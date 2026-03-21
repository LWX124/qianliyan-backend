package com.cheji.b.modular.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 商户评论树表
 * </p>
 *
 * @author Ashes
 * @since 2019-09-26
 */
@TableName("app_merchants_comments_tree")
public class MerchantsCommentsTree extends Model<MerchantsCommentsTree> {

    private static final long serialVersionUID = 1L;

    /**
     * 节点编码
     */
    @TableId("comments_code")
    private String commentsCode;
    /**
     * 节点名称
     */
    @TableField("comments_name")
    private String commentsName;
    /**
     * 节点上级编码
     */
    @TableField("parent_code")
    private String parentCode;
    /**
     * 节点所有上级编码
     */
    @TableField("parent_codes")
    private String parentCodes;
    /**
     * 当前层级排序号
     */
    @TableField("tree_sort")
    private BigDecimal treeSort;
    /**
     * 节点完整排序号
     */
    @TableField("tree_sorts")
    private String treeSorts;
    /**
     * 是否末级
     */
    @TableField("tree_leaf")
    private String treeLeaf;
    /**
     * 节点层次级别
     */
    @TableField("tree_level")
    private BigDecimal treeLevel;
    /**
     * 节点的全名称
     */
    @TableField("tree_names")
    private String treeNames;
    /**
     * 关联用户id
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 关联商户id
     */
    @TableField("user_b_id")
    private Integer userBId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论状态是否展示
     */
    private String state;
    /**
     * 服务分
     */
    @TableField("service_score")
    private Integer serviceScore;
    /**
     * 效率分
     */
    @TableField("efficiensy_score")
    private Integer efficiensyScore;
    /**
     * 评论时标签
     */
    private String lable;
    @TableField("create_date")
    private Date createDate;
    @TableField("update_date")
    private Date updateDate;


    public String getCommentsCode() {
        return commentsCode;
    }

    public void setCommentsCode(String commentsCode) {
        this.commentsCode = commentsCode;
    }

    public String getCommentsName() {
        return commentsName;
    }

    public void setCommentsName(String commentsName) {
        this.commentsName = commentsName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getParentCodes() {
        return parentCodes;
    }

    public void setParentCodes(String parentCodes) {
        this.parentCodes = parentCodes;
    }

    public BigDecimal getTreeSort() {
        return treeSort;
    }

    public void setTreeSort(BigDecimal treeSort) {
        this.treeSort = treeSort;
    }

    public String getTreeSorts() {
        return treeSorts;
    }

    public void setTreeSorts(String treeSorts) {
        this.treeSorts = treeSorts;
    }

    public String getTreeLeaf() {
        return treeLeaf;
    }

    public void setTreeLeaf(String treeLeaf) {
        this.treeLeaf = treeLeaf;
    }

    public BigDecimal getTreeLevel() {
        return treeLevel;
    }

    public void setTreeLevel(BigDecimal treeLevel) {
        this.treeLevel = treeLevel;
    }

    public String getTreeNames() {
        return treeNames;
    }

    public void setTreeNames(String treeNames) {
        this.treeNames = treeNames;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserBId() {
        return userBId;
    }

    public void setUserBId(Integer userBId) {
        this.userBId = userBId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(Integer serviceScore) {
        this.serviceScore = serviceScore;
    }

    public Integer getEfficiensyScore() {
        return efficiensyScore;
    }

    public void setEfficiensyScore(Integer efficiensyScore) {
        this.efficiensyScore = efficiensyScore;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    protected Serializable pkVal() {
        return this.commentsCode;
    }

    @Override
    public String toString() {
        return "MerchantsCommentsTree{" +
                "commentsCode='" + commentsCode + '\'' +
                ", commentsName='" + commentsName + '\'' +
                ", parentCode='" + parentCode + '\'' +
                ", parentCodes='" + parentCodes + '\'' +
                ", treeSort=" + treeSort +
                ", treeSorts='" + treeSorts + '\'' +
                ", treeLeaf='" + treeLeaf + '\'' +
                ", treeLevel=" + treeLevel +
                ", treeNames='" + treeNames + '\'' +
                ", userId=" + userId +
                ", userBId=" + userBId +
                ", content='" + content + '\'' +
                ", state='" + state + '\'' +
                ", serviceScore=" + serviceScore +
                ", efficiensyScore=" + efficiensyScore +
                ", lable='" + lable + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
