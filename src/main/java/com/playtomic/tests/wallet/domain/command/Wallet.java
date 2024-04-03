package com.playtomic.tests.wallet.domain.command;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

public final class Wallet {
    private final long id;
    private BigDecimal amount;

    public Wallet(long id) {
        this(id, BigDecimal.ZERO);
    }

    public Wallet(long id, BigDecimal amount) {
        this.id = id;
        this.amount = requireNonNull(amount);
    }

    public long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
