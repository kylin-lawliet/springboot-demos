package com.blackcat.shiro.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blackcat.shiro.entity.SysMenu;
import com.blackcat.shiro.entity.SysRole;
import com.blackcat.shiro.entity.SysUser;
import com.blackcat.shiro.service.SysMenuService;
import com.blackcat.shiro.service.SysRoleService;
import com.blackcat.shiro.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p> 描述 ：身份认证
 * @author : blackcat
 * @date : 2020/2/3 10:54
 */
public class ShiroRealm extends AuthorizingRealm {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysMenuService sysMenuService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 用户信息
        SysUser sysUser = (SysUser) principalCollection.getPrimaryPrincipal();
        // 赋予角色
        List<SysRole> roleList = sysRoleService.listRolesByUserId(sysUser.getId());
        roleList.forEach(role -> info.addRole(role.getName()));
        // 赋予权限
        List<SysMenu> menusList = sysMenuService.listByUserId(sysUser.getId());
        menusList.forEach(menu -> {
            if (StringUtils.isNotBlank(menu.getPermission())) {
                info.addStringPermission(menu.getPermission());
            }
        });
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        String password = new String((char[]) authenticationToken.getCredentials());

        SysUser user = sysUserService.getOne(new QueryWrapper<SysUser>().eq("username", username));
        //可以在这里直接对用户名校验,或者调用 CredentialsMatcher 校验
        if (user == null) {
            throw new UnknownAccountException("用户名或密码错误！");
        }
        if (!password.equals(user.getPassword())) {
            throw new IncorrectCredentialsException("用户名或密码错误！");
        }
        if ("1".equals(user.getStatus())) {
            throw new LockedAccountException("账号已被锁定,请联系管理员！");
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user,user.getPassword(), getName());
        return info;
    }
}
