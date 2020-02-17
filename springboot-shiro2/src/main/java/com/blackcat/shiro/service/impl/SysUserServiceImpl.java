package com.blackcat.shiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blackcat.shiro.entity.SysUser;
import com.blackcat.shiro.mapper.SysUserMapper;
import com.blackcat.shiro.service.SysUserService;
import org.springframework.stereotype.Service;


/**
 * <p> 系统用户表 服务实现类
 * @author blackcat
 * @date 2020-02-03
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

}
