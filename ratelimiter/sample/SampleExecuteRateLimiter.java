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
package com.hyts.assemble.ratelimiter.sample;

import com.hyts.assemble.ratelimiter.guava.annotation.GuavaExecuteRateLimiter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
public class SampleExecuteRateLimiter {


    @SneakyThrows
    @GuavaExecuteRateLimiter(permitsPerSecond=5)
    public void executeRateLimiter(int processTime){
        TimeUnit.SECONDS.sleep(processTime);
    }

}
