package com.study.boardfinalback.apiDto.users;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 로그인 시 사용되는 DTO
 */
@Getter
@Setter
public class LoginDto {

    @NotBlank(message = "아이디를 입력하세요")
    String id;

    @NotBlank(message = "비밀번호를 입력하세요")
    String password;

}
