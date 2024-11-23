package com.example.ozzy.test.service;

import com.example.ozzy.test.mapper.TestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestMapper testMapper;

    public void first() {
        System.out.println("TestService.first");
    }
}
