package com.playtomic.tests.wallet.domain.command;

import java.math.BigDecimal;

public final class WalletTopUp {
  private final long id;
  private final String paymentId;
  private final BigDecimal amount;
  private final long walletId;

  public WalletTopUp(long id, String paymentId, BigDecimal amount, long walletId) {
    this.id = id;
    this.paymentId = paymentId;
    this.amount = amount;
    this.walletId = walletId;
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

  public long getWalletId() {
    return walletId;
  }
}
