package com.knife.example.common.controller;

import com.knife.example.common.core.exception.UnauthorizedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("test")
    public String test() {
        throw new UnauthorizedException("aa");
        // return "test success";
    }
}
