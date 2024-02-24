package com.bynder.lottery.repository.jpa;

import com.bynder.lottery.repository.entity.LotteryEntity;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LotteryJpaRepository extends JpaRepository<LotteryEntity, Long> {
  @Query(
      "SELECT l FROM LotteryEntity l WHERE l.startTime < :currentTime AND l.endTime > :currentTime AND l.finished = false ORDER BY l.startTime DESC")
  Optional<LotteryEntity> findLastUnfinishedLottery(Instant currentTime);
}
