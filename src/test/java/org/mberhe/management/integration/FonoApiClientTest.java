package org.mberhe.management.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mberhe.management.phone.Phone;
import org.mberhe.management.phone.PhoneServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class FonoApiClientTest {

  static MockWebServer mockWebServer;
  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private FonoApiClientImpl fonoApiClient;

  @BeforeAll
  static void initialize() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start(8088);
  }

  @AfterAll
  static void cleanUp() throws IOException {
    mockWebServer.shutdown();
  }

  @BeforeEach
  void setUp() {

  }

  @Test
  void getDeviceDescription() {
    // Given
    mockWebServer.enqueue(
      new MockResponse()
        .setBody(getServerResponse())
        .addHeader("Content-Type", "application/json")
    );

    // When
    Mono<FonoDeviceDescription> deviceDescriptionMono =
      fonoApiClient.getDeviceDescription(PhoneServiceImpl.constructQueryParam(
        Phone.builder()
          .brand("Samsung")
          .model("Galaxy S9")
          .build())
      );

    // Then
    StepVerifier.create(deviceDescriptionMono)
      .expectNext(getFonoDeviceDescription())
      .verifyComplete();
  }

  private static FonoDeviceDescription getFonoDeviceDescription() {
    return new FonoDeviceDescription(
      "Samsung",
      "Galaxy S9",
      "GSM 850 / 900 / 1800 / 1900 - SIM 1 & SIM 2 (dual-SIM model only)",
      "GSM / HSPA / LTE",
      "HSDPA 850 / 900 / 1700(AWS) / 1900 / 2100",
      "LTE band 1(2100), 2(1900), 3(1800), 4(1700/2100), 5(850), 7(2600), 8(900), 12(700), 13(700), 17(700), 18(800), 19(800), 20(800), 25(1900), 26(850), 28(700), 32(1500), 38(2600), 39(1900), 40(2300), 41(2500), 66(1700/2100)"
    );
  }

  private String getServerResponse() {
    try {
      return objectMapper.writeValueAsString(new ServerResponse());
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  static class ServerResponse {
    List<FonoDeviceDescription> results = List.of(getFonoDeviceDescription());

    public List<FonoDeviceDescription> getResults() {
      return results;
    }
  }
}
