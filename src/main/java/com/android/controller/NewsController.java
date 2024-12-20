package com.android.controller;


import com.android.common.BaseResponse;
import com.android.common.utils.ResultUtils;
import com.android.model.Comment;
import com.android.model.News;
import com.android.service.NewsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
@Tag(name = "News", description = "新闻相关接口")
@CrossOrigin(origins ="*")
@Slf4j
public class NewsController {
    @Resource
    NewsService newsService;


    @GetMapping("/list")
    public BaseResponse<List<News>> getRandomNewsList() {
        List<News> newsList = newsService.getRandomNewsList();
        return ResultUtils.success(newsList);
    }

    @GetMapping("/checkIsFavorite")
    public BaseResponse<Boolean> checkIsFavorite(@RequestParam("userId") Long userId, @RequestParam("newsId") Long newsId) {
        log.info("检查用户是否收藏新闻，userId: {}, newsId: {}", userId, newsId);
        boolean isFavorite = newsService.checkIsFavorite(userId, newsId);
        return ResultUtils.success(isFavorite);
    }

    @GetMapping("/getFavoriteNews")
    public BaseResponse<List<News>> getFavoriteNews(@RequestParam("userId") Long userId) {
        log.info("获取用户收藏的新闻，userId: {}", userId);
        List<News> favoriteNews = newsService.getFavoriteNews(userId);
        return ResultUtils.success(favoriteNews);
    }

    @GetMapping("/getNewsByType")
    public BaseResponse<List<News>> getNewsByType(@RequestParam("newsType") String type) {
        log.info("获取新闻列表，type: {}", type);
        List<News> newsList = newsService.getNewsByType(type);
        return ResultUtils.success(newsList);
    }

    @GetMapping("/getBrowsingHistories")
    public BaseResponse<List<News>> getBrowsingHistories(@RequestParam("userId") Long userId) {
        log.info("获取用户浏览历史，userId: {}", userId);
        List<News> browsingHistories = newsService.getBrowsingHistories(userId);
        return ResultUtils.success(browsingHistories);
    }

    @GetMapping("/search")
    public BaseResponse<List<News>> searchNews(@RequestParam("keyword") String keyword) {
        log.info("搜索新闻，keyword: {}", keyword);
        List<News> newsList = newsService.searchNews(keyword);
        return ResultUtils.success(newsList);
    }

    @GetMapping("/content")
    public BaseResponse<String> getNewsDetail(@RequestParam("id") Long newsId) {
        log.info("访问新闻详情接口，url: {}", newsId);
        String newsDetail = newsService.getNewsContent(newsId);
        return ResultUtils.success(newsDetail);
    }

    @GetMapping("/getNewsComment")
    public BaseResponse<List<Comment>> getNewsComment(@RequestParam("newsId") Long newsId) {
        log.info("获取新闻评论，newsId: {}", newsId);
        List<Comment> commentList = newsService.getNewsComment(newsId);
        return ResultUtils.success(commentList);
    }

    @PostMapping("sendComment")
    public BaseResponse<Boolean> sendComment(@RequestBody Comment comment) {
        log.info("发送评论，comment: {}", comment);
        boolean result = newsService.saveComment(comment);
        return ResultUtils.success(result);
    }

    @DeleteMapping("delComment")
    public BaseResponse<Boolean> delComment(@RequestParam("commentId") Long commentId) {
        log.info("删除评论，commentId: {}", commentId);
        Boolean result = newsService.delComment(commentId);
        return ResultUtils.success(result);
    }


}
