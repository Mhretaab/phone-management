package org.mberhe.management.phone;


import org.mberhe.management.common.config.web.CustomPagination;
import org.mberhe.management.common.service.Service;
import org.mberhe.management.phone.borrowing.PhoneBorrowingDTO;
import org.mberhe.management.phone.dto.DeviceDetail;
import org.mberhe.management.phone.dto.PhoneDTO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

public interface PhoneService extends Service {
  Mono<Phone> addPhone(PhoneDTO phoneDTO);

  Mono<Phone> updatePhone(Integer id, PhoneDTO phoneDTO);

  Mono<DeviceDetail> getPhoneById(Integer id);

  Mono<DeviceDetail> getByAssignedId(String assignedId);

  Mono<Page<Phone>> getAll(CustomPagination pagination);

  Mono<Void> deleteById(Integer id);

  Mono<String> borrowPhone(PhoneBorrowingDTO phoneBorrowingDTO);

  Mono<Void> returnPhone(Integer phoneId);
}