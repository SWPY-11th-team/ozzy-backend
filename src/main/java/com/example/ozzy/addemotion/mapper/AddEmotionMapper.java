package com.example.ozzy.addemotion.mapper;

import com.example.ozzy.addemotion.dto.request.AddEmotionsRequest;
import com.example.ozzy.common.dto.UserDateRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AddEmotionMapper {

    int insertAddEmotions(@Param("emotion") String emtion);

    String findAddEmotions(UserDateRequest todayDate);
}
