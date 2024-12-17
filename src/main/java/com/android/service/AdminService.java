package com.android.service;

import com.android.model.BanRecord;
import com.android.model.request.AddNewsRequest;

import java.util.List;

public interface AdminService{

    Boolean banUser(Long userId, Long adminId, String reason);

    List<BanRecord> getBannedUsers();

    boolean unbanUser(Long userId);

    boolean addNews(AddNewsRequest addNewsRequest);
}
