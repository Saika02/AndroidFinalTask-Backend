package com.android.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.ToString;

/**
 * 评论表
 * @TableName comment
 */
@TableName(value ="comment")
@Data
@ToString
public class Comment implements Serializable {
    /**
     * 评论ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联的新闻ID
     */
    @TableField(value = "newsId")
    private Long newsId;

    /**
     * 评论用户ID
     */
    @TableField(value = "userId")
    private Long userId;

    /**
     * 评论用户昵称
     */
    @TableField(value = "username")
    private String username;

    /**
     * 评论用户头像
     */
    @TableField(value = "avatarUrl")
    private String avatarUrl;

    /**
     * 评论内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 评论时间
     */
    @TableField(value = "createTime")
    private String createTime;

    /**
     * 状态(1:正常 0:删除)
     */
    @TableField(value = "status")
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}