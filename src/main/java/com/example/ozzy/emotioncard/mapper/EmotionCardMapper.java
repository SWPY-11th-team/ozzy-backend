package com.example.ozzy.emotioncard.mapper;

import com.example.ozzy.diary.entity.Diary;
import com.example.ozzy.emotioncard.dto.request.LibraryRequest;
import com.example.ozzy.emotioncard.entity.EmotionCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmotionCardMapper {
    void saveEmotionCard(EmotionCard emotionCard);
    void updateEmotionCard(EmotionCard emotionCard);
    void deleteEmotionCard(int emotionCardSeq);
    EmotionCard getEmotionCardBySeq(int seq);
    List<Diary> findByDiaryDateStartsWith(LibraryRequest request);
}
