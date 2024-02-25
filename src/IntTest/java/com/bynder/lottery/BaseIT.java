package com.bynder.lottery;

import java.time.Clock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
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

  static {
    POSTGRES_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:15-alpine");

    POSTGRES_SQL_CONTAINER.start();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES_SQL_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES_SQL_CONTAINER::getUsername);
    registry.add("spring.datasource.password", POSTGRES_SQL_CONTAINER::getPassword);
  }
}
