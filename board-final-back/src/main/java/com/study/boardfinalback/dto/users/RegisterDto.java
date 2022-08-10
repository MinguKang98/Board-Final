package com.study.boardfinalback.dto.users;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * register 페이지에 사용되는 DTO
 */
@Getter
@Setter
public class RegisterDto {

    @Size(min = 2, max = 10, message = "이름은 2글자에서 10글자 사이여야 합니다.")
    private String name;

    @Size(min = 3, max = 10, message = "아이디는 3글자에서 10글자 사이여야 합니다.")
    private String id;

    @Email
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{4,16}$",
            message ="비밀번호는 4글자 이상 16글자 이하의 영문, 숫자, 특수문자의 조합이여야 합니다.")
    private String password;

}
