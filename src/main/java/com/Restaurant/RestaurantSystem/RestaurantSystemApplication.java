package com.Restaurant.RestaurantSystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.Restaurant.RestaurantSystem.entity.User;
import com.Restaurant.RestaurantSystem.repository.UserRepository;

@Controller
@SpringBootApplication
public class RestaurantSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantSystemApplication.class, args);
	}

	@GetMapping("/")
	public String hello() {
		return "hello";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/admin/index")
	public String adminIndex() {
		return "admin/index";
	}

	// アプリケーション起動時に初期データを投入するためのCommandLineRunner Bean。ここでは、管理者ユーザーを作成する。
	@Bean
	public CommandLineRunner dataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			// 初期管理者ユーザーを作成（存在しない場合）
			if (userRepository.findByUsername("admin").isEmpty()) {
				User admin = new User();
				admin.setUsername("admin");// ユーザー名を設定
				admin.setPassword(passwordEncoder.encode("adpass"));// パスワードをBCryptでハッシュ化して設定
				admin.setRole("ROLE_ADMIN");// 役割を設定
				userRepository.save(admin);// ユーザーをデータベースに保存
				}
		};
	}

}
