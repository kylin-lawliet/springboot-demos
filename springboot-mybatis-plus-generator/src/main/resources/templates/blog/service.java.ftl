package ${package.Service};

import ${package.Entity}.${entity};
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
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

  /**
   * <p> : 批量删除
   * @author : blackcat
   * @date : ${date}
   * @param ids 主键
   */
  void deleteBatchIds(Long[] ids);
}
</#if>
