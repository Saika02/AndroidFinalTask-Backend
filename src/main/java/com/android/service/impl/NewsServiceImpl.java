package com.android.service.impl;

import com.android.model.News;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.android.service.NewsService;
import com.android.mapper.NewsMapper;
import jakarta.annotation.Resource;
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

    @Override
    public List<News> getNewsList() {
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("newsType", "yaowen20200213");
        // 打印实际的SQL语句
        System.out.println(queryWrapper.getSqlSegment());
        return newsMapper.selectList(queryWrapper);
    }
}




