package com.bynder.lottery.util;

import com.bynder.lottery.domain.Ballot;
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
}
