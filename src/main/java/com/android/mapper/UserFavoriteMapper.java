package com.android.mapper;

import com.android.model.News;
import com.android.model.UserFavorite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * @author DELL G15
 * @description 针对表【user_favorite】的数据库操作Mapper
 * @createDate 2024-12-10 18:32:45
 * @Entity com.android.model.UserFavorite
 */
public interface UserFavoriteMapper extends BaseMapper<UserFavorite> {
    @Select("""
                        SELECT * FROM news WHERE news.newsId IN (
                            SELECT newsId FROM user_favorite WHERE userId = #{userId}
                        )
            """)
    List<News> getFavoriteNews(@Param("userId") Long userId);
}
