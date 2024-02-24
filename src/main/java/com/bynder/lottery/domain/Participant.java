package com.bynder.lottery.domain;

import com.bynder.lottery.entity.ParticipantEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Participant {

  // actual id will be set by the db
  @Builder.Default private Long id = null;

  private String name;

  public ParticipantEntity toEntity() {
    return ParticipantEntity.builder().id(id).name(name).build();
  }
}
