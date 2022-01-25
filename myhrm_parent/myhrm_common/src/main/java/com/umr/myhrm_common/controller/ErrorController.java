package com.umr.myhrm_common.controller;

import com.umr.myhrm_common.entity.Result;
import com.umr.myhrm_common.entity.ResultCode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * shiro 登录认证 & 权限认证 失败时访问的接口
 */
@RestController
@CrossOrigin
public class ErrorController {

    //认证失败公共跳转接口
    @RequestMapping("/autherror")
    public Result autherror(int code) {
        return code == 1 ? new Result(ResultCode.UNAUTHENTICATED) : new Result(ResultCode.UNAUTHORISE);
    }
}
