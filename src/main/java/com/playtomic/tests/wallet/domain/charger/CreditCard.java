package com.playtomic.tests.wallet.domain.charger;

import lombok.NonNull;
import lombok.Value;

@Value
public class CreditCard {
  private static final String PATTERN = "^(?:[0-9] ?){12,19}$";

  @NonNull String number;

  public CreditCard(@NonNull String number) {
    if (!number.matches(PATTERN)) {
      throw new IllegalArgumentException();
    }
    this.number = number;
  }
}
