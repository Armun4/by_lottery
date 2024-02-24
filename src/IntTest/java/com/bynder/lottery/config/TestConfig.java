package com.bynder.lottery.config;

import java.time.Clock;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

  @Bean
  Clock getClock() {
    return Mockito.mock(Clock.class);
  }
}
