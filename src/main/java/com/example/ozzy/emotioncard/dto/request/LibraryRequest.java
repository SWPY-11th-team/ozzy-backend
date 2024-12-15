package com.example.ozzy.emotioncard.dto.request;

import lombok.Getter;

public class LibraryRequest {

    private int seq;
    private String month;

    public LibraryRequest(int seq, String month) {
        this.seq = seq;
        this.month = month;
    }

    public int getSeq() {
        return seq;
    }

    public String getMonth() {
        return month;
    }
}
