package com.playtomic.tests.wallet.domain.command.repository;

import com.playtomic.tests.wallet.domain.command.entity.WalletTopUp;
import org.springframework.stereotype.Component;

@Component
public interface WalletTopUpRepository {
  long nextId();

  void store(WalletTopUp walletTopUp);
}
