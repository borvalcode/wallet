package com.playtomic.tests.wallet.domain.command.entity;

import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;

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

  public void topUp(BigDecimal amount) {
    requireNonNull(amount);
    requirePositive(amount);

    this.amount = this.amount.add(amount);
  }

  public void spend(BigDecimal amount) {
    requireNonNull(amount);
    requirePositive(amount);

    this.amount = this.amount.subtract(amount);
  }

  private void requirePositive(BigDecimal amount) {
    if (amount.signum() != 1) {
      throw new IllegalArgumentException("Needs to be positive");
    }
  }
}
