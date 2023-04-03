package com.hcm.common.core.domain;

import com.hcm.common.enums.ResultCodeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author pc
 */
@Data
public class ResultVO<T> implements Serializable {
    /**
     * 状态码
     */
    private Integer code;


    /**
     * 状态信息
     */
    private String msg;


    /**
     * 返回对象
     */
    private T data;

    public ResultVO() {
    }

    public ResultVO(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultVO(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> ResultVO<T> success() {
        return new ResultVO<T>(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage());
    }

    public static <T> ResultVO<T> success(String msg) {
        return new ResultVO<T>(ResultCodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> ResultVO<T> success(T data) {
        return new ResultVO<T>(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage(), data);
    }

    public static <T> ResultVO<T> success(T data, String msg) {
        return new ResultVO<T>(ResultCodeEnum.SUCCESS.getCode(), msg, data);
    }

    public static <T> ResultVO<T> fail() {
        return new ResultVO<T>(ResultCodeEnum.FAILED.getCode(), ResultCodeEnum.FAILED.getMessage());
    }

    public static <T> ResultVO<T> fail(T data) {
        return new ResultVO<T>(ResultCodeEnum.FAILED.getCode(), ResultCodeEnum.FAILED.getMessage(), data);
    }

    public static <T> ResultVO<T> fail( String msg,int code) {
        return new ResultVO<T>(code, msg);
    }

    public static <T> ResultVO<T> fail(T data, String msg) {
        return new ResultVO<T>(ResultCodeEnum.FAILED.getCode(), msg, data);
    }


}
