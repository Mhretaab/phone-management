package org.mberhe.management.integration;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface FonoApiClient {
  Mono<FonoDeviceDescription> getDeviceDescription(Map<String, Object> queryParams);
}
