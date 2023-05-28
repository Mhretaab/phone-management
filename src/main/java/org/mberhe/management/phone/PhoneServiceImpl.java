package org.mberhe.management.phone;


import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.mberhe.management.common.config.web.CustomPagination;
import org.mberhe.management.common.exceptions.EntityAlreadyExistsException;
import org.mberhe.management.common.exceptions.EntityNotFoundException;
import org.mberhe.management.integration.FonoApiClient;
import org.mberhe.management.integration.FonoDeviceDescription;
import org.mberhe.management.phone.borrowing.PhoneBorrowing;
import org.mberhe.management.phone.borrowing.PhoneBorrowingDTO;
import org.mberhe.management.phone.borrowing.PhoneBorrowingRepository;
import org.mberhe.management.phone.dto.DeviceDetail;
import org.mberhe.management.phone.dto.AvailabilityStatus;
import org.mberhe.management.phone.dto.PhoneDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Map;

import static org.mberhe.management.common.util.ErrorMessages.PHONE_ALREADY_EXISTS_ERROR_MSG;
import static org.mberhe.management.common.util.ErrorMessages.PHONE_NOT_FOUND_ERROR_MSG;
import static org.mberhe.management.common.util.ErrorMessages.buildErrorMessage;

@Service
@RequiredArgsConstructor
public class PhoneServiceImpl implements PhoneService {
  public static final String REQUEST_PROCESSING_ERROR_MSG = "Request processing failed";
  public static final String REQUEST_NO_PHONE_AVAILABLE_MSG = "Request processing failed. The requested phone already issued";
  public static final String REQUEST_PROCESSING_SUCCESS_MSG = "Request successfully processed";
  public static final String UPDATING_RETURNING_STATUS_ERROR_MSG = "Error occurred while updating returning status";
  private final PhoneRepository phoneRepository;

  private final PhoneBorrowingRepository phoneBorrowingRepository;
  private final FonoApiClient fonoApiClient;

  @Override
  public Mono<Phone> addPhone(final PhoneDTO phoneDTO) {

    return this.phoneRepository.findByAssignedId(phoneDTO.assignedId())
      .flatMap(existingPhone ->
        Mono.<Phone>error(
          new EntityAlreadyExistsException(
            buildErrorMessage(PHONE_ALREADY_EXISTS_ERROR_MSG, existingPhone.getBrand(), existingPhone.getModel())
          )
        )
      )
      .switchIfEmpty(
        Mono.defer(() ->
          this.phoneRepository.save(
            Phone.builder()
              .brand(phoneDTO.brand())
              .model(phoneDTO.model())
              .assignedId(phoneDTO.assignedId())
              .build()
          )
        )
      );
  }

  @Override
  public Mono<Phone> updatePhone(final Integer id, final PhoneDTO phoneDTO) {

    return this.phoneRepository.findById(id)
      .switchIfEmpty(Mono.error(new EntityNotFoundException(buildErrorMessage(PHONE_NOT_FOUND_ERROR_MSG, id))))
      .flatMap(existingPhone -> {
        existingPhone.setBrand(phoneDTO.brand());
        existingPhone.setModel(phoneDTO.model());
        existingPhone.setAssignedId(phoneDTO.assignedId());
        return this.phoneRepository.save(existingPhone);
      });
  }

  @Override
  public Mono<DeviceDetail> getPhoneById(final Integer id) {
    return this.phoneRepository.findById(id)
      .flatMap(phone -> this.fonoApiClient.getDeviceDescription(constructQueryParam(phone))
        .map(fonoDeviceDescription -> getDeviceDetail(phone, fonoDeviceDescription))
        .switchIfEmpty(Mono.just(getDeviceDetail(phone, null)))
      ).flatMap(deviceDetail ->
        this.phoneRepository.getPhoneAvailability(id)
          .map(phoneBorrowingProjection -> DeviceDetail.from(deviceDetail, phoneBorrowingProjection.getAvailable(),
            phoneBorrowingProjection.getTester()))
          .defaultIfEmpty(deviceDetail)
      );
  }

