package com.exam.tomatoback.auth.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum VerityType {
  EMAIL, PASSWORD;

  @JsonCreator
  public static VerityType from(String value) {
    return VerityType.valueOf(value.toUpperCase());
  }
}
