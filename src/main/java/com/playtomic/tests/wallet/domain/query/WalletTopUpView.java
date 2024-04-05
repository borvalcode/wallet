package com.playtomic.tests.wallet.domain.query;

import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public interface WalletTopUpView {
  Optional<WalletTopUpDetails> getWalletTopUp(long walletId, long topUpId);
}
