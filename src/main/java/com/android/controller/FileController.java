package com.android.controller;


import com.android.common.BaseResponse;
import com.android.common.utils.ResultUtils;
import com.android.model.User;
import com.android.service.FileService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RequestMapping("/file")
@Slf4j
@RestController
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Resource
    private FileService fileService;

    @PostMapping("/upload/avatar")
    public BaseResponse<String> uploadAvatar(@RequestParam("file") MultipartFile file,
                                           @RequestParam("userId") Long userID) throws ServletException, IOException {
        String url = fileService.uploadAvatar(userID, file);
        return ResultUtils.success(url);
    }
}
