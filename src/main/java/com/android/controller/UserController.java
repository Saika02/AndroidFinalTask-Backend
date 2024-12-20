package com.android.controller;

import com.android.common.BaseResponse;
import com.android.common.ErrorCode;
import com.android.common.exception.BusinessException;
import com.android.common.utils.ResultUtils;
import com.android.model.User;
import com.android.model.request.UserLoginRequest;
import com.android.model.request.UserRegisterRequest;
import com.android.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "用户相关接口")
@CrossOrigin(origins ="*")
@Slf4j
public class UserController {
    @Resource
    UserService userService;


    @GetMapping("/getUserById")
    public BaseResponse<User> getUserById(@RequestParam("userId") Long userId) {
        return ResultUtils.success(userService.getUserById(userId));
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String username = userLoginRequest.getUsername();
        String password = userLoginRequest.getPassword();
        log.info("用户登录，用户名：{}，密码：{}", username, password);
        User user = userService.userLogin(username, password);

        return ResultUtils.success(user);
    }

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String username = userRegisterRequest.getUsername();
        String password = userRegisterRequest.getPassword();
        return ResultUtils.success(userService.userRegister(username, password));
    }

    @PostMapping("/addUserFavorite")
    public BaseResponse<Integer> addUserFavorite(@RequestParam("userId") Long userId, @RequestParam("newsId") Long newsId) {
        return ResultUtils.success(userService.addFavorite(userId, newsId));
    }
    @PostMapping("/addHistory")
    public BaseResponse<Integer> addHistory(@RequestParam("userId") Long userId, @RequestParam("newsId") Long newsId) {
        return ResultUtils.success(userService.addHistory(userId, newsId));
    }
    @PostMapping("/removeUserFavorite")
    public BaseResponse<Integer> removeUserFavorite(@RequestParam("userId") Long userId, @RequestParam("newsId") Long newsId) {
        return ResultUtils.success(userService.removeFavorite(userId, newsId));
    }

    @GetMapping("/test")
    public BaseResponse<String> test() {
        return ResultUtils.success("test");
    }

    @DeleteMapping("/clearHistories")
    public BaseResponse<Integer> clearHistories(@RequestParam("userId") Long userId) {
        return ResultUtils.success(userService.clearHistories(userId));
    }

    @DeleteMapping("/removeOneHistory")
    public BaseResponse<Boolean> removeOneHistory(@RequestParam("userId") Long userId, @RequestParam("newsId") Long newsId) {
        return ResultUtils.success(userService.removeOneHistory(userId, newsId));
    }
} 