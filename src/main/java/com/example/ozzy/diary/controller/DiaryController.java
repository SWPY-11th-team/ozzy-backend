package com.example.ozzy.diary.controller;

import com.example.ozzy.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("/add")
    public String add() {
        System.out.println("DiaryController.add");
//        int usrId = UserContext.getUserId();
//        System.out.println("usrId = " + usrId);
        return "ok";
    }

    // 일기 감정 분석 및 문장 나누기
    @PostMapping("/split")
    public ResponseEntity<String> split() {
        String str = diaryService.split();

        return new ResponseEntity<>(str, HttpStatus.OK);
    }

}
