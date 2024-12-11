package com.example.ozzy.user.service;

import com.example.ozzy.common.exception.domain.CommonException;
import com.example.ozzy.common.exception.enums.ExceptionType;
import com.example.ozzy.oauth2.service.OAuth2UserPrincipal;
import com.example.ozzy.oauth2.user.Token;
import com.example.ozzy.oauth2.util.JwtTokenUtil;
import com.example.ozzy.user.dto.request.RefreshTokenRequest;
import com.example.ozzy.user.dto.request.UserRequest;
import com.example.ozzy.user.dto.response.UserResponse;
import com.example.ozzy.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

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
        LocalDateTime expirationDateFromToken = jwtTokenUtil.getExpirationDateFromToken(token.getRefreshToken());

        userMapper.insertUser(userRequest);     // 사용자 저장
        insertRefreshToken(new RefreshTokenRequest(token.getRefreshToken(), expirationDateFromToken, userRequest.getUserSeq()));    // refreshToken 발급
//        userMapper.insertServiceTerms(ServiceTermsRequest.firstAdd(userRequest.getUserSeq()));    // todo 여기서 할까 말까..
    }

    public void insertRefreshToken(RefreshTokenRequest refreshTokenRequest) {
        userMapper.insertRefreshToken(refreshTokenRequest);
    }

    public int findUser(OAuth2UserPrincipal principal) {
        Optional<UserResponse> userId = userMapper.findUserId(UserRequest.find(principal.getUserInfo().getEmail(), principal.getUserInfo().getProvider().toString()));
        return userId.map(UserResponse::getUserSeq).orElse(0);
    }

    public UserResponse selectUserInfo() {
        // UserContext.getUserId();
        return userMapper.selectUserInfo(90);
    }

    public void updateUserName(UserRequest user) {

        if (user.getNickName().isEmpty()) {
            throw new CommonException(ExceptionType.USER_NICK_NAME);
        }

        // UserContext.getUserId();
        userMapper.updateUserName(UserRequest.updateName(90, user.getNickName()));
    }

}
