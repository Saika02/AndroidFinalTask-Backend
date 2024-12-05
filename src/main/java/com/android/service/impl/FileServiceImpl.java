package com.android.service.impl;

import com.android.model.User;
import com.android.service.FileService;
import com.android.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Resource
    UserService userService;


    @Override
    public String uploadAvatar(Long userID, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        File dir = new File(uploadDir+"/avatar");
        if(!dir.exists()){
            dir.mkdir();
        }
        File destFile = new File(dir.getAbsolutePath()+File.separator+fileName);
        file.transferTo(destFile);
        String avatarUrl = "avatar/"+fileName;

        User user = userService.getUserById(userID);
        user.setAvatarUrl(avatarUrl);
        if(userService.updateUser(user) == 0){
            throw new IOException("更新用户头像失败");
        }
        return avatarUrl;
    }
}
