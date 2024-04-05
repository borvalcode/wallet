package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.domain.WalletNotFoundException;
import com.playtomic.tests.wallet.domain.command.CreateWallet;
import com.playtomic.tests.wallet.domain.command.TopUpWallet;
import com.playtomic.tests.wallet.domain.query.GetWallet;
import com.playtomic.tests.wallet.domain.query.WalletDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletController {
  private static final Logger log = LoggerFactory.getLogger(WalletController.class);

  private final CreateWallet createWallet;
  private final GetWallet getWallet;
  private final TopUpWallet topUpWallet;

  public WalletController(CreateWallet createWallet, GetWallet getWallet, TopUpWallet topUpWallet) {
    this.createWallet = createWallet;
    this.getWallet = getWallet;
    this.topUpWallet = topUpWallet;
  }

  @RequestMapping("/")
  void log() {
    log.info("Logging from /");
  }

  @GetMapping("/wallets/{walletId}")
  ResponseEntity<WalletDetails> getWallet(@PathVariable Long walletId) {
    log.info("Retrieving wallet {}", walletId);

    try {
      WalletDetails walletDetails = getWallet.execute(walletId);
      return ResponseEntity.ok(walletDetails);
    } catch (WalletNotFoundException ex) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/wallets")
  ResponseEntity<WalletDetails> createWallet() {
    log.info("Creating wallet ");

    long walletId = createWallet.execute();
    return getWallet(walletId);
  }

  @PostMapping("/wallets/{walletId}/top-up")
  ResponseEntity<WalletDetails> topUpWallet(
      @PathVariable Long walletId, @RequestBody TopupWalletRequest request) {
    log.info("Topping up wallet {}", walletId);

    try {
      topUpWallet.execute(
          TopUpWallet.Input.builder()
              .walletId(walletId)
              .creditCardNumber(request.getCreditCardNumber())
              .amount(request.getAmount())
              .build());
      return getWallet(walletId);
    } catch (WalletNotFoundException ex) {
      return ResponseEntity.notFound().build();
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
