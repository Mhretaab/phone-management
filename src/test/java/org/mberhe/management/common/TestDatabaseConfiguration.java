package org.mberhe.management.common;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;

@TestConfiguration
public class TestDatabaseConfiguration {

  @Bean
  public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
    ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
    initializer.setConnectionFactory(connectionFactory);

    return initializer;
  }
}