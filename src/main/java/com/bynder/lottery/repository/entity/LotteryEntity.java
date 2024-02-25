package com.bynder.lottery.repository.entity;

import com.bynder.lottery.domain.Lottery;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lotteries")
public class LotteryEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Instant startTime;

  private Instant endTime;

  private boolean finished = false;

  public Lottery toDomain() {
    return Lottery.builder()
        .endTime(endTime)
        .startTime(startTime)
        .id(id)
        .finished(finished)
        .build();
  }
}
