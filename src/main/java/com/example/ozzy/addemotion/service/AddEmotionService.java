package com.example.ozzy.addemotion.service;

import com.example.ozzy.addemotion.entity.AddEmotion;
import com.example.ozzy.addemotion.mapper.AddEmotionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AddEmotionService {

    private final AddEmotionMapper addEmotionMapper;

    public AddEmotionService(AddEmotionMapper addEmotionMapper) {
        this.addEmotionMapper = addEmotionMapper;
    }

    @Transactional
    public int saveAddEmotion() {
        AddEmotion addEmotion = new AddEmotion();
        addEmotion.setEmotions("");
        addEmotion.setCreateAt(LocalDateTime.now()); // 현재 시간으로 설정
        addEmotion.setUpdateAt(LocalDateTime.now()); // 현재 시간으로 설정

        addEmotionMapper.saveAddEmotion(addEmotion); // ADD_EMOTION 저장

        return addEmotion.getAddEmotionSeq(); // 생성된 PK 반환
    }
}
