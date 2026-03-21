package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 视频评论表
 * </p>
 *
 * @author Ashes
 * @since 2019-08-13
 */
@TableName("app_video_comments")
public class VideoCommentsEntity extends Model<VideoCommentsEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 节点编码
     */
    @TableId("tree_code")
    private String treeCode;
    /**
     * 节点名称
     */
    @TableField("tree_name")
    private String treeName;
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
     * 树节点的完整排序号
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
     * 节点的全名称
     */
    @TableField("tree_names")
    private String treeNames;
    /**
     * 节点状态(0:正常,1:删除,2:停用)
     */
    private String status;
    @TableField("create_date")
    private Date createDate;
    @TableField("update_date")
    private Date updateDate;

    @TableField("parent_id")
    private Integer parentId;
    /**
     * 评论内容
     */
    private String count;
    /**
     * 关联视频表
     */
    @TableField("video_id")
    private Long videoId;
    /**
     * 关联用户表
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 点赞数
     */
    @TableField("thumbs_up")
    private Integer thumbsUp;

    private Integer type;

    @TableField(exist = false)
    private String name;
    @TableField(exist = false)
    private String avatar;
    @TableField(exist = false)
    private String thumbsStatu;     //点赞状态

    @TableField(exist = false)
    private Integer totalNumer;//一级评论下面二级评论条数

    @TableField(exist = false)
    private String replyUser;   //回复人姓名

    @TableField(exist = false)
    private String time;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTreeCode() {
        return treeCode;
    }

    public void setTreeCode(String treeCode) {
        this.treeCode = treeCode;
    }

    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName;
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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getThumbsUp() {
        return thumbsUp;
    }

    public void setThumbsUp(Integer thumbsUp) {
        this.thumbsUp = thumbsUp;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getThumbsStatu() {
        return thumbsStatu;
    }

    public void setThumbsStatu(String thumbsStatu) {
        this.thumbsStatu = thumbsStatu;
    }

    public Integer getTotalNumer() {
        return totalNumer;
    }

    public void setTotalNumer(Integer totalNumer) {
        this.totalNumer = totalNumer;
    }

    public String getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(String replyUser) {
        this.replyUser = replyUser;
    }

    @Override
    protected Serializable pkVal() {
        return this.treeCode;
    }

    @Override
    public String toString() {
        return "VideoCommentsEntity{" +
                "treeCode='" + treeCode + '\'' +
                ", treeName='" + treeName + '\'' +
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
                ", parentId=" + parentId +
                ", count='" + count + '\'' +
                ", videoId=" + videoId +
                ", userId=" + userId +
                ", thumbsUp=" + thumbsUp +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", thumbsStatu='" + thumbsStatu + '\'' +
                ", totalNumer=" + totalNumer +
                ", replyUser='" + replyUser + '\'' +
                '}';
    }
}
