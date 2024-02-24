package com.bynder.lottery.util;

import static net.jqwik.time.api.DateTimes.instants;

import com.bynder.lottery.domain.Lottery;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import net.jqwik.api.Arbitrary;

public class LotteryArbitraryProvider {

  public static Arbitrary<Lottery> arbitraryLottery() {
    Arbitrary<Instant> instants = instants();

    return instants.map(
        instant ->
            Lottery.builder()
                .id(null)
                .endTime(instant.plus(1, ChronoUnit.DAYS))
                .startTime(instant)
                .build());
  }
}
