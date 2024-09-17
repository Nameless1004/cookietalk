package com.sparta.cookietalk;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableJpaAuditing
@SpringBootTest
class CookietalkApplicationTests {

    @Test
    void contextLoads() {
    }

}
