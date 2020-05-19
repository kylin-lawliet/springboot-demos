<!DOCTYPE html>
<html lang="en">
<head>

</head>
<body>
<#--<@shiro.guest>游客显示的信息</@shiro.guest> 自定义标签写法-->

<shiro:hasPermission name="user:add"><a href="/user/add">点击添加固定用户信息(后台写死,方便测试)</a></shiro:hasPermission><br/>
<shiro:hasPermission name="user:del"><a href="/user/del">点击删除固定用户信息(后台写死,方便测试)</a></shiro:hasPermission><br/>
<shiro:hasPermission name="user:view"><a href="/user/view">显示此内容表示拥有查看用户列表的权限</a></shiro:hasPermission><br/>



<!-- 用户没有身份验证时显示相应信息，即游客访问信息 -->
<shiro:guest>游客显示的信息</shiro:guest><br/>
<!-- 用户已经身份验证/记住我登录后显示相应的信息 -->
<shiro:user>用户已经登录过了</shiro:user><br/>
<!-- 用户已经身份验证通过，即Subject.login登录成功，不是记住我登录的 -->
<shiro:authenticated>不是记住我登录</shiro:authenticated><br/>
<!-- 显示用户身份信息，通常为登录帐号信息，默认调用Subject.getPrincipal()获取，即Primary Principal -->
<shiro:principal></shiro:principal><br/>
<!--用户已经身份验证通过，即没有调用Subject.login进行登录，包括记住我自动登录的也属于未进行身份验证，与guest标签的区别是，该标签包含已记住用户 -->
<shiro:notAuthenticated>已记住用户</shiro:notAuthenticated><br/>
<!-- 相当于Subject.getPrincipals().oneByType(String.class) -->
<shiro:principal type="java.lang.String"/><br/>
<!-- 相当于((com.blackcat.redis.entity.User)Subject.getPrincipals()).getUsername() -->
<shiro:principal property="username"/><br/>
<!-- 如果当前Subject有角色将显示body体内容 name="角色名" -->
<shiro:hasRole name="admin">这是admin角色</shiro:hasRole><br/>
<!-- 如果当前Subject有任意一个角色（或的关系）将显示body体内容。 name="角色名1,角色名2..." -->
<shiro:hasAnyRoles name="admin,vip">用户拥有admin角色 或者 vip角色</shiro:hasAnyRoles><br/>
<!-- 如果当前Subject没有角色将显示body体内容 -->
<shiro:lacksRole name="admin">如果不是admin角色,显示内容</shiro:lacksRole><br/>
<!-- 如果当前Subject有权限将显示body体内容 name="权限名" -->
<shiro:hasPermission name="user:add">用户拥有添加权限</shiro:hasPermission><br/>
<!-- 用户同时拥有以下两种权限,显示内容 -->
<shiro:hasAllPermissions name="user:add,user:view">用户同时拥有列表权限和添加权限</shiro:hasAllPermissions><br/>
<!-- 用户拥有以下权限任意一种 -->
<shiro:hasAnyPermissions name="user:view,user:delete">用户拥有列表权限或者删除权限</shiro:hasAnyPermissions><br/>
<!-- 如果当前Subject没有权限将显示body体内容 name="权限名" -->
<shiro:lacksPermission name="user:add">如果用户没有添加权限，显示的内容</shiro:lacksPermission><br/>



<a href="/logout">点我注销</a>
</body>
</html>