package com.example.ozzy.emotioncard.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class EmotionAnalysisResponse {
    private String reply;
    private Map<String, Double> emotionPercentages;
}
