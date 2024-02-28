package com.bynder.lottery.util;

import static net.jqwik.time.api.Dates.dates;

import com.bynder.lottery.domain.Lottery;
import java.time.LocalDate;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;

public class LotteryArbitraryProvider {

  public static Arbitrary<Lottery> arbitraryLottery() {
    Arbitrary<LocalDate> dates = dates();
    Arbitrary<Long> ids = Arbitraries.longs().between(1, 10000);

    return Combinators.combine(dates, ids)
        .as((date, id) -> Lottery.builder().id(id).date(date).build());
  }


  public static Arbitrary<Lottery> arbitraryLotteryForDate(LocalDate date) {
    Arbitrary<Long> ids = Arbitraries.longs().between(1, 10000);

    return ids.map(id ->
      Lottery.builder().id(id).date(date).build());
  }
}
