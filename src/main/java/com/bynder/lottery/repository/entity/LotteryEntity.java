package com.bynder.lottery.repository.entity;

import com.bynder.lottery.domain.Lottery;
import jakarta.persistence.*;
import java.time.LocalDate;
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

  private LocalDate date;

  private boolean finished;

  public Lottery toDomain() {
    return Lottery.builder().date(date).id(id).finished(finished).build();
  }
}
