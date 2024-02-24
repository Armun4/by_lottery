package com.bynder.lottery.domain;

import com.bynder.lottery.entity.LotteryEntity;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Lottery {

  // actual id will be set by the db
  @Builder.Default private Long id = null;

  private Instant startTime;

  private Instant endTime;

  @Builder.Default private Boolean finished = false;

  public LotteryEntity toEntity() {
    return LotteryEntity.builder().id(id).endTime(endTime).startTime(startTime).finished(finished).build();
  }
}
