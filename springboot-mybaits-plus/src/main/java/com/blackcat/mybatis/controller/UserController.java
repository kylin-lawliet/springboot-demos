package com.blackcat.mybatis.controller;

import com.blackcat.mybatis.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> ：Controller
 * @author : blackcat
 * @date : 2020/1/18 13:20
 */
@RestController
public class UserController {
    @Autowired
    private IUserService userService;

    /**
     * 新增测试
     */
    @RequestMapping("/addUser")
    public Object addUser(String userName) {
        return userService.addUser(userName);
    }

    /**
     * 查询测试
     */
    @RequestMapping("/findUserList")
    public Object findUserList(Integer pageNo, Integer pageSize) {
        return userService.findUserList(pageNo, pageSize);
    }
}
