package com.blackcat.shiro.controller;


import com.blackcat.shiro.service.SysUserService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p> 系统用户表 前端控制器
 * @author blackcat
 * @date 2020-02-03
 */
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Resource
    private SysUserService iSysUserService;

    /**
     * 创建固定写死的用户
     * @param model
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.GET)
    @ResponseBody
    public String login(Model model) {

        return "创建用户成功";

    }

    /**
     * 删除固定写死的用户
     * @param model
     * @return
     */
    @RequestMapping(value = "/del",method = RequestMethod.GET)
    @ResponseBody
    public String del(Model model) {

        return "删除用户名为wangsaichao用户成功";

    }

    @RequestMapping(value = "/view",method = RequestMethod.GET)
    @ResponseBody
    public String view(Model model) {

        return "这是用户列表页";

    }
}
