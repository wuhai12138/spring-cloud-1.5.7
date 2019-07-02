package com.sfy.gateway.config;

import com.sfy.gateway.zuul.CustomRouteLocator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.web.ZuulController;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
//import org.springframework.jdbc.core.JdbcTemplate;
import java.util.ArrayList;
import java.util.List;

@EnableSwagger2
@EnableZuulProxy
@SpringBootApplication
@Component
@Primary
public class ZullApplication {
//    private final RouteLocator routeLocator;
//
//    public SpringCloudZullApplication(RouteLocator routeLocator) {
//        this.routeLocator = routeLocator;
//    }
//
//    @Override
//    public List<SwaggerResource> get() {
//        List<SwaggerResource> resources = new ArrayList<>();
//        List<Route> routes = routeLocator.getRoutes();
//        //在这里遍历的时候，可以排除掉敏感微服务的路由
//        routes.forEach(route -> resources.add(swaggerResource(route.getId(),
//                route.getFullPath().replace("**", "v2/api-docs"),"2.0")));
//        return resources;
//    }

    @Autowired
    ZuulProperties properties;
    @Autowired
    ServerProperties server;
//    @Autowired
//    JdbcTemplate jdbcTemplate;
    @Autowired
    ZuulHandlerMapping zuulHandlerMapping;

    @Bean
    public CustomRouteLocator routeLocator() {
        CustomRouteLocator routeLocator = new CustomRouteLocator(this.server.getServletPrefix(), this.properties);
//        routeLocator.setJdbcTemplate(jdbcTemplate);
        return routeLocator;
    }

    @Primary
    @Bean
    public SwaggerResourcesProvider swaggerResourcesProvider() {
        return () -> {
//            Map<String, Object> handlerMap = zuulHandlerMapping.getHandlerMap();
            List<SwaggerResource> resources = new ArrayList<>();
            properties.getRoutes().values().stream()
                    .forEach(route ->
                            resources
                                    .add(swaggerResource(route.getId(),
                                            "/" + route.getServiceId() + "/v2/api-docs", "2.0")));
            return resources;
        };
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
