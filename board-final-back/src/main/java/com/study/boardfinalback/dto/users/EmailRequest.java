package com.study.boardfinalback.dto.users;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class EmailRequest {

    @Email
    @NotBlank(message = "이메일을 입력하세요.")
    private String email;

}
