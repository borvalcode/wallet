package com.playtomic.tests.wallet.domain.query;

import com.playtomic.tests.wallet.domain.exception.NotFoundException;
import com.playtomic.tests.wallet.domain.query.dto.WalletTopUpDetails;
import com.playtomic.tests.wallet.domain.query.view.WalletTopUpView;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GetWalletTopUpQuery {
  private final WalletTopUpView walletTopUpView;

  public GetWalletTopUpQuery(
      @Qualifier("databaseWalletTopUpView") WalletTopUpView walletTopUpView) {
    this.walletTopUpView = walletTopUpView;
  }

  public WalletTopUpDetails execute(long walletId, long topUpId) {
    return walletTopUpView.get(walletId, topUpId).orElseThrow(NotFoundException::new);
  }
}
