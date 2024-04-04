package com.playtomic.tests.wallet.domain.command;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.function.Consumer;

@Component
public interface WalletRepository {

    long nextId();

    void store(Wallet wallet);

    Optional<Boolean> update(long walletId, Consumer<Wallet> updating);

}
