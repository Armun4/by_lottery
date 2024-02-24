package com.bynder.lottery.entity;

import com.bynder.lottery.domain.Ballot;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "participant")
public class BallotEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private Long participantId;

  private Long lotteryId;

  public Ballot toDomain() {

    return Ballot.builder().lotteryId(id).participantId(participantId).lotteryId(lotteryId).build();
  }
}
