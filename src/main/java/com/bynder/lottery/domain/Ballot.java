package com.bynder.lottery.domain;

import com.bynder.lottery.entity.BallotEntity;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Ballot {

  private Long id;

  private Long participantId;

  private Long lotteryId;

  public BallotEntity toEntity() {
    return BallotEntity.builder().id(id).participantId(participantId).lotteryId(lotteryId).build();
  }
}
