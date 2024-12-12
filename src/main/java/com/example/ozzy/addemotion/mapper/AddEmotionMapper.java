package com.example.ozzy.addemotion.mapper;


import com.example.ozzy.addemotion.entity.AddEmotion;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddEmotionMapper {
    void saveAddEmotion(AddEmotion addEmotion);
}
