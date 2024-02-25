package com.bynder.lottery.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.bynder.lottery.BaseIT;
import com.bynder.lottery.domain.Lottery;
import com.bynder.lottery.repository.jpa.LotteryJpaRepository;
import com.bynder.lottery.util.LotteryArbitraryProvider;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

class LotteryRepositoryIT extends BaseIT {

  @Autowired LotteryRepository lotteryRepository;

  @Autowired LotteryJpaRepository jpaRepository;

  @Test
  void IdsAssignedByDb() {

    List<Lottery> lotteries =
        LotteryArbitraryProvider.arbitraryLottery().list().ofMaxSize(10).sample();

    lotteries.forEach(
        participant -> {
          assertThat(participant.getId()).isNull();
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
          assertThat(participant.getId()).isNotNull();
        });
  }

  @Test
  void canFetchLastOpenedLottery() {
    LocalDate closedStartTime =
        LocalDate.ofInstant(Instant.parse("2024-01-23T00:00:00Z"), ZoneOffset.UTC);
    Lottery alreadyClosed = Lottery.builder().date(closedStartTime).finished(true).build();

    LocalDate currentStartTime =
        LocalDate.ofInstant(Instant.parse("2024-01-24T00:00:00Z"), ZoneOffset.UTC);

    Lottery currentLottery = Lottery.builder().date(currentStartTime).finished(false).build();

    Mockito.when(clock.getZone()).thenReturn(ZoneOffset.UTC);
    Mockito.when(clock.instant())
        .thenReturn(Instant.parse("2024-01-24T00:00:00Z").plus(5, ChronoUnit.HOURS));

    lotteryRepository.save(alreadyClosed);
    Lottery savedCurrent = lotteryRepository.save(currentLottery);

    Optional<Lottery> retrievedCurrent = lotteryRepository.getCurrentLottery();

    assertThat(retrievedCurrent).isPresent();
    assertThat(retrievedCurrent.get()).usingRecursiveComparison().isEqualTo(savedCurrent);
  }
}
