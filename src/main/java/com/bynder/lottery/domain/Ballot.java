package com.bynder.lottery.domain;

import com.bynder.lottery.entity.BallotEntity;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Ballot {

  // actual id will be set by the db
  @Builder.Default private Long id = null;

  private long participantId;

  private long lotteryId;

  public BallotEntity toEntity() {
    return BallotEntity.builder().id(id).participantId(participantId).lotteryId(lotteryId).build();
  }
}
