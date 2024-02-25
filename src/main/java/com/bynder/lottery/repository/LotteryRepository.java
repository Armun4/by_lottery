package com.bynder.lottery.repository;

import com.bynder.lottery.domain.Lottery;
import com.bynder.lottery.repository.entity.LotteryEntity;
import com.bynder.lottery.repository.jpa.LotteryJpaRepository;
import java.time.Clock;
import java.time.Instant;
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
    Instant now = clock.instant();

    return lotteryJpaRepository.findLastUnfinishedLottery(now).map(LotteryEntity::toDomain);
  }
}
