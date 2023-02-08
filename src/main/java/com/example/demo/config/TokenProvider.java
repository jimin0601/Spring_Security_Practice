package com.example.demo.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private final String secret;
    private final long tokenValidityInMilliseconds;

    private Key key;

    public TokenProvider(
        @Value("${jwt.secret}") String secret,
        @Value("${hwt.token-validity-in-seconds") long tokenValidityInMilliseconds) {
            this.secret = secret;
            this.tokenValidityInMilliseconds = tokenValidityInMilliseconds * 1000;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /*
         토큰 생성 메서드
     */

    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds); // 토큰 만료기간

        // JWT 토큰 생성
        return Jwts.builder()
                // 토큰 용도
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                // 키랑 알고리즘 정보
                .signWith(key, SignatureAlgorithm.HS512)
                // 토큰 만료기간
                .setExpiration(validity)
                // 설정 끝내고 토큰 만들기
                .compact();
    }

    /*
        Toke에 담겨있는 정보를 이용해 Authentication 객체를 반환하는 메서드
     */

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                // parserBuilder 를 생성한 뒤, 토큰 값을 넣어서 Jws 를 생성합니다.
                // Jws 를 생성하면 메소드를 이용해서 토큰 내부의 값을 parse 해서 가져올 수 있습니다.
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        // 리스트 타입으로 반환
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /*
        토큰을 파싱하고 발생하는 예외를 처리, 문제가 있을 경우 false 반환
     */

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 토큰입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalStateException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
