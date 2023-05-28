package org.mberhe.management.common.config.persistence;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(basePackages = "com.mhretberhe.demo")
@EnableConfigurationProperties({R2dbcProperties.class, FlywayProperties.class})
@EnableR2dbcAuditing
public class PersistenceConfig {
  @Bean(initMethod = "migrate")
  public Flyway flyway(FlywayProperties flywayProperties, R2dbcProperties r2dbcProperties) {
    return Flyway.configure()
      .dataSource(
        flywayProperties.getUrl(),
        r2dbcProperties.getUsername(),
        r2dbcProperties.getPassword()
      )
      .locations(flywayProperties.getLocations()
        .stream()
        .toArray(String[]::new))
      .baselineOnMigrate(true)
      .load();
  }
}