package com.example.ozzy.addemotion.controller;

import com.example.ozzy.addemotion.dto.request.AddEmotionsRequest;
import com.example.ozzy.addemotion.service.AddEmotionService;
import com.example.ozzy.common.dto.UserDateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/add-emotion")
public class AddEmotionController {

    private final AddEmotionService addEmotionService;

    @PostMapping("/insert")
    public String insertAddEmotions(@RequestBody AddEmotionsRequest addEmotionsRequest) {
        System.out.println("addEmotionRequest = " + addEmotionsRequest);
        addEmotionService.insertAddEmotions(addEmotionsRequest);
        return null;
    }

    @PostMapping("/find")
    public List<String> selectAddEmotions(@RequestBody UserDateRequest userDateRequest) {
        System.out.println("userDateRequest = " + userDateRequest);
        return addEmotionService.findAddEmotions(userDateRequest);
    }

}
