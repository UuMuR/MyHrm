package com.umr.myhrm_common.handler;

import com.umr.myhrm_common.entity.Result;
import com.umr.myhrm_common.entity.ResultCode;
import com.umr.myhrm_common.exception.NoResultException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义公用异常处理器
 */
@ControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result error(HttpServletRequest request, HttpServletResponse response, Exception e) {
        //处理自己抛出的异常
        if (e.getClass() == NoResultException.class) {
            NoResultException nre = (NoResultException) e;
            return new Result(nre.getResultCode());
        }
        //授权异常
        if (e instanceof AuthorizationException) {
            return new Result(ResultCode.UNAUTHORISE);
        }
        return new Result(ResultCode.SERVER_ERROR);
    }
}
