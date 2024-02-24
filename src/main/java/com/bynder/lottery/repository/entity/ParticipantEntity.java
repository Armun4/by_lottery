package com.bynder.lottery.repository.entity;

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

  @Column(nullable = false)
  private String email;

  public Participant toDomain() {
    return Participant.builder().id(id).name(name).email(email).build();
  }
}
