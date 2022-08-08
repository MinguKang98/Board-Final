package com.study.boardfinalback.repository;

import com.study.boardfinalback.domain.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * users 테이블 관련 쿼리 결과를 User 클래스에 매핑하는 interface
 */
@Mapper
@Repository
public interface UserRepository {

    /**
     * 모든 User return
     *
     * @return : 모든 User
     */
    List<User> getUserList();

    /**
     * 입력받은 userSeq 가지는 Optional<User> return
     *
     * @param userSeq : return할 User의 userSeq
     * @return : 해당 userSeq 가지는 Optional<User> 인스턴스
     */
    Optional<User> getUserBySeq(int userSeq);

    /**
     * 입력받은 id 가지는 Optional<User> return
     *
     * @param id : return할 User의 id
     * @return : 해당 id 가지는 Optional<User> 인스턴스
     */
    Optional<User> getUserById(String id);

    /**
     * 입력받은 email 가지는 Optional<User> return
     *
     * @param email : return할 User의 email
     * @return : 해당 id 가지는 Optional<User> 인스턴스
     */
    Optional<User> getUserByEmail(String email);


    /**
     * 입력받은 User 추가
     * 
     * @param user : 추가할 User 인스턴스
     */
    void addUser(User user);

    /**
     * 입력받은 User password 수정
     *
     * @param user : userSeq과 수정할 password 가지는 User 인스턴스
     */
    void updatePassword(User user);

    /**
     * 입력받은 userSeq을 가지는 User 삭제
     *
     * @param userSeq : 삭제할 User의 userSeq
     */
    void deleteUser(int userSeq);

    /**
     * 입력받은 userSeq을 가지는 User 차단(userStatus BANNED로 변경)
     *
     * @param userSeq : 차단할 User의 userSeq
     */
    void banUser(int userSeq);

}
