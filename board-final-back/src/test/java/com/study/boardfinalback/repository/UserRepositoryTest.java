package com.study.boardfinalback.repository;

import com.study.boardfinalback.domain.user.User;
import com.study.boardfinalback.domain.user.UserRole;
import com.study.boardfinalback.domain.user.UserStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void 유저_모두_가져오기() throws Exception {
        ///when
        List<User> userList = userRepository.getUserList();

        //then
        assertThat(userList.size()).isEqualTo(3);
    }

    @Test
    public void 유저_가져오기_정상() throws Exception {
        //given
        int userSeq = 3;

        ///when
        Optional<User> user = userRepository.getUserBySeq(userSeq);

        //then
        assertThat(user.get().getName()).isEqualTo("lee");
    }

    @Test
    public void 유저_가져오기_오류() throws Exception {
        //given
        int userSeq = 321;

        ///when
        Optional<User> user = userRepository.getUserBySeq(userSeq);

        //then
        assertThat(user.isEmpty()).isTrue();
    }

    @Disabled
    @Test
    public void 유저_생성() throws Exception {
        //given
        User user = User.builder()
                .name("kim")
                .id("kim123")
                .email("abc@naver.com")
                .password("123a!")
                .role(UserRole.ROLE_MEMBER)
                .status(UserStatus.ALLOWED)
                .build();

        ///when
        userRepository.addUser(user);
        int userSeq = user.getUserSeq();

        //then
        Optional<User> user1 = userRepository.getUserBySeq(userSeq);
        assertThat(user1.get().getName()).isEqualTo(user.getName());
    }

    @Test
    public void 비밀번호_변경() throws Exception {
        //given
        int userSeq = 3;
        User user = User.builder()
                .userSeq(userSeq)
                .password("123abc!")
                .build();

        ///when
        userRepository.updatePassword(user);

        //then
        User user1 = userRepository.getUserBySeq(userSeq).get();
        assertThat(user1.getPassword()).isEqualTo("123abc!");
    }

    @Test
    public void 유저_삭제() throws Exception {
        //given
        int userSeq = 3;

        ///when
        userRepository.deleteUser(userSeq);

        //then
        Optional<User> user = userRepository.getUserBySeq(userSeq);
        assertThat(user.isEmpty()).isTrue();
    }

    @Test
    public void 유저_차단() throws Exception {
        //given
        int userSeq = 3;

        ///when
        userRepository.banUser(userSeq);

        //then
        Optional<User> user = userRepository.getUserBySeq(userSeq);
        assertThat(user.get().getStatus()).isEqualTo(UserStatus.BANNED);
    }

}