package com.playtomic.tests.wallet.domain.command;

import com.playtomic.tests.wallet.domain.command.entity.WalletTopUp;
import com.playtomic.tests.wallet.domain.command.repository.WalletTopUpRepository;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
public final class CreateWalletTopUpCommand {
  private final WalletTopUpRepository walletTopUpRepository;

  public CreateWalletTopUpCommand(WalletTopUpRepository walletTopUpRepository) {
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
