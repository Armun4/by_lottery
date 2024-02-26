package com.bynder.lottery.controller.Response;

import lombok.*;

@Builder
@Setter
@Getter
public class BallotResponse {

  private Long id;

  private long participantId;

  private long lotteryId;
}