  @Override
  public Mono<DeviceDetail> getByAssignedId(final String assignedId) {
    return this.phoneRepository.findByAssignedId(assignedId)
      .flatMap(phone -> this.fonoApiClient.getDeviceDescription(constructQueryParam(phone))
        .map(fonoDeviceDescription -> getDeviceDetail(phone, fonoDeviceDescription))
        .switchIfEmpty(Mono.just(getDeviceDetail(phone, null)))
      ).flatMap(deviceDetail ->
        this.phoneRepository.getPhoneAvailability(assignedId)
          .map(phoneBorrowingProjection -> DeviceDetail.from(deviceDetail, phoneBorrowingProjection.getAvailable(),
            phoneBorrowingProjection.getTester()))
          .defaultIfEmpty(deviceDetail)
      );
  }

  @NotNull
  public static Map<String, Object> constructQueryParam(Phone phone) {
    return Map.of("model", phone.getModel(), "brand", phone.getBrand());
  }

  private DeviceDetail getDeviceDetail(Phone phone, FonoDeviceDescription fonoDeviceDescription) {
    if (phone == null) {
      return null;
    }

    return new DeviceDetail(
      phone.getBrand(),
      phone.getModel(),
      phone.getAssignedId(),
      AvailabilityStatus.YES,
      "",
      fonoDeviceDescription == null ? null : fonoDeviceDescription.technology(),
      fonoDeviceDescription == null ? null : fonoDeviceDescription.twoGBands(),
      fonoDeviceDescription == null ? null : fonoDeviceDescription.threeGBands(),
      fonoDeviceDescription == null ? null : fonoDeviceDescription.fourGBands()
    );
  }

  @Override
  public Mono<Page<Phone>> getAll(final CustomPagination pagination) {

    final PageRequest pageRequest = createPaginationObject(pagination);

    return this.phoneRepository.findAllBy(pageRequest)
      .collectList()
      .zipWith(this.phoneRepository.count())
      .map(tuple -> new PageImpl<>(tuple.getT1(), pageRequest, tuple.getT2()));
  }

  @Override
  public Mono<Void> deleteById(Integer id) {
    return this.phoneRepository.deleteById(id);
  }

  @Override
  public Mono<String> borrowPhone(final PhoneBorrowingDTO phoneBorrowingDTO) {

    return this.phoneBorrowingRepository.existsByPhoneIdAndReturnedDateIsNull(phoneBorrowingDTO.phoneId())
      .flatMap(exists -> {
        if (exists) {
          return Mono.error(new PhoneBorrowingException(REQUEST_NO_PHONE_AVAILABLE_MSG));
        } else {
          return phoneBorrowingRepository.save(
            PhoneBorrowing.builder()
              .phoneId(phoneBorrowingDTO.phoneId())
              .testerId(phoneBorrowingDTO.testerId())
              .borrowedDate(LocalDate.now())
              .build()
          );
        }
      })
      .switchIfEmpty(
        Mono.error(new PhoneBorrowingException(REQUEST_PROCESSING_ERROR_MSG))
      )
      .map(phoneBorrowing -> REQUEST_PROCESSING_SUCCESS_MSG);
  }

  @Override
  public Mono<String> returnPhone(final Integer phoneId) {
    return this.phoneBorrowingRepository.findByPhoneIdAndReturnedDateIsNull(phoneId)
      .switchIfEmpty(
        Mono.error(new PhoneBorrowingException("Phone return status already updated"))
      )
      .flatMap(phoneBorrowing -> {
        phoneBorrowing.setReturnedDate(LocalDate.now());
        return this.phoneBorrowingRepository.save(phoneBorrowing);
      }).switchIfEmpty(
        Mono.error(new PhoneBorrowingException(UPDATING_RETURNING_STATUS_ERROR_MSG))
      ).map(phoneBorrowing -> REQUEST_PROCESSING_SUCCESS_MSG);
  }
}