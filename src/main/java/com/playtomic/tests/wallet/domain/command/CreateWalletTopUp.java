package com.playtomic.tests.wallet.domain.command;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
public final class CreateWalletTopUp {
  private final WalletTopUpRepository walletTopUpRepository;

  public CreateWalletTopUp(WalletTopUpRepository walletTopUpRepository) {
    this.walletTopUpRepository = walletTopUpRepository;
  }

  public long execute(Input input) {
    long id = walletTopUpRepository.nextId();

    WalletTopUp walletTopUp = new WalletTopUp(id, input.paymentId, input.amount, input.walletId);

    walletTopUpRepository.store(walletTopUp);

    return id;
  }

  @Value
  @Builder
  public static class Input {
    @NonNull String paymentId;
    @NonNull BigDecimal amount;
    long walletId;
  }
}
