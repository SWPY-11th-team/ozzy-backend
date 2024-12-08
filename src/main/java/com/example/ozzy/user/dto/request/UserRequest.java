package com.example.ozzy.user.dto.request;

import com.example.ozzy.oauth2.service.OAuth2UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    private int userSeq;
    private String nickName;
    private String provider;
    private String email;

    public static UserRequest add(int seq, OAuth2UserPrincipal principal) {
        return new UserRequest(
                seq,
                principal.getUserInfo().getNickname(),
                principal.getUserInfo().getProvider().toString(),
                principal.getUserInfo().getEmail()
        );
    }

}
