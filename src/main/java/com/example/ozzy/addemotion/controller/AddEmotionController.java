package com.example.ozzy.addemotion.controller;

import com.example.ozzy.addemotion.dto.request.AddEmotionRequest;
import com.example.ozzy.addemotion.dto.response.AddEmotionResponse;
import com.example.ozzy.addemotion.service.AddEmotionService;
import com.example.ozzy.common.exception.domain.CommonException;
import com.example.ozzy.common.exception.domain.DefaultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/add-emotion")
public class AddEmotionController {

    private final AddEmotionService addEmotionService;

    // 추가 감정 저장
    @PutMapping
    public ResponseEntity<?> saveAddEmotion(@RequestBody AddEmotionRequest addEmotionRequest) {
        try{
            AddEmotionResponse addEmotion = addEmotionService.updateAddEmotion(addEmotionRequest);
            DefaultResponse<AddEmotionResponse> response = new DefaultResponse<>("감정이 성공적으로 추가되었습니다.", 200, addEmotion);
            return ResponseEntity.ok(response);
        } catch (CommonException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DefaultResponse<>(e.getMessage(), e.getCode(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DefaultResponse<>(e.getMessage(), 500, null));
        }
    }

    // 추가 감정 조회
    @GetMapping
    public ResponseEntity<?> getAddEmotion(@RequestParam int addEmotionId) {
        try{
            AddEmotionResponse addEmotionResponse = addEmotionService.getAddEmotionByEmotionSeq(addEmotionId);
            DefaultResponse<AddEmotionResponse> response = new DefaultResponse<>("감정 조회 성공", 200, addEmotionResponse);
            return ResponseEntity.ok(response);
        } catch (CommonException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DefaultResponse<>(e.getMessage(), e.getCode(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DefaultResponse<>(e.getMessage(), 500, null));
        }
    }
}
