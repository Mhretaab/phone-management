package org.mberhe.management.phone;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mberhe.management.common.config.web.CustomPagination;
import org.mberhe.management.phone.borrowing.PhoneBorrowingDTO;
import org.mberhe.management.phone.dto.DeviceDetail;
import org.mberhe.management.phone.dto.PhoneDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/phones")
@RequiredArgsConstructor
@Tag(name = "Phone Management")
public class PhoneRestController {
  private final PhoneService phoneService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Phone> addPhone(@Valid @RequestBody final PhoneDTO phoneDTO) {
    return this.phoneService.addPhone(phoneDTO);
  }

  @PutMapping("/{phoneId}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<Phone> updatePhone(@PathVariable("phoneId") final Integer id, @Valid @RequestBody final PhoneDTO phoneDTO) {
    return this.phoneService.updatePhone(id, phoneDTO);
  }

  @GetMapping("/by-id/{phoneId}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<DeviceDetail> getPhoneById(@PathVariable("phoneId") final Integer id) {
    return this.phoneService.getPhoneById(id);
  }

  @GetMapping("/by-assigned-id/{assignedId}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<DeviceDetail> getPhoneByAssignedId(@PathVariable("assignedId") final String assignedId) {
    return this.phoneService.getByAssignedId(assignedId);
  }

  @GetMapping("/all")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Page<Phone>> getAll(CustomPagination pagination) {
    return this.phoneService.getAll(pagination);
  }

  @DeleteMapping("/{phoneId}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Void> deleteById(@PathVariable("phoneId") final Integer id) {
    return this.phoneService.deleteById(id);
  }

  @PostMapping("/borrow")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<String> borrowPhone(@Valid @RequestBody final PhoneBorrowingDTO phoneBorrowingDTO) {
    return this.phoneService.borrowPhone(phoneBorrowingDTO);
  }

  @PutMapping("/return/{phoneId}")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Void> returnPhone(@PathVariable("phoneId") final Integer phoneId) {
    return this.phoneService.returnPhone(phoneId);
  }
}