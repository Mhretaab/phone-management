package org.mberhe.management.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FonoApiClientImpl implements FonoApiClient {

  public static final String DEVICE_MODEL_FIELD_KEY = "model";
  public static final String FONO_URI = "/classes/MobileDevice";
  private final WebClient webClient;
  private final FonoApiProperties fonoApiProperties;
  private final ObjectMapper objectMapper;

  @Autowired
  public FonoApiClientImpl(final WebClient.Builder WebClientBuilder, final FonoApiProperties fonoApiProperties,
                           final ObjectMapper objectMapper) {

    this.fonoApiProperties = fonoApiProperties;

    DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
    factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

    webClient = WebClientBuilder
      .uriBuilderFactory(factory)
      .baseUrl(this.fonoApiProperties.getBaseUrl())
      .defaultHeaders(httpHeaders -> httpHeaders.addAll(getHttpHeaders()))
      .build();

    this.objectMapper = objectMapper;
  }

  @NotNull
  private HttpHeaders getHttpHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("X-Parse-Application-Id", this.fonoApiProperties.getApplicationId());
    headers.set("X-Parse-Rest-Api-Key", this.fonoApiProperties.getApplicationKey());
    return headers;
  }

  @Override
  public Mono<FonoDeviceDescription> getDeviceDescription(final Map<String, Object> queryParams) {

    return webClient.get()
      .uri(
        UriComponentsBuilder.fromUriString(fonoApiProperties.getBaseUrl() + FONO_URI)
          .queryParam("where", "{value}")
          .encode().build(constructParamValues(queryParams)).toString()
      )
      .retrieve()
      .bodyToMono(JsonNode.class)
      .log()
      .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))
      .flatMapMany(jsonNode -> Flux.fromIterable(() -> jsonNode.get("results").elements()))
      .next()
      .map(jsonNode -> {
        try {
          return objectMapper.treeToValue(jsonNode, FonoDeviceDescription.class);
        } catch (JsonProcessingException e) {
          throw new RuntimeException("Failed to convert JSON node to DeviceDescription object", e);
        }
      })
      .onErrorResume(err -> Mono.empty());
  }

  private String constructParamValues(final Map<String, Object> queryParams) {
    final String keyValueFormat = "\"%s\":\"%s\"";

    String value = queryParams.entrySet().stream()
      .map(entry -> String.format(keyValueFormat, entry.getKey(), entry.getValue()))
      .collect(Collectors.joining(","));

    return "{" + value + "}";
  }
}
