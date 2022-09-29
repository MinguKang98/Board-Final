package com.study.boardfinalback.jwt;

import com.study.boardfinalback.domain.users.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

/**
 * JWT 생성 클래스
 */
@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${jwt.token-valid-minutes}")
    private int TOKEN_VALID_MINUTES;

    /**
     * 입력받은 User의 id와 role을 포함하는 jwt token return
     *
     * @param user : jwt 토큰에 정보 넣을 User
     * @return : 입력받은 User의 id와 role을 포함한 jwt token
     */
    public String makeJwtToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime()
                + Duration.ofMinutes(this.TOKEN_VALID_MINUTES).toMillis());
        Claims claims = Jwts.claims(Map.of(
                "userSeq", user.getUserSeq(),
                "id", user.getId(),
                "role", user.getRole()));

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, this.SECRET_KEY)
                .compact();
    }

    /**
     * 입력받은 token을 복호화 후 body를 return
     *
     * @param token : jwt token
     * @return : 입력받은 token의 복호화 된 body
     */
    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(this.SECRET_KEY).parseClaimsJws(token).getBody();
    }

    /**
     * 입력받은 token의 유효성 검증
     *
     * @param token : jwt token
     * @return : 토큰 유효 여부
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(this.SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token : ", e);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token : ", e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token : ", e);
        } catch (SignatureException e) {
            log.error("Invalid JWT signature : ", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is null or empty : ", e);
        }
        return false;
    }

}
