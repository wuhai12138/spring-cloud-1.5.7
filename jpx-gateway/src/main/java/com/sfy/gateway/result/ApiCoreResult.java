package com.sfy.gateway.result;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 注：code只是业务角度的响应码，相当于HTTP状态码的扩展集，可以展示更明确的错误原因，不是业务数据里的状态
 *
 * @author jinpengxiang
 * @since 2017/1/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"code", "message", "data"})
public abstract class ApiCoreResult<E> implements Result {

    @ApiModelProperty(value = "业务码", dataType = "java.lang.Integer", required = true, position = 1)
//    @JsonSerialize(converter = StringToIntegerConverter.class)
    protected String code;

    @ApiModelProperty(value = "业务码说明", required = true, example = "成功", position = 2)
    protected String message;

    @ApiModelProperty(value = "业务数据", position = 100)
    protected E data;

//    @Transient
//    protected IRespCode[] getOKCodes() {
//        return new IRespCode[]{BaseCode.SUCCESS};
//    }
//
//    @Transient
//    public boolean isSuccess() {
//        // 兼容
//        return code.equals("00000000") || of(getOKCodes()).anyMatch(respOKCode -> Objects.equals(this.code, respOKCode.getRespCode()));
//    }

//    @Transient
//    public boolean isError() {
//        return !isSuccess();
//    }

//    @Transient
//    public IRespCode getRespCode() {
//        return new IRespCode() {
//            @Override
//            public String getCode() {
//                return code;
//            }
//
//            @Override
//            public String getMessage() {
//                return message;
//            }
//        };
//    }

//    public RuntimeException decomposeException() {
//        return new ApiException(getRespCode());
//    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("code", code);
        map.put("message", message);
        map.put("data", data);
        return map;
    }

//    private static final String GLOBAL_SUCCESS_CODE = System.getProperty("apiresult.success_code");
//
//    public String getCode() {
//        if (isNotBlank(GLOBAL_SUCCESS_CODE) && isSuccess()) {
//            return GLOBAL_SUCCESS_CODE;
//        }
//        return this.code;
//    }
//
//    static class StringToIntegerConverter extends StdConverter<String, Object> {
//
//        static final StringToIntegerConverter INSTANCE = new StringToIntegerConverter();
//
//        @Override
//        public Object convert(String value) {
//            if (isNotBlank(GLOBAL_SUCCESS_CODE)) {
//                return value;
//            }
//            return StringUtil.parseInt(value);
//        }
//    }
}
