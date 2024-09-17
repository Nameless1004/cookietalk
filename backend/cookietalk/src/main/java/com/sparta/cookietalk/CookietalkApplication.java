package com.sparta.cookietalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableJpaAuditing
@SpringBootApplication
public class CookietalkApplication {

    public static void main(String[] args) {
        SpringApplication.run(CookietalkApplication.class, args);
    }

}
