import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blackcat.mybatis.entity.SysMenu;
import com.blackcat.mybatis.entity.SysUser;
import com.blackcat.mybatis.mapper.SysMenuMapper;
import com.blackcat.mybatis.service.SysUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> 描述 ：QueryWrapper查询示例
 * - - - 基于mybatis 3.3.0
 * - - - 只是代码示写法例
 * @author : blackcat
 * @date : 2020/3/6 13:41
 * @http https://mp.baomidou.com/guide/wrapper.html
 */
@SpringBootTest
public class QueryWrapperTest {

    @Resource
    private SysUserService iSysUserService;
    @Resource
    private SysMenuMapper menuMapper;

    /**
     * <p> 描述 : QueryWrapper条件拼装
     * @author : blackcat
     * @date  : 2020/3/6 13:53
    */
    @Test
    public void QueryWrapper(){
        // boolean condition表示该条件是否加入最后生成的sql中

        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        Map<String, Object> map = new HashMap<>();
        map.put("id",1);
        map.put("name","老王");
        map.put("age",null);
        /*
        map : key为数据库字段名,value为字段值
        null2IsNull : 为true则在map的value为null时调用 isNull 方法,为false时则忽略value为null的
        */
        queryWrapper.allEq(map);// id = 1 and name = '老王' and age is null
        queryWrapper.allEq(map,false);// id = 1 and name = '老王'
        // filter : 过滤函数,是否允许字段传入比对条件中
        queryWrapper.allEq((k,v) -> k.indexOf("a") >= 0, map);
        queryWrapper.allEq((k,v) -> k.indexOf("a") >= 0, map, false);

        queryWrapper.isNull("name");// name is null
        queryWrapper.isNotNull("name");// name is not null
        queryWrapper.ge("age", 12);// age>=12
        queryWrapper.gt("age", 12);// age>12
        queryWrapper.lt("age", 12);// age<12
        queryWrapper.le("age", 12);// age<=12
        queryWrapper.eq("name", "asd");// name='asd'
        queryWrapper.ne("name", "asd");// name!='asd'
        queryWrapper.in("age", 1,2,3);// age in(1,2,3)
        queryWrapper.notIn("age", 1,2,3);// age not in(1,2,3)
        queryWrapper.likeLeft("name", "asd");// name like '%asd'
        queryWrapper.likeRight("name", "asd");// name like 'asd%'
        queryWrapper.like("name", "asd");// name like '%asd%'
        queryWrapper.notLike("name", "asd");// name not like '%asd%'
        queryWrapper.between("age", 10,12);// age between 10 and 12
        queryWrapper.notBetween("age", 10,12);// age not between 10 and 12
        queryWrapper.groupBy("name","age");// group by name,age
        queryWrapper.orderByAsc("name","age");// order by name,age asc
        queryWrapper.orderByDesc("name","age");// order by name,age desc
        queryWrapper.orderBy(true, true, "id", "name");// order by id ASC,name ASC

        // id in(select id form user where username='zhang')
        queryWrapper.inSql("id","select id form user where username='zhang'");
        // age in (1,2,3,4,5,6)
        queryWrapper.inSql("age", "1,2,3,4,5,6");
        // id not in(select id form user where username='zhang')
        queryWrapper.notInSql("id","select id form user where username='zhang'");
        // age not in (1,2,3,4,5,6)
        queryWrapper.notInSql("age", "1,2,3,4,5,6");

        // having sum(age) > 10 and name='zhang'
        queryWrapper.having("sum(age)>{0} and name={1}",10,"zhang");
        // having sum(age) > 10
        queryWrapper.having("sum(age) > 10");

        // 该方法可用于数据库函数 动态入参的params对应前面applySql内部的{index}部分.这样是不会有sql注入风险的,反之会有!
        // data_format(create_data,'%y-%m-%d')='2020-03-06'
        queryWrapper.apply("data_format(create_data,'%y-%m-%d')={0}","2020-03-06");
        // id = 1
        queryWrapper.apply("id = 1");

        // exists(select id form user where username='zhang')
        queryWrapper.exists("select id form user where username='zhang'");
        // not exists(select id form user where username='zhang')
        queryWrapper.notExists("select id form user where username='zhang'");
        // 无视优化规则直接拼装SQL最后部分 只能调用一次 多次调用以最后一次为准 有SQL注入风险
        queryWrapper.last("limit 1");

        // 多条件拼装 多条件连续拼接默认为and拼接
        // 如：name='asd' and age in(1,2,3)
        queryWrapper.eq("name", "asd").in("age", 1,2,3);
        // 使用or 如：name='asd' or age in(1,2,3)
        queryWrapper.eq("name", "asd").or().in("age", 1,2,3);
        // 嵌套
        queryWrapper.eq("name", "asd").and(i->i.in("age", 1,2,3).or().eq("mail", "qwer@.com") );
        queryWrapper.eq("name", "asd").or(i->i.in("age", 1,2,3).eq("mail", "qwer@.com") );

        // 正常嵌套 不带 AND 或者 OR
        // (name = '李白' and status <> '活着')
        queryWrapper.nested(i -> i.eq("name", "李白").ne("status", "活着"));

        // select
        /*
        select(String... sqlSelect)
        select(Predicate<TableFieldInfo> predicate)
        select(Class<T> entityClass, Predicate<TableFieldInfo> predicate)
        以上方分法为两类.
        第二类方法为:过滤查询字段(主键除外),入参不包含 class 的调用前需要wrapper内的entity属性有值!
        这两类方法重复调用以最后一次为准
        */
        queryWrapper.select("id", "name", "age");
        queryWrapper.select(i -> i.getProperty().startsWith("test"));

    }



