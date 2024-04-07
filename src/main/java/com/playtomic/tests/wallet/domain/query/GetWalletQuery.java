package com.playtomic.tests.wallet.domain.query;

import com.playtomic.tests.wallet.domain.exception.NotFoundException;
import com.playtomic.tests.wallet.domain.query.dto.WalletDetails;
import com.playtomic.tests.wallet.domain.query.view.WalletView;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GetWalletQuery {
  private final WalletView walletView;

  public GetWalletQuery(@Qualifier("databaseWalletView") WalletView walletView) {
    this.walletView = walletView;
  }

  public WalletDetails execute(long walletId) {
    return walletView.get(walletId).orElseThrow(NotFoundException::new);
  }
}
