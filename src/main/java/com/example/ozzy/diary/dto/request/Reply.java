package com.example.ozzy.diary.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Reply {

    private String reply;
    private List<String> sentences;

}
