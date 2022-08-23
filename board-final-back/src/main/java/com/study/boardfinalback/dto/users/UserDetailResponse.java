package com.study.boardfinalback.dto.users;

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
public class UserDetailResponse {

    private int userSeq;
    private LocalDateTime dateCreated;
    private String name;
    private String id;
    private String email;
    private UserRole role;

    public static UserDetailResponse ofUser(User user) {

        UserDetailResponse userDetailResponse = UserDetailResponse.builder()
                .userSeq(user.getUserSeq())
                .dateCreated(user.getDateCreated())
                .name(user.getName())
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        return userDetailResponse;
    }

}
