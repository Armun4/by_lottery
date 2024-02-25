package com.bynder.lottery.task;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.bynder.lottery.BaseIT;
import com.bynder.lottery.domain.Ballot;
import com.bynder.lottery.domain.Lottery;
import com.bynder.lottery.domain.Participant;
import com.bynder.lottery.domain.WinnerBallot;
import com.bynder.lottery.repository.BallotRepository;
import com.bynder.lottery.repository.LotteryRepository;
import com.bynder.lottery.repository.ParticipantRepository;
import com.bynder.lottery.repository.WinnerBallotRepository;
import com.bynder.lottery.repository.entity.LotteryEntity;
import com.bynder.lottery.repository.jpa.LotteryJpaRepository;
import com.bynder.lottery.util.BallotArbitrarityProvider;
import com.bynder.lottery.util.ParticipantArbitraryProvider;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LotteryTaskIT extends BaseIT {

  @Autowired BallotRepository ballotRepository;

  @Autowired ParticipantRepository participantRepository;

  @Autowired LotteryRepository lotteryRepository;

  @Autowired LotteryJpaRepository lotteryJpaRepository;

  @Autowired WinnerBallotRepository winnerBallotRepository;

  @Autowired LotteryTask lotteryTask;


  @BeforeEach
  void setUp(){
    when(clock.getZone()).thenReturn(ZoneOffset.UTC);
    when(clock.instant()).thenReturn(Instant.parse("2024-02-26T00:00:00Z"));


  }

  @Test
  void happyPath() {

    List<Participant> participants =
        ParticipantArbitraryProvider.arbitraryParticipants().list().ofSize(4).sample().stream()
            .map(participant -> participantRepository.save(participant))
            .toList();

    List<Long> participantIds = participants.stream().map(Participant::getId).toList();

    LocalDate today = LocalDate.of(2024, 02, 25);
    Lottery lottery = lotteryRepository.save(Lottery.builder().date(today).finished(false).build());

    List<Ballot> ballots =
        ballotRepository.saveAll(
            BallotArbitrarityProvider.arbitraryBallotsForLotteryAndSetOfUsers(
                    lottery.getId(), participantIds)
                .list()
                .ofSize(30)
                .sample());

    lotteryTask.runTask();

    WinnerBallot winnerBallot = winnerBallotRepository.getWinnerBallotOfDate(today).get();

    List<Long> ballotIds = ballots.stream().map(Ballot::getId).toList();

    assertThat(ballotIds.contains(winnerBallot.getBallotId())).isTrue();
    assertThat(winnerBallot.getLotteryId()).isEqualTo(lottery.getId());
    assertThat(participantIds.contains(winnerBallot.getParticipantId())).isTrue();
    assertThat(
            participants.stream()
                .map(Participant::getName)
                .toList()
                .contains(winnerBallot.getParticipantName()))
        .isTrue();

    Lottery closedLottery = lotteryJpaRepository.findById(lottery.getId()).get().toDomain();
    assertThat(closedLottery.getFinished()).isTrue();

    Optional<Lottery> nexTLottery =
        lotteryJpaRepository.findCurrentLottery(today.plusDays(1)).map(LotteryEntity::toDomain);
    assertThat(nexTLottery.isPresent()).isTrue();
    assertThat(nexTLottery.get().getFinished()).isFalse();
  }




  @Test
  void noBallotsSavedShouldThrowError() {

    List<Participant> participants =
            ParticipantArbitraryProvider.arbitraryParticipants().list().ofSize(4).sample().stream()
                    .map(participant -> participantRepository.save(participant))
                    .toList();

    List<Long> participantIds = participants.stream().map(Participant::getId).toList();

    LocalDate today = LocalDate.of(2024, 02, 25);
    Lottery lottery = lotteryRepository.save(Lottery.builder().date(today).finished(false).build());


    assertThatThrownBy(() -> lotteryTask.runTask()).isInstanceOf(NoSuchElementException.class)
            .hasMessage("No ballots found for lottery with ID: " + lottery.getId());


  }
}
