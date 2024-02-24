package com.bynder.lottery.repository;

import com.bynder.lottery.BaseIT;
import com.bynder.lottery.domain.Lottery;
import com.bynder.lottery.util.LotteryArbitraryProvider;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

class LotteryRepositoryIT extends BaseIT {

  @Autowired LotteryRepository lotteryRepository;

  @Test
  void IdsAssignedByDb() {

    List<Lottery> lotteries =
        LotteryArbitraryProvider.arbitraryLottery().list().ofMaxSize(10).sample();

    lotteries.forEach(
        participant -> {
          Assertions.assertThat(participant.getId()).isNull();
        });

    List<Lottery> saved =
        lotteries.stream()
            .map(
                participant -> {
                  return lotteryRepository.save(participant);
                })
            .toList();

    saved.forEach(
        participant -> {
          Assertions.assertThat(participant.getId()).isNotNull();
        });
  }

  @Test
  void canFetchLastOpenedLottery() {
    Instant closedStartTime = Instant.parse("2024-01-23T00:00:00Z");
    Instant closedEndTime = closedStartTime.plus(1, ChronoUnit.DAYS);

    Lottery alreadyClosed =
        Lottery.builder().startTime(closedStartTime).endTime(closedEndTime).finished(true).build();

    Instant currentStartTime = Instant.parse("2024-01-24T00:00:00Z");
    Instant currentEndTime = currentStartTime.plus(1, ChronoUnit.DAYS);

    Lottery currentLottery =
        Lottery.builder()
            .startTime(currentStartTime)
            .endTime(currentEndTime)
            .finished(false)
            .build();

      Mockito.when(clock.instant()).thenReturn(currentStartTime.plus(5,ChronoUnit.HOURS));



    lotteryRepository.save(alreadyClosed);
    Lottery savedCurrent = lotteryRepository.save(currentLottery);

    Optional<Lottery> retrievedCurrent = lotteryRepository.getCurrentLottery();

    Assertions.assertThat(retrievedCurrent).isPresent();
    Assertions.assertThat(retrievedCurrent.get()).usingRecursiveComparison().isEqualTo(savedCurrent);
  }
}
