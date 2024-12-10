package com.android.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName news_content
 */
@TableName(value ="news_content")
@Data
public class NewsContent implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    private Long contentId;


    @TableField(value = "newsId")
    private Long newsId;


    @TableField(value = "content")
    private String content;


    @TableField(value = "plainContent")
    private String plainContent;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}