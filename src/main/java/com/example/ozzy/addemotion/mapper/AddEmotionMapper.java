package com.example.ozzy.addemotion.mapper;


import com.example.ozzy.addemotion.entity.AddEmotion;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddEmotionMapper {
    void saveAddEmotion(AddEmotion addEmotion);
    void updateAddEmotion(AddEmotion addEmotion);
    void deleteAddEmotion(int addEmotionSeq);
    AddEmotion getAddEmotionBySeq(int addEmotionSeq);
}
