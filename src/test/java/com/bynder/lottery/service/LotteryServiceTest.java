package com.bynder.lottery.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testcontainers.shaded.com.google.common.base.Verify.verify;

import com.bynder.lottery.domain.Lottery;
import com.bynder.lottery.repository.LotteryRepository;
import com.bynder.lottery.util.LotteryArbitraryProvider;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LotteryServiceTest {

  @Mock private LotteryRepository lotteryRepository;

  @InjectMocks private LotteryService service;

  ArgumentCaptor<Lottery> lotteryCaptor = ArgumentCaptor.forClass(Lottery.class);

  @Test
  void testCloseCurrentAndCreateNext() {
    Lottery currentLottery = LotteryArbitraryProvider.arbitraryLottery().sample();

    LocalDate nextDay = currentLottery.getDate().plusDays(1);
    Lottery expectedNextLottery = Lottery.builder().date(nextDay).build();

    when(lotteryRepository.save(any(Lottery.class))).then(returnsFirstArg());

    service.closeCurrentAndCreateNext(currentLottery);

    verify(lotteryRepository, Mockito.times(2)).save(any(Lottery.class));

    verify(lotteryRepository, Mockito.times(2)).save(lotteryCaptor.capture());

    assertThat(lotteryCaptor.getAllValues().get(1).getDate())
        .isEqualTo(expectedNextLottery.getDate());
  }

  @Test
  void canGetCurrent() {
    Lottery currentLottery = LotteryArbitraryProvider.arbitraryLottery().sample();
    when(lotteryRepository.getCurrentLottery()).thenReturn(Optional.of(currentLottery));

    service.getCurrent();

    verify(lotteryRepository, Mockito.times(1)).getCurrentLottery();
  }
}
