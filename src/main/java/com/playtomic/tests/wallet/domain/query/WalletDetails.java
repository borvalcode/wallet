package com.playtomic.tests.wallet.domain.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletDetails {
    @NonNull
    private Long id;

    @NonNull
    private BigDecimal amount;
}