package com.playtomic.tests.wallet.infrastructure.query.view;

import com.playtomic.tests.wallet.domain.query.dto.WalletTopUpDetails;
import com.playtomic.tests.wallet.domain.query.view.WalletTopUpView;
import com.playtomic.tests.wallet.infrastructure.inmemory.InMemoryWalletStorage;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component("inMemoryWalletTopUpView")
public final class InMemoryWalletTopUpView implements WalletTopUpView {
  private final InMemoryWalletStorage walletStorage;

  public InMemoryWalletTopUpView(InMemoryWalletStorage walletStorage) {
    this.walletStorage = walletStorage;
  }

  @Override
  public Optional<WalletTopUpDetails> get(long walletId, long topUpId) {
    return walletStorage
        .get(walletId)
        .flatMap(
            wallet ->
                wallet.getWalletTopUps().stream()
                    .filter(walletTopUp -> walletTopUp.getId() == topUpId)
                    .findFirst())
        .map(
            topUp ->
                WalletTopUpDetails.builder()
                    .id(topUp.getId())
                    .paymentId(topUp.getPaymentId())
                    .amount(topUp.getAmount())
                    .walletId(walletId)
                    .build());
  }
}
