package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.domain.command.CreateWallet;
import com.playtomic.tests.wallet.domain.query.GetWallet;
import com.playtomic.tests.wallet.domain.query.WalletDetails;
import com.playtomic.tests.wallet.domain.query.WalletNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletController {
    private final static Logger log = LoggerFactory.getLogger(WalletController.class);

    private final CreateWallet createWallet;
    private final GetWallet getWallet;

    public WalletController(CreateWallet createWallet, GetWallet getWallet) {
        this.createWallet = createWallet;
        this.getWallet = getWallet;
    }

    @RequestMapping("/")
    void log() {
        log.info("Logging from /");
    }

    @GetMapping("/wallets/{walletId}")
    @ExceptionHandler(WalletNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
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
}
