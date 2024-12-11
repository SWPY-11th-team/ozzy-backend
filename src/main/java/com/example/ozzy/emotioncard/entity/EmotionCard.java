package com.example.ozzy.emotioncard.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmotionCard {
    private int emotionCardSeq; // 감정 카드 식별자 (PK)
    private String isAnalyzed; // 감정 분석 완료 여부 ('Y' 또는 'N')
    private String reply; // AI의 답변
    private int happy; // 기쁨으로 분류된 문장의 수
    private int sad; // 슬픔으로 분류된 문장의 수
    private int surprised; // 놀람으로 분류된 문장의 수
    private int angry; // 분노로 분류된 문장의 수
    private int fearful; // 공포로 분류된 문장의 수
    private int bad; // 나쁨으로 분류된 문장의 수
    private int neutrality; // 중립으로 분류된 문장의 수
    private LocalDateTime createAt; // 최초 생성일
    private LocalDateTime updateAt; // 최종 수정일
}