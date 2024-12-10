package com.android.once;

import com.android.constant.NewsContentConstants;
import com.android.mapper.NewsContentMapper;
import com.android.mapper.NewsMapper;
import com.android.model.News;
import com.android.model.NewsContent;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class NewsInitializer implements CommandLineRunner {

    private static final String BASE_URL = "https://news.163.com/special/cm_%s/?callback=data_callback";
    private final OkHttpClient client = new OkHttpClient();
    private static final int MIN_SLEEP_TIME = 3000; // 最小延迟3秒
    private static final int MAX_SLEEP_TIME = 7000; // 最大延迟7秒
    private static final Random random = new Random();

    @Resource
    private NewsMapper newsMapper;

    @Resource
    private NewsContentMapper newsContentMapper;

    @Override
    public void run(String... args) {
        // 第一步：检查是否已初始化
        Long newsCount = newsMapper.selectCount(null);
        if (newsCount > 0) {
            log.info("数据库中已有{}条新闻，检查是否需要爬取内容", newsCount);
            // 检查是否有未爬取内容的新闻
            fetchMissingContents();
            return;
        }

        // 第二步：初始化新闻列表
        log.info("开始初始化新闻列表...");
        initializeNewsList();

        // 第三步：爬取所有新闻内容
        log.info("开始爬取新闻内容...");
        fetchAllContents();
    }

    private void initializeNewsList() {
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
                log.info("成功保存 {} 类型的 {} 条新闻列表", typeDescs[i], newsList.size());

                // 每个分类之间也要休息一下
                Thread.sleep(MIN_SLEEP_TIME + random.nextInt(MAX_SLEEP_TIME - MIN_SLEEP_TIME));
            } catch (Exception e) {
                log.error("获取{}类型新闻列表失败: {}", typeDescs[i], e.getMessage());
            }
        }
        log.info("新闻列表初始化完成");
    }

    private void fetchAllContents() {
        List<News> allNews = newsMapper.selectList(null);
        int total = allNews.size();
        int current = 0;
        int success = 0;
        int failed = 0;
        int consecutiveFailures = 0;

        for (News news : allNews) {
            current++;
            try {
                log.info("正在爬取第 {}/{} 条新闻内容: {}", current, total, news.getTitle());
                fetchAndSaveContent(news);
                success++;
                consecutiveFailures = 0;
            } catch (Exception e) {
                failed++;
                consecutiveFailures++;
                log.error("爬取新闻内容失败: {}, URL: {}", e.getMessage(), news.getDocurl());

                if (consecutiveFailures >= 3) {
                    try {
                        log.warn("连续失败{}次，暂停5分钟...", consecutiveFailures);
                        Thread.sleep(300000);
                        consecutiveFailures = 0;
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
        log.info("新闻内容爬取完成，总数: {}，成功: {}，失败: {}", total, success, failed);
    }

    private void fetchMissingContents() {
        List<News> allNews = newsMapper.selectList(null);
        for (News news : allNews) {
            try {
                QueryWrapper<NewsContent> wrapper = new QueryWrapper<>();
                wrapper.eq(NewsContentConstants.NEWS_ID, news.getNewsId());
                if (newsContentMapper.selectCount(wrapper) == 0) {
                    log.info("爬取缺失的新闻内容: {}", news.getTitle());
                    fetchAndSaveContent(news);
                }
            } catch (Exception e) {
                log.error("爬取新闻内容失败: {}, URL: {}", e.getMessage(), news.getDocurl());
            }
        }
    }

    private void fetchAndSaveContent(News news) throws Exception {
        try {
            Document doc = Jsoup.connect(news.getDocurl())
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                    .header("Connection", "keep-alive")
                    .timeout(10000)
                    .get();

            Element contentDiv = doc.selectFirst("div.post_body");
            if (contentDiv == null) {
                // 如果找不到内容，删除这条新闻
                log.warn("新闻内容不存在，删除新闻: {}", news.getTitle());
                newsMapper.deleteById(news.getNewsId());
                return;
            }

            NewsContent newsContent = new NewsContent();
            newsContent.setNewsId(news.getNewsId());
            newsContent.setContent(contentDiv.html());
            newsContent.setPlainContent(contentDiv.text());
            newsContentMapper.insert(newsContent);

            // 随机延迟3-7秒
            int sleepTime = MIN_SLEEP_TIME + random.nextInt(MAX_SLEEP_TIME - MIN_SLEEP_TIME);
            log.info("成功爬取新闻: {}, 等待{}毫秒后继续", news.getTitle(), sleepTime);
            Thread.sleep(sleepTime);

        } catch (IOException e) {
            if (e.getMessage().contains("404")) {
                // 404错误，删除这条新闻
                log.warn("新闻页面404，删除新闻: {}", news.getTitle());
                newsMapper.deleteById(news.getNewsId());
                return;
            }
            if (e.getMessage().contains("429") || e.getMessage().contains("403")) {
                log.error("可能被限制访问，等待30秒后重试...");
                Thread.sleep(30000);
                throw new RuntimeException("访问被限制，请检查爬虫策略");
            }
            throw e;
        }
    }

    private String fetchData(String urlString) throws Exception {
        Request request = new Request.Builder()
                .url(urlString)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("请求失败: " + response);
            }
            String data = response.body().string();
            data = data.replace("data_callback(", "").replace("})", "}");
            return data;
        }
    }

    private List<News> parseNews(String jsonData, String newsType, String typeDesc) {
        List<News> newsList = new ArrayList<>();
        try {
            JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
            JsonArray newsArray = jsonObject.getAsJsonArray("news");

            for (JsonElement element : newsArray) {
                JsonObject newsObj = element.getAsJsonObject();
                News news = new News();
                news.setTitle(newsObj.get("title").getAsString());
                news.setDocurl(newsObj.get("docurl").getAsString());
                news.setPublishTime(newsObj.get("time").getAsString());
                JsonElement imgElement = newsObj.get("imgurl");
                if (imgElement != null && !imgElement.isJsonNull()) {
                    news.setImageUrl(imgElement.getAsString());
                }
                news.setNewsType(newsType);
                news.setTypeDesc(typeDesc);
                newsList.add(news);
            }
        } catch (Exception e) {
            log.error("解析新闻数据失败: {}", e.getMessage());
        }
        return newsList;
    }
}