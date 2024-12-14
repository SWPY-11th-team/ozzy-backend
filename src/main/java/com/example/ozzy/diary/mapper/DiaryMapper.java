package com.example.ozzy.diary.mapper;

import com.example.ozzy.diary.entity.Diary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface DiaryMapper {
    void saveDiary(Diary diary);
    void updateDiary(Diary diary);
    void deleteDiary(int userSeq, LocalDate diaryDate);
    int getWeeklyCount(int userSeq, LocalDate startDate);
    Diary getDiaryByUserAndDate(int userSeq, LocalDate diaryDate);
}
