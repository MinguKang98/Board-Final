package com.study.boardfinalback.repository;

import com.study.boardfinalback.domain.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface UserRepository {

    List<User> getUserList();

    Optional<User> getUserBySeq(int userSeq);

    void addUser(User user);

    void updatePassword(User user);

    void deleteUser(int userSeq);

    void banUser(int userSeq);

}
