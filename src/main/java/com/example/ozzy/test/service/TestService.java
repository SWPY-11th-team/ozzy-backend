package com.example.ozzy.test.service;

import com.example.ozzy.test.mapper.TestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestMapper testMapper;

    public int first() {
        int i = testMapper.selectOne();
        System.out.println("TestService.first select DB ==> " + i);

        return i;
    }
}
