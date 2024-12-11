package com.example.ozzy.addemotion.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
public class AddEmotionsRequest {
    private List<String> emotions;

}
