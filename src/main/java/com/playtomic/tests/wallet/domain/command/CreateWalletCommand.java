package com.playtomic.tests.wallet.domain.command;

import com.playtomic.tests.wallet.domain.command.entity.Wallet;
import com.playtomic.tests.wallet.domain.command.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public final class CreateWalletCommand {
  private final WalletRepository walletRepository;

  public CreateWalletCommand(
      @Qualifier("databaseWalletRepository") WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

  public long execute() {
    long walletId = walletRepository.nextId();

    Wallet wallet = new Wallet(walletId);

    walletRepository.store(wallet);

    return walletId;
  }
}
