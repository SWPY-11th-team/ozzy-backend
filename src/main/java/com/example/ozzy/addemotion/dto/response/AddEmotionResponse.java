package com.example.ozzy.addemotion.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class AddEmotionResponse {
    private int addEmotionSeq;
    private String emotions;
    private LocalDateTime createAt; // 최초 작성일
    private LocalDateTime updateAt; // 최종 수정일
}
