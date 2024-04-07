package com.playtomic.tests.wallet.domain.command.repository;

import org.springframework.stereotype.Component;

@Component
public interface WalletTopUpRepository {
  long nextId();
}
