package com.sfy.gateway.entity.config;

import java.io.Serializable;

public class GatewayApiDefine implements Serializable {
    private String id;

    private String path;

    private String serviceid;

    private String url;

    private Boolean retryable;

    private Boolean enabled;

    private Integer stripprefix;

    private String apiname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public String getServiceid() {
        return serviceid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid == null ? null : serviceid.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Boolean getRetryable() {
        return retryable;
    }

    public void setRetryable(Boolean retryable) {
        this.retryable = retryable;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getStripprefix() {
        return stripprefix;
    }

    public void setStripprefix(Integer stripprefix) {
        this.stripprefix = stripprefix;
    }

    public String getApiname() {
        return apiname;
    }

    public void setApiname(String apiname) {
        this.apiname = apiname == null ? null : apiname.trim();
    }
}