package com.playtomic.tests.wallet.infrastructure.inmemory;

import com.playtomic.tests.wallet.domain.command.Wallet;
import org.springframework.stereotype.Component;

@Component
public class InMemoryWalletStorage extends InMemoryStorage<Long, Wallet> {
  public InMemoryWalletStorage() {
    super(wallet -> new Wallet(wallet.getId()));
  }
}
