package com.example.ozzy.diary.mapper;

import com.example.ozzy.diary.entity.Diary;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DiaryMapper {
    void saveDiary(Diary diary);
    Diary getDiaryByUserAndDate(int userSeq, String diaryDate);
}
