package com.example.ozzy.diary.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Diary {
    private int userSeq; // 사용자 식별자
    private LocalDateTime diaryDate; // 작성일
    private String title; // 일기 제목
    private String content; // 일기 내용
    private int addEmotionSeq; // Add Emotion PK
    private int emotionCardSeq; // Emotion Card PK
    private LocalDateTime createAt; // 최초 작성일
    private LocalDateTime updateAt; // 최종 수정일
}
