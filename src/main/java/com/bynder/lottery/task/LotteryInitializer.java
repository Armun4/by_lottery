package com.bynder.lottery.task;

import com.bynder.lottery.domain.Lottery;
import com.bynder.lottery.repository.LotteryRepository;
import jakarta.annotation.PostConstruct;
import java.time.Clock;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LotteryInitializer {

  private final LotteryRepository lotteryRepository;
  private final Clock clock;

  @PostConstruct
  private void createLotteryIfNeeded() {
    LocalDate today = LocalDate.ofInstant(clock.instant(), clock.getZone());

    if (lotteryRepository.getCurrentLottery(today).isEmpty()) {
      // Create a new lottery for the current day
      Lottery lottery = Lottery.builder().date(today).build();
      lotteryRepository.save(lottery);
    }
  }
}
