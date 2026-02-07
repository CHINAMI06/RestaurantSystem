package com.Restaurant.RestaurantSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/css/**", "/js/**", "/images/**").permitAll() // トップページと静的ファイルは全員許可
                .anyRequest().authenticated() // それ以外はログインが必要
            )
            .formLogin((form) -> form
                .loginPage("/login") // 独自のログインページ（後で作成）を指定
                .permitAll()
            )
            .logout((logout) -> logout.permitAll());

        return http.build();
    }
}
