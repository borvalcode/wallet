package com.playtomic.tests.wallet.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopupWalletRequest {
    @NonNull
    private String creditCardNumber;

    @NonNull
    private BigDecimal amount;
}
