package com.bynder.lottery.e2e;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import com.bynder.lottery.BaseIT;
import com.bynder.lottery.config.TestConfig;
import com.bynder.lottery.controller.response.BallotResponse;
import com.bynder.lottery.controller.response.ParticipantResponse;
import com.bynder.lottery.controller.response.WinnerBallotResponse;
import com.bynder.lottery.domain.Ballot;
import com.bynder.lottery.domain.Lottery;
import com.bynder.lottery.domain.Participant;
import com.bynder.lottery.repository.BallotRepository;
import com.bynder.lottery.repository.LotteryRepository;
import com.bynder.lottery.repository.entity.ParticipantEntity;
import com.bynder.lottery.repository.jpa.BallotJpaRepository;
import com.bynder.lottery.repository.jpa.LotteryJpaRepository;
import com.bynder.lottery.repository.jpa.ParticipantJpaRepository;
import com.bynder.lottery.repository.jpa.WinnerBallotJpaRepository;
import com.bynder.lottery.task.LotteryTask;
import com.bynder.lottery.util.ParticipantArbitraryProvider;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BynderLotteryE2E extends BaseIT {

  @Autowired LotteryRepository lotteryRepository;

  @Autowired LotteryTask lotteryTask;

  @Autowired BallotRepository ballotRepository;

  @Autowired LotteryJpaRepository lotteryJpaRepository;

  @Autowired ParticipantJpaRepository participantJpaRepository;
  @Autowired WinnerBallotJpaRepository winnerBallotJpaRepository;
  @Autowired BallotJpaRepository ballotJpaRepository;

  @BeforeEach
  void cleanDb() {
    participantJpaRepository.deleteAll();
    winnerBallotJpaRepository.deleteAll();
    ballotJpaRepository.deleteAll();
  }

  void setUp() {
    Instant todayMidDayInstant = TestConfig.todayMidDayInstant;
    when(clock.instant()).thenReturn(todayMidDayInstant);
    when(clock.getZone()).thenReturn(ZoneOffset.UTC);
  }

  @Test
  void canExecuteWholeFlow() {

    // save participants via endpoint
    List<Participant> participants =
        ParticipantArbitraryProvider.arbitraryParticipants().list().ofSize(3).sample();
    participants.forEach(this::registerParticipant);

    // create a lottery
    Instant todayMidDayInstant = TestConfig.todayMidDayInstant;
    LocalDate currentDate = LocalDate.ofInstant(todayMidDayInstant, ZoneOffset.UTC);

    // Lottery should be generated at startUo
    Lottery savedLottery = lotteryRepository.getCurrentLottery(currentDate).get();

    List<Long> participantIds =
        participantJpaRepository.findAll().stream().map(ParticipantEntity::getId).toList();

    // save 3 ballots per participant
    participantIds.stream()
        .forEach(
            id -> {
              register3Ballots(id, savedLottery.getId());
            });

    runLotteryAndAssertIsClosed(savedLottery);

    getWinningBallot(currentDate, savedLottery.getId());
  }

  private void registerParticipant(Participant participant) {

    String email = participant.getEmail();
    String name = participant.getName();
    String request =
        String.format(
            """
                {
                  "name": "%s",
                  "email": "%s"
                }
                """,
            name, email);

    ParticipantResponse result =
        given()
            .port(port)
            .contentType(ContentType.JSON)
            .body(request)
            .when()
            .post("/v1/participant/register")
            .then()
            .statusCode(200)
            .extract()
            .response()
            .as(ParticipantResponse.class);

    ParticipantResponse expected = ParticipantResponse.builder().name(name).email(email).build();
    assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
  }

  private void register3Ballots(long participantId, long LotteryId) {
    String request =
        "{\n" + "  \"participantId\": " + participantId + ",\n" + "  \"amount\": 3\n" + "}";

    BallotResponse expected =
        BallotResponse.builder().lotteryId(LotteryId).participantId(participantId).build();
    TypeRef<List<BallotResponse>> ballotListType = new TypeRef<>() {};

    List<BallotResponse> result =
        given()
            .port(port)
            .contentType(ContentType.JSON)
            .body(request)
            .when()
            .post("/v1/ballot/submit")
            .then()
            .statusCode(200)
            .extract()
            .response()
            .as(ballotListType);

    result.forEach(
        savedBallot -> {
          assertThat(savedBallot)
              .usingRecursiveComparison()
              .ignoringFields("id")
              .isEqualTo(expected);
        });
  }

  private void runLotteryAndAssertIsClosed(Lottery savedLottery) {
    lotteryTask.runTask();

    Lottery lottery = lotteryJpaRepository.findById(savedLottery.getId()).get().toDomain();
    assertThat(lottery.getFinished()).isTrue();
  }

  private void getWinningBallot(LocalDate localDate, long lotteryId) {

    List<Long> ballotsForLottery =
        ballotRepository.findAllByLotteryId(lotteryId).stream().map(Ballot::getId).toList();

    WinnerBallotResponse result =
        given()
            .port(port)
            .queryParam("date", localDate.toString())
            .contentType(ContentType.JSON)
            .get("/v1/ballot/winner")
            .then()
            .statusCode(200)
            .extract()
            .response()
            .as(WinnerBallotResponse.class);

    assertThat(ballotsForLottery.contains(result.getBallotId())).isTrue();
  }
}
