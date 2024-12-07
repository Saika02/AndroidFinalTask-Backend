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

    @GetMapping("/test")
    public BaseResponse<String> test() {
        return ResultUtils.success("test");
    }
} 