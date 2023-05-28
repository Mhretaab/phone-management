package org.mberhe.management.phone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mberhe.management.common.config.web.CustomPagination;
import org.mberhe.management.common.exceptions.EntityAlreadyExistsException;
import org.mberhe.management.common.exceptions.EntityNotFoundException;
import org.mberhe.management.common.exceptions.handler.ErrorAttributesKey;
import org.mberhe.management.common.exceptions.handler.GlobalErrorAttributes;
import org.mberhe.management.phone.dto.DeviceDetail;
import org.mberhe.management.phone.dto.PhoneAvailability;
import org.mberhe.management.phone.dto.PhoneDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebFluxTest(controllers = {PhoneRestController.class})
@Import(GlobalErrorAttributes.class)
class PhoneRestControllerTest {

  @MockBean
  private PhoneService phoneService;

  @Autowired
  private WebTestClient webClient;

  PhoneDTO phoneDTO;

  @BeforeEach
  void setUp() {
    phoneDTO = new PhoneDTO("NewBrand", "NewModel", "LRdYFycYmp");
  }

  @Test
  void addPhone() {
    // Given
    Phone expectedPhone = Phone.builder()
      .brand(phoneDTO.brand())
      .model(phoneDTO.model())
      .assignedId(phoneDTO.assignedId())
      .build();

    given(phoneService.addPhone(phoneDTO)).willReturn(
      Mono.just(expectedPhone)
    );

    // When
    webClient.post()
      .uri("/phones")
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromObject(phoneDTO))
      .exchange()
      .expectStatus().isCreated()
      .expectBody(Phone.class)
      .isEqualTo(expectedPhone);

