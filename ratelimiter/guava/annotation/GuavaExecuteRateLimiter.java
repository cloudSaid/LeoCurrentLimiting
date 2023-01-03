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
package com.hyts.assemble.ratelimiter.guava.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.util.concurrent.TimeUnit;


@java.lang.annotation.Target({ElementType.METHOD,ElementType.FIELD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
@Component
@Autowired(required = false)
public @interface GuavaExecuteRateLimiter {

    /**
     * 返回的RateLimiter的速率，意味着每秒有多少个许可变成有效。
     */
    int permitsPerSecond() default 500;

    /**
     * 在这段时间内RateLimiter会增加它的速率，在抵达它的稳定速率或者最大速率之前
     */
    int warmupPeriod() default 5;

    /**
     * 参数warmupPeriod 的时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

}
