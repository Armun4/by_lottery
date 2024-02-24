package com.bynder.lottery.util;

import com.bynder.lottery.domain.Participant;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

public class ParticipantArbitraryProvider {

  public static Arbitrary<Participant> arbitraryParticipants() {
    Arbitrary<String> names = Arbitraries.strings();

    return names.map(
        name ->
            Participant.builder()
                .id(null)
                .name(name) // Use provided lotteryId
                .build());
  }
}
