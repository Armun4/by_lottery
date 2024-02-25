package com.bynder.lottery.service;

import com.bynder.lottery.domain.Lottery;
import com.bynder.lottery.repository.LotteryRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LotteryService {

  private final LotteryRepository lotteryRepository;

  public void closeCurrentAndCreateNext(Lottery current) {

    current.setFinished(true); // Set to true to close the current lottery

    lotteryRepository.save(current);

    LocalDate nextDay = current.getDate().plusDays(1);

    Lottery nextLottery = Lottery.builder().date(nextDay).build();

    lotteryRepository.save(nextLottery);
  }

  public Lottery getCurrent() {
    return lotteryRepository.getCurrentLottery().get();
  }
}
