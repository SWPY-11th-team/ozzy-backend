package com.example.ozzy.diary.controller;

import com.example.ozzy.common.exception.domain.CommonException;
import com.example.ozzy.common.exception.domain.DefaultResponse;
import com.example.ozzy.diary.dto.request.DiaryRequest;
import com.example.ozzy.diary.dto.response.DiaryResponse;
import com.example.ozzy.diary.service.DiaryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("/add")
    public ResponseEntity<DefaultResponse<String>> add(@RequestBody DiaryRequest diaryRequest) throws JsonProcessingException {
        System.out.println("DiaryController.add");

        // 일기 저장
        try {
            diaryService.saveDiary(diaryRequest);
            DefaultResponse<String> response = new DefaultResponse<>("일기가 성공적으로 저장되었습니다.", 201);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CommonException e) {
            DefaultResponse<String> response = new DefaultResponse<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getDiary(@RequestParam String diaryDate) {
        // JWT에서 userSeq 추출해야함
        int userSeq = 90;

        try {
            DiaryResponse diaryResponse = diaryService.getDiary(userSeq, diaryDate);
            return ResponseEntity.ok(diaryResponse);
        } catch (CommonException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
