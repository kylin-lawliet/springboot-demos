package com.blackcat.mybatis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blackcat.mybatis.entity.SysMenu;
import com.blackcat.mybatis.mapper.SysMenuMapper;
import com.blackcat.mybatis.service.SysMenuService;
import com.blackcat.mybatis.vo.BaseConditionVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author blackcat
 * @since 2020-01-26
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public Page selectPage(BaseConditionVO vo) {
        Page<SysMenu> page = new Page<>(vo.getPageNumber(), vo.getPageSize());
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .like(SysMenu::getPermission, vo.getKeywords())
            .like(SysMenu::getName, vo.getKeywords())
            .like(SysMenu::getUrl,vo.getKeywords())
            .orderByAsc(SysMenu::getParentId)
            .orderByDesc(SysMenu::getCreateTime);
        return sysMenuMapper.selectPage(page,queryWrapper);
    }

    @Override
    public PageInfo<SysMenu> findPageBreakByCondition(BaseConditionVO vo) {
        PageHelper.startPage(vo.getPageNumber(), vo.getPageSize());
        List<SysMenu> sysMenus = sysMenuMapper.findPageBreakByCondition(vo);
        PageInfo bean = new PageInfo<>(sysMenus);
        bean.setList(sysMenus);
        return bean;
    }

    @Override
    public List<SysMenu> listUserMenu(Map<String, Object> map) {
        return sysMenuMapper.listUserMenu(map);
    }

    @Override
    public List<SysMenu> listAllAvailableMenu() {
        return sysMenuMapper.listAllAvailableMenu();
    }

    @Override
    public void deleteBatchIds(Long[] ids) {
        sysMenuMapper.deleteBatchIds(Arrays.asList(ids));
    }
}
