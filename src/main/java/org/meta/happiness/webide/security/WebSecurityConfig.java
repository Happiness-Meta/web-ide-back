package org.meta.happiness.webide.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtFilter jwtFilter;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    @Bean //PasswordEncoder는 BCryptPasswordEncoder를 사용
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Security 필터 설정
    @Bean  //User Role Admin //SecurityFilterChain에 Bean으로 등록하는 과정
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                //csrf  //jw토큰을 사용하는 방식이기 때문에 csrf disable함.
                .csrf(AbstractHttpConfigurer::disable)
                //session을 사용하지 않으므로 disable 시킴
                .sessionManagement(
                        (sessionManage)->sessionManage.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정하겠다.
                .authorizeHttpRequests((authorize) ->
                        authorize
                                // 정적 자원에 대한 접근 허용
                                .requestMatchers("/ws/**", "/sub/**", "/pub/**").permitAll()
                                .requestMatchers("/","/css/**", "/js/**").permitAll()
                                .requestMatchers("/swagger-ui/**", "/api-docs/**").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/posts","/api/post/*", "/api/users","/api/replies", "/api/replies/*","/api/reply/*").permitAll()
                                .requestMatchers("/api/sign/**").permitAll()
//                                .anyRequest().authenticated()
                                .anyRequest().permitAll()
                )
                .exceptionHandling((handle)->
                        handle.authenticationEntryPoint(jwtAuthEntryPoint))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();

    }
}
