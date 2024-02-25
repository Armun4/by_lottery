package com.bynder.lottery.repository;

import com.bynder.lottery.domain.WinnerBallot;
import com.bynder.lottery.repository.entity.WinnerBallotEntity;
import com.bynder.lottery.repository.jpa.WinnerBallotJpaRepository;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WinnerBallotRepository {
  private final WinnerBallotJpaRepository jpaRepository;

  public WinnerBallot saveWinner(WinnerBallot winnerBallot) {
    return jpaRepository.save(winnerBallot.toEntity()).toDomain();
  }

  public Optional<WinnerBallot> getWinnerBallotOfDate(LocalDate date) {
    return jpaRepository.findByWinningDate(date).map(WinnerBallotEntity::toDomain);
  }
}
