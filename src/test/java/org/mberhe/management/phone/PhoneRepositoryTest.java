package org.mberhe.management.phone;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mberhe.management.common.AbstractTestContainer;
import org.mberhe.management.phone.borrowing.PhoneBorrowing;
import org.mberhe.management.phone.borrowing.PhoneBorrowingRepository;
import org.mberhe.management.phone.dto.PhoneBorrowingProjection;
import org.mberhe.management.user.Role;
import org.mberhe.management.user.User;
import org.mberhe.management.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


class PhoneRepositoryTest extends AbstractTestContainer {

  @Autowired
  private PhoneRepository phoneRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PhoneBorrowingRepository phoneBorrowingRepository;

  Phone newPhone;
  User newUser;
  PhoneBorrowing phoneBorrowing;

  @BeforeEach
  void setUp() {
    newPhone = phoneRepository.save(
      Phone.builder()
        .brand("NewBrand")
        .model("NewModel")
        .assignedId("EnNsw7EReh")
        .build()
    ).block();

    newUser = userRepository.save(
      User.builder()
        .firstName("Abel")
        .lastName("Hagos")
        .email("abel@email.com")
        .phoneNumber("+2519445683")
        .role(Role.TESTER)
        .build()
    ).block();

    phoneBorrowing = phoneBorrowingRepository.save(
      PhoneBorrowing.builder()
        .phoneId(newPhone.getId())
        .testerId(newUser.getId())
        .build()
    ).block();
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void givenId_whenGetPhoneBorrowingProjection_shouldReturnObject() {
    Mono<PhoneBorrowingProjection> projectionMono = phoneRepository.getPhoneBorrowingProjection(newPhone.getId());

    StepVerifier.create(projectionMono)
      .expectNext(
        PhoneBorrowingProjection.builder()
          .available(false)
          .phoneId(newPhone.getId())
          .tester(newUser.getFirstName() + ' ' + newUser.getLastName())
          .build()
      )
      .verifyComplete();
  }
}