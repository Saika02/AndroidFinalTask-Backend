package com.android.service.impl;

import com.android.common.ErrorCode;
import com.android.common.exception.BusinessException;
import com.android.constant.UserConstants;
import com.android.mapper.UserMapper;
import com.android.model.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.android.service.UserService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Map;

/**
 * @author DELL G15
 * @description 针对表【users】的数据库操作Service实现
 * @createDate 2024-11-23 21:08:16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    UserMapper userMapper;

    private static final String SALT = "Lzz";

    @Override
    public User getSafetyUser(User tempUser) {
        if (tempUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setUserId(tempUser.getUserId());
        safetyUser.setAvatarUrl(tempUser.getAvatarUrl());
        safetyUser.setUsername(tempUser.getUsername());
        safetyUser.setRole(tempUser.getRole());
        return safetyUser;
    }

    @Override
    public User userLogin(String username, String password) {
        if (StringUtils.isAnyBlank(username, password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名或密码不能为空");
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((password + SALT).getBytes());
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.apply("BINARY username = {0}", username)
                .eq("password", encryptPassword);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.LOGIN_ERROR, "用户名或密码错误");
        }
        User safetyUser = getSafetyUser(user);
        return safetyUser;
    }

    @Override
    public Long userRegister(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.apply("BINARY username = {0}", username)
                .eq(UserConstants.PASSWORD, password);
        User user = userMapper.selectOne(wrapper);
        if (user != null) {
            throw new BusinessException(ErrorCode.USER_EXISTS, "用户已存在");
        }
        User newUser = new User();
        String encryptPassword = DigestUtils.md5DigestAsHex((password + SALT).getBytes());
        newUser.setUsername(username);
        newUser.setPassword(encryptPassword);
        int res = userMapper.insert(newUser);
        if (res <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败");
        }
        return newUser.getUserId();
    }

    @Override
    public User getUserById(Long userId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "用户不存在");
        }
        return getSafetyUser(user);
    }

    @Override
    public int updateUser(User user) {
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户为空");
        }
        if(user.getUserId() == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID为空");
        }
        // 检查用户是否存在
        User existingUser = userMapper.selectById(user.getUserId());
        if (existingUser == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "用户不存在");
        }
        // 更新用户信息
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq(UserConstants.USER_ID, user.getUserId());
        // 只更新非空字段
        if (user.getUsername() != null) {
            userUpdateWrapper.set(UserConstants.USERNAME, user.getUsername());
        }
        if(user.getPassword() != null){
            userUpdateWrapper.set(UserConstants.PASSWORD, user.getPassword());
        }
        if (user.getAvatarUrl() != null) {
            userUpdateWrapper.set(UserConstants.AVATAR_URL, user.getAvatarUrl());
        }
        if (user.getRole() != null) {
            userUpdateWrapper.set(UserConstants.ROLE, user.getRole());
        }
        return userMapper.update(user, userUpdateWrapper);
    }
}




