package com.sfy.gateway.zuul;

//import com.sfy.gateway.config.CustomDisconfReloadUpdate;
import com.sfy.gateway.config.RedisOperator;
import com.sfy.gateway.entity.config.GatewayApiDefine;
import com.sfy.gateway.utils.ConstantSFY;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;

/****
 * @Description 监听路由链
 * @Param
 * @Author jpx
 * @Version  1.0
 * @Return
 * @Exception
 * @Date 2019/1/22
 */
@Slf4j
public class CustomRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {
//    private JdbcTemplate jdbcTemplate;
    private ZuulProperties properties;

    @Autowired
    protected RedisTemplate redisTemplate;

//    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
//        this.jdbcTemplate = jdbcTemplate;
//    }

    public CustomRouteLocator(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
        this.properties = properties;
        log.info("servletPath:{}",servletPath);
    }

    //TODO 父类已经提供了这个方法，这里写出来只是为了说明这一个方法很重要！！！
    @Override
    protected void doRefresh() {
        super.doRefresh();
    }


    @Override
    public void refresh() {
        doRefresh();
    }

    @Override
    protected Map<String, ZuulRoute> locateRoutes() {
        LinkedHashMap<String, ZuulRoute> routesMap = new LinkedHashMap<String, ZuulRoute>();
        //从application.properties中加载路由信息
//        routesMap.putAll(super.locateRoutes());
        //从db中加载路由信息
        routesMap.putAll(locateRoutesFromDB());
        //优化一下配置
        LinkedHashMap<String, ZuulRoute> values = new LinkedHashMap<>();
        properties.setRoutes(null);
        for (Map.Entry<String, ZuulRoute> entry : routesMap.entrySet()) {
            String path = entry.getKey();
            // Prepend with slash if not already present.
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (StringUtils.hasText(this.properties.getPrefix())) {
                path = this.properties.getPrefix() + path;
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
            }

            Set<String> send = new HashSet <>();
            send.add("*");
            entry.getValue().setSensitiveHeaders(send);
            values.put(path, entry.getValue());
        }

        properties.setRoutes(values);
        return values;
    }

    private Map<String, ZuulRoute> locateRoutesFromDB(){
        Map<String, ZuulRoute> routes = new LinkedHashMap<>();
//        CustomDisconfReloadUpdate.getConfigFilePath();
//        List<ZuulRouteVO> results =jdbcTemplate .query("select * from gateway_api_define where enabled = true ",
//                new BeanPropertyRowMapper<>(ZuulRouteVO.class));


        JSONArray jsonArray = (JSONArray) redisTemplate.opsForValue().get(ConstantSFY.ZUUL_ROUTE_VO_LIST_KEY);
//        List<ZuulRouteVO> results = new ArrayList<>();
//        for (Object obj : jsonArray){
//            GatewayApiDefine gatewayApiDefine = (GatewayApiDefine) obj;
//            ZuulRouteVO zuulRouteVO = new ZuulRouteVO();
//            zuulRouteVO.set
//        }
//        for (ZuulRouteVO dto : results) {
        if(null != jsonArray) {
            for (Object obj : jsonArray) {
                GatewayApiDefine gatewayApiDefine = (GatewayApiDefine)
                        JSONObject.toBean((JSONObject) obj, GatewayApiDefine.class);
//            if(org.apache.commons.lang.StringUtils.isBlank(dto.getPath()) || org.apache.commons.lang.StringUtils.isBlank(dto.getUrl()) ){
//                continue;
//            }
                ZuulRoute zuulRoute = new ZuulRoute();
                try {
//                org.springframework.beans.BeanUtils.copyProperties(dto,zuulRoute);
                    org.springframework.beans.BeanUtils.copyProperties(gatewayApiDefine, zuulRoute);
                    zuulRoute.setServiceId(gatewayApiDefine.getServiceid());
                } catch (Exception e) {
                    log.error("=============load zuul route info from db with error==============", e);
                }
                routes.put(zuulRoute.getPath(), zuulRoute);
            }
        }
        return routes;
    }

    public static class ZuulRouteVO implements Serializable {

        /**
         * The ID of the route (the same as its map key by default).
         */
        private String id;

        /**
         * The path (pattern) for the route, e.g. /foo/**.
         */
        private String path;

        /**
         * The service ID (if any) to map to this route. You can specify a physical URL or
         * a service, but not both.
         */
        private String serviceId;

        /**
         * A full physical URL to map to the route. An alternative is to use a service ID
         * and service discovery to find the physical address.
         */
        private String url;

        /**
         * Flag to determine whether the prefix for this route (the path, minus pattern
         * patcher) should be stripped before forwarding.
         */
        private boolean stripPrefix = true;

        /**
         * Flag to indicate that this route should be retryable (if supported). Generally
         * retry requires a service ID and ribbon.
         */
        private Boolean retryable;

        private String apiName;

        private Boolean enabled;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isStripPrefix() {
            return stripPrefix;
        }

        public void setStripPrefix(boolean stripPrefix) {
            this.stripPrefix = stripPrefix;
        }

        public Boolean getRetryable() {
            return retryable;
        }

        public void setRetryable(Boolean retryable) {
            this.retryable = retryable;
        }

        public String getApiName() {
            return apiName;
        }

        public void setApiName(String apiName) {
            this.apiName = apiName;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }
    }
}
