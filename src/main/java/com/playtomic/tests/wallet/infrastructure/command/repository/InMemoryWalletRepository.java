package com.playtomic.tests.wallet.infrastructure.command.repository;

import com.playtomic.tests.wallet.domain.command.entity.Wallet;
import com.playtomic.tests.wallet.domain.command.repository.WalletRepository;
import com.playtomic.tests.wallet.infrastructure.inmemory.InMemoryTopUpStorage;
import com.playtomic.tests.wallet.infrastructure.inmemory.InMemoryWalletStorage;
import java.util.Optional;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;

@Component("inMemoryWalletRepository")
public class InMemoryWalletRepository implements WalletRepository {
  private static long currentWalletId = 0;

  private final InMemoryWalletStorage walletStorage;
  private final InMemoryTopUpStorage topUpStorage;

  public InMemoryWalletRepository(
      InMemoryWalletStorage walletStorage, InMemoryTopUpStorage topUpStorage) {
    this.walletStorage = walletStorage;
    this.topUpStorage = topUpStorage;
  }

  @Override
  public long nextId() {
    return ++currentWalletId;
  }

  @Override
  public void store(Wallet wallet) {
    walletStorage.put(wallet.getId(), wallet);

    wallet
        .getNewWalletTopUps()
        .forEach(walletTopUp -> topUpStorage.put(walletTopUp.getId(), walletTopUp));
  }

  @Override
  public Optional<Boolean> update(long walletId, Consumer<Wallet> updating) {
    return walletStorage
        .get(walletId)
        .map(
            wallet -> {
              updating.accept(wallet);

              store(wallet);

              return true;
            });
  }
}
