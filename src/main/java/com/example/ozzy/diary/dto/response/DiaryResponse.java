package com.example.ozzy.diary.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class DiaryResponse {
    private LocalDate diaryDate; // 일기 작성일
    private int addEmotionSeq; // ADD_EMOTION 테이블의 PK
    private int emotionCardSeq; // EMOTION_CARD 테이블의 PK
    private String title; // 일기 제목
    private String content; // 일기 내용
    private LocalDateTime createAt; // 최초 작성일
    private LocalDateTime updateAt; // 최종 수정일
}
