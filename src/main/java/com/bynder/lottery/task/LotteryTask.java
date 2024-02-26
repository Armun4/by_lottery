package com.bynder.lottery.task;

import com.bynder.lottery.domain.Ballot;
import com.bynder.lottery.domain.Lottery;
import com.bynder.lottery.domain.WinnerBallot;
import com.bynder.lottery.repository.ParticipantRepository;
import com.bynder.lottery.service.BallotService;
import com.bynder.lottery.service.LotteryService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LotteryTask {

  private final BallotService ballotService;
  private final LotteryService lotteryService;

  private final ParticipantRepository participantRepository;

  @Scheduled(cron = "0 0 0 * * *")
  public void runTask() {

    Lottery currentLottery = lotteryService.getLotteryAtMidnight();

    lotteryService.closeCurrentAndCreateNext(currentLottery);

    List<Ballot> ballots = ballotService.getAllBallotsForLottery(currentLottery.getId());

    if (ballots.isEmpty()) {
      throw new NoSuchElementException(
          "No ballots found for lottery with ID: " + currentLottery.getId());
    }

    Ballot winningBallot = getWinner(ballots);

    WinnerBallot winnerBallot = getWinnerBallot(currentLottery, winningBallot);

    ballotService.saveWinner(winnerBallot);
  }

  private WinnerBallot getWinnerBallot(Lottery currentLottery, Ballot winner) {

    String participantName =
        participantRepository
            .get(winner.getParticipantId())
            .orElseThrow(
                () -> new RuntimeException("Participant not found event tho ballot was registered"))
            .getName();

    return WinnerBallot.builder()
        .ballotId(winner.getId())
        .participantId(winner.getParticipantId())
        .lotteryId(currentLottery.getId())
        .participantName(participantName)
        .winningDate(currentLottery.getDate())
        .build();
  }

  private Ballot getWinner(List<Ballot> ballots) {
    Random random = new Random();
    int randomIndex = random.nextInt(ballots.size());
    return ballots.get(randomIndex);
  }
}
