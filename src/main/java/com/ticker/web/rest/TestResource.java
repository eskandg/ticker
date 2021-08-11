package com.ticker.web.rest;

import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestResource {

    @PostMapping(value = "/test", produces = "application/json")
    public Map<String, Object> test(@RequestBody Map<String, Object> requestBody) {
        System.out.println(requestBody.toString());
        return requestBody;
    }
}
