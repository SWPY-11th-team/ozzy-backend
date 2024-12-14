package com.example.ozzy.user.dto.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserFromDate {

    private int todayFromDate;  // 가입일부터 오늘까지
    private int diaryCount; // 일기 입력 개수

}
