package com.hfu.project_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectServerApplication.class, args);
    }


    /*
    // 手動註冊一個admin
    @Bean
    CommandLineRunner runner(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            adminRepository.save(
                    new Admin(null,
                            "admin",
                            passwordEncoder.encode("admin")
                    )
            );
        };
    }

     */
}
