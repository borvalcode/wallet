package com.playtomic.tests.wallet.infrastructure.query.view;

import com.playtomic.tests.wallet.domain.query.dto.WalletTopUpDetails;
import com.playtomic.tests.wallet.domain.query.view.WalletTopUpView;
import com.playtomic.tests.wallet.infrastructure.inmemory.InMemoryTopUpStorage;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public final class InMemoryWalletTopUpView implements WalletTopUpView {
  private final InMemoryTopUpStorage topUpStorage;

  public InMemoryWalletTopUpView(InMemoryTopUpStorage topUpStorage) {
    this.topUpStorage = topUpStorage;
  }

  @Override
  public Optional<WalletTopUpDetails> getWalletTopUp(long walletId, long topUpId) {
    return topUpStorage
        .findOne(topUp -> topUp.getWalletId() == walletId && topUp.getId() == topUpId)
        .map(
            topUp ->
                WalletTopUpDetails.builder()
                    .id(topUp.getId())
                    .paymentId(topUp.getPaymentId())
                    .amount(topUp.getAmount())
                    .walletId(topUp.getWalletId())
                    .build());
  }
}
