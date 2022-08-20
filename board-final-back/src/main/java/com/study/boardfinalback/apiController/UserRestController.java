package com.study.boardfinalback.apiController;

import com.study.boardfinalback.annotation.CurrentUser;
import com.study.boardfinalback.apiDto.users.*;
import com.study.boardfinalback.domain.users.User;
import com.study.boardfinalback.domain.users.UserRole;
import com.study.boardfinalback.domain.users.UserStatus;
import com.study.boardfinalback.error.users.AuthorizationException;
import com.study.boardfinalback.error.users.DuplicateUserException;
import com.study.boardfinalback.error.users.UserNotFoundException;
import com.study.boardfinalback.error.users.PasswordChangeDtoValidException;
import com.study.boardfinalback.service.UserService;
import com.study.boardfinalback.utils.EncryptUtils;
import com.study.boardfinalback.utils.JwtUtils;
import com.study.boardfinalback.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * User 관련 api 요청 처리하는 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    /**
     * 입력받은 로그인 정보로 로그인을 시도한다.
     *
     * @param loginDto : 로그인에 필요한 정보 담긴 DTO
     * @return : 200 with TokenDto
     */
    @PostMapping("/api/login")
    public ResponseEntity<TokenDto> login(@Valid LoginDto loginDto) {

        User loginUser = userService.login(
                        loginDto.getId(),
                        EncryptUtils.encryptPassword(loginDto.getPassword()))
                .orElseThrow(UserNotFoundException::new);

        if (loginUser.getStatus() == UserStatus.BANNED) {
            log.info("차단된 유저입니다. id={}", loginUser.getUserSeq());
            throw new AuthorizationException();
        }

        String jwtToken = JwtUtils.makeJwtToken(loginUser);
        TokenDto token = new TokenDto(jwtToken);

        return new ResponseEntity(token, HttpStatus.OK);
    }

    /**
     * 입력받은 회원가입 정보로 회원가입을 시도한다.
     *
     * @param registerDto : 회원가입에 필요한 정보 담긴 DTO
     * @return : 201
     */
    @PostMapping("/api/users")
    public ResponseEntity register(@Valid RegisterDto registerDto) {

        if (!userService.identifyCheck(registerDto.getId())) {
            log.info("이미 등록된 회원입니다. id={}", registerDto.getId());
            throw new DuplicateUserException(Map.of("id", registerDto.getId()));
        }

        if (!userService.emailCheck(registerDto.getEmail())) {
            log.info("이미 등록된 회원입니다. email={}", registerDto.getEmail());
            throw new DuplicateUserException(Map.of("email", registerDto.getEmail()));
        }

        User user = User.builder()
                .name(registerDto.getName())
                .id(registerDto.getId())
                .email(registerDto.getEmail())
                .password(EncryptUtils.encryptPassword(registerDto.getPassword()))
                .role(UserRole.ROLE_MEMBER)
                .status(UserStatus.ALLOWED)
                .build();

        int userSeq = userService.saveUser(user);
        log.info("새로운 사용자가 등록되었습니다. userid={}", user.getId());

        return new ResponseEntity(HttpStatus.CREATED);
    }


    /**
     * 입력받은 userSeq을 가지는 User의 정보를 가져온다.
     *
     * @param userSeq : 확인할 User의 userSeq
     * @return : 200 with UserDetailDto
     */
    @GetMapping("/api/users/{userSeq}")
    public ResponseEntity<UserDetailDto> userDetail(@PathVariable("userSeq") int userSeq) {

        UserDetailDto userDetailDto = UserDetailDto.ofUser(userService.getUserBySeq(userSeq));

        return new ResponseEntity(userDetailDto, HttpStatus.OK);
    }

    /**
     * 유효성 검사를 통과하면 입력받은 새 비밀번호로 비밀번호를 변경한다.
     *
     * @param userSeq           : 비밀번호 변경할 User의 userSeq
     * @param passwordChangeDto : 비밀번호 변경에 필요한 정보 담긴 DTO
     * @param currentUser       : 현재 로그인한 유저
     * @return 204
     */
    @PutMapping("/api/users/{userSeq}")
    public ResponseEntity passwordChange(@PathVariable("userSeq") int userSeq,
                                         @Valid PasswordChangeDto passwordChangeDto,
                                         @CurrentUser User currentUser) {

        User originUser = userService.getUserBySeq(userSeq);
        UserUtils.checkAuthorization(currentUser, userSeq);

        Map<String, String> passwordChangeDtoValidErrors = UserUtils.passwordChangeDtoValidCheck(
                passwordChangeDto,
                originUser.getPassword());
        if (!passwordChangeDtoValidErrors.isEmpty()) {
            throw new PasswordChangeDtoValidException(passwordChangeDtoValidErrors);
        }

        User newUser = User.builder()
                .userSeq(userSeq)
                .password(
                        EncryptUtils.encryptPassword(passwordChangeDto.getNewPassword())
                )
                .build();


        userService.updateUserPassword(newUser);
        log.info("비밀번호가 변경되었습니다. user={}", originUser.getId());

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * 입력받은 userSeq을 가지는 User를 삭제한다.
     *
     * @param userSeq : 삭제할 User의 userSeq
     * @param currentUser : 현재 로그인한 User
     * @return : 204
     */
    @DeleteMapping("/api/users/{userSeq}")
    public ResponseEntity deleteUser(@PathVariable("userSeq") int userSeq,
                                     @CurrentUser User currentUser) {

        userService.getUserBySeq(userSeq);
        UserUtils.checkAuthorization(currentUser, userSeq);

        userService.deleteUser(userSeq);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * 입력한 id의 중복여부를 확인한다.
     *
     * @param id : 중복여부를 확인 할 id
     * @return : 204
     */
    @PostMapping("/api/users/id")
    public ResponseEntity identifyCheck(@RequestParam(value = "id") String id) {
        boolean identifyCheck = userService.identifyCheck(id);

        if (!identifyCheck) {
            throw new DuplicateUserException(Map.of("id", id));
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * 입력한 email의 중복여부를 확인한다.
     *
     * @param email : 중복여부를 확인 할 email
     * @return : 204
     */
    @PostMapping("/api/users/email")
    public ResponseEntity emailCheck(@RequestParam(value = "email") String email) {
        boolean emailCheck = userService.emailCheck(email);

        if (!emailCheck) {
            throw new DuplicateUserException(Map.of("email", email));
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
