package com.example.ozzy.test.controller;

import com.example.ozzy.test.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/test")
public class TestController {

    private final TestService testService;

    @GetMapping("/select-db")
    public int test() {
        return testService.first();
    }

    @GetMapping("/view")
    public String view() {
        return "index";
    }

    @GetMapping("/map")
    public ResponseEntity<Map<String, Object>> map() {
        int count = testService.first();
        return new ResponseEntity<>(Map.of("hi", "1","seq",count), HttpStatus.OK);
    }

}
