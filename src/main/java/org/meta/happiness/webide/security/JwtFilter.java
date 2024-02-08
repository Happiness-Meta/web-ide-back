package org.meta.happiness.webide.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;


    /* 인증(JWT) -> 인가
    * */


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //requestHeader에서 token을 parsing함
        String token = parseJwt(request);
        //토큰 유효시간이 만료되었을 때, 어떻게 처리할 것인가??
        if (token != null && !jwtUtil.isTokenValid(token)) {
            String refreshed = jwtUtil.generateRefreshToken(jwtUtil.getEmailFromToken(token));
            log.info("refreshed {}", refreshed);
        }
        /*토큰이 유효한지, 즉 만료시간 전인지 체크 후 유효하다면 (인증된)token을
         SecurityContextHolder에 등록함으로 써 인가하기*/
        if (token != null && jwtUtil.isTokenValid(token)) {
            Authentication auth = jwtUtil.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
            return headerAuth.substring(7);
        }
        return null;
    }


}
