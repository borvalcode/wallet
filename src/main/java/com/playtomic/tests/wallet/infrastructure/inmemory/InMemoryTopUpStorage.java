package com.playtomic.tests.wallet.infrastructure.inmemory;

import com.playtomic.tests.wallet.domain.command.entity.WalletTopUp;
import org.springframework.stereotype.Component;

@Component
public class InMemoryTopUpStorage extends InMemoryStorage<Long, WalletTopUp> {
  public InMemoryTopUpStorage() {
    super(
        walletTopUp ->
            new WalletTopUp(
                walletTopUp.getId(), walletTopUp.getPaymentId(), walletTopUp.getAmount()));
  }
}
