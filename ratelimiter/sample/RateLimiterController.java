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

import com.hyts.assemble.common.model.http.ResultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;


@Slf4j
@RestController
@RequestMapping("/api/ratelimiter")
@Api(value="限流能力测试Sample服务",tags = {"RateLimiter服务组件"},description = "RateLimiter服务组件")
public class RateLimiterController {


    @Autowired
    SampleExecuteRateLimiter executeRateLimiter;


    @GetMapping("/ratelimiter")
    @ApiOperation(value="测试限流控制效果",notes = "测试限流控制效果")
    public ResultResponse create(@RequestParam("processTime") int processTime,
                                 @RequestParam("threadCount") int threadCount){
        try {
            for(int idex = 0 ; idex<threadCount ;idex++){
                new Thread(()->{
                    try {
                        executeRateLimiter.executeRateLimiter(processTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            return ResultResponse.success("create ratelimiter is success!");
        }catch (Exception e){
            log.error("create ratelimiter is failure!",e);
            return ResultResponse.failure("create ratelimiter is failure!");
        }
    }
}
