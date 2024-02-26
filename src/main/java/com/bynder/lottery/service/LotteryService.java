package com.bynder.lottery.service;

import com.bynder.lottery.domain.Lottery;
import com.bynder.lottery.repository.LotteryRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LotteryService {

  private final LotteryRepository lotteryRepository;
  private final Clock clock;

  public void closeCurrentAndCreateNext(Lottery current) {

    current.setFinished(true); // Set to true to close the current lottery

    lotteryRepository.save(current);

    LocalDate nextDay = current.getDate().plusDays(1);

    Lottery nextLottery = Lottery.builder().date(nextDay).build();

    lotteryRepository.save(nextLottery);
  }

  public Lottery getLotteryAtMidnight() {

    // Task runs at 00:00, minus 5 minutes to make sure it picks up the current lottery even tho is
    // already the next day
    LocalDate today =
        LocalDate.ofInstant(clock.instant().minus(10, ChronoUnit.MINUTES), clock.getZone());

    return lotteryRepository.getCurrentLottery(today).get();
  }
}
