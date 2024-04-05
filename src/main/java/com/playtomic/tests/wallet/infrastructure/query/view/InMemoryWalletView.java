package com.playtomic.tests.wallet.infrastructure.query.view;

import com.playtomic.tests.wallet.domain.query.dto.WalletDetails;
import com.playtomic.tests.wallet.domain.query.view.WalletView;
import com.playtomic.tests.wallet.infrastructure.inmemory.InMemoryWalletStorage;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class InMemoryWalletView implements WalletView {
  private final InMemoryWalletStorage walletStorage;

  public InMemoryWalletView(InMemoryWalletStorage walletStorage) {
    this.walletStorage = walletStorage;
  }

  @Override
  public Optional<WalletDetails> get(long walletId) {
    return walletStorage
        .get(walletId)
        .map(wallet -> new WalletDetails(wallet.getId(), wallet.getAmount()));
  }
}
