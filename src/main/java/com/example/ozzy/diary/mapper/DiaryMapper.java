package com.example.ozzy.diary.mapper;

import com.example.ozzy.diary.entity.Diary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DiaryMapper {
    void saveDiary(Diary diary);
    void updateDiary(Diary diary);
    void deleteDiary(int userSeq, LocalDate diaryDate);
    List<LocalDate> getWeeklyCount(@Param("userSeq") int userSeq, @Param("startDate") LocalDate startDate);
    Diary getDiaryByUserAndDate(int userSeq, LocalDate diaryDate);
}
