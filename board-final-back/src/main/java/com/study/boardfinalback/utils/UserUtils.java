package com.study.boardfinalback.utils;

import com.study.boardfinalback.dto.users.PasswordChangeRequest;
import com.study.boardfinalback.domain.users.User;
import com.study.boardfinalback.domain.users.UserRole;
import com.study.boardfinalback.error.users.AuthorizationException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * User 관련 utility 제공 클래스
 */
@Slf4j
public class UserUtils {

    /**
     * 현재 로그인한 User와 접근하려는 User의 userSeq을 비교해 접근 권한이 없다면 throw new AuthorizationException()
     * 
     * @param currentUser : 현재 로그인한 User
     * @param userSeq : 접근하려는 User의 userSeq
     */
    public static void checkAuthorization(User currentUser, int userSeq) {

        int currentUserSeq = currentUser.getUserSeq();
        UserRole currentUserRole = currentUser.getRole();

        if ((currentUserSeq != userSeq) && (currentUserRole == UserRole.ROLE_MEMBER)) {
            throw new AuthorizationException();
        }

    }

    /**
     * posswordChangeDto를 확인하여 에러가 있다면 에러 메세지가 담긴 HashMap을 return. 에러가 없다면 빈 HashMap을 return
     *
     * @param passwordChangeRequest : 패스워드 변경 사항 담긴 DTO
     * @param originPassword : 기존 패스워드
     * @return : 에러가 없다면 빈 HashMap, 에러가 있다면 에러 메시지가 담긴 HashMap
     */
    public static Map<String, String> passwordChangeDtoValidCheck(
            PasswordChangeRequest passwordChangeRequest,
            String originPassword) {

        Map<String, String> map = new HashMap<>();

        if (passwordChangeRequest.getOriginPassword().equals(passwordChangeRequest.getNewPassword())) {
            log.info("비밀번호 변경 형식 에러 : 새 비밀번호가 기존 비밀번호와 일치합니다.");
            map.put("new password error", "새 비밀번호가 기존 비밀번호와 일치합니다.");
        }

        if (!passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getNewPasswordCheck())) {
            log.info("비밀번호 변경 형식 에러 : 새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.");
            map.put("new password check error", "새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.");
        }

        if (!originPassword.equals(
                EncryptUtils.encryptPassword(passwordChangeRequest.getOriginPassword()
                ))) {
            log.info("비밀번호 변경 형식 에러 : 기존 비밀번호가 일치하지 않습니다.");
            map.put("origin password error", "기존 비밀번호가 일치하지 않습니다.");
        }

        return map;
    }

}
