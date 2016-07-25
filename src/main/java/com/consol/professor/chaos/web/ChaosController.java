/*
 * Copyright 2006-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.professor.chaos.web;

import com.consol.professor.chaos.service.ChaosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author Christoph Deppisch
 */
@Controller
@RequestMapping("/chaos")
public class ChaosController {

    @Autowired
    private ChaosService chaosService;

    @RequestMapping(value = "/heap", method = RequestMethod.GET)
    public ResponseEntity useHeap(@RequestParam(value = "time", defaultValue = "3000") String time,
                                  @RequestParam(value = "size", defaultValue = "5") String size) {
        chaosService.createHeapUsage((Long.valueOf(size) * 1024), Long.valueOf(time));
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/cpu", method = RequestMethod.GET)
    public ResponseEntity useCpu(@RequestParam(value = "threads", defaultValue = "10") String threads,
                                 @RequestParam(value = "keepAlive", defaultValue = "5000") String keepAlive) {
        chaosService.createCpuUsage(Integer.valueOf(threads), Long.valueOf(keepAlive));
        return ResponseEntity.ok().build();
    }


}
