package com.example.ozzy.common.dto;

import com.example.ozzy.common.UserContext;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserDateRequest {

    private int userSeq;
    private String monthDate;
    private String todayDate;

    public UserDateRequest() {}

    public UserDateRequest(int userSeq, String monthDate, String todayDate) {
        this.userSeq = userSeq;
        this.monthDate = monthDate;
        this.todayDate = todayDate;
    }

    public static UserDateRequest monthDate(int userSeq, String monthDate) {
        return new UserDateRequest(
                userSeq,monthDate, null
        );
    }

    public static UserDateRequest todayDate(int userSeq, String todayDate) {
        return new UserDateRequest(
                userSeq,null, todayDate
        );
    }
}
