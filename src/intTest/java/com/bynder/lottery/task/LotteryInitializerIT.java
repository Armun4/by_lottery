package com.bynder.lottery.task;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.bynder.lottery.BaseIT;
import com.bynder.lottery.repository.entity.LotteryEntity;
import com.bynder.lottery.repository.jpa.LotteryJpaRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LotteryInitializerIT extends BaseIT {

  @Autowired LotteryJpaRepository lotteryJpaRepository;

  @Test
  void shouldCreateLotteryAtStartUp() {

    List<LotteryEntity> lotteryEntities = lotteryJpaRepository.findAll();

    assertThat(lotteryEntities.isEmpty()).isFalse();
  }
}
