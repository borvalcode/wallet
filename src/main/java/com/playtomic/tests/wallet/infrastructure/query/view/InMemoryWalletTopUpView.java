package com.playtomic.tests.wallet.infrastructure.query.view;

import com.playtomic.tests.wallet.domain.query.dto.WalletTopUpDetails;
import com.playtomic.tests.wallet.domain.query.view.WalletTopUpView;
import com.playtomic.tests.wallet.infrastructure.inmemory.InMemoryTopUpStorage;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component("inMemoryWalletTopUpView")
public final class InMemoryWalletTopUpView implements WalletTopUpView {
  private final InMemoryTopUpStorage topUpStorage;

  public InMemoryWalletTopUpView(InMemoryTopUpStorage topUpStorage) {
    this.topUpStorage = topUpStorage;
  }

  @Override
  public Optional<WalletTopUpDetails> get(long walletId, long topUpId) {
    // TODO: FIX
    return topUpStorage
        .findOne(topUp -> topUp.getId() == topUpId)
        .map(
            topUp ->
                WalletTopUpDetails.builder()
                    .id(topUp.getId())
                    .paymentId(topUp.getPaymentId())
                    .amount(topUp.getAmount())
                    .build());
  }
}
