package com.blackcat.shiro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blackcat.shiro.entity.SysMenu;

import java.util.List;

/**
 * <p> 权限表 服务类
 * @author blackcat
 * @date 2020-02-03
 */
public interface SysMenuService extends IService<SysMenu> {

  List<SysMenu> listByUserId(Integer userId);
}
