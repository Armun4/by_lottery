package com.bynder.lottery.repository.jpa;

import com.bynder.lottery.repository.entity.WinnerBallotEntity;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WinnerBallotJpaRepository extends JpaRepository<WinnerBallotEntity, Long> {

  Optional<WinnerBallotEntity> findByWinningDate(LocalDate date);
}
