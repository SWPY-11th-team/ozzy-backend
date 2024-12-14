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
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
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

    // 일기 생성
    public DiaryResponse saveDiary(DiaryRequest diaryRequest) throws JsonProcessingException {
        int userId = UserContext.getUserId();
        LocalDate diaryDate = parseDiaryDate(diaryRequest.getDiaryDate());
        Diary checkDiary = getExistingDiary(userId, diaryDate);

        if(checkDiary != null) {
            throw new CommonException("해당 일자에 작성된 일기가 있습니다. 수정을 이용하거나, 삭제 후 새로운 일기를 작성해주세요.", 409);
        }

        Diary diary = createDiaryFromRequest(diaryRequest, userId);
        diaryMapper.saveDiary(diary);
        analyzeDiary(diary);
        return convertToResponse(diary);
    }

    // 일기 수정
    public DiaryResponse updateDiary(DiaryRequest diaryRequest) throws JsonProcessingException {
        int userId = UserContext.getUserId();
        LocalDate dateTime = parseDiaryDate(diaryRequest.getDiaryDate());
        Diary existingDiary = getExistingDiary(userId, dateTime);

        if(existingDiary == null) {
            throw new CommonException("해당 일자에 작성한 일기가 없습니다.", 404);
        }

        updateDiaryFields(existingDiary, diaryRequest);
        diaryMapper.updateDiary(existingDiary);
        analyzeDiary(existingDiary);

        return convertToResponse(existingDiary);
    }

    // 일기 조회
    public DiaryResponse getDiary(String diaryDate) {
        int userId = UserContext.getUserId();
        Diary diary = getExistingDiary(userId, parseDiaryDate(diaryDate));

        if(diary == null) {
            throw new CommonException("해당 일자에 작성한 일기가 없습니다.", 404);
        }

        return convertToResponse(diary);
    }

    @Async
    public void analyzeDiary(Diary diary) throws JsonProcessingException {
        emotionCardService.analyzeDiary(diary);
    }

    private Diary createDiaryFromRequest(DiaryRequest diaryRequest, int userId) {
        LocalDate date = parseDiaryDate(diaryRequest.getDiaryDate());

        Diary diary = new Diary();
        diary.setUserSeq(userId);
        diary.setDiaryDate(date);
        diary.setTitle(diaryRequest.getTitle());
        diary.setContent(diaryRequest.getContent());

        // EMOTION_CARD 및 ADD_EMOTION 생성
        diary.setEmotionCardSeq(emotionCardService.saveEmotionCard());
        diary.setAddEmotionSeq(addEmotionService.saveAddEmotion());

        return diary;
    }

    // String을 LocalDate 타입으로 변환
    private LocalDate parseDiaryDate(String diaryDate) {
        return LocalDate.parse(diaryDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    // 일기 정보 확인
    private Diary getExistingDiary(int userSeq, LocalDate diaryDate) {
        return diaryMapper.getDiaryByUserAndDate(userSeq, diaryDate);
    }

    // 다이어리 정보 업데이트
    private void updateDiaryFields(Diary existingDiary, DiaryRequest diaryRequest) {
        existingDiary.setTitle(diaryRequest.getTitle());
        existingDiary.setContent(diaryRequest.getContent());
    }

    // 응답 생성
    private DiaryResponse convertToResponse(Diary diary) {
        DiaryResponse response = new DiaryResponse();
        response.setDiaryDate(diary.getDiaryDate());
        response.setTitle(diary.getTitle());
        response.setContent(diary.getContent());
        response.setEmotionCardSeq(diary.getEmotionCardSeq());
        response.setAddEmotionSeq(diary.getAddEmotionSeq());
        response.setCreateAt(diary.getCreateAt());
        response.setUpdateAt(diary.getUpdateAt()); // 업데이트 시각 설정
        return response;
    }
}
