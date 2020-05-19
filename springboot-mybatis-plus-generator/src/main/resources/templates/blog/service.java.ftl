package ${package.Service};

import ${package.Entity}.${com.blackcat.redis.entity};
import ${superServiceClassPackage};
import ${cfg.projectPath}.vo.BaseConditionVO;
import com.github.pagehelper.PageInfo;
import java.util.List;

/**
 * <p> ${table.comment!} 服务类
 * @author ${author}
 * @date ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${com.blackcat.redis.entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${com.blackcat.redis.entity}> {

}
</#if>
