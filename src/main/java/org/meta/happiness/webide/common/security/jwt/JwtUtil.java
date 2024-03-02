package org.meta.happiness.webide.common.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.meta.happiness.webide.common.security.UserDetailsImpl;
import org.meta.happiness.webide.common.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil implements InitializingBean {
    private final long ACCESS_TOKEN_VALID_PERIOD = 1000L * 60 * 30;
    private final String secret;
    private final UserDetailsServiceImpl userDetailsService;
    private final long REFRESH_TOKEN_VALID_PERIOD = 1000L * 60 * 60;
    private Key key;

    //생성자가 하나만 있기때문에 스프링이 자동으로 의존성 주입해주겠죠?
    public JwtUtil(@Value("${jwt.secret}") String secret, UserDetailsServiceImpl detailsService) {
        this.secret = secret;
        this.userDetailsService = detailsService;
    }

    //빈이 생성되고 의존성 주입을 받은 후에 secret값을 Base64 Decode해서 Key 변수에 할당하기 위해서
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    //JW Token 생성하기
    public String[] generateTokens(String email) {
        Date nowAccess = new Date();
        Date accessTokenExpireIn = new Date(nowAccess.getTime() + ACCESS_TOKEN_VALID_PERIOD);
        String accessToken = Jwts.builder()
                .setClaims(Jwts.claims().setSubject(email))
                .setIssuedAt(nowAccess)
                .setExpiration(accessTokenExpireIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        Date nowRefresh = new Date();
        Date refreshTokenExpireIn = new Date(nowRefresh.getTime() + REFRESH_TOKEN_VALID_PERIOD);
        String refreshToken = Jwts.builder()
                //User Email 저장
                .setClaims(Jwts.claims().setSubject(email))
                //발행시간 등록
                .setIssuedAt(nowRefresh)
                //만료시간 등록
                .setExpiration(refreshTokenExpireIn)
                //사용할 암호화 알고리즘과 signature에 들어갈 secret값 setting
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        return new String[]{accessToken, refreshToken};
    }

    public String generateToken(String email) {
        Date now = new Date();
        Date accessTokenExpireIn = new Date(now.getTime() + ACCESS_TOKEN_VALID_PERIOD);
        String accessToken = Jwts.builder()
                .setClaims(Jwts.claims().setSubject(email))
                .setIssuedAt(now)
                .setExpiration(accessTokenExpireIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        // refresh 관련된 객체도 생성이 되어야
        return accessToken;
    }

    //발급 받은 JW Token 만료시 재발급 받기
    public String generateRefreshToken(String email) {
        Date now = new Date();
        Date accessTokenExpireIn = new Date(now.getTime() + REFRESH_TOKEN_VALID_PERIOD);
        String refreshToken = Jwts.builder()
                //User Email 저장
                .setClaims(Jwts.claims().setSubject(email))
                //발행시간 등록
                .setIssuedAt(now)
                //만료시간 등록
                .setExpiration(accessTokenExpireIn)
                //사용할 암호화 알고리즘과 signature에 들어갈 secret값 setting
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        return refreshToken;
    }

    //jwt에서 인증정보 조회
    public Authentication getAuthentication(String token) {
        String email = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(email); //리팩토링해야하는 부분
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //토큰으로 부터 email
    //가지고 오기
    public String getEmailFromToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    //header에서 token 얻기
    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("X-AUTH-TOKEN");
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {//리팩토링 해야할 부분
            log.info("token expired {}", e.toString());
        }
        return false;
    }


}
