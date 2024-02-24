package com.bynder.lottery.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Setter;

@Builder
@Setter
public class Participant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;
}
