package com.android.model.request;

import lombok.Data;

@Data
public class AddNewsRequest {
    private String title;
    private String newsType;
    private String htmlContent;
    private String publishTime;
}
