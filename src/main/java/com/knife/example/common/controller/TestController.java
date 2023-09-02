package com.knife.example.common.controller;

import com.knife.example.common.core.exception.BusinessException;
import com.knife.example.common.core.exception.ForbiddenException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("test")
    public String test() {
        throw new ForbiddenException("aa");
        // return "test success";
    }
}
