package com.android.mapper;

import com.android.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

/**
* @author DELL G15
* @description 针对表【users】的数据库操作Mapper
* @createDate 2024-11-23 21:08:16
* @Entity com.lzz.androidbackend.model.Users
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




