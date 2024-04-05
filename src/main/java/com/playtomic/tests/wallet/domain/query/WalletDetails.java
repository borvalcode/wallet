package com.playtomic.tests.wallet.domain.query;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletDetails {
  @NonNull private Long id;

  @NonNull private BigDecimal amount;
}
