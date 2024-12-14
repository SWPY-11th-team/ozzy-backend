package com.example.ozzy.addemotion.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddEmotionRequest {
    private int addEmotionSeq;
    private String emotions;
}
