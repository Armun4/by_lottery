package com.bynder.lottery.task;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

import com.bynder.lottery.domain.Lottery;
import com.bynder.lottery.repository.LotteryRepository;
import com.bynder.lottery.util.LotteryArbitraryProvider;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LotteryInitializerTest {

  ArgumentCaptor<Lottery> lotteryCaptor = ArgumentCaptor.forClass(Lottery.class);

  @Mock private LotteryRepository lotteryRepository;

  @Mock private Clock clock;

  @InjectMocks private LotteryInitializer lotteryInitializer;

  @BeforeEach
  void setUp() {
    when(clock.instant()).thenReturn(Instant.now());
    when(clock.getZone()).thenReturn(ZoneId.systemDefault());
  }

  @Test
  void createLotteryIfNeeded_lotteryExists() {
    LocalDate today = LocalDate.now();
    Lottery lottery = LotteryArbitraryProvider.arbitraryLottery().sample();
    when(lotteryRepository.getCurrentLottery(today)).thenReturn(Optional.of(lottery));

    lotteryInitializer.createLotteryIfNeeded();

    verify(lotteryRepository).getCurrentLottery(today);
    verify(lotteryRepository, times(0)).save(any());
  }

  @Test
  void createLotteryIfNeeded_lotteryDoesNotExist() {

    LocalDate today = LocalDate.now();
    Lottery lottery = Lottery.builder().date(today).build();

    when(lotteryRepository.getCurrentLottery(today)).thenReturn(Optional.empty());
    when(lotteryRepository.save(any())).thenReturn(lottery);

    lotteryInitializer.createLotteryIfNeeded();

    verify(lotteryRepository).getCurrentLottery(today);
    verify(lotteryRepository).save(lotteryCaptor.capture());

    Lottery capturedArguments = lotteryCaptor.getValue();

    assertThat(capturedArguments).usingRecursiveComparison().isEqualTo(lottery);
  }
}