    /**
     * <p> 描述 : PageHelper分页插件
     * @author : blackcat
     * @date  : 2020/2/1 11:00
     */
    @Test
    public void page(){
        PageHelper.startPage(1, 3);
        List<SysMenu> list = menuMapper.findPageBreakByCondition(null); // 分页查询
        PageInfo<SysMenu> bean = new PageInfo<>(list);
        bean.setList(list);
    }

    /**
     * <p> 描述 : QueryWrapper分页
     * @author : blackcat
     * @date  : 2020/2/1 10:58
     */
    @Test
    public void page1(){
        Page<SysUser> page = new Page<>(1, 3);
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(SysUser::getNickname,"111")
                .like(SysUser::getMobile,"11")
                .orderByDesc(SysUser::getCreateTime);
        iSysUserService.page(page, queryWrapper);
    }

    /**
     * <p> 描述 :
     * @author : blackcat
     * @date  : 2020/2/1 11:24
     */
//    @Test
//    public void page2() {
//        Page<SysUser> page = new Page<>(1, 5);
//        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
//        queryWrapper.like("username", "111");
//        IPage<Map<String, SysUser>> mapIPage = (IPage<Map<String, SysUser>>) iSysUserService.pageMaps(queryWrapper);
//        System.out.println(mapIPage);
//    }

    /**
     * <p> 描述 : 返回的是一条实体记录，当出现多条时会报错
     * @author : blackcat
     * @date  : 2020/2/1 11:02
     */
    @Test
    public void selectOne() {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", "lqf");
        iSysUserService.getOne(queryWrapper);
    }

    /**
     * <p> 描述 : 根据 Wrapper 条件，查询总记录数
     * @author : blackcat
     * @date  : 2020/2/1 11:06
     */
    @Test
    public void selectCount() {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", "lqf");
        iSysUserService.count(queryWrapper);
    }


    /**
     * <p> 描述 : 查询全部记录
     * @author : blackcat
     * @date  : 2020/2/1 11:08
     * queryWrapper 实体对象封装操作类（可以为 null）为null查询全部
     */
    @Test
    public void selectList() {
        List<SysUser> list = iSysUserService.list();
    }

    /**
     * <p> 描述 : 根据 Wrapper 条件查询记录
     * @author : blackcat
     * @date  : 2020/2/1 11:21
     * queryWrapper 实体对象封装操作类（可以为 null）
     * 返回 List<Map<String, Object>> 类型数据
     */
    @Test
    public void selectMaps() {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("name");
        //iSysUserService.listMaps(QueryWrapper);
//        List<Map<String, Object>> maps = mapper.selectMaps(queryWrapper);
//        for (Map<String, Object> map : maps) {
//            System.out.println(map);
//        }
    }
}
