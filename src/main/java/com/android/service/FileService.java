package com.android.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String uploadAvatar(Long userID, MultipartFile file) throws IOException;
}
