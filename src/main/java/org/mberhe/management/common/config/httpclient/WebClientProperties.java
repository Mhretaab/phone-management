package org.mberhe.management.common.config.httpclient;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "web.client")
@Data
public class WebClientProperties {
  public int maxConnection;
  public long acquireTimeoutMillis;
  public int connectionTimeoutMillis;
  public long responseTimeoutMillis;
  public int readTimeoutMillis;
  public int writeTimeoutMillis;
}