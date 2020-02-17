package com.blackcat.mybatis.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p> ：Mybatis-Plus配置类
 * @author : blackcat
 * @date : 2020/1/18 12:44
 * LogicSqlInjector  在 mybatisplus 3.0.1 中
 */
@Configuration
@MapperScan("com.blackcat.com.blackcat.mybatis.mapper")//这个注解，作用相当于下面的@Bean MapperScannerConfigurer，2者配置1份即可
public class MybatisPlusConfig {

}
