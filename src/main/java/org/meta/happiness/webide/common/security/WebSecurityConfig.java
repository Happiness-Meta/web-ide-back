package org.meta.happiness.webide.common.security;

import lombok.RequiredArgsConstructor;
import org.meta.happiness.webide.common.security.jwt.JwtAuthEntryPoint;
import org.meta.happiness.webide.common.security.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtFilter jwtFilter;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    //Security 필터 설정
    @Bean  //User Role Admin //SecurityFilterChain에 Bean으로 등록하는 과정
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                //csrf  //jw토큰을 사용하는 방식이기 때문에 csrf disable함.
                .csrf(AbstractHttpConfigurer::disable)
                //cors
                .cors(corsCustmizer -> corsCustmizer.configurationSource(configurationSource()))
                //session을 사용하지 않으므로 disable 시킴
                .sessionManagement(
                        (sessionManage) -> sessionManage.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정하겠다.
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/api/run").permitAll()
                                .requestMatchers("/ws/**", "/sub/**", "/pub/**").permitAll()
                                .requestMatchers("/swagger-ui/**", "/api-docs/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/posts", "/api/post/*", "/api/users", "/api/replies", "/api/replies/*", "/api/reply/*").permitAll()
                                .requestMatchers("/api/sign/**").permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling((handle) ->
                        handle.authenticationEntryPoint(jwtAuthEntryPoint))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();

    }

//PasswordEncoder는 BCryptPasswordEncoder를 사용
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    CORS 설정
    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://processlogic.link/", "http://localhost:5173")); // 허용할 Origin
        configuration.setAllowedMethods(Collections.singletonList("*")); // 허용할 HTTP Methods

        configuration.setAllowCredentials(true);

        configuration.setAllowedHeaders(Collections.singletonList("*")); // 허용할 HTTP Headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
