package com.bynder.lottery.controller;

import com.bynder.lottery.controller.request.BallotSubmitRequest;
import com.bynder.lottery.domain.Ballot;
import com.bynder.lottery.domain.WinnerBallot;
import com.bynder.lottery.service.BallotService;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BallotController {

  private final BallotService ballotService;
  private final Clock clock;

  @PostMapping("/v1/ballot/submit")
  ResponseEntity<List<Ballot>> submitBallots(@RequestBody BallotSubmitRequest request) {

    validateRequest(request);

    List<Ballot> result =
        ballotService.saveBallots(request.getParticipantId(), request.getAmount());

    return ResponseEntity.ok(result);
  }

  @GetMapping("/v1/ballot/winner")
  ResponseEntity<WinnerBallot> getWinningBallot(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

    LocalDate today = LocalDate.ofInstant(clock.instant(), clock.getZone());
    if (date.isAfter(today)) {
      // If the specified date is after today, return a bad request response
      return ResponseEntity.badRequest().build();
    }

    WinnerBallot winnerBallot = ballotService.getWinner(date);

    return ResponseEntity.ok(winnerBallot);
  }

  private void validateRequest(BallotSubmitRequest request) {

    if (request.getAmount() == null) {
      throw new IllegalArgumentException("Invalid request, amount not set.");
    }

    if (request.getAmount() == 0) {
      throw new IllegalArgumentException("Invalid request, amount has to be more than 0");
    }

    if (request.getParticipantId() == null) {
      throw new IllegalArgumentException("Invalid request, participantId not set.");
    }
  }

  private void validateRequest(LocalDate date) {
    LocalDate today = LocalDate.ofInstant(clock.instant(), clock.getZone());
    if (date.isAfter(today)) {
      // If the specified date is after today, return a bad request response
      throw new IllegalArgumentException("The specified date cannot be after today");
    }
  }
}
