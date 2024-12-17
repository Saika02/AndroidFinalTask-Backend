package com.android.service;

import com.android.model.BanRecord;

import java.util.List;

public interface AdminService{

    Boolean banUser(Long userId, Long adminId, String reason);

    List<BanRecord> getBannedUsers();

    boolean unbanUser(Long userId);
}
