package com.bynder.lottery.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bynder.lottery.domain.Ballot;
import com.bynder.lottery.domain.Lottery;
import com.bynder.lottery.domain.Participant;
import com.bynder.lottery.repository.BallotRepository;
import com.bynder.lottery.repository.LotteryRepository;
import com.bynder.lottery.repository.ParticipantRepository;
import com.bynder.lottery.repository.WinnerBallotRepository;
import com.bynder.lottery.util.BallotArbitrarityProvider;
import com.bynder.lottery.util.LotteryArbitraryProvider;
import com.bynder.lottery.util.ParticipantArbitraryProvider;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BallotServiceTest {

  @Mock BallotRepository ballotRepository;
  @Mock ParticipantRepository participantRepository;

  @Mock LotteryRepository lotteryRepository;

  @Mock WinnerBallotRepository winnerBallotRepository;

  @Mock Clock clock;

  ArgumentCaptor<List<Ballot>> ballotArgumentCaptor = ArgumentCaptor.forClass(List.class);
  ArgumentCaptor<Lottery> lotteryArgumentCaptor = ArgumentCaptor.forClass(Lottery.class);

  @InjectMocks BallotService ballotService;

  @Test
  void canSave() {

    Participant participant = ParticipantArbitraryProvider.arbitraryParticipants().sample();
    Mockito.when(participantRepository.get(participant.getId()))
        .thenReturn(Optional.of(participant));

    Lottery lottery = LotteryArbitraryProvider.arbitraryLottery().sample();

    LocalDate today = lottery.getDate();
    ZonedDateTime midnightToday = today.atStartOfDay(ZoneOffset.UTC);
    Instant instantOfToday = midnightToday.toInstant();

    Mockito.when(clock.instant()).thenReturn(instantOfToday);
    Mockito.when(clock.getZone()).thenReturn(ZoneOffset.UTC);

    Mockito.when(lotteryRepository.getCurrentLottery(today)).thenReturn(Optional.of(lottery));

    int amount = 3;
    ballotService.saveBallots(participant.getId(), amount);

    List<Ballot> expected =
        Stream.generate(
                () ->
                    Ballot.builder()
                        .lotteryId(lottery.getId())
                        .participantId(participant.getId())
                        .build())
            .limit(amount)
            .toList();

    verify(participantRepository).get(participant.getId());
    verify(lotteryRepository).getCurrentLottery(today);
    verify(ballotRepository).saveAll(ballotArgumentCaptor.capture());

    List<Ballot> capturedArguments = ballotArgumentCaptor.getValue();

    assertThat(capturedArguments).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void shouldThrowExceptionNoParticipant() {

    long participantId = Arbitraries.longs().sample();
    Mockito.when(participantRepository.get(participantId)).thenReturn(Optional.empty());

    assertThatThrownBy(
            () -> {
              ballotService.saveBallots(participantId, 1);
            })
        // Specify the expected exception type
        .isInstanceOf(NoSuchElementException.class)
        // Optionally, assert the message or other details of the exception
        .hasMessage("Participant not found, please register");
  }

  @Test
  void shouldThrowExceptionNoLottery() {

    Participant participant = ParticipantArbitraryProvider.arbitraryParticipants().sample();
    Mockito.when(participantRepository.get(participant.getId()))
        .thenReturn(Optional.of(participant));

    Instant currentStartTime = Instant.parse("2024-01-24T00:00:00Z");

    long lotteryId = Arbitraries.longs().sample();

    LocalDate today = LocalDate.ofInstant(currentStartTime, ZoneId.systemDefault());

    Lottery expectedLottery = Lottery.builder().date(today).finished(false).id(lotteryId).build();

    int amountToAdd = 5;
    when(clock.instant()).thenReturn(currentStartTime.plus(amountToAdd, ChronoUnit.HOURS));
    when(clock.getZone()).thenReturn(ZoneOffset.UTC);
    when(lotteryRepository.save(any(Lottery.class))).thenReturn(expectedLottery);

    ballotService.saveBallots(participant.getId(), amountToAdd);

    List<Ballot> expected =
        Stream.generate(
                () ->
                    Ballot.builder()
                        .lotteryId(lotteryId)
                        .participantId(participant.getId())
                        .build())
            .limit(amountToAdd)
            .toList();

    verify(lotteryRepository).getCurrentLottery(today);
    verify(lotteryRepository).save(lotteryArgumentCaptor.capture());
    verify(ballotRepository).saveAll(ballotArgumentCaptor.capture());

    List<Ballot> capturedArguments = ballotArgumentCaptor.getValue();
    Lottery capturedLottery = lotteryArgumentCaptor.getValue();

    assertThat(capturedArguments).usingRecursiveComparison().isEqualTo(expected);

    assertThat(capturedLottery)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(expectedLottery);
  }

  @Test
  void canGetBallotsForLotteryId() {

    long lotteryId = Arbitraries.longs().sample();

    List<Ballot> ballots =
        BallotArbitrarityProvider.arbitraryBallotsForLottery(lotteryId)
            .list()
            .ofMaxSize(12)
            .sample();
    when(ballotRepository.findAllByLotteryId(lotteryId)).thenReturn(ballots);

    List<Ballot> result = ballotService.getAllBallotsForLottery(lotteryId);

    verify(ballotRepository, Mockito.times(1)).findAllByLotteryId(lotteryId);
  }
}
