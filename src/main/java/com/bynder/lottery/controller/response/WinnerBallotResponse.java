package com.bynder.lottery.controller.response;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class WinnerBallotResponse {

  private long ballotId;

  private long lotteryId;

  private long participantId;

  private String participantName;

  private LocalDate winningDate;
}
