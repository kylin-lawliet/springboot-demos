package com.blackcat.mybatis.tag;

import com.blackcat.mybatis.service.SysMenuService;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p> ：自定义的freemarker标签
 * @author : blackcat
 * @date : 2020/1/17 13:53
 */
@Component
public class CustomTag implements TemplateDirectiveModel {

    private static final String METHOD_KEY = "method";
    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels, TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);
        if (map.containsKey(METHOD_KEY)) {
            String method = map.get(METHOD_KEY).toString();
            switch (method) {// 标签属性
                case "availableMenus":
                    // 获取所有可用的菜单资源
                    environment.setVariable("availableMenus", builder.build().wrap(sysMenuService.listAllAvailableMenu()));
                    break;
                case "menus":
                    Integer userId = null;
                    if (map.containsKey("userId")) {
                        String userIdStr = map.get("userId").toString();
                        if(StringUtils.isEmpty(userIdStr)){
                            return;
                        }
                        userId = Integer.parseInt(userIdStr);
                    }
                    Map<String, Object> params = new HashMap<>(2);
                    params.put("type", "menu");
                    params.put("userId", userId);
                    // 获取用户的资源列表
                    environment.setVariable("menus", builder.build().wrap(sysMenuService.listUserMenu(params)));
                    break;
                default:
                    break;
            }
        }
        templateDirectiveBody.render(environment.getOut());
    }
}
