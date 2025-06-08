package com.example.bankapp.config;

import com.example.bankapp.entity.User;
import com.example.bankapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    public CommandLineRunner createAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("root").isEmpty()) {
                User admin = new User();
                admin.setUsername("root");
                admin.setPassword(passwordEncoder.encode("1234")); // 🔐 비밀번호 암호화
                admin.setRole("ADMIN");
                admin.setName("관리자");
                userRepository.save(admin);

                System.out.println("✅ 관리자 계정 'root' 가 생성되었습니다 (비밀번호: 1234)");
            } else {
                System.out.println("ℹ️ 관리자 계정 'root' 이미 존재함");
            }
        };
    }
}
