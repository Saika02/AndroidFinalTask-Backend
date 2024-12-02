package com.android.controller;


import com.android.common.BaseResponse;
import com.android.common.utils.ResultUtils;
import com.android.model.News;
import com.android.service.NewsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public BaseResponse<List<News>> getNewsList() {
        List<News> newsList = newsService.getNewsList();
        return ResultUtils.success(newsList);
    }
}
