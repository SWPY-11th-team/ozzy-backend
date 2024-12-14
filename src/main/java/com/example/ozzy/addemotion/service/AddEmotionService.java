package com.example.ozzy.addemotion.service;

import com.example.ozzy.addemotion.dto.request.AddEmotionRequest;
import com.example.ozzy.addemotion.dto.response.AddEmotionResponse;
import com.example.ozzy.addemotion.entity.AddEmotion;
import com.example.ozzy.addemotion.mapper.AddEmotionMapper;
import com.example.ozzy.common.exception.domain.CommonException;
import org.springframework.stereotype.Service;

@Service
public class AddEmotionService {

    private final AddEmotionMapper addEmotionMapper;

    public AddEmotionService(AddEmotionMapper addEmotionMapper) {
        this.addEmotionMapper = addEmotionMapper;
    }

    public int initAddEmotion() {
        AddEmotion addEmotion = new AddEmotion();
        addEmotion.setEmotions("");
        addEmotionMapper.saveAddEmotion(addEmotion); // ADD_EMOTION 저장

        return addEmotion.getAddEmotionSeq(); // 생성된 PK 반환
    }

    public AddEmotionResponse updateAddEmotion(AddEmotionRequest addEmotionRequest) {
        AddEmotion addEmotion = addEmotionMapper.getAddEmotionBySeq(addEmotionRequest.getAddEmotionSeq());

        if (addEmotion == null) {
            throw new CommonException("저장된 추가 감정이 없습니다.", 404);
        }

        addEmotion.setEmotions(addEmotionRequest.getEmotions());
        addEmotionMapper.updateAddEmotion(addEmotion);

        return getAddEmotionByEmotionSeq(addEmotion.getAddEmotionSeq());
    }

    public AddEmotionResponse getAddEmotionByEmotionSeq(int addEmotionSeq) {
        AddEmotion addEmotion = addEmotionMapper.getAddEmotionBySeq(addEmotionSeq);

        if(addEmotion == null) {
            throw new CommonException("저장된 추가 감정이 없습니다.", 404);
        }

        AddEmotionResponse response = new AddEmotionResponse();
        response.setAddEmotionSeq(addEmotion.getAddEmotionSeq());
        response.setEmotions(addEmotion.getEmotions());
        response.setCreateAt(addEmotion.getCreateAt());
        response.setUpdateAt(addEmotion.getUpdateAt());

        return response;
    }

    public void clearAddEmotion(int addEmotionSeq) {
        AddEmotion addEmotion = addEmotionMapper.getAddEmotionBySeq(addEmotionSeq);

        addEmotion.setEmotions("");
        addEmotionMapper.updateAddEmotion(addEmotion);
    }
}
