package com.playtomic.tests.wallet.infrastructure.query;

import com.playtomic.tests.wallet.domain.command.Wallet;
import com.playtomic.tests.wallet.domain.query.WalletDetails;
import com.playtomic.tests.wallet.domain.query.WalletView;
import com.playtomic.tests.wallet.infrastructure.inmemory.InMemoryStorage;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InMemoryWalletView implements WalletView {
    private final InMemoryStorage<Long, Wallet> walletStorage;

    public InMemoryWalletView(InMemoryStorage<Long, Wallet> walletStorage) {
        this.walletStorage = walletStorage;
    }

    @Override
    public Optional<WalletDetails> get(long walletId) {
        return walletStorage.get(walletId).map(wallet -> new WalletDetails(wallet.getId(), wallet.getAmount()));
    }
}
