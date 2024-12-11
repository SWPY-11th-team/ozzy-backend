package com.example.ozzy.user.mapper;

import com.example.ozzy.user.dto.request.RefreshTokenRequest;
import com.example.ozzy.user.dto.request.ServiceTermsRequest;
import com.example.ozzy.user.dto.request.UserRequest;
import com.example.ozzy.user.dto.response.UserResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {
    int getNextUserSeq();
    int insertUser(UserRequest request);
    int insertRefreshToken(RefreshTokenRequest request);
    int insertServiceTerms(ServiceTermsRequest request);
    Optional<UserResponse> findUserId(UserRequest request);
    UserResponse selectUserInfo(@Param("seq") int seq);
    int updateUserName(UserRequest request);
}
