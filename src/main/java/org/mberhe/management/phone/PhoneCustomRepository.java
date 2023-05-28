package org.mberhe.management.phone;

import org.mberhe.management.phone.dto.PhoneAvailability;
import reactor.core.publisher.Mono;

public interface PhoneCustomRepository {
  Mono<PhoneAvailability> getPhoneAvailability(Integer phoneId);
  Mono<PhoneAvailability> getPhoneAvailability(String phoneAssignedId);
}
