package com.sfy.quartz.entity.feign.es;

import lombok.Data;

@Data
public class StringResult {
    public String id;

    public StringResult(String id) {
        this.id = id;
    }
}
