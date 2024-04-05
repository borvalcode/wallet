package com.playtomic.tests.wallet.domain.charger;

import lombok.NonNull;
import lombok.Value;

@Value
public class Charge {
  @NonNull String paymentId;
}
