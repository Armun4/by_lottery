package com.bynder.lottery.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bynder.lottery.domain.WinnerBallot;
import com.bynder.lottery.repository.entity.WinnerBallotEntity;
import com.bynder.lottery.repository.jpa.WinnerBallotJpaRepository;
import com.bynder.lottery.util.BallotArbitrarityProvider;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WinnerBallotRepositoryTest {

  ArgumentCaptor<WinnerBallotEntity> argumentCaptor =
      ArgumentCaptor.forClass(WinnerBallotEntity.class);

  @Mock WinnerBallotJpaRepository jpaRepository;

  @InjectMocks WinnerBallotRepository winnerBallotRepository;

  @Test
  void canSaveWinner() {
    WinnerBallot winnerBallot = BallotArbitrarityProvider.arbitraryWinnerBallots().sample();
    WinnerBallotEntity winnerBallotEntity = winnerBallot.toEntity();

    when(jpaRepository.save(any())).thenReturn(winnerBallotEntity);

    winnerBallotRepository.saveWinner(winnerBallot);

    verify(jpaRepository).save(argumentCaptor.capture());

    WinnerBallotEntity capturedArguments = argumentCaptor.getValue();

    assertThat(capturedArguments).usingRecursiveComparison().isEqualTo(winnerBallotEntity);
  }

  @Test
  void cangetWinnerBallotOfDate() {

    LocalDate today = LocalDate.of(2024, 2, 28);
    WinnerBallot winnerBallot = BallotArbitrarityProvider.arbitraryWinnerBallots().sample();
    WinnerBallotEntity winnerBallotEntity = winnerBallot.toEntity();

    when(jpaRepository.findByWinningDate(today))
        .thenReturn(Optional.ofNullable(winnerBallotEntity));

    winnerBallotRepository.getWinnerBallotOfDate(today);

    verify(jpaRepository).findByWinningDate(today);
  }
}
