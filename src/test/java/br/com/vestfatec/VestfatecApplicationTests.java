package br.com.vestfatec;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class VestfatecApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void deveDemonstrarUmaFalha() {
        org.junit.jupiter.api.Assertions.fail(
                "Falha intencional para demonstrar o relatório no Jenkins");
    }
}


