package com.bynder.lottery.repository.entity;

import com.bynder.lottery.domain.WinnerBallot;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "winner_ballots")
public class WinnerBallotEntity {
  @Id private long ballotId;

  private long lotteryId;

  private long participantId;

  private String participantName;

  private LocalDate winningDate;

  public WinnerBallot toDomain() {
    return WinnerBallot.builder()
        .ballotId(ballotId)
        .participantId(participantId)
        .participantName(participantName)
        .lotteryId(lotteryId)
        .winningDate(winningDate)
        .build();
  }
}
