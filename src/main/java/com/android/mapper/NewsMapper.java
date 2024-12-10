package com.android.mapper;

import com.android.model.News;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author DELL G15
* @description 针对表【news(新闻表)】的数据库操作Mapper
* @createDate 2024-12-03 00:17:14
* @Entity com.android.model.News
*/
public interface NewsMapper extends BaseMapper<News> {

}




