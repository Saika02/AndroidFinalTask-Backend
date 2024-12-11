package com.android.service;

import com.android.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author DELL G15
* @description 针对表【users】的数据库操作Service
* @createDate 2024-11-23 21:08:16
*/
public interface UserService extends IService<User> {
    User userLogin(String username,String password);

    Long userRegister(String userAccount, String userPassword);

    User getUserById(Long userId);

    User getSafetyUser(User tempUser);

    int updateUser(User user);

    int addFavorite(Long userId, Long newsId);

    int removeFavorite(Long userId, Long newsId);

    Integer addHistory(Long userId, Long newsId);

    Integer clearHistories(Long userId);
}
