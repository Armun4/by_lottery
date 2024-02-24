package com.bynder.lottery.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Lottery {

  private long id;
  private List<Long> ballotIds;

  private Instant startTime;

  private Instant endTime;
}
