package com.android.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * @TableName users
 */
@TableName(value = "user")
@Data
public class User implements Serializable {

    @TableId(value = "userId", type = IdType.AUTO)
    private Long userId;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("avatarUrl")
    private String avatarUrl;

    @TableField("role")
    private Integer role;

    @TableField("status")
    private Integer status;

}