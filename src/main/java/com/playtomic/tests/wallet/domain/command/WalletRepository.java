package com.playtomic.tests.wallet.domain.command;

import java.util.Optional;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;

@Component
public interface WalletRepository {

  long nextId();

  void store(Wallet wallet);

  Optional<Boolean> update(long walletId, Consumer<Wallet> updating);
}
