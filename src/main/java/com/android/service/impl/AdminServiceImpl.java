package com.android.service.impl;


import com.android.common.ErrorCode;
import com.android.common.exception.BusinessException;
import com.android.mapper.BanRecordMapper;
import com.android.mapper.UserMapper;
import com.android.model.BanRecord;
import com.android.model.User;
import com.android.service.AdminService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    BanRecordMapper banRecordMapper;

    @Resource
    UserMapper userMapper;


    @Override
    public Boolean banUser(Long userId, Long adminId, String reason) {
        boolean result = false;
        User user = userMapper.selectById(userId);
        User admin = userMapper.selectById(adminId);
        if (user != null) {
            if(user.getStatus() == 1){
                throw new BusinessException(ErrorCode.USER_BANNED, "用户已被封禁");
            }
            user.setStatus(1);
            result = userMapper.updateById(user) > 0;
        }
        // 2. 创建封禁记录
        BanRecord record = new BanRecord();
        record.setUserId(userId);
        record.setAdminId(adminId);
        record.setActionType(1);
        record.setReason(reason);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        record.setActionTime(sdf.format(new Date()));
        record.setUsername(user.getUsername());
        record.setAdminName(admin.getUsername());

        return result & banRecordMapper.insert(record) > 0;
    }

    @Override
    public List<BanRecord> getBannedUsers() {
        List<BanRecord> banRecords = banRecordMapper.selectList(null);
        banRecords.forEach(banRecord -> {
            banRecord.setUsername(userMapper.selectById(banRecord.getUserId()).getUsername());
            banRecord.setAdminName(userMapper.selectById(banRecord.getAdminId()).getUsername());
        });
        return banRecords;
    }

    @Override
    public boolean unbanUser(Long userId) {
        Boolean res = false;
        User user = userMapper.selectById(userId);
        if (user != null) {
            user.setStatus(0);
            res = userMapper.updateById(user) > 0;
        }
        QueryWrapper<BanRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("userId", userId);
        return res & banRecordMapper.delete(wrapper) > 0;
    }
}
