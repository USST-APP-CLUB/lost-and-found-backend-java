package com.usst.weapp.lostandfound.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.usst.weapp.lostandfound.constants.enums.ResultCode;
import com.usst.weapp.lostandfound.constants.enums.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * @Author Sunforge
 * @Date 2021-07-11 21:15
 * @Version V1.0.0
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Response {
    private Integer code;
    private String message;
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    private Response(@NonNull ResultCode resultCode){
        this.code = resultCode.getCode();
        this.message = resultCode.getDescription();
    }
    private Response(@NonNull ResultCode resultCode, @NonNull String message){
        this.code = resultCode.getCode();
        this.message = message;
    }
    private Response(@NonNull ResultCode resultCode, @NonNull Object data){
        this.code = resultCode.getCode();
        this.message = resultCode.getDescription();
        this.data = data;
    }
    private Response(@NonNull ResultCode resultCode, @NonNull String message, @NonNull Object data){
        this.code = resultCode.getCode();
        this.message = message;
        this.data = data;
    }

    public static Response success(){
        return new Response(ResultCodeEnum.SUCCESS);
    }

    public static Response success(@NonNull String message){
        return new Response(ResultCodeEnum.SUCCESS, message);
    }
    public static Response success(@NonNull Object data){
        return new Response(ResultCodeEnum.SUCCESS, data);
    }

    public static Response success(@NonNull String message, @NonNull Object data){
        return new Response(ResultCodeEnum.SUCCESS, message, data);
    }

    public static  Response fail(@NonNull String message){
        return new Response(ResultCodeEnum.UNSPECIFIED, message);
    }

    public static  Response fail(@NonNull String message, @NonNull Object data){
        return new Response(ResultCodeEnum.UNSPECIFIED, message, data);
    }

    public static  Response fail(@NonNull ResultCode resultCode){
        return new Response(resultCode);
    }

    public static  Response fail(@NonNull ResultCode resultCode, @NonNull String message){
        return new Response(resultCode, message);
    }

    public static  Response fail(@NonNull ResultCode resultCode, @NonNull String message, @NonNull Object data){
        return new Response(resultCode, message, data);
    }

//    public static ResponseDTO fail(RuntimeException e){
//        return new ResponseDTO(ErrorCodeEnum.UNSPECIFIED, e.getMessage());
//    }
}
