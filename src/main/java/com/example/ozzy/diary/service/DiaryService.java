package com.example.ozzy.diary.service;

import com.example.ozzy.addemotion.service.AddEmotionService;
import com.example.ozzy.common.UserContext;
import com.example.ozzy.common.exception.domain.CommonException;
import com.example.ozzy.diary.dto.request.DiaryRequest;
import com.example.ozzy.diary.dto.response.DiaryResponse;
import com.example.ozzy.diary.entity.Diary;
import com.example.ozzy.diary.mapper.DiaryMapper;
import com.example.ozzy.emotioncard.service.EmotionCardService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DiaryService {

    private final DiaryMapper diaryMapper;
    private final EmotionCardService emotionCardService;
    private final AddEmotionService addEmotionService;

    public DiaryService(DiaryMapper diaryMapper, EmotionCardService emotionCardService, AddEmotionService addEmotionService) {
        this.diaryMapper = diaryMapper;
        this.emotionCardService = emotionCardService;
        this.addEmotionService = addEmotionService;
    }

    public void saveDiary(DiaryRequest diaryRequest) throws JsonProcessingException {
        Diary diary = new Diary();

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");
//        LocalDateTime dateTime = LocalDateTime.parse(diaryRequest.getDiaryDate(), formatter);

        LocalDate date = LocalDate.parse(diaryRequest.getDiaryDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        diary.setUserSeq(UserContext.getUserId());
        diary.setDiaryDate(date);
        diary.setTitle(diaryRequest.getTitle());
        diary.setContent(diaryRequest.getContent());

        // EMOTION_CARD()를 생성하고 diary의 EMOTION_CARD_SEQ 추가;
        int emotionCardSeq = emotionCardService.saveEmotionCard();
        diary.setEmotionCardSeq(emotionCardSeq);

        // ADD_EMOTION()를 생성하고 diary의 ADD_EMOTION_SEQ 추가;
        int addEmotionSeq = addEmotionService.saveAddEmotion();
        diary.setAddEmotionSeq(addEmotionSeq);

        diaryMapper.saveDiary(diary);

        // 비동기로 analyzeDiary 호출
        analyzeDiary(diary);
    }

    public void analyzeDiary(Diary diary) throws JsonProcessingException {
        emotionCardService.analyzeDiary(diary);
    }

    public DiaryResponse getDiary(String diaryDate) {

        Diary diary = diaryMapper.getDiaryByUserAndDate(UserContext.getUserId(), diaryDate);
        if (diary == null) {
            throw new CommonException("일기를 찾을 수 없습니다.", 404);
        }

        // Diary 객체를 DiaryResponse DTO로 변환
        DiaryResponse response = new DiaryResponse();
        response.setUserSeq(diary.getUserSeq());
        response.setDiaryDate(diary.getDiaryDate());
        response.setAddEmotionSeq(diary.getAddEmotionSeq());
        response.setEmotionCardSeq(diary.getEmotionCardSeq());
        response.setTitle(diary.getTitle());
        response.setContent(diary.getContent());
        response.setCreateAt(diary.getCreateAt());
        response.setUpdateAt(diary.getUpdateAt());

        return response;
    }
}
