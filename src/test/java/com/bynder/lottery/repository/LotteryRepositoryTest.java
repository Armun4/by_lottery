package com.bynder.lottery.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bynder.lottery.domain.Lottery;
import com.bynder.lottery.repository.entity.LotteryEntity;
import com.bynder.lottery.repository.jpa.LotteryJpaRepository;
import com.bynder.lottery.util.LotteryArbitraryProvider;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LotteryRepositoryTest {

  ArgumentCaptor<LotteryEntity> argumentCaptor = ArgumentCaptor.forClass(LotteryEntity.class);

  @Mock LotteryJpaRepository jpaRepository;

  @InjectMocks LotteryRepository lotteryRepository;

  @Test
  void save() {
    Lottery lottery = LotteryArbitraryProvider.arbitraryLottery().sample();
    LotteryEntity lotteryEntity = lottery.toEntity();

    when(jpaRepository.save(any())).thenReturn(lotteryEntity);

    lotteryRepository.save(lottery);

    verify(jpaRepository).save(argumentCaptor.capture());

    LotteryEntity capturedArguments = argumentCaptor.getValue();

    assertThat(capturedArguments).usingRecursiveComparison().isEqualTo(lotteryEntity);
  }

  @Test
  void getCurrentLottery() {
    LocalDate today = LocalDate.of(2024, 2, 28);
    Lottery lottery = LotteryArbitraryProvider.arbitraryLottery().sample();
    LotteryEntity lotteryEntity = lottery.toEntity();
    when(jpaRepository.findCurrentLottery(today)).thenReturn(Optional.of(lotteryEntity));

    Optional<Lottery> currentLottery = lotteryRepository.getCurrentLottery(today);

    verify(jpaRepository).findCurrentLottery(today);
  }
}
