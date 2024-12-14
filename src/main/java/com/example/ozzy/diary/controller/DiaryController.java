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

    @PostMapping
    public ResponseEntity<DefaultResponse<DiaryResponse>> add(@RequestBody DiaryRequest diaryRequest) throws JsonProcessingException {
        if (!diaryRequest.getDiaryDate().matches("\\d{4}-\\d{2}-\\d{2}")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new DefaultResponse<>("잘못된 날짜 형식입니다. ex) yyyy-MM-dd", HttpStatus.BAD_REQUEST.value()));
        }

        // 일기 저장
        try {
            DiaryResponse savedDiary = diaryService.saveDiary(diaryRequest);
            DefaultResponse<DiaryResponse> response = new DefaultResponse<>("일기가 성공적으로 저장되었습니다.", 201, savedDiary);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CommonException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new DefaultResponse<>(e.getMessage(), e.getCode(), null));
        }
    }

    @GetMapping
    public ResponseEntity<?> getDiary(@RequestParam String diaryDate) {
        if (!diaryDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new DefaultResponse<>("잘못된 날짜 형식입니다. ex) yyyy-MM-dd", HttpStatus.BAD_REQUEST.value()));
        }

        // 일기 조회
        try {
            DiaryResponse diaryResponse = diaryService.getDiary(diaryDate);
            return ResponseEntity.status(200).body(new DefaultResponse<>("일기 조회 성공", 200, diaryResponse));
        } catch (CommonException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DefaultResponse<>(e.getMessage(), e.getCode(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DefaultResponse<>(e.getMessage(), 500, null));
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody DiaryRequest diaryRequest) throws JsonProcessingException {
        if (!diaryRequest.getDiaryDate().matches("\\d{4}-\\d{2}-\\d{2}")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new DefaultResponse<>("잘못된 날짜 형식입니다. ex) yyyy-MM-dd", HttpStatus.BAD_REQUEST.value()));
        }

        // 일기 업데이트
        try {
            DiaryResponse updatedDiary = diaryService.updateDiary(diaryRequest);
            DefaultResponse<DiaryResponse> response = new DefaultResponse<>("일기가 성공적으로 업데이트되었습니다.", 200, updatedDiary);
            return ResponseEntity.ok(response);
        } catch (CommonException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DefaultResponse<>(e.getMessage(), e.getCode(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam String diaryDate)  {
        if (!diaryDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new DefaultResponse<>("잘못된 날짜 형식입니다. ex) yyyy-MM-dd", HttpStatus.BAD_REQUEST.value()));
        }

        try {
            diaryService.deleteDiary(diaryDate);
            DefaultResponse<DiaryResponse> response = new DefaultResponse<>("일기가 성공적으로 삭제되었습니다.", 200);
            return ResponseEntity.ok(response);
        } catch (CommonException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DefaultResponse<>(e.getMessage(), e.getCode(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
