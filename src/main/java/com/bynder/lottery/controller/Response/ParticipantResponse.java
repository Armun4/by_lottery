package com.bynder.lottery.controller.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ParticipantResponse {

  private String name;

  private String email;
}
