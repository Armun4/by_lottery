package com.bynder.lottery.controller;

import static io.restassured.RestAssured.given;

import com.bynder.lottery.BaseIT;
import com.bynder.lottery.domain.Ballot;
import com.bynder.lottery.domain.Lottery;
import com.bynder.lottery.domain.Participant;
import com.bynder.lottery.repository.LotteryRepository;
import com.bynder.lottery.repository.ParticipantRepository;
import com.bynder.lottery.util.ParticipantArbitraryProvider;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

public class BallotControllerIT extends BaseIT {

  @Autowired ParticipantRepository participantRepository;

  @Autowired LotteryRepository lotteryRepository;

  @Test
  void shouldBeAbleToCreateBallot() {
    Participant participant = ParticipantArbitraryProvider.arbitraryParticipants().sample();
    Participant savedParticipant = participantRepository.save(participant);

    String request =
        "{\n"
            + "  \"participantId\": "
            + savedParticipant.getId()
            + ",\n"
            + "  \"amount\": 5\n"
            + "}";

    Instant currentStartTime = Instant.parse("2024-01-24T00:00:00Z");

    Lottery currentLottery =
        Lottery.builder()
            .startTime(currentStartTime)
            .endTime(currentStartTime.plus(1, ChronoUnit.DAYS))
            .finished(false)
            .build();

    Lottery savedLottery = lotteryRepository.save(currentLottery);

    Ballot expected =
        Ballot.builder()
            .lotteryId(savedLottery.getId())
            .participantId(savedParticipant.getId())
            .build();

    Mockito.when(clock.instant()).thenReturn(currentStartTime.plus(5, ChronoUnit.HOURS));

    Class<Ballot> ballotClass = Ballot.class;

    TypeRef<List<Ballot>> ballotListType = new TypeRef<>() {};

    List<Ballot> result =
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
          Assertions.assertThat(savedBallot)
              .usingRecursiveComparison()
              .ignoringFields("id")
              .isEqualTo(expected);
        });
  }

  @Test
  void shouldThrow400ifParticipantIdIsNull() {

    String request = "{\n" + "  \"amount\": 5\n" + "}";

    String result = getResult(request, 400);

    Assertions.assertThat(result).isEqualTo("Invalid request, participantId not set.");
  }

  @Test
  void shouldThrow400ifAmountIsNull() {

    String request = "{\n" + "  \"participantId\": 123456,\n" + "}";
    String result = getResult(request, 400);

    Assertions.assertThat(result).isEqualTo("Invalid request, amount not set.");
  }

  @Test
  void shouldThrow400ifAmountIs0() {

    String request = "{\n" + "  \"participantId\": 123456,\n" + "  \"amount\": 0\n" + "}";

    String result = getResult(request, 400);

    Assertions.assertThat(result).isEqualTo("Invalid request, amount has to be more than 0");
  }

  @Test
  void shouldThrow400ifUserNotFound() {

    String request = "{\n" + "  \"participantId\": 123456,\n" + "  \"amount\": 2\n" + "}";

    String result = getResult(request, 400);

    Assertions.assertThat(result).isEqualTo("Participant not found, please register");
  }



  @Test
  void shouldThrow500ifLotteryNotFound() {

    Participant participant = ParticipantArbitraryProvider.arbitraryParticipants().sample();
    Participant savedParticipant = participantRepository.save(participant);

    String request = "{\n" + "  \"participantId\": 123456,\n" + "  \"amount\": 2\n" + "}";

    String result = getResult(request, 500);

    Assertions.assertThat(result).isEqualTo("No current lottery found. Please check again later.");
  }

  private String getResult(String request, int code) {
    return given()
        .port(port)
        .contentType(ContentType.JSON)
        .body(request)
        .when()
        .post("/v1/ballot/submit")
        .then()
        .statusCode(code)
        .extract()
        .body()
        .asString();
  }
}
