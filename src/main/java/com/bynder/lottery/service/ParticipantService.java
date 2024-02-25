package com.bynder.lottery.service;

import com.bynder.lottery.domain.Participant;
import com.bynder.lottery.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantService {

  private final ParticipantRepository participantRepository;

  public Participant saveParticipant(Participant participant) {

    return participantRepository.save(participant);
  }

  public Participant getParticipant(long id) {
    return participantRepository
        .get(id)
        .orElseThrow(() -> new RuntimeException("Participant lottery found, please register"));
  }
}
