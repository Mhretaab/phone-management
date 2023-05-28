package org.mberhe.management.common;


import org.junit.jupiter.api.AfterAll;
import org.testcontainers.containers.MySQLContainer;

@RepositoryTest
public abstract class AbstractTestContainer {

  private static MySQLContainer mySQLContainer;

  static {
    mySQLContainer = new MySQLContainer<>("mysql:8.0.33");
    mySQLContainer
      .withUsername("root")
      .withPassword("password")
      .withExposedPorts(3306, 3308)
      //.withReuse(true)
      .start();

    //System.setProperty("spring.r2dbc.url", "r2dbc:tc:mysql:///testDb?TC_IMAGE_TAG=8.0.33");
    System.setProperty("spring.r2dbc.url", mySQLContainer.getJdbcUrl().replace("jdbc", "r2dbc"));
    System.setProperty("spring.r2dbc.username", mySQLContainer.getUsername());
    System.setProperty("spring.r2dbc.password", mySQLContainer.getPassword());
    System.setProperty("spring.flyway.url", mySQLContainer.getJdbcUrl());
    System.setProperty("spring.flyway.user", mySQLContainer.getUsername());
    System.setProperty("spring.flyway.password", mySQLContainer.getPassword());
  }

  @AfterAll
  public static void cleanup() {
    mySQLContainer.stop();
  }
}