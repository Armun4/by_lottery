package com.bynder.lottery.entity;

import com.bynder.lottery.domain.Participant;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "participant")
public class ParticipantEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  public Participant toDomain() {
    return Participant.builder().id(id).name(name).build();
  }
}
