package com.bynder.lottery.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.bynder.lottery.BaseIT;
import com.bynder.lottery.domain.WinnerBallot;
import com.bynder.lottery.util.BallotArbitrarityProvider;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WinnerBallotRepositoryIT extends BaseIT {

  @Autowired WinnerBallotRepository jpaRepository;

  @Test
  void canSaveAndRetrieve() {
    List<WinnerBallot> winnerBallots =
        BallotArbitrarityProvider.arbitraryWinnerBallots()
            .list()
                .ofSize(10)
            .sample();

    winnerBallots.forEach(
        winnerBallot -> {
          jpaRepository.saveWinner(winnerBallot);
        });

    winnerBallots.forEach(
        winnerBallot -> {
          WinnerBallot result =
              jpaRepository.getWinnerBallotOfDate(winnerBallot.getWinningDate()).get();

          assertThat(result).usingRecursiveComparison().isEqualTo(winnerBallot);
        });
  }
}
