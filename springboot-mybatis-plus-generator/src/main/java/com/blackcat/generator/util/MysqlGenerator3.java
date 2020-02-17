package com.blackcat.generator.util;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * mybatis plus mysql 代码生成器
 * @author : blackcat
 * @date : 2020/1/6 17:22
 * 进阶版 自定义模板输出
*/
public class MysqlGenerator3 {

    public static void main(String[] args) {
        Map<String, String> param = new HashMap<>();
        param.put("projectPath","D:\\test");// 代码输出项目地址
        param.put("author","blackcat");// 作者
        param.put("url","localhost:3306/blog");// ip/数据库
        param.put("driverName","com.mysql.cj.jdbc.Driver");
        param.put("username","root");// 数据库用户
        param.put("password","111111");// 数据库密码
        param.put("open","true");// 是否打开输出目录
        param.put("parent","com.blackcat.blog.core");// 顶层包结构
        param.put("mapper","mapper");// 生成的mapper包名
        param.put("entity","entity");// 生成的entity包名
        param.put("service","service");// 生成的service包名
        param.put("serviceImpl","service.impl");// 生成的serviceImpl包名
        param.put("xml","mappers");// 生成的mapper.xml包名
        param.put("model","");// 生成的mapper.xml包名下的模块名称 空则无 如:mappers.shiro
        param.put("xmlName","Mapper");// 生成的mapper.xml的文件结尾名称如UserMapper.xml
        // 设置模板 freemarker模板:/templates/xx.ftl  velocity模板:/templates/xx.vm
        //param.put("htmlTemplatePath","/templates/list.html.ftl");//
        param.put("table","");//数据库表名，多个英文逗号分割  如果不设置就是生成所有的表
        //"sys_menu,sys_role,sys_role_menu,sys_user,sys_user_role"

        generator(param);
    }

    /* *
     * 代码生成
     * @author : blackcat
     * @date : 2020/1/7 13:44
     * @param [param]
    */
    private static void generator(Map<String, String> param){
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(param.get("projectPath") + "/src/main/java");
        gc.setAuthor(param.get("author"));
        gc.setOpen(Boolean.parseBoolean(param.get("open")));// 是否打开输出目录
        gc.setServiceName("%sService");// service 命名方式
        gc.setServiceImplName("%sServiceImpl");// service impl 命名方式
        gc.setMapperName("%sMapper");// 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setXmlName("%sMapper");// 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setFileOverride(true);// 是否覆盖已有文件
        gc.setActiveRecord(true);// 开启 ActiveRecord(活动记录) 模式
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML 开启 BaseResultMap
        gc.setBaseColumnList(false);// XML columList
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://"+param.get("url")+"?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC");
        dsc.setDriverName(param.get("driverName"));
        dsc.setUsername(param.get("username"));
        dsc.setPassword(param.get("password"));
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        if(StringUtils.isNotBlank(param.get("model"))){
            pc.setModuleName(param.get("model"));
        }
        pc.setParent(param.get("parent"));
        pc.setMapper(param.get("mapper"));
        pc.setEntity(param.get("entity"));
        pc.setService(param.get("service"));
        pc.setServiceImpl(param.get("serviceImpl"));
        pc.setXml(param.get("xml"));
        mpg.setPackageInfo(pc);

        // 自定义需要填充的字段
        //List<TableFill> tableFillList = new ArrayList<>();
        //如 每张表都有一个创建时间、修改时间如下是配置
        //TableFill createField = new TableFill("gmt_create", FieldFill.INSERT);
        //TableFill modifiedField = new TableFill("gmt_modified", FieldFill.INSERT_UPDATE);
        //tableFillList.add(createField);
        //tableFillList.add(modifiedField);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
//                map.put("abc", "自定义属性描述"); // 模板中，通过${cfg.abc}获取属性
                map.put("projectPath", param.get("parent"));
                this.setMap(map);
            }
        };
        /*// 模板引擎
        String htmlTemplatePath = param.get("htmlTemplatePath");
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义 XXlist.html 生成
        focList.add(new FileOutConfig(htmlTemplatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return param.get("projectPath") + "/" + tableInfo.getEntityName() + "/list.html";
            }
        });
        cfg.setFileOutConfigList(focList);*/
        mpg.setCfg(cfg);


        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        templateConfig.setService("templates/generator/service.java");
        templateConfig.setServiceImpl("templates/generator/serviceImpl.java");
        templateConfig.setMapper("templates/generator/mapper.java");
        templateConfig.setController("templates/generator/controller.java");
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        //数据库表映射到实体的命名策略，默认：不做任何改变，原样输出
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //数据库表字段映射到实体的命名策略，未指定按照 naming 执行
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);// 是否使用lombok
        strategy.setControllerMappingHyphenStyle(true);// 驼峰转连字符
        if (StringUtils.isNotEmpty(param.get("table"))) {
            strategy.setInclude(param.get("table").split(","));//要设置生成哪些表 如果不设置就是生成所有的表
        }
        // strategy.setSuperControllerClass("com.sxt.BaseController");// 公共父类
        // strategy.setSuperEntityColumns("person_id","person_name");// 写于父类中的公共字段
        // strategy.setExclude(new String[]{"test"}); // 排除生成的表
        // 自定义实体父类
        // strategy.setSuperEntityClass("com.baomidou.demo.TestEntity");
        // 自定义实体，公共字段
        // strategy.setSuperEntityColumns(new String[] { "test_id", "age" });
        // 自定义 mapper 父类
        // strategy.setSuperMapperClass("com.baomidou.demo.TestMapper");
        // 自定义 service 父类
        // strategy.setSuperServiceClass("com.baomidou.demo.TestService");
        // 自定义 service 实现类父类
        // strategy.setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl");
        // 自定义 controller 父类
        // strategy.setSuperControllerClass("com.baomidou.demo.TestController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        // strategy.setEntityBuilderModel(true);

        mpg.setStrategy(strategy);// 数据库表配置
        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！ 默认 Veloctiy
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}