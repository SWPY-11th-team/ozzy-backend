package com.example.ozzy.emotioncard.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class LibraryResponse {
    private String date; // 일기 날짜
    private List<Map<String, Integer>> topEmotions; // Top 3 감정 정보
}
