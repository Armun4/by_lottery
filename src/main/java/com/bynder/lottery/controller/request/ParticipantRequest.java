package com.bynder.lottery.controller.request;

import com.bynder.lottery.domain.Participant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ParticipantRequest {

  private String name;

  private String email;

  public Participant toDomain() {
    return Participant.builder().name(name).email(email).build();
  }
}
