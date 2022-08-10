package com.study.boardfinalback.dto.users;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * passwordChange 페이지에 사용되는 DTO
 */
@Getter
@Setter
public class PasswordChangeDto {

    @NotBlank
    String originPassword;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{4,16}$",
            message ="새 비밀번호는 4글자 이상 16글자 이하의 영문, 숫자, 특수문자의 조합이여야 합니다.")
    String newPassword;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{4,16}$",
            message ="새 비밀번호 확인은 4글자 이상 16글자 이하의 영문, 숫자, 특수문자의 조합이여야 합니다.")
    String newPasswordCheck;

}
