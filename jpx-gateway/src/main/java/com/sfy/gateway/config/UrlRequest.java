package com.sfy.gateway.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.sfy.gateway.service.MyPathMatcher;
import com.sfy.gateway.service.RedisAuthenticationCodeServices;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import com.sfy.gateway.result.ApiResult;
import com.sfy.gateway.utils.ConstantSFY;
import com.sfy.gateway.utils.MessageSFY;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Slf4j
public class UrlRequest extends ZuulFilter {
    @Autowired
    private MyConfig myConfig;
    @Autowired
    protected RedisTemplate redisTemplate;
    @Autowired
    private RedisAuthenticationCodeServices redisAuthenticationCodeServices;
    @Autowired
    MyPathMatcher myPathMatcher;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        HttpServletRequest request = (HttpServletRequest) RequestContext.getCurrentContext().getRequest();
        RequestContext context = RequestContext.getCurrentContext();
        String rootName = "";
        String requestUrl = request.getRequestURI();
        if (StringUtils.isNotBlank(myConfig.getDevUrl())) {
            rootName = myConfig.getDevUrl();
        }
        log.info(MessageSFY.getMessageTitle(rootName));
        log.info(MessageSFY.getMessageTitle(request.getMethod()));
        log.info(MessageSFY.getMessageTitle(request.getHeader("phone")));
        log.info(MessageSFY.getMessageTitle(request.getHeader("device-id")));
        log.info(MessageSFY.getMessageTitle(requestUrl));
        log.info(MessageSFY.getMessageTitle(rootName));

//        if(!myConfig.getNotAuthList().contains(requestUrl)) {
        if (requestUrl.indexOf("/auth") != -1) {
            if (null == request.getHeader("phone")) {
                this.sendRedirect(rootName, context, ConstantSFY.CODE_4013);
                return null;
            }
            if (null == request.getHeader("device-id")) {
                this.sendRedirect(rootName, context, ConstantSFY.CODE_4012);
                return null;
            }
            if (null != request.getHeader("access-token")) {
                if (null != redisAuthenticationCodeServices.getAuthorityList(request.getHeader("access-token"))) {
                    List<String> authorityList = redisAuthenticationCodeServices.getAuthorityList(request.getHeader("access-token"));
                        /*if(!authorityList.contains(requestUrl)){
                            this.sendRedirect(rootName, context, ConstantSFY.CODE_403);
                            return null;
                        }*/
                    if (!myPathMatcher.matches(requestUrl, authorityList)) {
                        this.sendRedirect(rootName, context, ConstantSFY.CODE_403);
                        return null;
                    }
                }
            } else {
                this.sendRedirect(rootName, context, ConstantSFY.CODE_4023);
                return null;
            }
//                String deviceId = this.getRedisKey(request.getHeader("phone") + ":device-id");
//                if (StringUtils.isNotBlank(deviceId)) {
//                    if (!deviceId.equalsIgnoreCase(request.getHeader("device-id"))) {
//                        this.sendRedirect(rootName, context, ConstantSFY.CODE_4011);
//                        return null;
//                    }
//                }else{
//                    this.sendRedirect(rootName, context, ConstantSFY.CODE_4008);
//                    return null;
//                }
        }
//        }

        return null;
    }

    public String getRedisKey(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void responseBody(RequestContext context, int code, String message) {
        context.setSendZuulResponse(false);
        context.setResponseStatusCode(code);
        ApiResult<Object> result = ApiResult.error(code, message, null);
        context.setResponseBody(JSONObject.fromObject(result).toString());
    }

    public void sendRedirect(String rootName, RequestContext context, int code) {
        try {
            context.setSendZuulResponse(false);
            context.getResponse().sendRedirect(rootName + "/errorLogin?code=" + code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}