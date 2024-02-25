package com.bynder.lottery.repository;

import com.bynder.lottery.domain.Lottery;
import com.bynder.lottery.repository.entity.LotteryEntity;
import com.bynder.lottery.repository.jpa.LotteryJpaRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LotteryRepository {

  private final LotteryJpaRepository lotteryJpaRepository;
  private final Clock clock;

  public Lottery save(Lottery lottery) {
    return lotteryJpaRepository.save(lottery.toEntity()).toDomain();
  }

  public Optional<Lottery> getCurrentLottery() {
    // Task runs at 00:00, minus 5 minutes to make sure it picks up the current lottery even tho is
    // already the next day
    LocalDate today =
        LocalDate.ofInstant(clock.instant().minus(10, ChronoUnit.MINUTES), clock.getZone());

    return lotteryJpaRepository.findCurrentLottery(today).map(LotteryEntity::toDomain);
  }
}
