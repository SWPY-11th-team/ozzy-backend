package com.example.ozzy.diary.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class DiaryInfoRequest {
    private int userSeq;
    private String diaryDate;
}
