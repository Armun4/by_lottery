package com.bynder.lottery.controller;

import com.bynder.lottery.task.LotteryTask;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LotteryTaskController {

  private final LotteryTask lotteryTask;

  @GetMapping("/v1/lottery")
  ResponseEntity<String> submitBallots() {

    lotteryTask.runTask();

    return ResponseEntity.ok("Lottery ran");
  }
}
