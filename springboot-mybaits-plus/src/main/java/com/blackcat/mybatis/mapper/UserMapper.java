package com.blackcat.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blackcat.mybatis.entity.User;

import java.util.List;

/**
 * <p> ：Mapper
 * @author : blackcat
 * @date : 2020/1/18 12:57
 */
public interface UserMapper  extends BaseMapper<User> {
    /**
     * 获取用户信息
     */
    List<User> findAllUser();
}
