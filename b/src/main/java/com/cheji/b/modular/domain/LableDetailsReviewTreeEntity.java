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
 * 标签和明细表
 * </p>
 *
 * @author Ashes
 * @since 2019-09-28
 */
@TableName("app_lable_details_review_tree")
public class LableDetailsReviewTreeEntity extends Model<LableDetailsReviewTreeEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 节点编码
     */
    @TableId("lable_code")
    private String lableCode;
    /**
     * 节点名称
     */
    @TableField("lable_name")
    private String lableName;
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
     * 树节点完整排序号
     */
    @TableField("tree_sorts")
    private String treeSorts;
    /**
     * 是否最末级
     */
    @TableField("tree_leaf")
    private String treeLeaf;
    /**
     * 节点层次级别
     */
    @TableField("tree_level")
    private BigDecimal treeLevel;
    /**
     * 节点得全名称
     */
    @TableField("tree_names")
    private String treeNames;
    /**
     * 状态
     */
    private String status;
    /**
     * 创建时间
     */
    @TableField("create_date")
    private Date createDate;
    /**
     * 修改时间
     */
    @TableField("update_date")
    private Date updateDate;
    /**
     * 标签id
     */
    @TableField("lable_id")
    private Integer lableId;
    /**
     * 标签详情id
     */
    @TableField("lable_details_id")
    private Integer lableDetailsId;
    /**
     * 返点比例
     */
    private BigDecimal rebates;
    /**
     * 商户id
     */
    @TableField("user_b_id")
    private Integer userBId;
    /**
     * 是否展示
     */
    private Integer show;
    /**
     * 审核未通过原因
     */
    private String reson;
    /**
     * 审核状态(0.未审核，1.审核通过，2.审核未通过)
     */
    private Integer state;

    private String remake;

    private Integer index;


    /**
     * 原价
     */
    @TableField("original_price")
    private BigDecimal originalPrice;
    /**
     * 优惠价
     */
    @TableField("preferential_price")
    private BigDecimal preferentialPrice;
    /**
     * 到手价
     */
    @TableField("thrie_price")
    private BigDecimal thriePrice;

    @TableField("clean_type")
    private Integer cleanType;

    @TableField(exist = false)
    private String url;


    public Integer getCleanType() {
        return cleanType;
    }

    public void setCleanType(Integer cleanType) {
        this.cleanType = cleanType;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getPreferentialPrice() {
        return preferentialPrice;
    }

    public void setPreferentialPrice(BigDecimal preferentialPrice) {
        this.preferentialPrice = preferentialPrice;
    }

    public BigDecimal getThriePrice() {
        return thriePrice;
    }

    public void setThriePrice(BigDecimal thriePrice) {
        this.thriePrice = thriePrice;
    }

    public String getLableCode() {
        return lableCode;
    }

    public void setLableCode(String lableCode) {
        this.lableCode = lableCode;
    }

    public String getLableName() {
        return lableName;
    }

    public void setLableName(String lableName) {
        this.lableName = lableName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Integer getLableId() {
        return lableId;
    }

    public void setLableId(Integer lableId) {
        this.lableId = lableId;
    }

    public Integer getLableDetailsId() {
        return lableDetailsId;
    }

    public void setLableDetailsId(Integer lableDetailsId) {
        this.lableDetailsId = lableDetailsId;
    }

    public BigDecimal getRebates() {
        return rebates;
    }

    public void setRebates(BigDecimal rebates) {
        this.rebates = rebates;
    }

    public Integer getUserBId() {
        return userBId;
    }

    public void setUserBId(Integer userBId) {
        this.userBId = userBId;
    }

    public Integer getShow() {
        return show;
    }

    public void setShow(Integer show) {
        this.show = show;
    }

    public String getReson() {
        return reson;
    }

    public void setReson(String reson) {
        this.reson = reson;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    protected Serializable pkVal() {
        return this.lableCode;
    }

    @Override
    public String toString() {
        return "LableDetailsReviewTreeEntity{" +
                "lableCode='" + lableCode + '\'' +
                ", lableName='" + lableName + '\'' +
                ", parentCode='" + parentCode + '\'' +
                ", parentCodes='" + parentCodes + '\'' +
                ", treeSort=" + treeSort +
                ", treeSorts='" + treeSorts + '\'' +
                ", treeLeaf='" + treeLeaf + '\'' +
                ", treeLevel=" + treeLevel +
                ", treeNames='" + treeNames + '\'' +
                ", status='" + status + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", lableId=" + lableId +
                ", lableDetailsId=" + lableDetailsId +
                ", rebates=" + rebates +
                ", userBId=" + userBId +
                ", show=" + show +
                ", reson='" + reson + '\'' +
                ", state=" + state +
                ", remake='" + remake + '\'' +
                ", index=" + index +
                ", originalPrice=" + originalPrice +
                ", preferentialPrice=" + preferentialPrice +
                ", thriePrice=" + thriePrice +
                ", cleanType=" + cleanType +
                ", url='" + url + '\'' +
                '}';
    }
}
