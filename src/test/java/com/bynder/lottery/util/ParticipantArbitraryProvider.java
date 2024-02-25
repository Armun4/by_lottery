package com.bynder.lottery.util;

import com.bynder.lottery.domain.Participant;
import java.util.List;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;

public class ParticipantArbitraryProvider {

  public static Arbitrary<Participant> arbitraryParticipants() {
    Arbitrary<String> names = Arbitraries.strings().alpha().ofMaxLength(10);
    Arbitrary<Long> ids = Arbitraries.longs().between(1, 10000);

    Arbitrary<String> emails =
        Arbitraries.of(List.of("hola@gmail.com", "bynder@gmial.com", "lottery@gmail.com"));

    return Combinators.combine(names, emails, ids)
        .as((name, email, id) -> Participant.builder().id(id).email(email).name(name).build());
  }
}
