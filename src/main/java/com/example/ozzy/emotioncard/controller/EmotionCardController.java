package com.example.ozzy.emotioncard.controller;

import com.example.ozzy.common.UserContext;
import com.example.ozzy.common.exception.domain.CommonException;
import com.example.ozzy.common.exception.domain.DefaultResponse;
import com.example.ozzy.diary.dto.response.DiaryResponse;
import com.example.ozzy.emotioncard.dto.request.EmotionCardRequest;
import com.example.ozzy.emotioncard.dto.response.EmotionAnalysisResponse;
import com.example.ozzy.emotioncard.service.EmotionCardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emotion-card")
public class EmotionCardController {

    private final EmotionCardService emotionCardService;

    public EmotionCardController(EmotionCardService emotionCardService) {
        this.emotionCardService = emotionCardService;
    }

    @GetMapping("/get")
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
}
