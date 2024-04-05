package com.playtomic.tests.wallet.infrastructure.command;

import com.playtomic.tests.wallet.domain.command.WalletTopUp;
import com.playtomic.tests.wallet.domain.command.WalletTopUpRepository;
import com.playtomic.tests.wallet.infrastructure.inmemory.InMemoryTopUpStorage;
import org.springframework.stereotype.Service;

@Service
public class InMemoryWalletTopUpRepository implements WalletTopUpRepository {
  private static long currentWalletId = 0;

  private final InMemoryTopUpStorage paymentStorage;

  public InMemoryWalletTopUpRepository(InMemoryTopUpStorage paymentStorage) {
    this.paymentStorage = paymentStorage;
  }

  @Override
  public long nextId() {
    return ++currentWalletId;
  }

  @Override
  public void store(WalletTopUp walletTopUp) {
    paymentStorage.put(walletTopUp.getId(), walletTopUp);
  }
}
