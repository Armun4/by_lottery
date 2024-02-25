package com.bynder.lottery.repository;

import com.bynder.lottery.BaseIT;
import com.bynder.lottery.domain.WinnerBallot;
import com.bynder.lottery.util.BallotArbitrarityProvider;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WinnerBallotRepositoryIT extends BaseIT {

  @Autowired WinnerBallotRepository jpaRepository;

  @Test
  void canSaveAndRetrieve() {
    List<WinnerBallot> winnerBallots =
        BallotArbitrarityProvider.arbitraryWinnerBallots()
            .list()
            .ofMinSize(2)
            .ofMaxSize(10)
            .sample();

    winnerBallots.forEach(
        winnerBallot -> {
          jpaRepository.saveWinner(winnerBallot);
        });

    winnerBallots.forEach(
        winnerBallot -> {
          WinnerBallot result =
              jpaRepository.getWinnerBallotOfDate(winnerBallot.getWinningDate()).get();

          Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(winnerBallot);
        });
  }
}
