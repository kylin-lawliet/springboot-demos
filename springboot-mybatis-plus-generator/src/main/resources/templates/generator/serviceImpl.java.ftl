package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${cfg.projectPath}.vo.BaseConditionVO;
import ${superServiceImplClassPackage};
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;


/**
 * <p> ${table.comment!} 服务实现类
 * @author ${author}
 * @date ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    @Resource
    private ${table.mapperName} ${table.entityPath}Mapper;

    @Override
    public PageInfo<${entity}> findPageBreakByCondition(BaseConditionVO vo) {
        PageHelper.startPage(vo.getPageNumber(), vo.getPageSize());
        List<${entity}> list = ${table.entityPath}Mapper.findPageBreakByCondition(vo);
        PageInfo bean = new PageInfo<>(list);
        bean.setList(list);
        return bean;
    }

    @Override
    public void deleteBatchIds(Long[] ids) {
        ${table.entityPath}Mapper.deleteBatchIds(Arrays.asList(ids));
    }
}
</#if>
