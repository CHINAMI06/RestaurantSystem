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
                .requestMatchers("/", "/menu", "/reservation", "/reservation/**","/css/**", "/js/**", "/images/**").permitAll() // トップページ、静的ファイル、レストランメニュー、予約関連は全員許可
                .requestMatchers("/h2-console/**").permitAll() // H2コンソールは全員許可
                .requestMatchers("/admin/**").authenticated() // /admin/以下のURLはログイン必須
                .anyRequest().authenticated() // それ以外はログインが必要
            )
            .formLogin((form) -> form
                .loginPage("/login") // 独自のログインページを指定
                .defaultSuccessUrl("/admin/index", true) // trueにより強制的にログイン成功後のリダイレクト先を指定
                .permitAll()
            )
            .logout((logout) -> logout.permitAll())

        // H2 Consoleを表示するためにCSRF保護とFrameOptionsを調整
        .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")) // H2コンソールへのリクエストのみ、CSRF対策を無効化する
        .headers(headers -> headers.frameOptions(frame -> frame.disable())); //フレーム（iframe）内でのページ表示制限を解除する

        return http.build();
    }
}
