package com.study.boardfinaltemplate.domain.users;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

/**
 * 유저 클래스
 */
@Getter
@Alias(value = "User")
@NoArgsConstructor
public class User {

    private int userSeq;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    private String name;
    private String id;
    private String email;
    private String password;
    private UserRole role;
    private UserStatus status;

    @Builder
    public User(int userSeq,
                String name,
                String id,
                String email,
                String password,
                UserRole role,
                UserStatus status) {

        this.userSeq = userSeq;
        this.name = name;
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

}
