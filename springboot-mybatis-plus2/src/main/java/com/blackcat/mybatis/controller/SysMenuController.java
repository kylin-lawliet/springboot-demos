package com.blackcat.mybatis.controller;


import com.blackcat.mybatis.entity.SysMenu;
import com.blackcat.mybatis.service.SysMenuService;
import com.blackcat.mybatis.util.ResponseStatusEnum;
import com.blackcat.mybatis.util.ResultUtil;
import com.blackcat.mybatis.vo.BaseConditionVO;
import com.blackcat.mybatis.vo.PageResult;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author blackcat
 * @since 2020-01-26
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController {

    @Resource
    private SysMenuService sysMenuService;

    @RequestMapping("/test")
    public String test(){
        sysMenuService.getById(1);
        return "ok";
    }

    @RequestMapping("/list")
    public PageResult list(BaseConditionVO vo){
        PageInfo<SysMenu> pageInfo = sysMenuService.findPageBreakByCondition(vo);
        return ResultUtil.tablePage(pageInfo);
    }

    @PostMapping(value = "/add")
    public ResultUtil add(SysMenu menu) {
        sysMenuService.save(menu);
        return ResultUtil.ok(String.valueOf(ResponseStatusEnum.SUCCESS));
    }

    @PostMapping(value = "/remove")
    public ResultUtil remove(Long[] ids) {
        if (null == ids) {
            return ResultUtil.error(String.valueOf(ResponseStatusEnum.REMOVE_ERROR));
        }
        sysMenuService.deleteBatchIds(ids);
        return ResultUtil.ok("成功删除 [" + ids.length + "] 个数据");
    }

    @PostMapping("/get/{id}")
    public ResultUtil get(@PathVariable Long id) {
        return ResultUtil.ok().put("data",sysMenuService.getById(id));
    }

    @PostMapping("/edit")
    public ResultUtil edit(SysMenu menu) {
        try {
            sysMenuService.updateById(menu);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(String.valueOf(ResponseStatusEnum.SAVE_ERROR));
        }
        return ResultUtil.ok(String.valueOf(ResponseStatusEnum.SUCCESS));
    }
}
