package com.sfy.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 语音字典
 * </p>
 *
 * @author 金鹏祥
 * @since 2019-03-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_voice_dict")
@ApiModel(value = "SysVoiceDict对象", description = "语音字典")
public class SysVoiceDict implements Serializable {

    private static final long serialVersionUID = 1L;

    private String voiceId;

    @ApiModelProperty(value = "内容1/内容2/内容3(不能有空格或符号)")
    private String value;

    @ApiModelProperty(value = "请求链接(GET:name=%name%)")
    private String requestUrl;

    @ApiModelProperty(value = "请求类型（GET/POST）")
    private String requestType;

    @ApiModelProperty(value = "方向/去向（指定该URL去向到哪里）")
    private String direction;

    @ApiModelProperty(value = "POST请求BODY构造体")
    private String requestBody;

    @ApiModelProperty(value = "是否执行模式（业务方向：默认对话模式；查询该字典中有记录才是执行模式）")
    private String isAsr;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "更新操作人")
    private String updateId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;


}
