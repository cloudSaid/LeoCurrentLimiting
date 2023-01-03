/**
 * Copyright [2020] [LiBo/Alex of copyright liboware@gmail.com ]
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyts.assemble.ratelimiter.guava.aop;

import com.google.common.base.Joiner;
import com.google.common.util.concurrent.RateLimiter;
import com.hyts.assemble.ratelimiter.guava.GuavaExecuteRateLimiterFactory;
import com.hyts.assemble.ratelimiter.guava.GuavaRateLimiterParam;
import com.hyts.assemble.ratelimiter.guava.annotation.GuavaExecuteRateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Aspect
@Component
public class GuavaExecuteRateLimterAspect {



    @Pointcut("@annotation(com.hyts.assemble.ratelimiter.guava.annotation.GuavaExecuteRateLimiter)")
    public void methodPointCut() {}


    @Autowired
    GuavaExecuteRateLimiterFactory executeRateLimiterFactory;


    ConcurrentHashMap<String, RateLimiter> rateLimiterConcurrentHashMap = new ConcurrentHashMap<>();



    Joiner joiner = Joiner.on("-").skipNulls();



    @Around("methodPointCut()")
        public Object doMethod(ProceedingJoinPoint proceedingJoinPoint){

        MethodSignature methodSignature = (MethodSignature)proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        GuavaExecuteRateLimiter guavaExecuteRateLimiter = method.getAnnotation(GuavaExecuteRateLimiter.class);

        GuavaRateLimiterParam guavaRateLimiterParam = GuavaRateLimiterParam.builder().
                permitsPerSecond(guavaExecuteRateLimiter.permitsPerSecond()).
                timeUnit(guavaExecuteRateLimiter.timeUnit()).
                warmupPeriod(guavaExecuteRateLimiter.warmupPeriod()).build();

        String key = joiner.join(guavaExecuteRateLimiter.permitsPerSecond(),
                guavaExecuteRateLimiter.timeUnit().toString()
                ,guavaExecuteRateLimiter.warmupPeriod());

        RateLimiter rateLimiter = rateLimiterConcurrentHashMap.
                computeIfAbsent(key,param-> executeRateLimiterFactory.create(guavaRateLimiterParam));

        try {
            double rateValue = rateLimiter.acquire();
            log.info("执行限流方法操作处理:当前qps:{} delay rate limiter value:{}",guavaExecuteRateLimiter.permitsPerSecond(),rateValue);
            return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        } catch (Throwable e) {
            log.error("执行限流控制方法失败！",e);
            return null;
        }
    }

}
