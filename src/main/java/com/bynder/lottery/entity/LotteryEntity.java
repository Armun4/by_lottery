package com.bynder.lottery.entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "participant")
public class LotteryEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private Instant startTime;

  private Instant endTime;
}
