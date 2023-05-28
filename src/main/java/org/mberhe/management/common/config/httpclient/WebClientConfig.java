package org.mberhe.management.common.config.httpclient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

  @Autowired
  private WebClientProperties webClientProperties;

  @Bean
  public WebClient.Builder webClientBuilder() {

    ConnectionProvider connProvider = ConnectionProvider
      .builder("webclient-conn-pool")
      .maxConnections(webClientProperties.maxConnection)
      .pendingAcquireTimeout(Duration.ofMillis(webClientProperties.acquireTimeoutMillis))
      .build();

    HttpClient httpClient = HttpClient.create(connProvider)
      .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, webClientProperties.connectionTimeoutMillis)
      .responseTimeout(Duration.ofMillis(webClientProperties.responseTimeoutMillis))
      .doOnConnected(conn ->
        conn
          .addHandlerLast(new ReadTimeoutHandler(webClientProperties.readTimeoutMillis, TimeUnit.MILLISECONDS))
          .addHandlerLast(new WriteTimeoutHandler(webClientProperties.writeTimeoutMillis, TimeUnit.MILLISECONDS))
      );

    return WebClient.builder()
      .clientConnector(new ReactorClientHttpConnector(httpClient));
  }
}