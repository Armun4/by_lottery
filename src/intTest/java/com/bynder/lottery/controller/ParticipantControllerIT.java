package com.bynder.lottery.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.bynder.lottery.BaseIT;
import com.bynder.lottery.domain.Participant;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

public class ParticipantControllerIT extends BaseIT {

  @Test
  void shouldBeAbleToSaveParticipant() {

    String request =
        """
                          {
                          "name": "John Doe",
                          "email": "johndoe@example.com"
                        }\s""";

    Participant expected =
        Participant.builder().name("John Doe").email("johndoe@example.com").build();

    Participant result =
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
            .as(Participant.class);

    assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
  }

  @Test
  void shouldThrow400ifNameIsNull() {

    String request =
        """
                        {
                          "email": "johndoe@example.com"
                        }""";

    String result = getResultAsString(request);
  }

  @Test
  void shouldThrow400ifEmailIsNull() {

    String request =
        """
                {
                  "name": "John Doe"
                }""";

    String result = getResultAsString(request);

    assertThat(result).isEqualTo("Invalid request, email not set");
  }

  @Test
  void shouldThrow400ifEmailIsNotValid() {

    String request =
        """
                          {
                          "name": "John Doe",
                          "email": "jo..hndoe@example..com"
                        }\s""";

    Participant expected =
        Participant.builder().name("John Doe").email("johndoe@example.com").build();

    String result = getResultAsString(request);

    assertThat(result).isEqualTo("Invalid request, email is not valid");
  }

  private String getResultAsString(String request) {
    return given()
        .port(port)
        .contentType(ContentType.JSON)
        .body(request)
        .when()
        .post("/v1/participant/register")
        .then()
        .statusCode(400)
        .extract()
        .body()
        .asString();
  }
}
