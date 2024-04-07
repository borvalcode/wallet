package com.playtomic.tests.wallet.infrastructure.command.repository;

import com.playtomic.tests.wallet.domain.command.repository.WalletTopUpRepository;
import org.springframework.stereotype.Component;

@Component("inMemoryWalletTopUpRepository")
public class InMemoryWalletTopUpRepository implements WalletTopUpRepository {
  private static long currentWalletId = 0;

  @Override
  public long nextId() {
    return ++currentWalletId;
  }
}
