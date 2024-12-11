package com.example.ozzy.emotioncard.controller;

import com.example.ozzy.common.exception.domain.CommonException;
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

    @PostMapping("/analyze")
    public ResponseEntity<?> getEmotionAnalysis(@RequestBody EmotionCardRequest request) {
        try {
            EmotionAnalysisResponse response = emotionCardService.getEmotionAnalysis(request.getEmotionCardSeq());
            return ResponseEntity.ok(response);
        } catch (CommonException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
