package com.android.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户封禁记录表
 * @TableName ban_record
 */
@TableName(value ="ban_record")
@Data
public class BanRecord implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 被封禁用户ID
     */
    @TableField(value = "userId")
    private Long userId;

    /**
     * 操作管理员ID
     */
    @TableField(value = "adminId")
    private Long adminId;

    /**
     * 操作类型(0:解封 1:封禁)
     */
    @TableField(value = "actionType")
    private Integer actionType;

    /**
     * 操作时间
     */
    @TableField(value = "actionTime")
    private String actionTime;

    /**
     * 操作原因
     */
    @TableField(value = "reason")
    private String reason;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private String username;    // 被封禁用户名
    @TableField(exist = false)
    private String adminName;   // 管理员名称
}