package com.android.common;

/**
 * 错误码
 */
public enum ErrorCode {
    //通用
    SUCCESS(0, "ok", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "请求数据为空", ""),

    NOT_LOGIN(40100, "未登录", ""),
    NO_AUTH(40101, "无权限", ""),

    LOGIN_ERROR(40200,"用户名或密码错误",""),


    USER_NOT_FOUND(40300,"用户不存在",""),
    USER_EXISTS(40301,"用户已存在",""),
    USER_BANNED(40302,"用户已被封禁",""),

    NEWS_NOT_FOUND(40400,"新闻不存在",""),
    NEWS_EXISTS(40401,"新闻已存在",""),

    FAVORITE_EXISTS(40500,"收藏已存在",""),
    FAVORITE_NOT_FOUND(40501,"收藏不存在",""),

    SYSTEM_ERROR(50000, "系统内部异常", "");

    private final int code;

    /**
     * 状态码信息
     */
    private final String message;

    /**
     * 状态码描述（详情）
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
