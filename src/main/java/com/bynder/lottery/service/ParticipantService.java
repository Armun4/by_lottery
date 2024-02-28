package com.bynder.lottery.service;

import com.bynder.lottery.domain.Participant;
import com.bynder.lottery.repository.ParticipantRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantService {

  private final ParticipantRepository participantRepository;

  public Participant saveParticipant(Participant participant) {
    return participantRepository.save(participant);
  }

  public Optional<Participant> getParticipant(long participantId) {
    return participantRepository.get(participantId);
  }
}
