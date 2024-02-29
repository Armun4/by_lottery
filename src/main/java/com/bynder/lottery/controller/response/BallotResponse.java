package com.bynder.lottery.controller.response;

import lombok.*;

@Builder
@Setter
@Getter
public class BallotResponse {

  private Long id;

  private long participantId;

  private long lotteryId;
}
