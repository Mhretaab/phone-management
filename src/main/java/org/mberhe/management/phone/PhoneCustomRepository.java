package org.mberhe.management.phone;

import org.mberhe.management.phone.dto.PhoneBorrowingProjection;
import reactor.core.publisher.Mono;

public interface PhoneCustomRepository {
  Mono<PhoneBorrowingProjection> getPhoneBorrowingProjection(Integer phoneId);
  Mono<PhoneBorrowingProjection> getPhoneBorrowingProjection(String phoneAssignedId);
}
