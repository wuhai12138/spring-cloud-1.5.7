package com.sfy.user.client.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 金鹏祥 on 2019/5/20.
 */
public class SysDict implements Serializable {

    private static final long serialVersionUID = 1L;

    private String dictId;
    private String dictType;
    private String dictCode;
    private String dictName;
    private Date createTime;
    private Date updateTime;
    private String infoJson;

    public String getDictId() {
        return dictId;
    }

    public void setDictId(String dictId) {
        this.dictId = dictId == null ? null : dictId.trim();
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType == null ? null : dictType.trim();
    }

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode == null ? null : dictCode.trim();
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName == null ? null : dictName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getInfoJson() {
        return infoJson;
    }

    public void setInfoJson(String infoJson) {
        this.infoJson = infoJson == null ? null : infoJson.trim();
    }
}
