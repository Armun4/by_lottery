package com.bynder.lottery.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

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
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
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

    LocalDate currentDate =
        LocalDate.ofInstant(Instant.parse("2024-01-24T00:00:00Z"), ZoneOffset.UTC);

    Lottery currentLottery = Lottery.builder().date(currentDate).finished(false).build();

    Lottery savedLottery = lotteryRepository.save(currentLottery);

    Ballot expected =
        Ballot.builder()
            .lotteryId(savedLottery.getId())
            .participantId(savedParticipant.getId())
            .build();

    when(clock.instant())
        .thenReturn(Instant.parse("2024-01-24T00:00:00Z").plus(5, ChronoUnit.HOURS));
    when(clock.getZone()).thenReturn(ZoneOffset.UTC);

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
          assertThat(savedBallot)
              .usingRecursiveComparison()
              .ignoringFields("id")
              .isEqualTo(expected);
        });
  }

  @Test
  void shouldThrow400ifParticipantIdIsNull() {

    String request = """
            {
              "amount": 5
            }""";

    String result = getResultAsString(request, 400);

    Assertions.assertThat(result).isEqualTo("Invalid request, participantId not set.");
  }

  @Test
  void shouldThrow400ifAmountIsNull() {

    String request = """
            {
              "participantId": 123456
            }""";
    String result = getResultAsString(request, 400);

    assertThat(result).isEqualTo("Invalid request, amount not set.");
  }

  @Test
  void shouldThrow400ifAmountIs0() {

    String request =
        """
            {
              "participantId": 123456,
              "amount": 0
            }""";

    String result = getResultAsString(request, 400);

    assertThat(result).isEqualTo("Invalid request, amount has to be more than 0");
  }

  @Test
  void shouldThrow400ifUserNotFound() {

    String request =
        """
            {
              "participantId": 123456,
              "amount": 2
            }""";

    String result = getResultAsString(request, 400);

    assertThat(result).isEqualTo("Participant not found, please register");
  }

  @Test
  void shouldThrow500ifLotteryNotFound() {

    Participant participant = ParticipantArbitraryProvider.arbitraryParticipants().sample();
    Participant savedParticipant = participantRepository.save(participant);
    Instant currentStartTime = Instant.parse("2024-01-24T00:00:00Z");

    when(clock.instant()).thenReturn(currentStartTime.plus(5, ChronoUnit.HOURS));
    when(clock.getZone()).thenReturn(ZoneOffset.UTC);

    String request =
        "{\n"
            + "  \"participantId\": "
            + savedParticipant.getId()
            + ",\n"
            + "  \"amount\": 5\n"
            + "}";

    String result = getResultAsString(request, 500);

    assertThat(result).isEqualTo("No current lottery found. Please check again later.");
  }

  private String getResultAsString(String request, int code) {
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
