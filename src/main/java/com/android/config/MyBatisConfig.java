package com.android.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.android.mapper") // 开启事务管理
public class MyBatisConfig {
} 