package com.android.service;

import com.android.model.News;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author DELL G15
* @description 针对表【news(新闻表)】的数据库操作Service
* @createDate 2024-12-03 00:17:14
*/
public interface NewsService extends IService<News> {


    List<News> getNewsList();

    String getNewsDetail(String newsUrl);

}
