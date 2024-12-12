package com.example.ozzy.emotioncard.mapper;

import com.example.ozzy.emotioncard.entity.EmotionCard;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmotionCardMapper {
    void saveEmotionCard(EmotionCard emotionCard);
    void updateEmotionCard(EmotionCard emotionCard);
    EmotionCard getEmotionCardBySeq(int seq);
}
