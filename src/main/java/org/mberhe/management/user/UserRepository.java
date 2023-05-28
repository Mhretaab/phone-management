package org.mberhe.management.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Integer>, ReactiveSortingRepository<User, Integer> {
  Flux<User> findAllBy(Pageable pageable);

  Flux<User> findByFirstNameIgnoreCaseLikeOrLastNameIgnoreCaseLike(String firstName, String lastName, Pageable pageable);
  Mono<Long> countByFirstNameIgnoreCaseLikeOrLastNameIgnoreCaseLike(String firstName, String lastName);

  Mono<User> findByEmailIgnoreCaseOrPhoneNumberLike(String email, String phoneNumber);

  Mono<User> findByEmail(String email);

  Mono<User> findByPhoneNumber(String phoneNumber);
}