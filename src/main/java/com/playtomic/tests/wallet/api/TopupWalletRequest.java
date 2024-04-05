package com.playtomic.tests.wallet.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopupWalletRequest {
  @NonNull
  @JsonProperty("credit_card_number")
  private String creditCardNumber;

  @NonNull private BigDecimal amount;
}
