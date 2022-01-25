package com.umr.myhrm_common.exception;

import com.umr.myhrm_common.entity.ResultCode;
import lombok.Getter;

/**
 * 通用异常
 * 名字起错了，就它当是CommonException吧
 */
@Getter
public class NoResultException extends Exception{

    private ResultCode resultCode;

    public NoResultException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }
}
