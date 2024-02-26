package com.bynder.lottery.controller;

import static io.restassured.RestAssured.given;
import static net.jqwik.time.api.Dates.dates;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import com.bynder.lottery.BaseIT;
import com.bynder.lottery.controller.Response.BallotResponse;
import com.bynder.lottery.controller.Response.WinnerBallotResponse;
import com.bynder.lottery.domain.Lottery;
import com.bynder.lottery.domain.Participant;
import com.bynder.lottery.domain.WinnerBallot;
import com.bynder.lottery.repository.LotteryRepository;
import com.bynder.lottery.repository.ParticipantRepository;
import com.bynder.lottery.repository.WinnerBallotRepository;
import com.bynder.lottery.util.BallotArbitrarityProvider;
import com.bynder.lottery.util.ParticipantArbitraryProvider;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

public class BallotControllerIT extends BaseIT {

  @Autowired ParticipantRepository participantRepository;

  @Autowired LotteryRepository lotteryRepository;

  @Autowired WinnerBallotRepository winnerBallotRepository;

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

    BallotResponse expected =
        BallotResponse.builder()
            .lotteryId(savedLottery.getId())
            .participantId(savedParticipant.getId())
            .build();

    when(clock.instant())
        .thenReturn(Instant.parse("2024-01-24T00:00:00Z").plus(5, ChronoUnit.HOURS));
    when(clock.getZone()).thenReturn(ZoneOffset.UTC);

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

  @Test
  void shouldThrow400ifParticipantIdIsNull() {

    String request = """
                {
                  "amount": 5
                }""";

    String result = getSubmitBallotResultAsString(request, 400);

    Assertions.assertThat(result).isEqualTo("Invalid request, participantId not set.");
  }

  @Test
  void shouldThrow400ifAmountIsNull() {

    String request =
        """
                {
                  "participantId": 123456
                }""";
    String result = getSubmitBallotResultAsString(request, 400);

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

    String result = getSubmitBallotResultAsString(request, 400);

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

    String result = getSubmitBallotResultAsString(request, 400);

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

    String result = getSubmitBallotResultAsString(request, 500);

    assertThat(result).isEqualTo("No current lottery found. Please check again later.");
  }

  private String getSubmitBallotResultAsString(String request, int code) {
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

  @Test
  void shouldBeAbleToRetrieveWinningBallot() {

    WinnerBallot winnerBallot = BallotArbitrarityProvider.arbitraryWinnerBallots().sample();
    winnerBallotRepository.saveWinner(winnerBallot);

    LocalDate localDate = winnerBallot.getWinningDate();

    ZonedDateTime midnightToday = localDate.atStartOfDay(ZoneOffset.UTC);
    Instant instantOfToday = midnightToday.toInstant();

    Mockito.when(clock.instant()).thenReturn(instantOfToday);
    Mockito.when(clock.getZone()).thenReturn(ZoneOffset.UTC);

    WinnerBallotResponse result =
        given()
            .port(port)
            .queryParam("date", winnerBallot.getWinningDate().toString())
            .contentType(ContentType.JSON)
            .get("/v1/ballot/winner")
            .then()
            .statusCode(200)
            .extract()
            .response()
            .as(WinnerBallotResponse.class);

    assertThat(result).usingRecursiveComparison().isEqualTo(winnerBallot);
  }

  @Test
  void shouldReturn400IfDateIsMoreThanCurrent() {

    LocalDate today = dates().atTheEarliest(LocalDate.of(2012, 1, 1)).sample();
    ZonedDateTime midnightToday = today.atStartOfDay(ZoneOffset.UTC);
    Instant instantOfToday = midnightToday.toInstant();

    Mockito.when(clock.instant()).thenReturn(instantOfToday);
    Mockito.when(clock.getZone()).thenReturn(ZoneOffset.UTC);

    String result =
        given()
            .port(port)
            .queryParam("date", today.plusDays(5).toString())
            .contentType(ContentType.JSON)
            .get("/v1/ballot/winner")
            .then()
            .statusCode(400)
            .extract()
            .body()
            .asString();

    assertThat(result)
        .isEqualTo("Invalid request, it is not possible to retrieve winner for future Lotteries");
  }

  @Test
  void shouldBeAbleThrow401ifWinnerNotFoundForValidDate() {

    LocalDate today = dates().atTheEarliest(LocalDate.of(2012, 1, 1)).sample();
    ZonedDateTime midnightToday = today.atStartOfDay(ZoneOffset.UTC);
    Instant instantOfToday = midnightToday.toInstant();

    Mockito.when(clock.instant()).thenReturn(instantOfToday);
    Mockito.when(clock.getZone()).thenReturn(ZoneOffset.UTC);

    String result =
        given()
            .port(port)
            .queryParam("date", today.minusDays(5).toString())
            .contentType(ContentType.JSON)
            .get("/v1/ballot/winner")
            .then()
            .statusCode(404)
            .extract()
            .body()
            .asString();

    assertThat(result).isEqualTo("Winner ballot not found for given date");
  }
}
