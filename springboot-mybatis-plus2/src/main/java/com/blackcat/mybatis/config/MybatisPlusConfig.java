package com.blackcat.mybatis.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Component;

/**
 * <p> 描述 ：Mybatis-Plus 配置类
 * @author : blackcat
 * @date : 2020/1/26 11:30
 */
@Component
@MapperScan("com.blackcat.mybatis.mapper") //扫描Mapper
public class MybatisPlusConfig {

}
