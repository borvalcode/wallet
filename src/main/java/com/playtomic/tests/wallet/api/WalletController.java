package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.domain.NotFoundException;
import com.playtomic.tests.wallet.domain.command.CreateWalletCommand;
import com.playtomic.tests.wallet.domain.command.TopUpWalletCommand;
import com.playtomic.tests.wallet.domain.query.GetWalletQuery;
import com.playtomic.tests.wallet.domain.query.GetWalletTopUpQuery;
import com.playtomic.tests.wallet.domain.query.dto.WalletDetails;
import com.playtomic.tests.wallet.domain.query.dto.WalletTopUpDetails;
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

  private final CreateWalletCommand createWalletCommand;
  private final GetWalletQuery getWalletQuery;
  private final TopUpWalletCommand topUpWalletCommand;
  private final GetWalletTopUpQuery getWalletTopUpQuery;

  public WalletController(
      CreateWalletCommand createWalletCommand,
      GetWalletQuery getWalletQuery,
      TopUpWalletCommand topUpWalletCommand,
      GetWalletTopUpQuery getWalletTopUpQuery) {
    this.createWalletCommand = createWalletCommand;
    this.getWalletQuery = getWalletQuery;
    this.topUpWalletCommand = topUpWalletCommand;
    this.getWalletTopUpQuery = getWalletTopUpQuery;
  }

  @RequestMapping("/")
  void log() {
    log.info("Logging from /");
  }

  @GetMapping("/wallets/{walletId}")
  ResponseEntity<WalletDetails> getWallet(@PathVariable Long walletId) {
    log.info("Retrieving wallet {}", walletId);

    try {
      WalletDetails walletDetails = getWalletQuery.execute(walletId);
      return ResponseEntity.ok(walletDetails);
    } catch (NotFoundException ex) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/wallets")
  ResponseEntity<WalletDetails> createWallet() {
    log.info("Creating wallet ");

    long walletId = createWalletCommand.execute();
    return getWallet(walletId);
  }

  @PostMapping("/wallets/{walletId}/top-ups")
  ResponseEntity<WalletTopUpDetails> createTopUp(
      @PathVariable Long walletId, @RequestBody TopupWalletRequest request) {
    log.info("Topping up wallet {}", walletId);

    try {
      TopUpWalletCommand.Result result =
          topUpWalletCommand.execute(
              TopUpWalletCommand.Input.builder()
                  .walletId(walletId)
                  .creditCardNumber(request.getCreditCardNumber())
                  .amount(request.getAmount())
                  .build());

      return getTopUp(walletId, result.getTopUpId());
    } catch (NotFoundException ex) {
      return ResponseEntity.notFound().build();
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping("/wallets/{walletId}/top-ups/{topUpId}")
  ResponseEntity<WalletTopUpDetails> getTopUp(
      @PathVariable Long walletId, @PathVariable Long topUpId) {
    log.info("Retrieving wallet {}", walletId);

    try {
      WalletTopUpDetails walletTopUpDetails = getWalletTopUpQuery.execute(walletId, topUpId);
      return ResponseEntity.ok(walletTopUpDetails);
    } catch (NotFoundException ex) {
      return ResponseEntity.notFound().build();
    }
  }
}
