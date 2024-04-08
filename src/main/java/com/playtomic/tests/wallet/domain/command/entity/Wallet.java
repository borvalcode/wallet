package com.playtomic.tests.wallet.domain.command.entity;

import static java.util.Objects.requireNonNull;

import com.playtomic.tests.wallet.domain.exception.ValidationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Wallet {
  private final long id;
  private BigDecimal amount;
  private final Map<Long, WalletTopUp> walletTopUps;

  public Wallet(long id) {
    this(id, BigDecimal.ZERO);
  }

  public Wallet(long id, BigDecimal amount) {
    this(id, amount, new ArrayList<>());
  }

  public Wallet(long id, BigDecimal amount, List<WalletTopUp> walletTopUps) {
    this.id = id;
    this.amount = requireNonNull(amount);
    this.walletTopUps = new HashMap<>();
    walletTopUps.forEach(walletTopUp -> this.walletTopUps.put(walletTopUp.getId(), walletTopUp));
  }

  public long getId() {
    return id;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public List<WalletTopUp> getWalletTopUps() {
    return new ArrayList<>(walletTopUps.values());
  }

  private void requirePositive(BigDecimal amount) {
    if (amount.signum() != 1) {
      throw new ValidationException("Needs to be positive");
    }
  }

  public void topUp(WalletTopUp walletTopUp) {
    requireNonNull(walletTopUp);

    if (walletTopUps.containsKey(walletTopUp.getId())) {
      throw new RuntimeException("Can't add 2 topUps with same id");
    }

    walletTopUps.put(walletTopUp.getId(), walletTopUp);

    topUp(walletTopUp.getAmount());
  }

  private void topUp(BigDecimal amount) {
    requireNonNull(amount);
    requirePositive(amount);

    this.amount = this.amount.add(amount);
  }
}
