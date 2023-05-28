package org.mberhe.management.integration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "fono.api")
@Data
public class FonoApiProperties {
  public String baseUrl;
  public String applicationId;
  public String applicationKey;
}
