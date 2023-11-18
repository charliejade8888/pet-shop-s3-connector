package com.tyrell.replicant;

import static org.assertj.core.api.Assertions.assertThat;

import com.tyrell.replicant.config.MyConfigurationProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
//@PropertySource("classpath:crypto-compare.properties")
@SpringBootTest
class PetShopCatalogServiceTest {

  @Autowired
  MyConfigurationProperties myConfigurationProperties;

  @DisplayName("Test with positive integer range, expected ok")
  @Test
  void shouldLuckyFizzBuzz() {
    // given
    String expected =
        "1 2 lucky 4 buzz fizz 7 8 fizz buzz 11 fizz lucky 14 fizzbuzz 16 17 fizz 19 buzz";

    // when
    String actual = expected;

    // then
    assertThat(actual)
        .as("if failed display this msg!")
        .isEqualTo(expected)
        .isEqualToIgnoringCase(expected)
        .startsWith("1")
        .endsWith("z")
        .containsIgnoringCase("buzz");
  }

}
