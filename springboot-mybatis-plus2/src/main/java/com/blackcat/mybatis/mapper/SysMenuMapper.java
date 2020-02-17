package com.blackcat.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blackcat.mybatis.entity.SysMenu;
import com.blackcat.mybatis.vo.BaseConditionVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author blackcat
 * @since 2020-01-26
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    /**
     * <p> : 分页查询
     * @author : blackcat
     * @date : 2020/1/19 13:27
     * @param vo 条件封装
     * @return  List<SysMenu>
     */
    List<SysMenu> findPageBreakByCondition(BaseConditionVO vo);

    /**
     * <p> : 获取用户的资源列表
     * @author : blackcat
     * @date : 2020/1/17 14:33
     * @param map
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
}
