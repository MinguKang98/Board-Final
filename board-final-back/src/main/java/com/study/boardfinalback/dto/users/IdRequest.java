package com.study.boardfinalback.dto.users;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class IdRequest {

    @Size(min = 3, max = 10, message = "아이디는 3글자에서 10글자 사이여야 합니다.")
    @NotBlank(message = "아이디를 입력하세요")
    private String id;

}
