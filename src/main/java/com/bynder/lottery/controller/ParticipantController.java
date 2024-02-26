package com.bynder.lottery.controller;

import static com.bynder.lottery.controller.util.EmailValidator.isEmailValid;

import com.bynder.lottery.controller.Response.ParticipantResponse;
import com.bynder.lottery.controller.request.ParticipantRequest;
import com.bynder.lottery.domain.Participant;
import com.bynder.lottery.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ParticipantController {

  private final ParticipantService participantService;

  @PostMapping("/v1/participant/register")
  ResponseEntity<ParticipantResponse> saveParticipant(@RequestBody ParticipantRequest request) {
    validateRequest(request);

    Participant result = participantService.saveParticipant(request.toDomain());

    return ResponseEntity.ok(result.toResponse());
  }

  private void validateRequest(ParticipantRequest request) {

    if (request.getName() == null || request.getName().isEmpty()) {
      throw new IllegalArgumentException("Invalid request, name not set.");
    }

    if (request.getEmail() == null || request.getEmail().isEmpty()) {
      throw new IllegalArgumentException("Invalid request, email not set");
    }

    boolean emailValid = isEmailValid(request.getEmail());

    if (!emailValid) {
      throw new IllegalArgumentException("Invalid request, email is not valid");
    }
  }
}
