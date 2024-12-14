package com.example.ozzy.diary.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DiaryRequest {
    private String diaryDate;
    private String title;
    private String content;
}
