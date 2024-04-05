package com.playtomic.tests.wallet.domain.query;

import java.math.BigDecimal;
import lombok.NonNull;
import lombok.Value;

@Value
public class WalletDetails {
  @NonNull Long id;
  @NonNull BigDecimal amount;
}
