package com.blackcat.mybatis.config;

import com.blackcat.mybatis.tag.CustomTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * <p> ：freemarker配置类
 * @author : blackcat
 * @date : 2020/1/17 13:52
 */
@Configuration
public class FreeMarkerConfig {

    @Autowired(required = false)
    protected freemarker.template.Configuration configuration;

    @Autowired(required = false)
    protected CustomTag customTag;

    /**
     * <p> 添加自定义标签
     * @author : blackcat
     * @date : 2020/1/17 13:53
    */
    @PostConstruct
    public void setSharedVariable() {
        // 自定义标签
        configuration.setSharedVariable("blog", customTag);
        // shiro标签
        //configuration.setSharedVariable("shiro", new ShiroTags());
    }
}
