package com.usst.weapp.lostandfound.utils;

import com.usst.weapp.lostandfound.constants.enums.ResultCode;
import com.usst.weapp.lostandfound.constants.enums.ResultCodeEnum;
import lombok.Data;

import javax.xml.transform.Result;


/**
 * @Author Sunforge
 * @Date 2021-07-13 9:14
 * @Version V1.0.0
 * @Description
 */
@Data
public class MyException extends RuntimeException{
    private static final long serialVersionUID = -7864604160297181941L;

    /** 错误码 */
    protected final ResultCode resultCode;

    /**
     * 这个是和谐一些不必要的地方,冗余的字段
     * 尽量不要用
     */
    private String code;

    /**
     * 无参默认构造UNSPECIFIED (unspecified)
     */
    public MyException() {
        super(ResultCodeEnum.UNSPECIFIED.getDescription());
        this.resultCode = ResultCodeEnum.UNSPECIFIED;
    }

    /**
     * 指定详细描述构造通用异常(unspecified + message)
     * @param detailedMessage 详细描述
     */
    public MyException(final String detailedMessage) {
        super(detailedMessage);
        this.resultCode = ResultCodeEnum.UNSPECIFIED;
    }

    /**
     * 指定错误码构造通用异常 (resultcode)
     * @param resultCode 错误码
     */
    public MyException(final ResultCode resultCode) {
        super(resultCode.getDescription());
        this.resultCode = resultCode;
    }

    /**
     * 指定错误码和异常信息构造通用异常 (resultcode + message)
     * @param resultCode 错误码
     * @param detailedMessage 详细描述
     */
    public MyException(final ResultCode resultCode, final String detailedMessage) {
        super(detailedMessage);
        this.resultCode = resultCode;
    }



    /**
     * 指定导火索构造通用异常 (unspecified + t)
     * @param t 导火索
     */
    public MyException(final Throwable t) {
        super(t);
        this.resultCode = ResultCodeEnum.UNSPECIFIED;
    }


    /**
     * 构造通用异常 (unspecified + t + message)
     * @param detailedMessage 详细描述
     * @param t 导火索
     */
    public MyException(final String detailedMessage, final Throwable t) {
        super(detailedMessage, t);
        this.resultCode = ResultCodeEnum.UNSPECIFIED;
    }


    /**
     * 构造通用异常 (resultcode + t)
     * @param resultCode 错误码
     * @param t 导火索
     */
    public MyException(final ResultCode resultCode, final Throwable t) {
        super(resultCode.getDescription(), t);
        this.resultCode = resultCode;
    }


    /**
     * 构造详细异常 (resultcode + t + message)
     * @param resultCode 错误码
     * @param detailedMessage 详细描述
     * @param t 导火索
     */
    public MyException(final ResultCode resultCode, final String detailedMessage,
                       final Throwable t) {
        super(detailedMessage, t);
        this.resultCode = resultCode;
    }


}
