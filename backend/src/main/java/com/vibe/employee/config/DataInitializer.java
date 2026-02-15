package com.vibe.employee.config;

import com.vibe.employee.model.Manager;
import com.vibe.employee.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final ManagerRepository managerRepository;
    private final com.vibe.employee.repository.EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (managerRepository.count() == 0) {
                managerRepository.save(Manager.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .name("관리자")
                        .role("ROLE_SUPER_ADMIN")
                        .build());

                managerRepository.save(Manager.builder()
                        .username("user")
                        .password(passwordEncoder.encode("user123"))
                        .name("일반사용자")
                        .role("ROLE_READ_ONLY")
                        .build());
            }

            if (employeeRepository.count() == 0) {
                employeeRepository.save(com.vibe.employee.model.Employee.builder()
                        .name("김철수")
                        .department("개발팀")
                        .position("팀장")
                        .email("chulsoo.kim@vibe.com")
                        .joinedDate(java.time.LocalDate.parse("2023-01-01"))
                        .build());

                employeeRepository.save(com.vibe.employee.model.Employee.builder()
                        .name("이영희")
                        .department("인사팀")
                        .position("대리")
                        .email("younghee.lee@vibe.com")
                        .joinedDate(java.time.LocalDate.parse("2023-05-15"))
                        .build());
            }
        };
    }
}
