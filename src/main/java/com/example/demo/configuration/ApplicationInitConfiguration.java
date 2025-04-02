package com.example.demo.configuration;

import com.example.demo.Repository.Entity.AccountEntity;
import com.example.demo.Repository.IRepository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Configuration
@Slf4j
@Component
public class ApplicationInitConfiguration {
    private final PasswordEncoder passwordEncoder;

    private final AccountRepository accountRepository;

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner() {
        return args -> {
            if(accountRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {
                AccountEntity accountEntity = AccountEntity.builder()
                        .username(ADMIN_USER_NAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .role("ADMIN")
                        .build();

                accountRepository.save(accountEntity);
                log.warn("admin user has been created with default password: admin, please change it");
            }
            log.info("Application initialization completed .....");
        };
    }
}
