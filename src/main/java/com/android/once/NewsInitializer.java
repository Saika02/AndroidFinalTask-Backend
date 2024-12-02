package com.android.once;

import com.android.mapper.NewsMapper;
import com.android.model.News;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class NewsInitializer implements CommandLineRunner {

    private static final String BASE_URL = "https://news.163.com/special/cm_%s/?callback=data_callback";
    private static final OkHttpClient client = new OkHttpClient();

    private final NewsMapper newsMapper;

    public NewsInitializer(NewsMapper newsMapper) {
        this.newsMapper = newsMapper;
    }

    @Override
    public void run(String... args) {
        // 检查是否已有新闻数据
        Long newsCount = newsMapper.selectCount(null);
        if (newsCount > 0) {
            log.info("数据库中已有{}条新闻，跳过初始化", newsCount);
            return;
        }

        log.info("开始初始化新闻数据...");
        String[] newsTypes = {"yaowen20200213", "guonei", "guoji", "war", "tech", "money", "sports", "ent"};
        String[] typeDescs = {"要闻", "国内", "国际", "军事", "科技", "财经", "体育", "娱乐"};

        for (int i = 0; i < newsTypes.length; i++) {
            try {
                String url = String.format(BASE_URL, newsTypes[i]);
                String jsonData = fetchData(url);
                List<News> newsList = parseNews(jsonData, newsTypes[i], typeDescs[i]);
                for (News news : newsList) {
                    newsMapper.insert(news);
                }
                log.info("成功保存 {} 类型的 {} 条新闻", typeDescs[i], newsList.size());
            } catch (Exception e) {
                log.error("获取{}类型新闻失败: {}", typeDescs[i], e.getMessage());
            }
        }
        log.info("新闻数据初始化完成");
    }

    private String fetchData(String urlString) throws Exception {
        Request request = new Request.Builder()
                .url(urlString)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String jsonData = response.body().string();
            return jsonData.substring(jsonData.indexOf("(") + 1, jsonData.lastIndexOf(")"));
        }
    }

    private List<News> parseNews(String jsonData, String newsType, String typeDesc) {
        List<News> newsList = new ArrayList<>();
        JsonArray jsonArray = JsonParser.parseString(jsonData).getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject obj = jsonArray.get(i).getAsJsonObject();
            News news = new News();
            news.setTitle(obj.get("title").getAsString());
            news.setPublishTime(obj.get("time").getAsString());
            news.setDocurl(obj.get("docurl").getAsString());
            news.setImageUrl(obj.get("imgurl").getAsString());
            news.setNewsType(newsType);
            news.setTypeDesc(typeDesc);
            newsList.add(news);
        }
        return newsList;
    }
}