package com.android.controller;

import com.android.common.BaseResponse;
import com.android.common.utils.ResultUtils;
import com.android.model.BanRecord;
import com.android.model.request.AddNewsRequest;
import com.android.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.JobName;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "News", description = "新闻相关接口")
@CrossOrigin(origins = "*")
@Slf4j
public class AdminController {

    @Resource
    AdminService adminService;


    @GetMapping("banUser")
    public BaseResponse<Boolean> banUser(@RequestParam("userId") Long userId, @RequestParam("adminId") Long adminId,@RequestParam("reason") String reason) {
        log.info("封禁用户，userId: {}, adminId: {}", userId, adminId);
        boolean result = adminService.banUser(userId,adminId,reason);
        return ResultUtils.success(result);
    }

    @GetMapping("getBannedUsers")
    public BaseResponse<List<BanRecord>> getBannedUsers() {
        log.info("获取封禁用户列表");
        List<BanRecord> bannedUsers = adminService.getBannedUsers();
        return ResultUtils.success(bannedUsers);
    }

    @GetMapping("unbanUser")
    public BaseResponse<Boolean> unbanUser(@RequestParam("userId") Long userId) {
        log.info("解封用户，userId: {}", userId);
        boolean result = adminService.unbanUser(userId);
        return ResultUtils.success(result);
    }

    @PostMapping("addNews")
    public BaseResponse<Boolean> addNews(@RequestBody AddNewsRequest addNewsRequest) {
        log.info("添加新闻，news: {}", addNewsRequest);
        boolean result = adminService.addNews(addNewsRequest);
        return ResultUtils.success(result);
    }

    @DeleteMapping("deleteNews")
    public BaseResponse<Boolean> deleteNews(@RequestParam("newsId") Long newsId) {
        log.info("删除新闻，newsId: {}", newsId);
        boolean result = adminService.deleteNews(newsId);
        return ResultUtils.success(result);
    }
}
