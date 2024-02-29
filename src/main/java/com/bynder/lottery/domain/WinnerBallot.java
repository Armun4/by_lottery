package com.bynder.lottery.domain;

import com.bynder.lottery.controller.response.WinnerBallotResponse;
import com.bynder.lottery.repository.entity.WinnerBallotEntity;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WinnerBallot {

  private long ballotId;

  private long lotteryId;

  private long participantId;

  private String participantName;

  private LocalDate winningDate;

  public WinnerBallotEntity toEntity() {
    return WinnerBallotEntity.builder()
        .ballotId(ballotId)
        .participantId(participantId)
        .lotteryId(lotteryId)
        .participantName(participantName)
        .winningDate(winningDate)
        .build();
  }

  public WinnerBallotResponse toResponse() {
    return WinnerBallotResponse.builder()
        .ballotId(ballotId)
        .participantId(participantId)
        .lotteryId(lotteryId)
        .participantName(participantName)
        .winningDate(winningDate)
        .build();
  }
}
