package com.sparta.cookietalk.common.config;

import com.sparta.cookietalk.common.enums.UserRole;
import com.sparta.cookietalk.security.filter.JwtSecurityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@EnableMethodSecurity(securedEnabled  = true) // @Secured 사용가능하게
public class WebSecurityConfig {

    private final JwtSecurityFilter jwtSecurityFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
           http.csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // SessionManagementFilter, SecurityContextPersistenceFilter
            )
            .addFilterBefore(jwtSecurityFilter, SecurityContextHolderAwareRequestFilter.class)
            .formLogin(AbstractHttpConfigurer::disable) // UsernamePasswordAuthenticationFilter, DefaultLoginPageGeneratingFilter 비활성화
            .anonymous(AbstractHttpConfigurer::disable) // AnonymousAuthenticationFilter 비활성화
            .httpBasic(AbstractHttpConfigurer::disable) // BasicAuthenticationFilter 비활성화
            .logout(AbstractHttpConfigurer::disable) // LogoutFilter 비활성화
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/signup", "/auth/signin", "/auth/reissue").permitAll()
                .requestMatchers("/auth/signout").authenticated()
                .anyRequest().authenticated()
            );

        http.cors(c -> {
            c.configurationSource(corsConfigurationSource);});

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
