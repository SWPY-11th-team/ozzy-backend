package com.example.ozzy.addemotion.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AddEmotion {
    private int addEmotionSeq; // 추가 감정 식별자 (PK)
    private String emotions; // 사용자 추가 감정 문자열
    private LocalDateTime createAt; // 최초 생성일
    private LocalDateTime updateAt; // 최종 수정일
}