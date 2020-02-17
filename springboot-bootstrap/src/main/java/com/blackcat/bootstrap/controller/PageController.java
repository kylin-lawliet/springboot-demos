package com.blackcat.bootstrap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p> 描述 ：
 *
 * @author : blackcat
 * @date : 2020/2/6 17:09
 */
@Controller
public class PageController {

    /**
     * <p> 描述 : 页面样式示例
     * @author : blackcat
     * @date  : 2020/2/6 17:10
    */
    @RequestMapping("/style")
    public String style(){
        return "style";
    }

    /**
     * <p> 描述 : 徽章样式示例
     * @author : blackcat
     * @date  : 2020/2/6 17:10
     */
    @RequestMapping("/header")
    public String header(){
        return "header";
    }


}
