package com.playtomic.tests.wallet.api;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopupWalletRequest {
  @NonNull private String creditCardNumber;
  @NonNull private BigDecimal amount;
}
