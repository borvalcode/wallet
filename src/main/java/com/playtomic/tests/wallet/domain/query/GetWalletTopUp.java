package com.playtomic.tests.wallet.domain.query;

import com.playtomic.tests.wallet.domain.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GetWalletTopUp {
  private final WalletTopUpView walletTopUpView;

  public GetWalletTopUp(WalletTopUpView walletTopUpView) {
    this.walletTopUpView = walletTopUpView;
  }

  public WalletTopUpDetails execute(long walletId, long topUpId) {
    return walletTopUpView.getWalletTopUp(walletId, topUpId).orElseThrow(NotFoundException::new);
  }
}
