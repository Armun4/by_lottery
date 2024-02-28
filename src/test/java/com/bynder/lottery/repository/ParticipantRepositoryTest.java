package com.bynder.lottery.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bynder.lottery.domain.Participant;
import com.bynder.lottery.repository.entity.ParticipantEntity;
import com.bynder.lottery.repository.jpa.ParticipantJpaRepository;
import com.bynder.lottery.util.ParticipantArbitraryProvider;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ParticipantRepositoryTest {
  @Mock ParticipantJpaRepository participantJpaRepository;
  @InjectMocks ParticipantRepository participantRepository;

  ArgumentCaptor<ParticipantEntity> argumentCaptor =
      ArgumentCaptor.forClass(ParticipantEntity.class);

  @Test
  void canSaveParticipant() {

    Participant participant = ParticipantArbitraryProvider.arbitraryParticipants().sample();
    ParticipantEntity participantEntity = participant.toEntity();

    when(participantJpaRepository.save(any())).thenReturn(participantEntity);

    participantRepository.save(participant);

    verify(participantJpaRepository).save(argumentCaptor.capture());

    ParticipantEntity capturedArguments = argumentCaptor.getValue();

    assertThat(capturedArguments).usingRecursiveComparison().isEqualTo(participantEntity);
  }

  @Test
  void canGetParticipant() {
    Participant participant = ParticipantArbitraryProvider.arbitraryParticipants().sample();
    ParticipantEntity participantEntity = participant.toEntity();

    when(participantJpaRepository.findById(participant.getId()))
        .thenReturn(Optional.ofNullable(participantEntity));

    participantRepository.get(participant.getId());

    verify(participantJpaRepository).findById(participant.getId());
  }
}