    // Then
    verify(phoneService, times(1)).addPhone(phoneDTO);
  }

  @Test
  void givenPhoneExists_whenAddPhone_thenShouldThrowError() {
    // Given
    Phone expectedPhone = Phone.builder()
      .brand(phoneDTO.brand())
      .model(phoneDTO.model())
      .assignedId(phoneDTO.assignedId())
      .build();

    String errorMsg = "Phone already exists";
    given(phoneService.addPhone(phoneDTO)).willReturn(Mono.error(new EntityAlreadyExistsException(errorMsg)));

    // When
    webClient.post()
      .uri("/phones")
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromObject(phoneDTO))
      .exchange()
      .expectStatus().isBadRequest()
      .expectBody()
      .jsonPath("$." + ErrorAttributesKey.TIME.getKey()).isNotEmpty()
      .jsonPath("$." + ErrorAttributesKey.MESSAGE.getKey()).isEqualTo(errorMsg)
      .jsonPath("$." + ErrorAttributesKey.CODE.getKey()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    // Then
    verify(phoneService, times(1)).addPhone(phoneDTO);
  }

  @Test
  void givenPhoneHasNoBrand_whenAddPhone_thenShouldThrowError() {
    // Given
    PhoneDTO invalidPhone = new PhoneDTO(null, "model", "LRdYFycYmp");

    // When
    webClient.post()
      .uri("/phones")
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromObject(invalidPhone))
      .exchange()
      .expectStatus().isBadRequest()
      .expectBody()
      .jsonPath("$." + ErrorAttributesKey.TIME.getKey()).isNotEmpty()
      .jsonPath("$." + ErrorAttributesKey.MESSAGE.getKey()).isNotEmpty()
      .jsonPath("$." + ErrorAttributesKey.CODE.getKey()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  void givenPhoneHasNoModel_whenAddPhone_thenShouldThrowError() {
    // Given
    PhoneDTO invalidPhone = new PhoneDTO("brand", null, "LRdYFycYmp");

    // When
    webClient.post()
      .uri("/phones")
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromObject(invalidPhone))
      .exchange()
      .expectStatus().isBadRequest()
      .expectBody()
      .jsonPath("$." + ErrorAttributesKey.TIME.getKey()).isNotEmpty()
      .jsonPath("$." + ErrorAttributesKey.MESSAGE.getKey()).isNotEmpty()
      .jsonPath("$." + ErrorAttributesKey.CODE.getKey()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  void updatePhone() {
    // Given
    Phone expectedPhone = Phone.builder()
      .brand(phoneDTO.brand())
      .model(phoneDTO.model())
      .assignedId(phoneDTO.assignedId())
      .build();
    expectedPhone.setId(1);

    given(phoneService.updatePhone(1, phoneDTO)).willReturn(
      Mono.just(expectedPhone)
    );

    // When
    webClient.put()
      .uri("/phones/{phoneId}", expectedPhone.getId())
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromObject(phoneDTO))
      .exchange()
      .expectStatus().isAccepted()
      .expectBody(Phone.class)
      .isEqualTo(expectedPhone);

    // Then
    verify(phoneService, times(1)).updatePhone(1, phoneDTO);
  }

  @Test
  void givenPhoneNotExists_whenUpdatePhone_thenShouldReturnError() {
    // Given
    Phone expectedPhone = Phone.builder()
      .brand(phoneDTO.brand())
      .model(phoneDTO.model())
      .assignedId(phoneDTO.assignedId())
      .build();
    expectedPhone.setId(1);

    String errorMsg = "Phone not found";
    given(phoneService.updatePhone(1, phoneDTO)).willReturn(
      Mono.error(new EntityNotFoundException(errorMsg))
    );

    // When
    webClient.put()
      .uri("/phones/{phoneId}", expectedPhone.getId())
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromObject(phoneDTO))
      .exchange()
      .expectStatus().isBadRequest()
      .expectBody()
      .jsonPath("$." + ErrorAttributesKey.TIME.getKey()).isNotEmpty()
      .jsonPath("$." + ErrorAttributesKey.MESSAGE.getKey()).isEqualTo(errorMsg)
      .jsonPath("$." + ErrorAttributesKey.CODE.getKey()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    // Then
    verify(phoneService, times(1)).updatePhone(1, phoneDTO);
  }

  @Test
  void givenInvalidBrand_whenUpdatePhone_thenShouldReturnError() {
    // Given
    Phone expectedPhone = Phone.builder()
      .brand(phoneDTO.brand())
      .model(phoneDTO.model())
      .assignedId(phoneDTO.assignedId())
      .build();
    expectedPhone.setId(1);

    PhoneDTO invalidPhone = new PhoneDTO(null, "model", "LRdYFycYmp");

    // When
    webClient.put()
      .uri("/phones/{phoneId}", expectedPhone.getId())
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromObject(invalidPhone))
      .exchange()
      .expectStatus().isBadRequest()
      .expectBody()
      .jsonPath("$." + ErrorAttributesKey.TIME.getKey()).isNotEmpty()
      .jsonPath("$." + ErrorAttributesKey.MESSAGE.getKey()).isNotEmpty()
      .jsonPath("$." + ErrorAttributesKey.CODE.getKey()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  void givenInvalidModel_whenUpdatePhone_thenShouldReturnError() {
    // Given
    Phone expectedPhone = Phone.builder()
      .brand(phoneDTO.brand())
      .model(phoneDTO.model())
      .assignedId(phoneDTO.assignedId())
      .build();
    expectedPhone.setId(1);

    PhoneDTO invalidPhone = new PhoneDTO("brand", null, "LRdYFycYmp");

    // When
    webClient.put()
      .uri("/phones/{phoneId}", expectedPhone.getId())
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromObject(invalidPhone))
      .exchange()
      .expectStatus().isBadRequest()
      .expectBody()
      .jsonPath("$." + ErrorAttributesKey.TIME.getKey()).isNotEmpty()
      .jsonPath("$." + ErrorAttributesKey.MESSAGE.getKey()).isNotEmpty()
      .jsonPath("$." + ErrorAttributesKey.CODE.getKey()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  void getPhoneById() {
    // Given
    Phone expectedPhone = Phone.builder()
      .brand(phoneDTO.brand())
      .model(phoneDTO.model())
      .assignedId(phoneDTO.assignedId())
      .build();
    expectedPhone.setId(1);

    DeviceDetail deviceDetail = new DeviceDetail(
      "NewBrand",
      "NewModel",
      "LRdYFycYmp",
      PhoneAvailability.YES,
      "",
      "GSM / HSPA / LTE",
      "GSM 850 / 900 / 1800 / 1900 - SIM 1 & SIM 2 (dual-SIM model only)",
      "HSDPA 850 / 900 / 1700(AWS) / 1900 / 2100",
      "LTE band 1(2100), 2(1900), 3(1800), 4(1700/2100), 5(850), 7(2600), 8(900), 17(700), 20(800), 28(700)"
    );

    given(phoneService.getPhoneById(1)).willReturn(Mono.just(deviceDetail));

    // When
    webClient.get()
      .uri("/phones/by-id/{phoneId}", expectedPhone.getId())
      .exchange()
      .expectStatus().isOk()
      .expectBody(DeviceDetail.class)
      .isEqualTo(deviceDetail);

    // Then
    verify(phoneService, times(1)).getPhoneById(1);
  }

  @Test
  void getPhoneByAssignedId() {
    // Given
    Phone expectedPhone = Phone.builder()
      .brand(phoneDTO.brand())
      .model(phoneDTO.model())
      .assignedId(phoneDTO.assignedId())
      .build();
    expectedPhone.setId(1);

    DeviceDetail deviceDetail = new DeviceDetail(
      "NewBrand",
      "NewModel",
      phoneDTO.assignedId(),
      PhoneAvailability.YES,
      "",
      "GSM / HSPA / LTE",
      "GSM 850 / 900 / 1800 / 1900 - SIM 1 & SIM 2 (dual-SIM model only)",
      "HSDPA 850 / 900 / 1700(AWS) / 1900 / 2100",
      "LTE band 1(2100), 2(1900), 3(1800), 4(1700/2100), 5(850), 7(2600), 8(900), 17(700), 20(800), 28(700)"
    );

    given(phoneService.getByAssignedId(phoneDTO.assignedId())).willReturn(Mono.just(deviceDetail));

    // When
    webClient.get()
      .uri("/phones/by-assigned-id/{assignedId}", phoneDTO.assignedId())
      .exchange()
      .expectStatus().isOk()
      .expectBody(DeviceDetail.class)
      .isEqualTo(deviceDetail);

    // Then
    verify(phoneService, times(1)).getByAssignedId(phoneDTO.assignedId());
  }

  @Test
  void getAll() {
    // Given
    Phone phone = Phone.builder()
      .brand(phoneDTO.brand())
      .model(phoneDTO.model())
      .assignedId(phoneDTO.assignedId())
      .build();
    phone.setId(1);
    CustomPagination pagination = CustomPagination.builder().build();
    given(phoneService.getAll(pagination)).willReturn(Mono.just(new PageImpl<>(List.of(phone), Pageable.ofSize(10), 1)));

    // When
    webClient.get()
      .uri(uriBuilder ->
        uriBuilder
          .path("/phones/all")
          .queryParam("page", pagination.getPage())
          .queryParam("size", pagination.getSize())
          .queryParam("sortBy", pagination.getSortBy())
          .queryParam("sortOrder", pagination.getSortOrder())
          .build()
      )
      .exchange()
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$.content.length()").isEqualTo(1)
      .jsonPath("$.content[0].brand").isEqualTo("NewBrand")
      .jsonPath("$.content[0].model").isEqualTo("NewModel");

    // Then
    verify(phoneService, times(1)).getAll(pagination);
  }

  @Test
  void deleteById() {
    // Given
    given(phoneService.deleteById(1)).willReturn(Mono.empty());

    // When
    webClient.delete()
      .uri("/phones/{phoneId}", 1)
      .exchange()
      .expectStatus().isOk();

    // Then
    verify(phoneService, times(1)).deleteById(1);
  }
}
