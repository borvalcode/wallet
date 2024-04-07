package com.playtomic.tests.wallet.domain.command.entity;

import static java.util.Objects.requireNonNull;

import com.playtomic.tests.wallet.domain.exception.ValidationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class Wallet {
  private final long id;
  private BigDecimal amount;
  private final List<WalletTopUp> newWalletTopUps = new ArrayList<>();

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

  public List<WalletTopUp> getNewWalletTopUps() {
    return newWalletTopUps;
  }

  private void requirePositive(BigDecimal amount) {
    if (amount.signum() != 1) {
      throw new ValidationException("Needs to be positive");
    }
  }

  public void topUp(WalletTopUp walletTopUp) {
    requireNonNull(walletTopUp);

    if (newWalletTopUps.stream().anyMatch(topUp -> topUp.getId() == walletTopUp.getId())) {
      throw new RuntimeException("Can't add 2 topUps with same id");
    }

    newWalletTopUps.add(walletTopUp);

    topUp(walletTopUp.getAmount());
  }

  private void topUp(BigDecimal amount) {
    requireNonNull(amount);
    requirePositive(amount);

    this.amount = this.amount.add(amount);
  }
}
