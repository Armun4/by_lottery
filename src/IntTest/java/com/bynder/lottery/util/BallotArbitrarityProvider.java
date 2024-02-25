package com.bynder.lottery.util;

import static net.jqwik.time.api.Dates.dates;

import com.bynder.lottery.domain.Ballot;
import com.bynder.lottery.domain.WinnerBallot;
import java.time.LocalDate;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;

public class BallotArbitrarityProvider {

  public static Arbitrary<Ballot> arbitraryBallots() {
    Arbitrary<Long> participantIds = Arbitraries.longs();
    Arbitrary<Long> lotteryIds = Arbitraries.longs();

    return Combinators.combine(participantIds, lotteryIds)
        .as(
            (participantId, lotteryId) ->
                Ballot.builder()
                    .id(null)
                    .participantId(participantId)
                    .lotteryId(lotteryId)
                    .build());
  }

  public static Arbitrary<Ballot> arbitraryBallotsForLottery(long lotteryId) {
    Arbitrary<Long> participantIds = Arbitraries.longs();

    return participantIds.map(
        participantId ->
            Ballot.builder()
                .id(null)
                .participantId(participantId)
                .lotteryId(lotteryId) // Use provided lotteryId
                .build());
  }

  public static Arbitrary<WinnerBallot> arbitraryWinnerBallots() {
    Arbitrary<Long> participantIds = Arbitraries.longs().between(1, 1000);
    Arbitrary<Long> lotteryIds = Arbitraries.longs().between(1, 1000);
    Arbitrary<Long> ballotIds = Arbitraries.longs().between(1, 1000);
    Arbitrary<String> names = Arbitraries.strings();
    Arbitrary<LocalDate> dates = dates().atTheEarliest(LocalDate.of(2010, 1, 1));

    return Combinators.combine(participantIds, lotteryIds, ballotIds, dates, names)
        .as(
            (participantId, lotteryId, ballotId, date, name) ->
                WinnerBallot.builder()
                    .ballotId(ballotId)
                    .participantId(participantId)
                    .lotteryId(lotteryId)
                    .winningDate(date)
                    .participantName(name)
                    .build());
  }
}
