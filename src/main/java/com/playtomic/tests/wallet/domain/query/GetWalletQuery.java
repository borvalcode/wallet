package com.playtomic.tests.wallet.domain.query;

import com.playtomic.tests.wallet.domain.NotFoundException;
import com.playtomic.tests.wallet.domain.query.dto.WalletDetails;
import com.playtomic.tests.wallet.domain.query.view.WalletView;
import org.springframework.stereotype.Service;

@Service
public class GetWalletQuery {
  private final WalletView walletView;

  public GetWalletQuery(WalletView walletView) {
    this.walletView = walletView;
  }

  public WalletDetails execute(long walletId) {
    return walletView.get(walletId).orElseThrow(NotFoundException::new);
  }
}
