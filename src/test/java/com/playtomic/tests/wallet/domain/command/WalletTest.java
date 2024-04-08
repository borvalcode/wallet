package com.playtomic.tests.wallet.domain.command;

import static org.junit.jupiter.api.Assertions.*;

import com.playtomic.tests.wallet.domain.command.entity.Wallet;
import com.playtomic.tests.wallet.domain.command.entity.WalletTopUp;
import com.playtomic.tests.wallet.domain.exception.ValidationException;
import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class WalletTest {

  @Test
  void newWallet() {
    Wallet wallet = new Wallet(1);

    assertEquals(BigDecimal.ZERO, wallet.getAmount());
  }

  @Test
  void topUpAWalletIncreasesAmount() {
    Wallet wallet = new Wallet(1);

    wallet.topUp(new WalletTopUp(1, "FOO", new BigDecimal("1.5")));

    assertEquals(new BigDecimal("1.5"), wallet.getAmount());
  }

  @Test
  void topUpWalletAmountAccumulation() {
    Wallet wallet = new Wallet(1);

    wallet.topUp(new WalletTopUp(1, "FOO", new BigDecimal("1.5")));
    wallet.topUp(new WalletTopUp(2, "FOO2", new BigDecimal("2.2")));

    assertEquals(new BigDecimal("3.7"), wallet.getAmount());
  }

  @Test
  void topUpSavedInAList() {
    Wallet wallet = new Wallet(1);

    WalletTopUp walletTopUp1 = new WalletTopUp(1, "FOO", new BigDecimal("1.5"));
    wallet.topUp(walletTopUp1);

    WalletTopUp walletTopUp2 = new WalletTopUp(2, "FOO2", new BigDecimal("2.2"));
    wallet.topUp(walletTopUp2);

    assertEquals(wallet.getWalletTopUps(), Arrays.asList(walletTopUp1, walletTopUp2));
  }

  @Test
  void cantAddTwoTopUpsWithSameId() {
    Wallet wallet = new Wallet(1);

    WalletTopUp walletTopUp1 = new WalletTopUp(1, "FOO", new BigDecimal("1.5"));
    wallet.topUp(walletTopUp1);

    WalletTopUp walletTopUp2 = new WalletTopUp(1, "FOO2", new BigDecimal("2.2"));

    assertThrows(RuntimeException.class, () -> wallet.topUp(walletTopUp2));
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

    assertThrows(
        ValidationException.class, () -> wallet.topUp(new WalletTopUp(1, "FOO", BigDecimal.ZERO)));
    assertEquals(amount, wallet.getAmount());
  }

  @Test
  void topUpErrorIfNegativeAmount() {
    Wallet wallet = new Wallet(1);

    BigDecimal amount = wallet.getAmount();

    assertThrows(
        ValidationException.class,
        () -> wallet.topUp(new WalletTopUp(1, "FOO", new BigDecimal("-1.5"))));
    assertEquals(amount, wallet.getAmount());
  }
}
