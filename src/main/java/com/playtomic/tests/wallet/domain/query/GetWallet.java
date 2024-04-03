package com.playtomic.tests.wallet.domain.query;

import org.springframework.stereotype.Service;

@Service
public class GetWallet {
    private final WalletView walletView;

    public GetWallet(WalletView walletView) {
        this.walletView = walletView;
    }

    public WalletDetails execute(long walletId) {
        return walletView.get(walletId).orElseThrow(WalletNotFoundException::new);
    }
}
