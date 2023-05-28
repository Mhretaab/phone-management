package org.mberhe.management.phone;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PhoneRepository
  extends ReactiveCrudRepository<Phone, Integer>, ReactiveSortingRepository<Phone, Integer>, PhoneCustomRepository {
  Flux<Phone> findAllBy(Pageable pageable);

  Mono<Phone> findByAssignedId(String assignedId);
}