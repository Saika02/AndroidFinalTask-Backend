package com.android.service.impl;

import com.android.model.News;
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

    @Override
    public List<News> getNewsList() {
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("newsType", "yaowen20200213");
        // 打印实际的SQL语句
        System.out.println(queryWrapper.getSqlSegment());
        return newsMapper.selectList(queryWrapper);
    }

    @Override
    public String getNewsDetail(String newsUrl) {
        try {
            Document doc = Jsoup.connect(newsUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                    .timeout(10000)
                    .get();

            Element contentDiv = doc.selectFirst("div.post_body");
            if (contentDiv == null) {
                throw new RuntimeException("未找到新闻内容");
            }

            return buildFormattedHtml(contentDiv.html());
        } catch (Exception e) {
            throw new RuntimeException("获取新闻详情失败: " + e.getMessage());
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




