package com.android.model;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 新闻表
 * @TableName news
 */
@TableName(value ="news")
@Data
public class News implements Serializable {
    /**
     * 新闻ID
     */
    @TableId(value = "newsId", type = IdType.AUTO)
    private Long newsId;

    /**
     * 新闻标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 新闻内容
     */
    @TableField(value = "docurl")
    private String docurl;

    /**
     * 发布时间
     */
    @TableField(value = "publishTime")
    private String publishTime;

    /**
     * 图片URL
     */
    @TableField(value = "imageUrl")
    private String imageUrl;

    /**
     * 新闻类型(yaowen/guonei/guoji/war/tech/money/sports/ent)
     */
    @TableField(value = "newsType")
    private String newsType;

    /**
     * 类型描述(要闻/国内/国际/军事/科技/财经/体育/娱乐)
     */
    @TableField(value = "typeDesc")
    private String typeDesc;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private Date createTime;

    /**
     * 是否删除(0:未删除,1:已删除)
     */
    @TableLogic
    @TableField(value = "isDeleted")
    private Integer isDeleted;

}