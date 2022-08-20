package com.study.boardfinalback.apiDto.users;

import com.study.boardfinalback.domain.users.User;
import com.study.boardfinalback.domain.users.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 유저 정보 확인 시 사용하는 DTO
 */
@Getter
@Setter
@Builder
public class UserDetailDto {

    private int userSeq;
    private LocalDateTime dateCreated;
    private String name;
    private String id;
    private String email;
    private UserRole role;

    public static UserDetailDto ofUser(User user) {

        UserDetailDto userDetailDto = UserDetailDto.builder()
                .userSeq(user.getUserSeq())
                .dateCreated(user.getDateCreated())
                .name(user.getName())
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        return userDetailDto;
    }

}
