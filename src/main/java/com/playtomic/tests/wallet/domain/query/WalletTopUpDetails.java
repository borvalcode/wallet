package com.playtomic.tests.wallet.domain.query;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class WalletTopUpDetails {
  @NonNull Long id;
  @NonNull String paymentId;
  @NonNull BigDecimal amount;
  @NonNull Long walletId;
}
