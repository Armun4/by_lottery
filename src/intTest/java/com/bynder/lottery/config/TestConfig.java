package com.bynder.lottery.config;

import static org.mockito.Mockito.when;

import com.bynder.lottery.repository.LotteryRepository;
import com.bynder.lottery.task.LotteryInitializer;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

  public static final Instant todayMidDayInstant = Instant.parse("2024-01-24T10:00:00Z");

  @Bean
  LotteryInitializer lotteryInitializer(
      @Autowired LotteryRepository lotteryRepository, @Autowired Clock clock) {

    when(clock.instant()).thenReturn(todayMidDayInstant);
    when(clock.getZone()).thenReturn(ZoneOffset.UTC);

    return new LotteryInitializer(lotteryRepository, clock);
  }
}
