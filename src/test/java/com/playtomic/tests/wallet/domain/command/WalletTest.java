package com.playtomic.tests.wallet.domain.command;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    @Test
    void newWallet() {
        Wallet wallet = new Wallet(1);

        assertEquals(BigDecimal.ZERO, wallet.getAmount());
    }

    @Test
    void topUpAWallet() {
        Wallet wallet = new Wallet(1);

        wallet.topUp(new BigDecimal("1.5"));

        assertEquals(new BigDecimal("1.5"), wallet.getAmount());
    }

    @Test
    void topUpWalletAccumulation() {
        Wallet wallet = new Wallet(1);

        wallet.topUp(new BigDecimal("1.5"));
        wallet.topUp(new BigDecimal("2.2"));

        assertEquals(new BigDecimal("3.7"), wallet.getAmount());
    }

    @Test
    void topUpErrorIfNullParam() {
        Wallet wallet = new Wallet(1);

        BigDecimal amount = wallet.getAmount();

        assertThrows(NullPointerException.class, () -> wallet.topUp(null));
        assertEquals(amount, wallet.getAmount());
    }

    @Test
    void topUpErrorIfZeroAmount() {
        Wallet wallet = new Wallet(1);

        BigDecimal amount = wallet.getAmount();

        assertThrows(IllegalArgumentException.class, () -> wallet.topUp(BigDecimal.ZERO));
        assertEquals(amount, wallet.getAmount());
    }

    @Test
    void topUpErrorIfNegativeAmount() {
        Wallet wallet = new Wallet(1);

        BigDecimal amount = wallet.getAmount();

        assertThrows(IllegalArgumentException.class, () -> wallet.topUp(new BigDecimal("-1")));
        assertEquals(amount, wallet.getAmount());
    }

    @Test
    void deductAWallet() {
        Wallet wallet = new Wallet(1);

        wallet.deduct(new BigDecimal("1.5"));

        assertEquals(new BigDecimal("-1.5"), wallet.getAmount());
    }

    @Test
    void deductWalletAccumulation() {
        Wallet wallet = new Wallet(1);

        wallet.deduct(new BigDecimal("1.5"));
        wallet.deduct(new BigDecimal("2.2"));

        assertEquals(new BigDecimal("-3.7"), wallet.getAmount());
    }

    @Test
    void deductErrorIfNullParam() {
        Wallet wallet = new Wallet(1);

        BigDecimal amount = wallet.getAmount();

        assertThrows(NullPointerException.class, () -> wallet.deduct(null));
        assertEquals(amount, wallet.getAmount());
    }

    @Test
    void deductErrorIfZeroAmount() {
        Wallet wallet = new Wallet(1);

        BigDecimal amount = wallet.getAmount();

        assertThrows(IllegalArgumentException.class, () -> wallet.deduct(BigDecimal.ZERO));
        assertEquals(amount, wallet.getAmount());
    }

    @Test
    void deductErrorIfNegativeAmount() {
        Wallet wallet = new Wallet(1);

        BigDecimal amount = wallet.getAmount();

        assertThrows(IllegalArgumentException.class, () -> wallet.deduct(new BigDecimal("-1")));
        assertEquals(amount, wallet.getAmount());
    }
}