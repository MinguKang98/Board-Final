package com.study.boardfinalback.apiDto.users;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 비밀번호 변경 시 사용되는 DTO
 */
@Getter
@Setter
public class PasswordChangeDto {

    @NotBlank(message = "기존 비밀번호를 입력하세요.")
    String originPassword;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{4,16}$",
            message ="새 비밀번호는 4글자 이상 16글자 이하의 영문, 숫자, 특수문자의 조합이여야 합니다.")
    String newPassword;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{4,16}$",
            message ="새 비밀번호 확인은 4글자 이상 16글자 이하의 영문, 숫자, 특수문자의 조합이여야 합니다.")
    String newPasswordCheck;

}