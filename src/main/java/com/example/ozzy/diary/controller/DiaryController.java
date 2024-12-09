package com.example.ozzy.diary.controller;

import com.example.ozzy.common.UserContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/diary")
public class DiaryController {

    @PostMapping("/add")
    public String add() {
        System.out.println("DiaryController.add");
        int usrId = UserContext.getUserId();
        System.out.println("usrId = " + usrId);
        return "ok";
    }

}
