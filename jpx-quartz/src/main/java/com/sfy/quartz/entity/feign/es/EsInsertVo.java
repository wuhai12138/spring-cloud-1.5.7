package com.sfy.quartz.entity.feign.es;

import lombok.Data;

/**
 * @author 金鹏祥
 * @date 2019/4/10 10:05
 * @description
 */
@Data
public class EsInsertVo {
    private String indexName;
    private String esType;
    private String idKey;
    private Object object;

    public EsInsertVo(String indexName, String esType, String idKey, Object object) {
        this.indexName = indexName;
        this.esType = esType;
        this.idKey = idKey;
        this.object = object;
    }

    public EsInsertVo() {
    }
}
