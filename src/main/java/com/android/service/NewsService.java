package com.android.service;

import com.android.model.News;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface NewsService extends IService<News> {


    List<News> getRandomNewsList();

    String getNewsContent(Long newsId);

    boolean checkIsFavorite(Long userId, Long newsId);

    List<News> getFavoriteNews(Long userId);

    List<News> getBrowsingHistories(Long userId);

    List<News> getNewsByType(String type);

    List<News> searchNews(String keyword);
}
