package com.playtomic.tests.wallet;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.playtomic.tests.wallet.api.TopupWalletRequest;
import com.playtomic.tests.wallet.domain.query.WalletDetails;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@WireMockTest(httpPort = 9999)
public class WalletApplicationIT {

  @Autowired private WebTestClient webTestClient;

  @Test
  public void newWallet() {
    long walletId = createWallet();

    assertWalletAmountZero(walletId);
  }

  @Test
  void walletNotFound() {
    getWalletDetails(123).expectStatus().isNotFound();
  }

  @Test
  void invalidWalletId() {
    getWalletDetails("FOO").expectStatus().isBadRequest();
  }

  @Test
  void topUpOk() {
    stubFor(
        WireMock.post("/v1/stripe-simulator/charges")
            .withHeader("Content-Type", containing("json"))
            .withRequestBody(
                equalToJson("{\"credit_card\": \"4242 4242 4242 4242\", \"amount\": 5}"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("{\"id\": \"123\"}")));

    long walletId = createWallet();

    topUpWallet(walletId, new TopupWalletRequest("4242 4242 4242 4242", new BigDecimal(5)))
        .expectStatus()
        .isOk()
        .expectBody(WalletDetails.class)
        .isEqualTo(new WalletDetails(walletId, new BigDecimal(5)));
  }

  @Test
  void topUpErrorAmountTooSmall() {
    stubFor(
        WireMock.post("/v1/stripe-simulator/charges")
            .withHeader("Content-Type", containing("json"))
            .withRequestBody(
                equalToJson("{\"credit_card\": \"4242 4242 4242 4242\", \"amount\": 5}"))
            .willReturn(aResponse().withStatus(422)));

    long walletId = createWallet();

    topUpWallet(walletId, new TopupWalletRequest("4242 4242 4242 4242", new BigDecimal(5)))
        .expectStatus()
        .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

    assertWalletAmountZero(walletId);
  }

  @Test
  void topUpErrorUnknown() {
    stubFor(
        WireMock.post("/v1/stripe-simulator/charges")
            .withHeader("Content-Type", containing("json"))
            .withRequestBody(
                equalToJson("{\"credit_card\": \"4242 4242 4242 4242\", \"amount\": 5}"))
            .willReturn(aResponse().withStatus(500)));

    long walletId = createWallet();

    topUpWallet(walletId, new TopupWalletRequest("4242 4242 4242 4242", new BigDecimal(5)))
        .expectStatus()
        .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

    assertWalletAmountZero(walletId);
  }

  @Test
  void topUpUnknownWalletId() {
    topUpWallet(123, new TopupWalletRequest("4242 4242 4242 4242", new BigDecimal(5)))
        .expectStatus()
        .isNotFound();
  }

  @Test
  void topUpInvalidWalletId() {
    topUpWallet("FOO", new TopupWalletRequest("4242 4242 4242 4242", new BigDecimal(5)))
        .expectStatus()
        .isBadRequest();
  }

  @ParameterizedTest
  @MethodSource("provideInvalidTopUpRequests")
  void topUpInvalidRequest(TopupWalletRequest request) {
    long walletId = createWallet();

    topUpWallet(walletId, request).expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
  }

  private void assertWalletAmountZero(long walletId) {
    getWalletDetails(walletId)
        .expectStatus()
        .isOk()
        .expectBody(WalletDetails.class)
        .isEqualTo(new WalletDetails(walletId, BigDecimal.ZERO));
  }

  private WebTestClient.ResponseSpec getWalletDetails(Object walletId) {
    return webTestClient.get().uri("/wallets/{walletId}", walletId).exchange();
  }

  private WebTestClient.ResponseSpec topUpWallet(Object walletId, TopupWalletRequest request) {
    return webTestClient
        .post()
        .uri("/wallets/{walletId}/top-up", walletId)
        .bodyValue(request)
        .exchange();
  }

  private Long createWallet() {
    return webTestClient
        .post()
        .uri("/wallets")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(WalletDetails.class)
        .returnResult()
        .getResponseBody()
        .getId();
  }

  private static List<TopupWalletRequest> provideInvalidTopUpRequests() {
    return Arrays.asList(
        new TopupWalletRequest("BAR", new BigDecimal(5)),
        new TopupWalletRequest("4242 4242 4242 4242", new BigDecimal(0)),
        new TopupWalletRequest("4242 4242 4242 4242", new BigDecimal(-1)));
  }
}
