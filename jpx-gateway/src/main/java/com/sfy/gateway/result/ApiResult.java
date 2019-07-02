package com.sfy.gateway.result;

import com.sfy.gateway.utils.MessageSFY;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import java.beans.ConstructorProperties;
import java.util.function.Function;

/**
 * @author jinpengxiang
 * @since 2016/11/17
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class ApiResult<E> extends ApiCoreResult<E> {

    private final static int SUCCESS_CODE = 200;
    private final static String SUCCESS_MESSAGE = "请求成功";

    public static <E> ApiResult<E> success(E data) {
        ApiResult<E> obj = build(SUCCESS_CODE, SUCCESS_MESSAGE, data);
        log.info(MessageSFY.getMessageTitle(JSONObject.fromObject(obj).toString()));
        return obj;
    }

    public static <E> ApiResult<E> error(int code, String message, E data) {
        ApiResult<E> obj = build(code, message, data);
        log.info(MessageSFY.getMessageTitle(JSONObject.fromObject(obj).toString()));
        return obj;
    }

    private static <E> ApiResult<E> build(int code, String message, E data) {
        return new ApiResult <>(code + "", message, data);
    }

//    public static <E> ApiResult<E> err(IRespCode code) {
//        return err(code, code.getMessage());
//    }
//
//    public static <E> ApiResult<E> err(IRespCode code, String message) {
//        return err(code.getRespCode(), message);
//    }
//
//    public static <E> ApiResult<E> err(IRespCode code, E data) {
//        return build(code.getRespCode(), code.getMessage(), data);
//    }
//
//    public static <E> ApiResult<E> err(String message) {
//        return err(BaseCode.SERVER_ERROR.getRespCode(), message);
//    }
//
//    public static <E> ApiResult<E> err(String code, String message) {
//        if (code == null) {
//            return err(message);
//        }
//        return build(code, message, null);
//    }
//
//    public static <E> ApiResult<E> build(IRespCode code) {
//        return build(code, null);
//    }
//
//    public static <E> ApiResult<E> build(IRespCode code, E data) {
//        return build(code.getRespCode(), code.getMessage(), data);
//    }
//
//    public static <E> ApiResult<E> build(String code, String message, E data) {
//        return new ApiResult<>(code, message, data);
//    }
//
//    public static <E> ApiResult<E> with(Throwable e) {
//        if (e != null && e instanceof ApiException) {
//            return err(((ApiException) e).getCode(), e.getMessage());
//        }
//        if (e != null) {
//            return err(BaseCode.SERVER_ERROR, e.getMessage());
//        }
//        return err(BaseCode.SERVER_ERROR);
//    }

    @ConstructorProperties({"code", "message", "data"})
    public ApiResult(String code, String message, E data) {
        super(code, message, data);
    }

    public <T> ApiResult<T> map(Function<E, T> mapper) {
        T data = mapper.apply(getData());
        return new ApiResult<>(getCode(), getMessage(), data);
    }
}
