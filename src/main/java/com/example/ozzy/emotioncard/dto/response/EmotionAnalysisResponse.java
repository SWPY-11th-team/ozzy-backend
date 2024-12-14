package com.example.ozzy.emotioncard.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class EmotionAnalysisResponse {
    private String reply;
    private List<Map.Entry<String, Double>> emotionPercentages;
}
