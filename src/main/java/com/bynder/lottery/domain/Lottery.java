package com.bynder.lottery.domain;

import com.bynder.lottery.repository.entity.LotteryEntity;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Lottery {

  // actual id will be set by the db
  @Builder.Default private Long id = null;

  private LocalDate date;

  @Builder.Default private Boolean finished = false;

  public LotteryEntity toEntity() {
    return LotteryEntity.builder().id(id).date(date).finished(finished).build();
  }
}
