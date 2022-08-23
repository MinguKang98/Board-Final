package com.study.boardfinalback.restcontroller;

import com.study.boardfinalback.annotation.CurrentUser;
import com.study.boardfinalback.dto.users.*;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
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
     * @param loginRequest : 로그인에 필요한 정보 담긴 DTO
     * @return : TokenDto
     */
    @PostMapping("/api/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

        User loginUser = userService.login(
                        loginRequest.getId(),
                        EncryptUtils.encryptPassword(loginRequest.getPassword()))
                .orElseThrow(UserNotFoundException::new);

        if (loginUser.getStatus() == UserStatus.BANNED) {
            log.info("차단된 유저입니다. id={}", loginUser.getUserSeq());
            throw new AuthorizationException();
        }

        String jwtToken = JwtUtils.makeJwtToken(loginUser);
        TokenResponse tokenResponse = TokenResponse.builder()
                .token(jwtToken)
                .userSeq(loginUser.getUserSeq())
                .build();

        return ResponseEntity.ok(tokenResponse);
    }

    /**
     * 입력받은 회원가입 정보로 회원가입을 시도한다.
     *
     * @param registerRequest : 회원가입에 필요한 정보 담긴 DTO
     * @return : void
     */
    @PostMapping("/api/users")
    public ResponseEntity register(@Valid @RequestBody RegisterRequest registerRequest) {

        if (!userService.identifyCheck(registerRequest.getId())) {
            log.info("이미 등록된 회원입니다. id={}", registerRequest.getId());
            throw new DuplicateUserException(Map.of("id", registerRequest.getId()));
        }

        if (!userService.emailCheck(registerRequest.getEmail())) {
            log.info("이미 등록된 회원입니다. email={}", registerRequest.getEmail());
            throw new DuplicateUserException(Map.of("email", registerRequest.getEmail()));
        }

        User user = User.builder()
                .name(registerRequest.getName())
                .id(registerRequest.getId())
                .email(registerRequest.getEmail())
                .password(EncryptUtils.encryptPassword(registerRequest.getPassword()))
                .role(UserRole.ROLE_MEMBER)
                .status(UserStatus.ALLOWED)
                .build();

        int userSeq = userService.saveUser(user);
        log.info("새로운 사용자가 등록되었습니다. userid={}", user.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{seq}")
                .buildAndExpand(userSeq)
                .toUri();

        return ResponseEntity.created(location).build();
    }


    /**
     * 입력받은 userSeq을 가지는 User의 정보를 가져온다.
     *
     * @param userSeq : 확인할 User의 userSeq
     * @return : UserDetailDto
     */
    @GetMapping("/api/users/{userSeq}")
    public ResponseEntity<UserDetailResponse> userDetail(@PathVariable("userSeq") int userSeq) {

        UserDetailResponse userDetailResponse = UserDetailResponse.ofUser(userService.getUserBySeq(userSeq));

        return ResponseEntity.ok(userDetailResponse);
    }

    /**
     * 유효성 검사를 통과하면 입력받은 새 비밀번호로 비밀번호를 변경한다.
     *
     * @param userSeq               : 비밀번호 변경할 User의 userSeq
     * @param passwordChangeRequest : 비밀번호 변경에 필요한 정보 담긴 DTO
     * @param currentUser           : 현재 로그인한 유저
     * @return : void
     */
    @PutMapping("/api/users/{userSeq}")
    public ResponseEntity passwordChange(@PathVariable("userSeq") int userSeq,
                                         @Valid @RequestBody PasswordChangeRequest passwordChangeRequest,
                                         @CurrentUser User currentUser) {

        User originUser = userService.getUserBySeq(userSeq);
        UserUtils.checkAuthorization(currentUser, userSeq);

        Map<String, String> passwordChangeDtoValidErrors = UserUtils.passwordChangeDtoValidCheck(
                passwordChangeRequest,
                originUser.getPassword());
        if (!passwordChangeDtoValidErrors.isEmpty()) {
            throw new PasswordChangeDtoValidException(passwordChangeDtoValidErrors);
        }

        User newUser = User.builder()
                .userSeq(userSeq)
                .password(
                        EncryptUtils.encryptPassword(passwordChangeRequest.getNewPassword())
                )
                .build();


        userService.updateUserPassword(newUser);
        log.info("비밀번호가 변경되었습니다. user={}", originUser.getId());

        return ResponseEntity.noContent().build();
    }

    /**
     * 입력받은 userSeq을 가지는 User를 삭제한다.
     *
     * @param userSeq     : 삭제할 User의 userSeq
     * @param currentUser : 현재 로그인한 User
     * @return : void
     */
    @DeleteMapping("/api/users/{userSeq}")
    public ResponseEntity deleteUser(@PathVariable("userSeq") int userSeq,
                                     @CurrentUser User currentUser) {

        userService.getUserBySeq(userSeq);
        UserUtils.checkAuthorization(currentUser, userSeq);

        userService.deleteUser(userSeq);

        return ResponseEntity.noContent().build();
    }

    /**
     * 입력한 id의 중복여부를 확인한다.
     *
     * @param idRequest : 중복여부를 확인 할 id
     * @return : void
     */
    @PostMapping("/api/users/id")
    public ResponseEntity identifyCheck(@Valid @RequestBody IdRequest idRequest) {
        boolean identifyCheck = userService.identifyCheck(idRequest.getId());

        if (!identifyCheck) {
            throw new DuplicateUserException(Map.of("id", idRequest.getId()));
        }

        return ResponseEntity.noContent().build();
    }

    /**
     * 입력한 email의 중복여부를 확인한다.
     *
     * @param emailRequest : 중복여부를 확인 할 email
     * @return : void
     */
    @PostMapping("/api/users/email")
    public ResponseEntity emailCheck(@Valid @RequestBody EmailRequest emailRequest) {
        boolean emailCheck = userService.emailCheck(emailRequest.getEmail());

        if (!emailCheck) {
            throw new DuplicateUserException(Map.of("email", emailRequest.getEmail()));
        }

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/users/{userSeq}/ban")
    public ResponseEntity banUser(@PathVariable("userSeq") int userSeq,
                                  @CurrentUser User currentUser) {

        if (currentUser.getRole() != UserRole.ROLE_ADMIN) {
            throw new AuthorizationException();
        }

        userService.getUserBySeq(userSeq);
        userService.banUser(userSeq);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/users/{userSeq}/unban")
    public ResponseEntity unbanUser(@PathVariable("userSeq") int userSeq,
                                    @CurrentUser User currentUser) {

        if (currentUser.getRole() != UserRole.ROLE_ADMIN) {
            throw new AuthorizationException();
        }

        userService.getUserBySeq(userSeq);
        userService.unbanUser(userSeq);
        return ResponseEntity.noContent().build();
    }

}
