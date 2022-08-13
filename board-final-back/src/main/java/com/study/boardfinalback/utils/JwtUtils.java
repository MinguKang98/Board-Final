package com.study.boardfinalback.utils;

import com.study.boardfinalback.domain.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Duration;
import java.util.Date;

/**
 * JWT Token 관련 utility 제공 클래스
 */
public class JwtUtils {

    /**
     * 입력받은 User의 id와 role을 포함하는 jwt token return
     *
     * @param user : jwt 토큰에 정보 넣을 User
     * @return : 입력받은 User의 id와 role을 포함한 jwt token
     */
    public static String makeJwtToken(User user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(30).toMillis()))
                .claim("userSeq", user.getUserSeq())
                .claim("id", user.getId())
                .claim("role", user.getRole())
                .signWith(SignatureAlgorithm.HS256, "secret")
                .compact();
    }

    /**
     * 입력받은 token을 복호화 후 body를 return
     *
     * @param token : jwt token
     * @return : 입력받은 token의 복호화 된 body
     */
    public static Claims getClaims(String token) {
        return Jwts.parser().setSigningKey("secret").parseClaimsJws(token).getBody();
    }
    
    //TODO 토큰 유효성 확인

}
