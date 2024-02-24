package com.bynder.lottery.demo;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

import com.bynder.lottery.BaseIT;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CustomerControllerTest extends BaseIT {

  @Autowired CustomerRepository customerRepository;

  @BeforeEach
  void setUp() {
    customerRepository.deleteAll();
  }

  @Test
  void shouldGetAllCustomers() {
    List<Customer> customers =
        List.of(
            new Customer(null, "John", "john@mail.com"),
            new Customer(null, "Dennis", "dennis@mail.com"));
    customerRepository.saveAll(customers);

    given()
        .contentType(ContentType.JSON)
        .when()
        .get("/api/customers")
        .then()
        .statusCode(200)
        .body(".", hasSize(2));
  }
}
