package com.playtomic.tests.wallet.domain.query;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletView {

    Optional<WalletDetails> get(long walletId);
}
