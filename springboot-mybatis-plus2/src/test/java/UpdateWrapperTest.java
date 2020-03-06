import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.blackcat.mybatis.entity.SysRoleMenu;
import com.blackcat.mybatis.service.SysRoleMenuService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * <p> 描述 ：UpdateWrapper示例
 * - - - 基于mybatis 3.3.0
 * - - - 只是代码示写法例
 * - - - 查询条件与QueryWrapper相同不在重写
 * @author : blackcat
 * @date : 2020/3/6 13:49
 * @http https://mp.baomidou.com/guide/wrapper.html
 */
@SpringBootTest
public class UpdateWrapperTest {

    @Resource
    SysRoleMenuService iSysRoleMenuService;

    public void UpdateWrapper() {
        UpdateWrapper<SysRoleMenu> updateWrapper  = new UpdateWrapper<>();
        updateWrapper.set("name", "老李头");// name='老李头'
        updateWrapper.set("name", "");// 数据库字段值变为空字符串
        updateWrapper.set("name", null);// 数据库字段值变为null
        updateWrapper.setSql("name = '老李头'");// name='老李头'

    }

    /**
     * <p> 描述 : 删除
     * - - - UpdateWrapper 删除的查询条件
     * @author : blackcat
     * @date  : 2020/3/6 13:51
    */
    @Test
    public void delete() {
        String roleId = "1";
        String menuId="1,2,3";
        String[] resourcesArr = menuId.split(",");
        UpdateWrapper<SysRoleMenu> updateWrapper  = new UpdateWrapper<>();
        updateWrapper.eq("role_id", roleId);
        updateWrapper.in("menu_id",resourcesArr);
        iSysRoleMenuService.remove(updateWrapper);
    }

    /**
     * <p> 描述 : 根据 UpdateWrapper 条件，更新记录
     * - - - entity 实体对象 (set 值,不能为 null)
     * - - - updateWrapper 修改查询条件
     * @author : blackcat
     * @date  : 2020/3/6 13:52
    */
    @Test
    public void update() {
        // 方法一
        UpdateWrapper<SysRoleMenu> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("name", "老李头");// name='老李头'

        // 方法二
        //修改值
        SysRoleMenu menu = new SysRoleMenu();
        menu.setMenuId((long) 1);
        //修改条件
        UpdateWrapper<SysRoleMenu> menuUpdateWrapper = new UpdateWrapper<>();
        menuUpdateWrapper.eq("id", "1");
        iSysRoleMenuService.update(menu, menuUpdateWrapper);
    }
}
