package com.example.ozzy.emotioncard.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class OverallEmotionStats {
    private String month; // 해당 월
    private Map<String, Integer> emotions; // 감정별 통계
}
