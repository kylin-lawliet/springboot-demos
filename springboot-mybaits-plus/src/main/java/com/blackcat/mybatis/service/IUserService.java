package com.blackcat.mybatis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blackcat.mybatis.entity.User;

/**
 * <p> ：
 * @author : blackcat
 * @date : 2020/1/18 12:59
 */
public interface IUserService extends IService<User> {

    /**
     * 添加新用户
     */
    Object addUser(String userName);

    /**
     * 查询用户列表
     */
    Object findUserList(Integer pageNo, Integer pageSize);
}
