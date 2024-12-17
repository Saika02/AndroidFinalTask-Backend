package com.android.constant;

public final class NewsConstants {
    private NewsConstants() {} // 私有构造函数，防止实例化

    // 数据库字段名
    public static final String NEWS_ID = "newsId";
    public static final String TITLE = "title";
    public static final String DOCURL = "docurl";
    public static final String PUBLISH_TIME = "publishTime";
    public static final String NEWS_TYPE = "newsType";
    public static final String IMAGE_URL = "imageUrl";
    public static final String CREATE_TIME = "createTime";
    public static final String UPDATE_TIME = "updateTime";
    public static final String TYPE_DESC = "typeDesc";

    // 新闻类型常量
    public static final String TYPE_YAOWEN = "yaowen20200213";
    public static final String TYPE_GUONEI = "guonei";
    public static final String TYPE_GUOJI = "guoji";
    public static final String TYPE_WAR = "war";
    public static final String TYPE_TECH = "tech";
    public static final String TYPE_MONEY = "money";
    public static final String TYPE_SPORTS = "sports";
    public static final String TYPE_ENT = "ent";

    // 新闻类型描述
    public static final String DESC_YAOWEN = "要闻";
    public static final String DESC_GUONEI = "国内";
    public static final String DESC_GUOJI = "国际";
    public static final String DESC_WAR = "军事";
    public static final String DESC_TECH = "科技";
    public static final String DESC_MONEY = "财经";
    public static final String DESC_SPORTS = "体育";
    public static final String DESC_ENT = "娱乐";

    public static String translateType(String type) {
        switch (type) {
            case TYPE_YAOWEN:
                return DESC_YAOWEN;
            case TYPE_GUONEI:
                return DESC_GUONEI;
            case TYPE_GUOJI:
                return DESC_GUOJI;
            case TYPE_WAR:
                return DESC_WAR;
            case TYPE_TECH:
                return DESC_TECH;
            case TYPE_MONEY:
                return DESC_MONEY;
            case TYPE_SPORTS:
                return DESC_SPORTS;
            case TYPE_ENT:
                return DESC_ENT;
            default:
                return "未知";
        }
    }
}