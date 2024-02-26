package com.bynder.lottery;

import static org.mockito.Mockito.when;

import com.bynder.lottery.repository.LotteryRepository;
import com.bynder.lottery.task.LotteryInitializer;
import io.restassured.RestAssured;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIT {

  @LocalServerPort public Integer port;

  private static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER;

  @Autowired public Clock clock;

  @BeforeEach
  void beforeEach() {

    RestAssured.baseURI = "http://localhost:" + port;
  }

  static {
    POSTGRES_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:15-alpine");

    POSTGRES_SQL_CONTAINER.start();
  }

  @Bean
  public LotteryInitializer lotteryInitializer(LotteryRepository lotteryRepository, Clock clock) {
    Instant todayMidDayInstant = Instant.parse("2024-01-24T10:00:00Z");
    when(clock.instant()).thenReturn(todayMidDayInstant);
    when(clock.getZone()).thenReturn(ZoneOffset.UTC);

    return new LotteryInitializer(lotteryRepository, clock);
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES_SQL_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES_SQL_CONTAINER::getUsername);
    registry.add("spring.datasource.password", POSTGRES_SQL_CONTAINER::getPassword);
  }
}
