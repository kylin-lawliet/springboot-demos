package com.blackcat.mybatis.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blackcat.mybatis.entity.SysMenu;
import com.blackcat.mybatis.vo.BaseConditionVO;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author blackcat
 * @since 2020-01-26
 */
public interface SysMenuService extends IService<SysMenu> {
    Page selectPage(BaseConditionVO vo);

    /**
     * <p> : 分页查询
     * @author : blackcat
     * @date : 2020/1/19 13:27
     * @param vo 筛选条件封装
     * @return  List<SysMenu>
     */
    PageInfo<SysMenu> findPageBreakByCondition(BaseConditionVO vo);

    /**
     * <p> : 获取用户的资源列表
     * @author : blackcat
     * @date : 2020/1/17 14:33
     * @param map 筛选条件
     * @return List<SysMenu>
     */
    List<SysMenu> listUserMenu(Map<String, Object> map);

    /**
     * <p> : 获取所有可用的菜单资源
     * @author : blackcat
     * @date : 2020/1/17 14:11
     * @return List<MenuExtend> 权限集合
     */
    List<SysMenu> listAllAvailableMenu();

    /**
     * <p> : 批量删除
     * @author : blackcat
     * @date : 2020/1/20 21:46
     */
    void deleteBatchIds(Long[] ids);
}
