package com.playtomic.tests.wallet.domain.command;

import org.springframework.stereotype.Component;

@Component
public interface WalletTopUpRepository {

  long nextId();

  void store(WalletTopUp walletTopUp);
}
