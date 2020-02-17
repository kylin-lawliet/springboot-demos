package com.blackcat.mybatis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blackcat.mybatis.entity.User;
import com.blackcat.mybatis.mapper.UserMapper;
import com.blackcat.mybatis.service.IUserService;
import lombok.Data;
import org.springframework.stereotype.Service;

/**
 * <p> ：
 * @author : blackcat
 * @date : 2020/1/18 13:01
 */
@Service("userService")
@Data
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public Object addUser(String userName) {
        User user = new User(null, userName);
        int ret = baseMapper.insert(user);
        return ret;
    }

    @Override
    public Object findUserList(Integer pageNo, Integer pageSize) {
        IPage<User> page = new Page<>(pageNo, pageSize);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("USER_ID");
        IPage<User> userIPage = baseMapper.selectPage(page, wrapper);
        return userIPage;
    }
}
