package com.bynder.lottery.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bynder.lottery.domain.Participant;
import com.bynder.lottery.repository.ParticipantRepository;
import com.bynder.lottery.util.ParticipantArbitraryProvider;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {
  @Mock ParticipantRepository participantRepository;

  @InjectMocks ParticipantService participantService;

  @Test
  void canSaveParticipant() {

    Participant participant = ParticipantArbitraryProvider.arbitraryParticipants().sample();

    when(participantRepository.save(participant)).thenReturn(participant);

    participantService.saveParticipant(participant);

    verify(participantRepository).save(participant);
  }

  @Test
  void canGetParticipant() {

    Participant participant = ParticipantArbitraryProvider.arbitraryParticipants().sample();

    when(participantRepository.get(participant.getId())).thenReturn(Optional.of(participant));

    participantService.getParticipant(participant.getId());

    verify(participantRepository).get(participant.getId());
  }
}
