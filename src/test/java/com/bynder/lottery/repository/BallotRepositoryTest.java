package com.bynder.lottery.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bynder.lottery.domain.Ballot;
import com.bynder.lottery.repository.entity.BallotEntity;
import com.bynder.lottery.repository.jpa.BallotJpaRepository;
import com.bynder.lottery.util.BallotArbitrarityProvider;
import java.util.List;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BallotRepositoryTest {

  ArgumentCaptor<List<BallotEntity>> ballotArgumentCaptor = ArgumentCaptor.forClass(List.class);

  @Mock BallotJpaRepository jpaRepository;

  @InjectMocks BallotRepository ballotRepository;

  @Test
  void canSaveAll() {
    List<Ballot> ballots = BallotArbitrarityProvider.arbitraryBallots().list().ofSize(10).sample();

    List<BallotEntity> ballotsEntities = ballots.stream().map(Ballot::toEntity).toList();

    when(jpaRepository.saveAll(any())).thenReturn(ballotsEntities);

    ballotRepository.saveAll(ballots);

    verify(jpaRepository).saveAll(ballotArgumentCaptor.capture());

    List<BallotEntity> capturedArguments = ballotArgumentCaptor.getValue();

    assertThat(capturedArguments).usingRecursiveComparison().isEqualTo(ballotsEntities);
  }

  @Test
  void canFindByLottery() {

    long lotteryId = Arbitraries.longs().sample();
    List<Ballot> ballots =
        BallotArbitrarityProvider.arbitraryBallotsForLottery(lotteryId).list().ofSize(10).sample();
    List<BallotEntity> ballotsEntities = ballots.stream().map(Ballot::toEntity).toList();

    when(jpaRepository.findAllByLotteryId(lotteryId)).thenReturn(ballotsEntities);

    ballotRepository.findAllByLotteryId(lotteryId);

    verify(jpaRepository).findAllByLotteryId(lotteryId);
  }
}
