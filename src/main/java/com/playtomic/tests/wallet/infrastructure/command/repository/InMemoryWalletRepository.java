package com.playtomic.tests.wallet.infrastructure.command.repository;

import com.playtomic.tests.wallet.domain.command.entity.Wallet;
import com.playtomic.tests.wallet.domain.command.repository.WalletRepository;
import com.playtomic.tests.wallet.infrastructure.inmemory.InMemoryWalletStorage;
import java.util.Optional;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;

@Component("inMemoryWalletRepository")
public class InMemoryWalletRepository implements WalletRepository {
  private static long currentWalletId = 0;

  private final InMemoryWalletStorage walletStorage;

  public InMemoryWalletRepository(InMemoryWalletStorage walletStorage) {
    this.walletStorage = walletStorage;
  }

  @Override
  public long nextId() {
    return ++currentWalletId;
  }

  @Override
  public void store(Wallet wallet) {
    walletStorage.put(wallet.getId(), wallet);
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
