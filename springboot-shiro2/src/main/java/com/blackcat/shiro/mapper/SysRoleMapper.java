package com.blackcat.shiro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blackcat.shiro.entity.SysRole;

import java.util.List;

/**
 * <p> 角色表 Mapper 接口
 * @author blackcat
 * @date 2020-02-03
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> listRolesByUserId(Integer userId);
}
