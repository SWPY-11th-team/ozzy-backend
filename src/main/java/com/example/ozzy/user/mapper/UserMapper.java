package com.example.ozzy.user.mapper;

import com.example.ozzy.user.dto.request.RefreshTokenRequest;
import com.example.ozzy.user.dto.request.UserRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int getNextUserSeq();
    int insertUser(UserRequest request);
    int insertRefreshToken(RefreshTokenRequest request);
}
