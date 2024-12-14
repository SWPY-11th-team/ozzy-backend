package com.example.ozzy.emotioncard.controller;

import com.example.ozzy.common.exception.domain.CommonException;
import com.example.ozzy.common.exception.domain.DefaultResponse;
import com.example.ozzy.emotioncard.dto.response.EmotionAnalysisResponse;
import com.example.ozzy.emotioncard.dto.response.LibraryResponse;
import com.example.ozzy.emotioncard.dto.response.OverallEmotionStats;
import com.example.ozzy.emotioncard.service.EmotionCardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emotion-card")
public class EmotionCardController {

    private final EmotionCardService emotionCardService;

    public EmotionCardController(EmotionCardService emotionCardService) {
        this.emotionCardService = emotionCardService;
    }

    @GetMapping
    public ResponseEntity<?> getEmotionCard(@RequestParam int emotionCardId) {
        try {
            EmotionAnalysisResponse emotionAnalysisResponse = emotionCardService.getEmotionAnalysis(emotionCardId);
            return ResponseEntity.status(200).body(new DefaultResponse<>("감정 조회 성공", 200, emotionAnalysisResponse));
        } catch (CommonException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DefaultResponse<>(e.getMessage(), e.getCode(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DefaultResponse<>(e.getMessage(), 500, null));
        }
    }

    @GetMapping("/library")
    public ResponseEntity<?> getEmotionCardLibrary(@RequestParam String month) {
        if (!month.matches("\\d{4}-\\d{2}")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new DefaultResponse<>("잘못된 날짜 형식입니다. ex) yyyy-MM", HttpStatus.BAD_REQUEST.value()));
        }

        try {
            // 월별 감정 통계 및 Top 3 감정 정보 조회 로직
            List<LibraryResponse> monthlyEmotions = emotionCardService.getMonthlyEmotions(month);
            OverallEmotionStats overallStats = emotionCardService.getOverallEmotionStats(month);

            // 최종 응답 객체
            Map<String, Object> response = new HashMap<>();
            response.put("monthlyEmotions", monthlyEmotions);
            response.put("overallEmotionStats", overallStats);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new DefaultResponse<>("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
}
