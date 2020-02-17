package com.blackcat.shiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blackcat.shiro.entity.SysUserRole;
import com.blackcat.shiro.mapper.SysUserRoleMapper;
import com.blackcat.shiro.service.SysUserRoleService;
import org.springframework.stereotype.Service;


/**
 * <p> 用户与角色关系表 服务实现类
 * @author blackcat
 * @date 2020-02-03
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

}
