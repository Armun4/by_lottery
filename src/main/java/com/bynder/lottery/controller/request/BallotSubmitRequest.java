package com.bynder.lottery.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BallotSubmitRequest {
  private Long participantId;

  private Integer amount;
}
