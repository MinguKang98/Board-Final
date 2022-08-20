package com.study.boardfinalback.service;

import com.study.boardfinalback.domain.users.User;
import com.study.boardfinalback.error.users.UserNotFoundException;
import com.study.boardfinalback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User 클래스 관련 비즈니스 로직 구현
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 입력받은 userSeq 가지는 User return. 해당 User 없다면 throw UserNotFoundException
     *
     * @param userSeq : return할 User의 userSeq
     * @return : 입력받은 userSeq 가지는 User, 없다면 throw UserNotFoundException
     */
    public User getUserBySeq(int userSeq) {

        User user = userRepository.getUserBySeq(userSeq)
                .orElseThrow(UserNotFoundException::new);
        return user;
    }

    /**
     * 입력받은 id 가지는 User return. 해당 User 없다면 throw UserNotFoundException
     *
     * @param id : return할 User의 id
     * @return : 입력받은 id 가지는 User, 없다면 throw UserNotFoundException
     */
    public User getUserById(String id) {

        User user = userRepository.getUserById(id)
                .orElseThrow(UserNotFoundException::new);
        return user;
    }

    /**
     * 입력받은 User 추가
     *
     * @param user : 추가할 User 인스턴스
     * @return : 추가된 User의 userSeq
     */
    public int saveUser(User user) {

        userRepository.addUser(user);
        return user.getUserSeq();
    }

    /**
     * 입력받은 userSeq 가지는 User 삭제
     *
     * @param userSeq : 삭제할 User의 userSeq
     */
    public void deleteUser(int userSeq) {

        userRepository.deleteUser(userSeq);
    }

    /**
     * 입력받은 User의 password 수정
     * 
     * @param user : 수정할 User의 userSeq과 변경할 password를 가지는 User 인스턴스
     */
    public void updateUserPassword(User user) {

        userRepository.updatePassword(user);
    }


    /**
     * 입력받은 userSeq 가지는 User 차단(User의 status BANNED로 수정)
     *
     * @param userSeq
     */
    public void banUser(int userSeq) {

        userRepository.banUser(userSeq);
    }

    /**
     * 입력받은 id를 가지는 User가 있는지 확인하는 id 중복 체크를 한다.
     *
     * @param id : 중복 체크할 id
     * @return : 사용이 가능한 id면 true, 사용 불가능한 id면 false
     */
    public boolean identifyCheck(String id) {

        Optional<User> user = userRepository.getUserById(id);
        if (user.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 입력받은 email를 가지는 User가 있는지 확인하는 email 중복 체크를 한다.
     *
     * @param email : 중복 체크할 email
     * @return : 사용이 가능한 email이면 true, 사용 불가능한 email이면 false
     */
    public boolean emailCheck(String email) {

        Optional<User> user = userRepository.getUserByEmail(email);
        if (user.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 입력받은 id와 password를 가지는 User를 찾아 return. 해당 User가 존재하지 않는다면 Optional.empty() return
     *
     * @param id : return할 User의 id
     * @param password : return할 User의 password
     * @return : id와 password가 일치하는 User, 없다면 Optional.empty()
     */
    public Optional<User> login(String id, String password) {

        Optional<User> user = userRepository.getUserById(id);

        if (user.isEmpty()) {
            log.debug("해당 id를 가지는 유저가 없습니다.");
            return Optional.empty();
        }

        if (!password.equals(user.get().getPassword())) {
            log.debug("잘못된 비밀번호를 입력하였습니다.");
            return Optional.empty();
        }
        log.info("로그인 성공 - userSeq ={}",user.get().getUserSeq());
        return user;
    }

}
