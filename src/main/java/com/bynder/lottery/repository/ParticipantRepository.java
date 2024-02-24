package com.bynder.lottery.repository;

import com.bynder.lottery.domain.Participant;
import com.bynder.lottery.repository.entity.ParticipantEntity;
import com.bynder.lottery.repository.jpa.ParticipantJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ParticipantRepository {

  private final ParticipantJpaRepository participantJpaRepository;

  public Participant save(Participant participant) {
    return participantJpaRepository.save(participant.toEntity()).toDomain();
  }

  public Optional<Participant> get(long participantId) {
    return participantJpaRepository.findById(participantId).map(ParticipantEntity::toDomain);
  }
}
