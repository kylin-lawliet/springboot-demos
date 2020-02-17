package ${package.Controller};


import org.springframework.web.bind.annotation.RequestMapping;


import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};
import ${cfg.projectPath}.vo.BaseConditionVO;
import ${cfg.projectPath}.util.ResultUtil;
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
    private ${table.serviceName} i${entity}Service;

    /**
    * <p> 描述 : 获取列表数据
    * @author : blackcat
    * @date  : ${date}
    */
    @RequestMapping("/list")
    public PageResult list(BaseConditionVO vo){
        PageInfo<${entity}> pageInfo = i${entity}Service.findPageBreakByCondition(vo);
        return ResultUtil.tablePage(pageInfo);
    }

    /**
    * <p> 描述 : 添加
    * @author : blackcat
    * @date  : ${date}
    */
    @PostMapping(value = "/add")
    public ResultUtil add(${entity} entity) {
        i${entity}Service.save(entity);
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
           i${entity}Service.deleteBatchIds(ids);
        return ResultUtil.ok("成功删除 [" + ids.length + "] 个数据");
    }

    /**
    * <p> 描述 : 获取详情
    * @author : blackcat
    * @date  : ${date}
    */
    @PostMapping("/get/{id}")
    public ResultUtil get(@PathVariable Long id) {
        return ResultUtil.ok().put("data",i${entity}Service.getById(id));
    }

    /**
    * <p> 描述 : 编辑
    * @author : blackcat
    * @date  : ${date}
    */
    @PostMapping("/edit")
    public ResultUtil edit(${entity} entity) {
        try {
            i${entity}Service.updateById(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(String.valueOf(ResponseStatusEnum.SAVE_ERROR));
        }
        return ResultUtil.ok(String.valueOf(ResponseStatusEnum.SUCCESS));
    }
}
</#if>
