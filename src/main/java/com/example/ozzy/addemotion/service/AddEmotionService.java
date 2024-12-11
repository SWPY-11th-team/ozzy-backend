package com.example.ozzy.addemotion.service;

import com.example.ozzy.addemotion.dto.request.AddEmotionsRequest;
import com.example.ozzy.addemotion.mapper.AddEmotionMapper;
import com.example.ozzy.common.dto.UserDateRequest;
import com.example.ozzy.common.utils.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddEmotionService {

    private final AddEmotionMapper addEmotionMapper;
    private final ObjectMapperUtil objectMapperUtil;

    public int insertAddEmotions(AddEmotionsRequest addEmotionsRequest) {

        String emotion = String.join(",", addEmotionsRequest.getEmotions());

        try {
            int i = addEmotionMapper.insertAddEmotions(emotion);
        } catch (Exception e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        }

        return 0;
    }

    public List<String> findAddEmotions(UserDateRequest userDateRequest) {
        String str = addEmotionMapper.findAddEmotions(UserDateRequest.todayDate(90, userDateRequest.getTodayDate()));
        String[] emotions = str.split(",");
        return Arrays.asList(emotions);
    }
}
