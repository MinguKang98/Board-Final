package com.study.boardfinalback.apiDto.users;

import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 후 token 보낼 때 사용하는 DTO
 */
@Getter
@Setter
public class TokenDto {

    private String grantType = "Bearer";
    private String token;

    public TokenDto(String token) {
        this.token = token;
    }

}
