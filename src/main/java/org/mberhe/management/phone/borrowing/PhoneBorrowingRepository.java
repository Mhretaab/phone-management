package org.mberhe.management.phone.borrowing;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PhoneBorrowingRepository
  extends ReactiveCrudRepository<PhoneBorrowing, Integer>, ReactiveSortingRepository<PhoneBorrowing, Integer> {

  Mono<PhoneBorrowing> findByPhoneIdAndReturnedDateIsNull(Integer phoneId);

  Mono<Boolean> existsByPhoneIdAndReturnedDateIsNull(Integer phoneId);
}
