package org.zerock.j2.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class JWTUtil {

    // JWT 토큰 처리시 오류 생성
    public static class CustomJWTException extends RuntimeException{
        public CustomJWTException(String msg){
            super(msg);
        }
    }

    @Value("${org.zerock.jwt.secret}")
    private String key;


    public String generate(Map<String, Object> claimMap, int min) {

        // JWT Token 생성 해더
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");

        // claims
        Map<String, Object> claims = new HashMap<>();
        claims.putAll(claimMap);

        SecretKey key = null;

        try {
            // application.properties에 지정한 key를 HMAC-SHA로 변환
           key  = Keys.hmacShaKeyFor(this.key.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {

        }

        String jwtStr = Jwts.builder()
                .setHeader(headers)
                .setClaims(claims)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
                .signWith(key)
                .compact();

        return jwtStr;

    }

    // 토큰 검증
    public Map<String, Object> validateToken(String token){

        Map<String, Object> claims = null;

        if(token == null){
            throw new CustomJWTException("NullToken");
        }

        try{
            SecretKey key = Keys.hmacShaKeyFor(this.key.getBytes(StandardCharsets.UTF_8));

            claims = Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody();

        }catch(MalformedJwtException e){
            // JWT 문자열이 문제있을떄
            throw new CustomJWTException("MalFormed");
        }catch (ExpiredJwtException e){
            // 기간 만료
            throw  new CustomJWTException("Expired");
        }catch(InvalidClaimException e){
            // 인증 되지않은 토큰
            throw new CustomJWTException("Invalid");
        }catch(JwtException e){
            // JWT에서 문제있을때
            throw new CustomJWTException(e.getMessage());
        }catch (Exception e){
            //상위 인터페이스
            throw new CustomJWTException("Error");

        }

        return claims;
    }


}
