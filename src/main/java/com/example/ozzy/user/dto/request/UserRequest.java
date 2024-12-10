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

    public static UserRequest find(String email, String provider) {
        return new UserRequest(0, null, provider, email);
    }

    public static UserRequest updateName(int seq, String name) {
        return new UserRequest(seq, name, null, null);
    }

}
