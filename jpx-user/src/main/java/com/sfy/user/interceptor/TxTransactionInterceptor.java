package com.sfy.user.interceptor;

import com.lorne.tx.springcloud.interceptor.TxManagerInterceptor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Aspect
@Component
public class TxTransactionInterceptor implements Ordered {

    @Override
    public int getOrder() {
        return 1;
    }

    @Resource
    private TxManagerInterceptor txManagerInterceptor;

    @Around("execution(* com.sfy.user.service.tx.*Service.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        return txManagerInterceptor.around(point);
    }
}
