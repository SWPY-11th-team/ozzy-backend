package com.example.ozzy.diary.mapper;

import com.example.ozzy.diary.entity.Diary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface DiaryMapper {
    void saveDiary(Diary diary);
    void updateDiary(Diary diary);
    Diary getDiaryByUserAndDate(int userSeq, LocalDate diaryDate);
}
