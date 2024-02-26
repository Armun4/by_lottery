package com.bynder.lottery.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.bynder.lottery.BaseIT;
import com.bynder.lottery.domain.WinnerBallot;
import com.bynder.lottery.repository.jpa.WinnerBallotJpaRepository;
import com.bynder.lottery.util.BallotArbitrarityProvider;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WinnerBallotRepositoryIT extends BaseIT {

  @Autowired WinnerBallotRepository repository;

  @Autowired WinnerBallotJpaRepository jpaRepository;

  @BeforeEach
  void cleanDb() {
    jpaRepository.deleteAll();
  }

  @Test
  void canSaveAndRetrieve() {

    WinnerBallot winnerBallot = BallotArbitrarityProvider.arbitraryWinnerBallots().sample();

    repository.saveWinner(winnerBallot);

    Optional<WinnerBallot> result = repository.getWinnerBallotOfDate(winnerBallot.getWinningDate());

    assertThat(result.isPresent()).isTrue();
    assertThat(result.get()).usingRecursiveComparison().isEqualTo(winnerBallot);
  }
}
