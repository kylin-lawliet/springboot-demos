package com.blackcat.shiro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blackcat.shiro.entity.SysRole;

import java.util.List;

/**
 * <p> 角色表 服务类
 * @author blackcat
 * @date 2020-02-03
 */
public interface SysRoleService extends IService<SysRole> {

  List<SysRole> listRolesByUserId(Integer userId);
}
