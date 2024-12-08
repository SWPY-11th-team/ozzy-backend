package com.example.ozzy.user.service;

import com.example.ozzy.oauth2.user.Token;
import com.example.ozzy.oauth2.util.CookieUtils;
import com.example.ozzy.oauth2.util.JwtTokenUtil;
import com.example.ozzy.user.dto.request.RefreshTokenRequest;
import com.example.ozzy.user.dto.request.UserRequest;
import com.example.ozzy.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final JwtTokenUtil jwtTokenUtil;

    public int getNextUserSeq() {
        return userMapper.getNextUserSeq();
    }

    /**
     * 사용자생성, 토큰생성, 약관생성
     * 저장 순서 중요!
     * */
    @Transactional
    public void insertUser(UserRequest userRequest, Token token) {
        String expirationDateFromToken = jwtTokenUtil.getExpirationDateFromToken(token.getRefreshToken());

        userMapper.insertUser(userRequest);     // 사용자 저장
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(token.getRefreshToken(), expirationDateFromToken, userRequest.getUserSeq());
        userMapper.insertRefreshToken(refreshTokenRequest);
//        userMapper.insertServiceTerms(ServiceTermsRequest.firstAdd(userRequest.getUserSeq()));
    }

}
