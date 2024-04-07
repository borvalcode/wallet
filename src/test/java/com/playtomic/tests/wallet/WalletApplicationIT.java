package com.playtomic.tests.wallet;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.playtomic.tests.wallet.api.TopupWalletRequest;
import com.playtomic.tests.wallet.domain.query.dto.WalletDetails;
import com.playtomic.tests.wallet.domain.query.dto.WalletTopUpDetails;
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

    WalletTopUpDetails walletTopUpDetails =
        topUpWallet(walletId, new TopupWalletRequest("4242 4242 4242 4242", new BigDecimal(5)))
            .expectStatus()
            .isOk()
            .expectBody(WalletTopUpDetails.class)
            .returnResult()
            .getResponseBody();

    assertNotNull(walletTopUpDetails.getId());
    assertEquals("123", walletTopUpDetails.getPaymentId());
    assertEquals(new BigDecimal(5), walletTopUpDetails.getAmount().stripTrailingZeros());
    assertEquals(walletId, walletTopUpDetails.getWalletId());
  }

  @Test
  void topUpIncreasesBalance() {
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
        .expectBody(WalletTopUpDetails.class);

    BigDecimal balance =
        getWalletDetails(walletId)
            .expectBody(WalletDetails.class)
            .returnResult()
            .getResponseBody()
            .getAmount();

    assertEquals(new BigDecimal(5), balance.stripTrailingZeros());
  }

  @Test
  void getTopUp() {
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

    WalletTopUpDetails walletTopUpDetails =
        topUpWallet(walletId, new TopupWalletRequest("4242 4242 4242 4242", new BigDecimal(5)))
            .expectStatus()
            .isOk()
            .expectBody(WalletTopUpDetails.class)
            .returnResult()
            .getResponseBody();

    getTopUpDetails(walletId, walletTopUpDetails.getId())
        .expectStatus()
        .isOk()
        .expectBody(WalletTopUpDetails.class)
        .isEqualTo(walletTopUpDetails);
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
    stubFor(
        WireMock.post("/v1/stripe-simulator/charges")
            .withHeader("Content-Type", containing("json"))
            .withRequestBody(
                equalToJson(
                    String.format(
                        "{\"credit_card\": \"%s\", \"amount\": %s}",
                        request.getCreditCardNumber(), request.getAmount())))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("{\"id\": \"123\"}")));

    long walletId = createWallet();

    topUpWallet(walletId, request).expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
  }

  private void assertWalletAmountZero(long walletId) {
    BigDecimal amount =
        getWalletDetails(walletId)
            .expectStatus()
            .isOk()
            .expectBody(WalletDetails.class)
            .returnResult()
            .getResponseBody()
            .getAmount();

    assertEquals(BigDecimal.ZERO, amount.stripTrailingZeros());
  }

  private WebTestClient.ResponseSpec getWalletDetails(Object walletId) {
    return webTestClient.get().uri("/wallets/{walletId}", walletId).exchange();
  }

  private WebTestClient.ResponseSpec getTopUpDetails(Object walletId, Object topUpId) {
    return webTestClient
        .get()
        .uri("/wallets/{walletId}/top-ups/{topUpId}", walletId, topUpId)
        .exchange();
  }

  private WebTestClient.ResponseSpec topUpWallet(Object walletId, TopupWalletRequest request) {
    return webTestClient
        .post()
        .uri("/wallets/{walletId}/top-ups", walletId)
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
