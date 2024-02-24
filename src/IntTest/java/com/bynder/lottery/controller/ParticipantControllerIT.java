package com.bynder.lottery.controller;

import static io.restassured.RestAssured.given;

import com.bynder.lottery.BaseIT;
import com.bynder.lottery.domain.Participant;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParticipantControllerIT extends BaseIT {

  @Test
  void shouldBeAbleToSaveParticipant() {

    String request =
        "  {\n" + "  \"name\": \"John Doe\",\n" + "  \"email\": \"johndoe@example.com\"\n" + "} ";

    Participant expected =
        Participant.builder().name("John Doe").email("johndoe@example.com").build();

    Participant result =
        given()
            .port(port)
            .contentType(ContentType.JSON)
            .body(request)
            .when()
            .post("/v1/participant/save")
            .then()
            .statusCode(200)
            .extract()
            .response()
            .as(Participant.class);

    Assertions.assertThat(result)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(expected);
  }

  @Test
  void shouldThrow400ifNameIsNull() {

    String request = "{\n" + "  \"email\": \"johndoe@example.com\"\n" + "}";

   String result =  given()
        .port(port)
        .contentType(ContentType.JSON)
        .body(request)
        .when()
        .post("/v1/participant/save")
        .then()
        .statusCode(400).extract().body().asString();

        Assertions.assertThat(result).isEqualTo("Invalid request, name not set.");
  }


  @Test
  void shouldThrow400ifEmailIsNull() {

    String request = "{\n" +
            "  \"name\": \"John Doe\"\n" +
            "}";

   String result =  given()
        .port(port)
        .contentType(ContentType.JSON)
        .body(request)
        .when()
        .post("/v1/participant/save")
        .then()
        .statusCode(400).extract().body().asString();

        Assertions.assertThat(result).isEqualTo("Invalid request, email not set");
  }



    @Test
    void shouldThrow400ifEmailIsNotValid() {

        String request =
                "  {\n" + "  \"name\": \"John Doe\",\n" + "  \"email\": \"jo..hndoe@example..com\"\n" + "} ";

        Participant expected =
                Participant.builder().name("John Doe").email("johndoe@example.com").build();

        String result =  given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/v1/participant/save")
                .then()
                .statusCode(400).extract().body().asString();

        Assertions.assertThat(result).isEqualTo("Invalid request, email is not valid");
    }

}
