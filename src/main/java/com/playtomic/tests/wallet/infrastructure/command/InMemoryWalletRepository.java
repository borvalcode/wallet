package com.playtomic.tests.wallet.infrastructure.command;

import com.playtomic.tests.wallet.domain.command.Wallet;
import com.playtomic.tests.wallet.domain.command.WalletRepository;
import com.playtomic.tests.wallet.infrastructure.inmemory.InMemoryStorage;
import java.util.Optional;
import java.util.function.Consumer;
import org.springframework.stereotype.Service;

@Service
public class InMemoryWalletRepository implements WalletRepository {
  private static long currentWalletId = 0;

  private final InMemoryStorage<Long, Wallet> walletStorage;

  public InMemoryWalletRepository(InMemoryStorage<Long, Wallet> walletStorage) {
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
