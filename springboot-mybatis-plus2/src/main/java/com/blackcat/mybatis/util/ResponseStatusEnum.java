package com.blackcat.mybatis.util;

/**
 * <p> : controller返回状态枚举
 * @author : blackcat
 * @date : 2020/1/20 16:51
*/
public enum ResponseStatusEnum {

    /**
     * @param code 错误码
     * @param message 错误信息
    */
    SUCCESS(200, "操作成功！"),
    ERROR(500, "服务器未知错误！"),
    SAVE_ERROR(500, "资源修改失败！"),
    REMOVE_ERROR(500, "请至少选择一条记录！"),
    UNAUTHORIZED(500, "尚未登录！"),
    FORBIDDEN(500, "您没有操作权限！"),
    NOT_FOUND(500, "资源不存在！"),
    LOGIN_ERROR(500, "账号或密码错误！"),
    USER_EXIST(500, "已存在的用户！"),
    INVALID_AUTHCODE(500, "手机验证码无效！"),
    INVALID_TOKEN(500, "无效的TOKEN，您没有操作权限！"),
    INVALID_ACCESS(500, "无效的请求，该请求已过期！"),
    DELETE_ERROR(500, "删除失败！");

    private Integer code;
    private String message;

    ResponseStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResponseStatusEnum getResponseStatus(String message) {
        for (ResponseStatusEnum ut : ResponseStatusEnum.values()) {
            if (ut.getMessage() == message) {
                return ut;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
