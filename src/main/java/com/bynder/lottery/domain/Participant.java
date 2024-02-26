package com.bynder.lottery.domain;

import com.bynder.lottery.controller.Response.ParticipantResponse;
import com.bynder.lottery.repository.entity.ParticipantEntity;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Participant {

  // actual id will be set by the db
  @Builder.Default private Long id = null;

  private String name;
  private String email;

  public ParticipantEntity toEntity() {
    return ParticipantEntity.builder().id(id).name(name).email(email).build();
  }

  public ParticipantResponse toResponse() {

    return ParticipantResponse.builder().email(email).name(name).build();
  }
}
