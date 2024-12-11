package com.example.ozzy.diary.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaryRequest {
    private int userSeq;
    private String diaryDate;
    private String title;
    private String content;
}
