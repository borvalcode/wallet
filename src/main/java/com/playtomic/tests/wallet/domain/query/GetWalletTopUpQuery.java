package com.playtomic.tests.wallet.domain.query;

import com.playtomic.tests.wallet.domain.NotFoundException;
import com.playtomic.tests.wallet.domain.query.dto.WalletTopUpDetails;
import com.playtomic.tests.wallet.domain.query.view.WalletTopUpView;
import org.springframework.stereotype.Service;

@Service
public class GetWalletTopUpQuery {
  private final WalletTopUpView walletTopUpView;

  public GetWalletTopUpQuery(WalletTopUpView walletTopUpView) {
    this.walletTopUpView = walletTopUpView;
  }

  public WalletTopUpDetails execute(long walletId, long topUpId) {
    return walletTopUpView.getWalletTopUp(walletId, topUpId).orElseThrow(NotFoundException::new);
  }
}
