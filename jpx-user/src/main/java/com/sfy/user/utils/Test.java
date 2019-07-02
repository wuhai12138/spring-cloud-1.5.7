package com.sfy.user.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sfy.user.Application;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 金鹏祥
 * @date 2019/5/29 18:03
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class Test {
    @Autowired
    WebApplicationContext applicationContext;

    @org.junit.Test
    public void getAllUrl() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            Map<String, String> map1 = new HashMap<String, String>();
            RequestMappingInfo info = m.getKey();
            HandlerMethod method = m.getValue();
            PatternsRequestCondition p = info.getPatternsCondition();
            for (String url : p.getPatterns()) {
                if (!url.startsWith("/auth")) {
                    continue;
                }
                map1.put("url", url);
                map1.put("method", method.getMethod().getName()); // 方法名
                list.add(map1);
            }
//            map1.put("className", method.getMethod().getDeclaringClass().getName()); // 类名
//            RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
//            for (RequestMethod requestMethod : methodsCondition.getMethods()) {
//                map1.put("type", requestMethod.toString());
//            }

        }

        System.out.println(JSON.toJSONString(list));
        JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(list));
        return;
    }

}
