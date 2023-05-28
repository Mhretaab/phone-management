package org.mberhe.management.integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FonoDeviceDescription(
  String brand,
  String model,
  String technology,
  String twoGBands,
  String threeGBands,
  String fourGBands
) {
}

