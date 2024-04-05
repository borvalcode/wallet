package com.playtomic.tests.wallet.domain.command;

import org.springframework.stereotype.Service;

@Service
public final class CreateWallet {
  private final WalletRepository walletRepository;

  public CreateWallet(WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

  public long execute() {
    long walletId = walletRepository.nextId();

    Wallet wallet = new Wallet(walletId);

    walletRepository.store(wallet);

    return walletId;
  }
}
