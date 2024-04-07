package com.playtomic.tests.wallet.domain.command.entity;

import java.math.BigDecimal;

public final class WalletTopUp {
  private final long id;
  private final String paymentId;
  private final BigDecimal amount;

  public WalletTopUp(long id, String paymentId, BigDecimal amount) {
    this.id = id;
    this.paymentId = paymentId;
    this.amount = amount;
  }

  public long getId() {
    return id;
  }

  public String getPaymentId() {
    return paymentId;
  }

  public BigDecimal getAmount() {
    return amount;
  }
}
