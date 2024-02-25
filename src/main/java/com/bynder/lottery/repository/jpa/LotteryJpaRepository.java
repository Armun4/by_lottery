package com.bynder.lottery.repository.jpa;

import com.bynder.lottery.repository.entity.LotteryEntity;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LotteryJpaRepository extends JpaRepository<LotteryEntity, Long> {
  @Query(
      "SELECT l FROM LotteryEntity l WHERE l.date = :currentDate AND l.finished = false ORDER BY l.date DESC")
  Optional<LotteryEntity> findCurrentLottery(@Param("currentDate") LocalDate currentDate);
}
