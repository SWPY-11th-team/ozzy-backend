package com.example.ozzy.diary.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeeklyDiaryCountResponse {
    private int count;
    private List<String> dates;
}
