package com.example.bangyo.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // yml, properties 등의 환경변수에서 가져온다고 가정 (임의 값 예시)
    @Value("${jwt.secretKey:default_secret_key_please_change}")
    private String secretKey;

    // 토큰 유효 기간(밀리초) 예: 1시간
    private final long validityInMilliseconds = 60 * 60 * 1000L;

    /**
     * 토큰 생성
     */
    public String generateToken(Authentication authentication) {
        // UserDetailsService에서 설정한 UserDetails의 username(email)
        String email = authentication.getName();
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        // 시크릿키 객체 생성
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.builder()
                .setSubject(email)               // 토큰 제목(보통 username이나 userId)
                .setIssuedAt(now)                // 토큰 발급 시간
                .setExpiration(validity)         // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명
                .compact();
    }

    /**
     * 토큰에서 인증 정보 추출 (여기서는 email)
     */
    public String getEmail(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    /**
     * 토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            System.out.println("Invalid JWT signature.");
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT token compact of handler are invalid");
        }
        return false;
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
