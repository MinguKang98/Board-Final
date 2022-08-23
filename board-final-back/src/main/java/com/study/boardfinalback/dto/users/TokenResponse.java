package com.study.boardfinalback.dto.users;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 후 token 보낼 때 사용하는 DTO
 */
@Getter
@Setter
public class TokenResponse {

    private String grantType = "Bearer";
    private String token;
    private int userSeq;

    @Builder
    public TokenResponse(String token, int userSeq) {
        this.token = token;
        this.userSeq = userSeq;
    }

}
