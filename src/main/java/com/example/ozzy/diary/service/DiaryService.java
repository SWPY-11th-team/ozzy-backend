package com.example.ozzy.diary.service;

import com.example.ozzy.addemotion.service.AddEmotionService;
import com.example.ozzy.common.exception.domain.CommonException;
import com.example.ozzy.diary.dto.request.DiaryRequest;
import com.example.ozzy.diary.dto.response.DiaryResponse;
import com.example.ozzy.diary.entity.Diary;
import com.example.ozzy.diary.mapper.DiaryMapper;
import com.example.ozzy.emotioncard.service.EmotionCardService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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

    @Transactional
    public void saveDiary(DiaryRequest diaryRequest) throws JsonProcessingException {
        Diary diary = new Diary();

        ZonedDateTime kstNow = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

        diary.setUserSeq(diaryRequest.getUserSeq());
        diary.setDiaryDate(LocalDateTime.parse(diaryRequest.getDiaryDate()));
        diary.setTitle(diaryRequest.getTitle());
        diary.setContent(diaryRequest.getContent());
        diary.setCreateAt(kstNow.toLocalDateTime());
        diary.setUpdateAt(kstNow.toLocalDateTime());

        // EMOTION_CARD()를 생성하고 diary의 EMOTION_CARD_SEQ 추가;
        int emotionCardSeq = emotionCardService.saveEmotionCard();
        diary.setEmotionCardSeq(emotionCardSeq);

        // ADD_EMOTION()를 생성하고 diary의 ADD_EMOTION_SEQ 추가;
        int addEmotionSeq = addEmotionService.saveAddEmotion();
        diary.setAddEmotionSeq(addEmotionSeq);

        try {
            diaryMapper.saveDiary(diary);
        } catch (Exception e) {
            // 에러 처리
        }

        // 비동기로 analyzeDiary 호출
        analyzeDiary(diary);
    }

    @Async
    public void analyzeDiary(Diary diary) throws JsonProcessingException {
        emotionCardService.analyzeDiary(diary);
    }

    public DiaryResponse getDiary(int userSeq, String diaryDate) {
        Diary diary = diaryMapper.getDiaryByUserAndDate(userSeq, diaryDate);
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
