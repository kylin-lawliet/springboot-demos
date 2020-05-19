package com.blackcat.redis.controller;

import com.blackcat.redis.model.User;
import com.blackcat.redis.util.RedisUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p> 描述 ：
 *
 * @author : blackcat
 * @date : 2020/5/16 13:29
 */
@RestController
public class TestController {

//    @Resource
//    private RedisUtil redisUtil;

    @RequestMapping("/set")
    public void set(){
//        User user = User.builder().name("张三").age(25).motto("我是座右铭").gender("男").build();
//        redisUtil.set("user1",user);
    }
}
