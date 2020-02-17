package com.blackcat.shiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blackcat.shiro.entity.SysRole;
import com.blackcat.shiro.mapper.SysRoleMapper;
import com.blackcat.shiro.service.SysRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p> 角色表 服务实现类
 * @author blackcat
 * @date 2020-02-03
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Override
    public List<SysRole> listRolesByUserId(Integer userId) {
        return sysRoleMapper.listRolesByUserId(userId);
    }

}
