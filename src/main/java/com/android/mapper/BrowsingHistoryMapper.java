package com.android.mapper;

import com.android.model.BrowsingHistory;
import com.android.model.News;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author DELL G15
* @description 针对表【browsing_history】的数据库操作Mapper
* @createDate 2024-12-11 01:19:11
* @Entity com.android.model.BrowsingHistory
*/
public interface BrowsingHistoryMapper extends BaseMapper<BrowsingHistory> {
    @Select("""
            SELECT n.* FROM news n 
            INNER JOIN browsing_history bh ON n.newsId = bh.newsId 
            WHERE bh.userId = #{userId} 
            ORDER BY bh.browseTime DESC;
""")
    List<News> selectHistoryNews(@Param("userId") Long userId);
}




