package com.blackcat.shiro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blackcat.shiro.entity.SysMenu;

import java.util.List;

/**
 * <p> 权限表 Mapper 接口
 * @author blackcat
 * @date 2020-02-03
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> listByUserId(Integer userId);
}
