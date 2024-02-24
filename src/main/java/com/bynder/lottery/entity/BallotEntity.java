package com.bynder.lottery.entity;

import com.bynder.lottery.domain.Ballot;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ballots")
public class BallotEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private long participantId;

  private long lotteryId;

  public Ballot toDomain() {

    return Ballot.builder().id(id).participantId(participantId).lotteryId(lotteryId).build();
  }
}
