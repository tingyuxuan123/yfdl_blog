package com.yfdl.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)  //为空的不序列化前台不会回到这个字段
public class R<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    public R() {
        this.code = AppHttpCodeEnum.SUCCESS.getCode();
        this.msg = AppHttpCodeEnum.SUCCESS.getMsg();
    }

    public R(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public R(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public R(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static R errorResult(int code, String msg) {
        R result = new R();
        return result.error(code, msg);
    }
    public static R successResult() {
        R result = new R();
        return result;
    }
    public static R successResult(int code, String msg) {
        R result = new R();
        return result.success(code, null, msg);
    }

    public static R successResult(Object data) {
        R result = setAppHttpCodeEnum(AppHttpCodeEnum.SUCCESS, AppHttpCodeEnum.SUCCESS.getMsg());
        if(data!=null) {
            result.setData(data);
        }
        return result;
    }

    public static R errorResult(AppHttpCodeEnum enums){
        return setAppHttpCodeEnum(enums,enums.getMsg());
    }

    public static R errorResult(AppHttpCodeEnum enums, String msg){
        return setAppHttpCodeEnum(enums,msg);
    }

    public static R setAppHttpCodeEnum(AppHttpCodeEnum enums){
        return successResult(enums.getCode(),enums.getMsg());
    }

    private static R setAppHttpCodeEnum(AppHttpCodeEnum enums, String msg){
        return successResult(enums.getCode(),msg);
    }


    public R<?> error(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public R<?> success(Integer code,T data){
        this.code=code;
        this.data=data;
        return this;
    }

    public R<?> success(Integer code,T data,String msg){
        this.code=code;
        this.data=data;
        this.msg=msg;
        return this;
    }

    public R<?> success(T data){
        this.code=code;
        this.data=data;
        return this;
    }


}
