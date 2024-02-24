package com.bynder.lottery.controller.util;

import java.util.regex.Pattern;

public class EmailValidator {

  // more info here https://www.baeldung.com/java-email-validation-regex
  static final String regexPattern =
      "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
          + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

  public static boolean isEmailValid(String email) {
    return Pattern.compile(regexPattern).matcher(email).matches();
  }
}
