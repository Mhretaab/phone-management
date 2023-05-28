package org.mberhe.management.common;

import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.flywaydb.core.Flyway;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@TestConfiguration
public class TestConfig {
  @Bean
  @Profile("test")
  public ConnectionFactory connectionFactory() {
    return H2ConnectionFactory.inMemory("testdb");
  }

  @Bean(initMethod = "migrate")
  @Profile("test")
  public Flyway flyway() {
    return new Flyway(Flyway.configure()
      .baselineOnMigrate(true)
      .dataSource(
        "jdbc:h2:mem:testdb",
        "sa",
        "")
    );
  }
}
