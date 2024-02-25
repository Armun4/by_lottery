package com.bynder.lottery.controller;

import com.bynder.lottery.controller.request.BallotSubmitRequest;
import com.bynder.lottery.domain.Ballot;
import com.bynder.lottery.service.BallotService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BallotController {

  private final BallotService ballotService;

  @PostMapping("/v1/ballot/submit")
  ResponseEntity<List<Ballot>> submitBallots(@RequestBody BallotSubmitRequest request) {

    validateRequest(request);

    List<Ballot> result =
        ballotService.saveBallots(request.getParticipantId(), request.getAmount());

    return ResponseEntity.ok(result);
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
}
