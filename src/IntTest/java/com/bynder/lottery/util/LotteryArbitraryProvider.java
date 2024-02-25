package com.bynder.lottery.util;

import static net.jqwik.time.api.Dates.dates;

import com.bynder.lottery.domain.Lottery;
import java.time.LocalDate;
import net.jqwik.api.Arbitrary;

public class LotteryArbitraryProvider {

  public static Arbitrary<Lottery> arbitraryLottery() {
    Arbitrary<LocalDate> dates = dates();

    return dates.map(date -> Lottery.builder().id(null).date(date).build());
  }
}
