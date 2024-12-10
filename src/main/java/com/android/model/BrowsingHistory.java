package com.android.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName browsing_history
 */
@TableName(value = "browsing_history")
@Data
public class BrowsingHistory implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "userId")
    private Long userId;


    @TableField(value = "newsId")
    private Long newsId;


    @TableField(value = "browseTime")
    private Date browseTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}