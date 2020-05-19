package com.blackcat.redis;

import com.blackcat.redis.model.User;
import com.blackcat.redis.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * <p> 描述 ：测试
 * @author : blackcat
 * @date : 2020/5/14 13:16
 */
@SpringBootTest
class RedisTest {

    @Resource
    private RedisUtil redisUtil;

    @Test
    void test(){


    }

    @Test
    void set(){
        redisUtil.set("123","321");
        redisUtil.set("456","654",60,TimeUnit.SECONDS);

        User user = User.builder().name("张三").age(25).motto("我是座右铭").gender("男").build();
        redisUtil.set("user",user);
    }

    @Test
    void get(){
        redisUtil.get("123");

        User user = redisUtil.get("user", User.class);
        System.out.println(user);
    }


    /**
     * <p> 描述 : 存入数据
     * @author : blackcat
     * @date  : 2020/5/16 16:14   
    @Test
    void setTest(){
        User user = User.builder().name("张三").age(25).motto("我是座右铭").gender("男").build();

        // 向redis里存入数据
        redisUtil.set("user",user);
        redisUtil.set("key","value");
        // 向redis里存入数据并设置缓存时间
        redisUtil.set("user2",user, 60 * 10, TimeUnit.SECONDS);
    }

    *//**
     * <p> 描述 : 获取数据
     * @author : blackcat
     * @date  : 2020/5/16 16:14   
     *//*
    @Test
    void getTest(){
        redisUtil.get("key");
        redisUtil.get("user",User.class);

        User user= redisUtil.getObj("user",User.class);

        System.out.println(user);
    }

    *//**
     * <p> 描述 : 是否存在key
     * @author : blackcat
     * @date  : 2020/5/16 16:14   
     *//*
    @Test
    void hasKeyTest(){
        redisUtil.hasKey("111");// get(...) => result -> null
        redisUtil.hasKey("key");// get(...) => result -> value
    }
    
    *//**
     * <p> 描述 : 根据key设置过期时间
     * @author : blackcat
     * @date  : 2020/5/16 16:14   
    *//*
    @Test
    void expireTest(){
        redisUtil.setExpire("123123",60 * 10, TimeUnit.SECONDS);
    }

    *//**
     * <p> 描述 : 删除一个或多个key
     * @author : blackcat
     * @date  : 2020/5/16 16:14
     *//*
    @Test
    void deleteTest(){
        redisUtil.delete("key123");// 删除单个key
        redisUtil.delete("key123","user");// 删除多个key

        // 关于key为null
        redisUtil.delete(null);// 此写法通过
        redisUtil.delete("key123",null);// 此写法错误 报错：non null key required
    }*/

}
