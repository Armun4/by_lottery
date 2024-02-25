package com.bynder.lottery.service;

import com.bynder.lottery.domain.Ballot;
import com.bynder.lottery.domain.Lottery;
import com.bynder.lottery.domain.Participant;
import com.bynder.lottery.repository.BallotRepository;
import com.bynder.lottery.repository.LotteryRepository;
import com.bynder.lottery.repository.ParticipantRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BallotService {

  private final BallotRepository ballotRepository;
  private final ParticipantRepository participantRepository;
  private final LotteryRepository lotteryRepository;

  public List<Ballot> saveBallots(long participantId, int amount) {
    Participant participant =
        participantRepository
            .get(participantId)
            .orElseThrow(
                () -> new NoSuchElementException("Participant not found, please register"));

    Lottery currentLottery =
        lotteryRepository
            .getCurrentLottery()
            .orElseThrow(
                () -> new RuntimeException("No current lottery found. Please check again later."));

    List<Ballot> ballots =
        Stream.generate(
                () ->
                    Ballot.builder()
                        .lotteryId(currentLottery.getId())
                        .participantId(participant.getId())
                        .build())
            .limit(amount)
            .toList();

    return ballotRepository.saveAll(ballots);
  }

  List<Ballot> getAllBallotsForLottery(long LotteryId) {
    return ballotRepository.findAllByLotteryId(LotteryId);
  }
}
