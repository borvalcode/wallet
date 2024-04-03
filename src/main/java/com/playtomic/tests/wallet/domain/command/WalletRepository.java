package com.playtomic.tests.wallet.domain.command;

import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository {

    long nextId();

    void store(Wallet wallet);

}
