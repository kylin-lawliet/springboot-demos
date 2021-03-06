package ${package.Controller};


import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import ${package.Service}.${table.serviceName};
import ${package.Entity}.${com.blackcat.redis.entity};
import ${cfg.projectPath}.vo.BaseConditionVO;
import ${cfg.projectPath}.object.PageResult;
import com.blackcat.blog.util.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p> ${table.comment!} 前端控制器
 * @author ${author}
 * @date ${date}
 */
@RestController
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    @Resource
    private ${table.serviceName} i${com.blackcat.redis.entity}Service;

    /**
    * <p> 描述 : 获取列表数据
    * @author : blackcat
    * @date  : ${date}
    */
    @RequestMapping("/list")
    public PageResult list(BaseConditionVO vo){
        Page<${com.blackcat.redis.entity}> page = new Page<>(vo.getPageNumber(), vo.getPageSize());
        QueryWrapper<${com.blackcat.redis.entity}> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(vo.getKeywords())){
        }
        i${com.blackcat.redis.entity}Service.page(page, queryWrapper);
        return ResultUtil.tablePage(page);
    }

    /**
    * <p> 描述 : 添加
    * @author : blackcat
    * @date  : ${date}
    */
    @PostMapping(value = "/add")
    public ResultUtil add(${com.blackcat.redis.entity} com.blackcat.redis.entity) {
        i${com.blackcat.redis.entity}Service.save(com.blackcat.redis.entity);
        return ResultUtil.ok(String.valueOf(ResponseStatusEnum.SUCCESS));
    }

    /**
    * <p> 描述 : 删除
    * @author : blackcat
    * @date  : ${date}
    */
    @PostMapping(value = "/remove")
    public ResultUtil remove(Long[] ids) {
        if (null == ids) {
           return ResultUtil.error(String.valueOf(ResponseStatusEnum.REMOVE_ERROR));
        }
        i${com.blackcat.redis.entity}Service.remove(new UpdateWrapper<${com.blackcat.redis.entity}>().in("id", ids));
        return ResultUtil.ok("成功删除 [" + ids.length + "] 个数据");
    }

    /**
    * <p> 描述 : 获取详情
    * @author : blackcat
    * @date  : ${date}
    */
    @PostMapping("/get/{id}")
    public ResultUtil get(@PathVariable Long id) {
        return ResultUtil.ok().put("data",i${com.blackcat.redis.entity}Service.getById(id));
    }

    /**
    * <p> 描述 : 编辑
    * @author : blackcat
    * @date : ${date}
    */
    @PostMapping("/edit")
    public ResultUtil edit(${com.blackcat.redis.entity} com.blackcat.redis.entity) {
    try {
    com.blackcat.redis.entity.setUpdateTime(LocalDateTime.now());
    i${com.blackcat.redis.entity}Service.updateById(entity);
    } catch (Exception e) {
    e.printStackTrace();
    return ResultUtil.error(String.valueOf(ResponseStatusEnum.SAVE_ERROR));
    }
    return ResultUtil.ok(String.valueOf(ResponseStatusEnum.SUCCESS));
    }
    }
