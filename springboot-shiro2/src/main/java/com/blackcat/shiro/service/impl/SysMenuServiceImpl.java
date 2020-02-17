package com.blackcat.shiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blackcat.shiro.entity.SysMenu;
import com.blackcat.shiro.mapper.SysMenuMapper;
import com.blackcat.shiro.service.SysMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p> 权限表 服务实现类
 * @author blackcat
 * @date 2020-02-03
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> listByUserId(Integer userId) {
        return sysMenuMapper.listByUserId(userId);
    }
}
