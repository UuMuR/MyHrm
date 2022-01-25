package com.umr.myhrm_common.entity;

/**
 * 枚举类型：结果状态
 */

public enum ResultCode {

    SUCCESS(true,10000,"操作成功！"),
    //---系统错误返回码-----
    FAIL(false,10001,"操作失败"),
    UNAUTHENTICATED(false,10002,"您还未登录"),
    UNAUTHORISE(false,10003,"权限不足"),
    SERVER_ERROR(false,99999,"抱歉，系统繁忙，请稍后重试！"),
    NONE_RESULT(false, 10004, "无相关信息！"),

    //---用户操作返回码  2xxxx----
    LOGIN_ERROR(false, 20001, "用户名或密码错误"),


    //---企业操作返回码  3xxxx----
    //---权限操作返回码  4xxxx----
    //---其他操作返回码  5xxxx----

    //---暂未解决的问题  9xxxx----
    ADMIN_DEPARTMENT(false, 90001, "该模块下平台管理员操作问题尚未解决"),
    ADMIN_USERS(false, 90002, "该模块下平台管理员操作问题尚未解决");

    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;

    ResultCode(boolean success,int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public boolean success() {
        return success;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

}
