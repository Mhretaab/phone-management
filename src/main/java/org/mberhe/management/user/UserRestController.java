package org.mberhe.management.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mberhe.management.common.config.web.CustomPagination;
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
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management")
public class UserRestController {
  private final UserService userService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<User> createUser(@Valid @RequestBody final UserDTO userDTO) {
    return this.userService.createUser(userDTO);
  }

  @PutMapping("/{userId}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<User> updateUser(@PathVariable("userId") final Integer id, @Valid @RequestBody final UserDTO userDTO) {
    return this.userService.updateUser(id, userDTO);
  }

  @GetMapping("/all")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Page<User>> getAll(final CustomPagination pagination) {
    return this.userService.getAll(pagination);
  }

  @GetMapping("/by-name/{name}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Page<User>> getByName(@PathVariable("name") final String name, final CustomPagination pagination) {
    return this.userService.getByName(name, pagination);
  }

  @GetMapping("/by-email-or-phone/{email}/{phone}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<User> getByEmailOrPhoneNumber(@PathVariable("email") final String email,
                                            @PathVariable("phone") final String phoneNumber) {
    return this.userService.getByEmailOrPhoneNumber(email, phoneNumber);
  }

  @GetMapping("/by-email/{email}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<User> getByEmail(@PathVariable("email") final String email) {
    return this.userService.getByEmail(email);
  }

  @GetMapping("/by-phone/{phone}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<User> getByPhoneNumber(@PathVariable("phone") final String phoneNumber) {
    return this.userService.getByPhoneNumber(phoneNumber);
  }

  @GetMapping("/by-id/{userId}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<User> getById(@PathVariable("userId") final Integer id) {
    return this.userService.getById(id);
  }

  @DeleteMapping("/{userId}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Void> deleteById(@PathVariable("userId") final Integer id) {
    return this.userService.deleteById(id);
  }
}
