package com.android.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName user_favorite
 */
@TableName(value ="user_favorite")
@Data
public class UserFavorite implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    @TableField(value = "userId")
    private Long userId;


    @TableField(value = "newsId")
    private Long newsId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}