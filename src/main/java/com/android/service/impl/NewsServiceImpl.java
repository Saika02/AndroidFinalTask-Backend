package com.android.service.impl;

import com.android.common.ErrorCode;
import com.android.common.exception.BusinessException;
import com.android.constant.NewsConstants;
import com.android.constant.UserConstants;
import com.android.mapper.*;
import com.android.model.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.android.service.NewsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author DELL G15
 * @description 针对表【news(新闻表)】的数据库操作Service实现
 * @createDate 2024-12-03 00:17:14
 */
@Service
@Slf4j
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News>
        implements NewsService {


    @Resource
    private NewsMapper newsMapper;
    @Resource
    private NewsContentMapper newsContentMapper;

    @Resource
    private UserFavoriteMapper userFavoriteMapper;

    @Resource
    private BrowsingHistoryMapper browsingHistoryMapper;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public List<News> getRandomNewsList() {
        int limit = 20;
        // 获取所有新闻类型
        String[] newsTypes = {
                NewsConstants.TYPE_YAOWEN,
                NewsConstants.TYPE_GUONEI,
                NewsConstants.TYPE_GUOJI,
                NewsConstants.TYPE_WAR,
                NewsConstants.TYPE_TECH,
                NewsConstants.TYPE_MONEY,
                NewsConstants.TYPE_SPORTS,
                NewsConstants.TYPE_ENT
        };

        int perTypeLimit = (int) Math.ceil((double) limit / newsTypes.length);

        List<News> result = new ArrayList<>();
        for (String type : newsTypes) {
            QueryWrapper<News> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(NewsConstants.NEWS_TYPE, type)
                    .orderByAsc("RAND()")
                    .last("LIMIT " + perTypeLimit);
            result.addAll(newsMapper.selectList(queryWrapper));
        }
        if (result.size() < limit) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "获取新闻失败");
        }
        // 随机打乱结果
        Collections.shuffle(result);
        // 如果结果数量超过limit，截取前limit条
        return result.size() > limit ? result.subList(0, limit) : result;
    }


    @Override
    public String getNewsContent(Long newsId) {
        QueryWrapper<NewsContent> wrapper = new QueryWrapper<>();
        wrapper.eq("newsId", newsId);
        NewsContent newsContent = newsContentMapper.selectOne(wrapper);
        if (newsContent == null) {
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
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据库查询失败");
        }
    }

    @Override
    public List<News> getBrowsingHistories(Long userId) {
        try {
            return browsingHistoryMapper.selectHistoryNews(userId);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据库查询失败");
        }
    }

    @Override
    public List<News> getNewsByType(String type) {
        int limit = 20;
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(NewsConstants.NEWS_TYPE, type)
                .orderByAsc("RAND()")
                .last("LIMIT " + limit);
        List<News> list = newsMapper.selectList(queryWrapper);
        if (list.size() < limit) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "获取新闻失败");
        }
        return list;
    }

    @Override
    public List<News> searchNews(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return null;
        }
        return newsMapper.searchNews(keyword);
    }

    @Override
    public List<Comment> getNewsComment(Long newsId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq(NewsConstants.NEWS_ID, newsId);
        return commentMapper.selectList(wrapper);
    }

    @Override
    public boolean saveComment(Comment comment) {
        User user = userMapper.selectById(comment.getUserId());
        if(user.getStatus() == 1){
            throw new BusinessException(ErrorCode.USER_BANNED, "用户已被封禁");
        }
        log.info("发送评论，comment: {}", comment);
        return commentMapper.insert(comment) > 0;
    }

    @Override
    public Boolean delComment(Long commentId) {
        return commentMapper.deleteById(commentId) > 0;
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




