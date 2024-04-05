package com.playtomic.tests.wallet.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import lombok.Value;

@Value
public class Payment {

  @NonNull private String id;

  @JsonCreator
  public Payment(@JsonProperty(value = "id", required = true) String id) {
    this.id = id;
  }
}
