package com.bynder.lottery.domain;

import com.bynder.lottery.controller.response.BallotResponse;
import com.bynder.lottery.repository.entity.BallotEntity;
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

  public BallotResponse toResponse() {
    return BallotResponse.builder()
        .id(id)
        .participantId(participantId)
        .lotteryId(lotteryId)
        .build();
  }
}
