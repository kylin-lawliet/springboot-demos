package ${package.Mapper};

import ${package.Entity}.${com.blackcat.redis.entity};
import ${cfg.projectPath}.vo.BaseConditionVO;
import ${superMapperClassPackage};
import java.util.List;
/**
 * <p> ${table.comment!} Mapper 接口
 * @author ${author}
 * @date ${date}
 */
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${com.blackcat.redis.entity}>
<#else>
public interface ${table.mapperName} extends ${superMapperClass}<${com.blackcat.redis.entity}> {

}
</#if>
