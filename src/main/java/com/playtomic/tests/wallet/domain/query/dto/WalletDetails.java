package com.playtomic.tests.wallet.domain.query.dto;

import java.math.BigDecimal;
import lombok.NonNull;
import lombok.Value;

@Value
public class WalletDetails {
  @NonNull Long id;
  @NonNull BigDecimal amount;
}
