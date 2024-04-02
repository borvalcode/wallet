package com.playtomic.tests.wallet;

import com.playtomic.tests.wallet.api.dto.Wallet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class WalletApplicationIT {

	@Autowired
	private WebTestClient webTestClient;


	@Test
	public void okTest() {
		String walletId = "123";

		webTestClient.get()
				.uri("/wallets/{walletId}", walletId)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Wallet.class)
				.isEqualTo(new Wallet(walletId));
	}
}
