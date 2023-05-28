package org.mberhe.management.phone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mberhe.management.common.config.web.CustomPagination;
import org.mberhe.management.common.exceptions.EntityAlreadyExistsException;
import org.mberhe.management.common.exceptions.EntityNotFoundException;
import org.mberhe.management.integration.FonoApiClient;
import org.mberhe.management.integration.FonoDeviceDescription;
import org.mberhe.management.phone.dto.DeviceDetail;
import org.mberhe.management.phone.dto.PhoneAvailability;
import org.mberhe.management.phone.dto.PhoneDTO;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PhoneServiceTest {

  @Mock
  private PhoneRepository phoneRepository;
  @Mock
  private FonoApiClient fonoApiClient;
  @InjectMocks
  private PhoneServiceImpl phoneService;

  Phone newPhone;

  @BeforeEach
  void setUp() {
    newPhone = Phone.builder()
      .brand("NewBrand")
      .model("NewModel")
      .assignedId("EnNsw7EReh")
      .build();

    newPhone.setCreatedAt(LocalDateTime.now());
    newPhone.setUpdatedAt(LocalDateTime.now());
  }

  @Test
  void givenPhoneAlreadyExists_whenAddPhone_thenShouldThrowError() {
    // Given
    PhoneDTO phoneDTO = new PhoneDTO(newPhone.getBrand(), newPhone.getModel(), newPhone.getAssignedId());
    given(phoneRepository.findByAssignedId(newPhone.getAssignedId())).willReturn(
      Mono.just(newPhone));

    // When
    Mono<Phone> phoneMono = phoneService.addPhone(phoneDTO);

    // Then
    StepVerifier.create(phoneMono)
      .expectError(EntityAlreadyExistsException.class)
      .verify();

    verify(phoneRepository, times(1)).findByAssignedId(newPhone.getAssignedId());
    verify(phoneRepository, times(0)).save(newPhone);
  }

  @Test
  void addPhone() {
    // Given
    PhoneDTO phoneDTO = new PhoneDTO(newPhone.getBrand(), newPhone.getModel(), newPhone.getAssignedId());
    given(phoneRepository.findByAssignedId(newPhone.getAssignedId())).willReturn(Mono.empty());
    given(phoneRepository.save(newPhone)).willReturn(Mono.just(newPhone));

    // When
    Mono<Phone> phoneMono = phoneService.addPhone(phoneDTO);

    // Then
    StepVerifier.create(phoneMono)
      .expectNext(newPhone)
      .verifyComplete();

    verify(phoneRepository, times(1)).findByAssignedId(newPhone.getAssignedId());
    verify(phoneRepository, times(1)).save(newPhone);
  }

  @Test
  void updatePhone() {
    // Given
    PhoneDTO phoneDTO = new PhoneDTO(newPhone.getBrand(), newPhone.getModel(), "MS5pbcSom8");
    newPhone.setId(1);
    newPhone.setAssignedId("MS5pbcSom8");
    given(phoneRepository.findById(1)).willReturn(Mono.just(newPhone));
    given(phoneRepository.save(newPhone)).willReturn(Mono.just(newPhone));

    // When
    Mono<Phone> phoneMono = phoneService.updatePhone(1, phoneDTO);

    // Then
    StepVerifier.create(phoneMono)
      .expectNext(newPhone)
      .verifyComplete();

    verify(phoneRepository, times(1)).findById(1);
    verify(phoneRepository, times(1)).save(newPhone);
  }

  @Test
  void givenPhoneNotFound_whenUpdatePhone_shouldThrowError() {
    // Given
    PhoneDTO phoneDTO = new PhoneDTO(newPhone.getBrand(), newPhone.getModel(), "MS5pbcSom8");
    newPhone.setId(1);
    newPhone.setAssignedId("MS5pbcSom8");
    given(phoneRepository.findById(1)).willReturn(Mono.empty());

    // When
    Mono<Phone> phoneMono = phoneService.updatePhone(1, phoneDTO);

    // Then
    StepVerifier.create(phoneMono)
      .expectError(EntityNotFoundException.class)
      .verify();

    verify(phoneRepository, times(1)).findById(1);
    verify(phoneRepository, times(0)).save(newPhone);
  }

  @Test
  void getPhoneById() {
    // Given
    newPhone.setId(1);
    FonoDeviceDescription fonoDeviceDescription = new FonoDeviceDescription(
      "NewBrand",
      "NewModel",
      "GSM / HSPA / LTE",
      "GSM 850 / 900 / 1800 / 1900 - SIM 1 & SIM 2 (dual-SIM model only)",
      "HSDPA 850 / 900 / 1700(AWS) / 1900 / 2100",
      "LTE band 1(2100), 2(1900), 3(1800), 4(1700/2100), 5(850), 7(2600), 8(900), 17(700), 20(800), 28(700)"
    );
    given(phoneRepository.findById(1)).willReturn(Mono.just(newPhone));
    given(fonoApiClient.getDeviceDescription(PhoneServiceImpl.constructQueryParam(newPhone))).willReturn(
      Mono.just(fonoDeviceDescription));

    // When
    Mono<DeviceDetail> deviceDetailMono = phoneService.getPhoneById(1);

    // Then
    DeviceDetail deviceDetail = new DeviceDetail(
      "NewBrand",
      "NewModel",
      newPhone.getAssignedId(),
      PhoneAvailability.YES,
      "",
      "GSM / HSPA / LTE",
      "GSM 850 / 900 / 1800 / 1900 - SIM 1 & SIM 2 (dual-SIM model only)",
      "HSDPA 850 / 900 / 1700(AWS) / 1900 / 2100",
      "LTE band 1(2100), 2(1900), 3(1800), 4(1700/2100), 5(850), 7(2600), 8(900), 17(700), 20(800), 28(700)"
    );

    StepVerifier.create(deviceDetailMono)
      .expectNext(deviceDetail)
      .verifyComplete();

    verify(phoneRepository, times(1)).findById(1);
    verify(fonoApiClient, times(1)).getDeviceDescription(PhoneServiceImpl.constructQueryParam(newPhone));
  }

  @Test
  void givenFonoClientReturnError_whenGetPhoneById_thenShouldReturnDeviceDetail() {
    // Given
    newPhone.setId(1);
    given(phoneRepository.findById(1)).willReturn(Mono.just(newPhone));
    given(fonoApiClient.getDeviceDescription(PhoneServiceImpl.constructQueryParam(newPhone))).willReturn(Mono.empty());

    // When
    Mono<DeviceDetail> deviceDetailMono = phoneService.getPhoneById(1);

    // Then
    DeviceDetail deviceDetail = new DeviceDetail(
      "NewBrand",
      "NewModel",
      newPhone.getAssignedId(),
      PhoneAvailability.YES,
      "",
      null,
      null,
      null,
      null
    );

    StepVerifier.create(deviceDetailMono)
      .expectNext(deviceDetail)
      .verifyComplete();

    verify(phoneRepository, times(1)).findById(1);
    verify(fonoApiClient, times(1)).getDeviceDescription(PhoneServiceImpl.constructQueryParam(newPhone));
  }

  @Test
  void getAll() {
    // Given
    CustomPagination pagination = CustomPagination.builder().build();
    given(phoneRepository.findAllBy(phoneService.createPaginationObject(pagination))).willReturn(Flux.just(newPhone));
    given(phoneRepository.count()).willReturn(Mono.just(1L));

    // When
    Mono<Page<Phone>> phonesMono = phoneService.getAll(pagination);

    // Then
    StepVerifier.create(phonesMono)
      .expectNext(new PageImpl<>(List.of(newPhone), phoneService.createPaginationObject(pagination), 1))
      .verifyComplete();

    verify(phoneRepository, times(1)).findAllBy(phoneService.createPaginationObject(pagination));
    verify(phoneRepository, times(1)).count();
  }

  @Test
  void deleteById() {
    // Given
    given(phoneRepository.deleteById(1)).willReturn(Mono.empty());

    // When
    Mono<Void> voidMono = phoneService.deleteById(1);

    // Then
    StepVerifier.create(voidMono)
      .verifyComplete();

    verify(phoneRepository, times(1)).deleteById(1);
  }
}
