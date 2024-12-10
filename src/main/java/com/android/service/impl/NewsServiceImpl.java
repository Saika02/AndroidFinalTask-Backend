package com.android.service.impl;

import com.android.common.ErrorCode;
import com.android.common.exception.BusinessException;
import com.android.constant.NewsConstants;
import com.android.constant.UserConstants;
import com.android.mapper.BrowsingHistoryMapper;
import com.android.mapper.NewsContentMapper;
import com.android.mapper.UserFavoriteMapper;
import com.android.model.News;
import com.android.model.NewsContent;
import com.android.model.UserFavorite;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.android.service.NewsService;
import com.android.mapper.NewsMapper;
import jakarta.annotation.Resource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author DELL G15
* @description 针对表【news(新闻表)】的数据库操作Service实现
* @createDate 2024-12-03 00:17:14
*/
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News>
    implements NewsService{


    @Resource
    private NewsMapper newsMapper;
    @Resource
    private NewsContentMapper newsContentMapper;

    @Resource
    private UserFavoriteMapper userFavoriteMapper;

    @Resource
    private BrowsingHistoryMapper browsingHistoryMapper;

    @Override
    public List<News> getNewsList() {
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("newsType", "yaowen20200213");
        // 打印实际的SQL语句
        System.out.println(queryWrapper.getSqlSegment());
        return newsMapper.selectList(queryWrapper);
    }



    @Override
    public String getNewsContent(Long newsId) {
        QueryWrapper<NewsContent> wrapper = new QueryWrapper<>();
        wrapper.eq("newsId", newsId);
        NewsContent newsContent = newsContentMapper.selectOne(wrapper);
        if(newsContent == null) {
            throw new BusinessException(ErrorCode.NEWS_NOT_FOUND, "新闻内容不存在");
        }
        return buildFormattedHtml(newsContent.getContent());
    }

    @Override
    public boolean checkIsFavorite(Long userId, Long newsId) {
        News news = newsMapper.selectById(newsId);
        if (news == null) {
            throw new BusinessException(ErrorCode.NEWS_NOT_FOUND, "新闻不存在");
        }
        QueryWrapper<UserFavorite> wrapper = new QueryWrapper<>();
        wrapper.eq(UserConstants.USER_ID, userId);
        wrapper.eq(NewsConstants.NEWS_ID, newsId);
        return userFavoriteMapper.selectCount(wrapper) > 0;
    }

    @Override
    public List<News> getFavoriteNews(Long userId) {
        try {
            return userFavoriteMapper.getFavoriteNews(userId);
        }
        catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据库查询失败");
        }
    }

    @Override
    public List<News> getBrowsingHistories(Long userId) {
        try {
            return browsingHistoryMapper.selectHistoryNews(userId);
        }
        catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据库查询失败");
        }
    }

    private String buildFormattedHtml(String content) {
        return "<html>" +
                "<head>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<style>" +
                "body{padding:16px;line-height:1.6;font-size:16px;color:#333}" +
                "img{max-width:100%;height:auto;display:block;margin:10px auto}" +
                "p{margin:10px 0}" +
                "</style>" +
                "</head>" +
                "<body>" + content + "</body></html>";
    }


}




