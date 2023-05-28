package org.mberhe.management.user;


import lombok.RequiredArgsConstructor;
import org.mberhe.management.common.config.web.CustomPagination;
import org.mberhe.management.common.exceptions.EntityAlreadyExistsException;
import org.mberhe.management.common.exceptions.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static org.mberhe.management.common.util.ErrorMessages.USER_ALREADY_EXISTS_ERROR_MSG;
import static org.mberhe.management.common.util.ErrorMessages.USER_NOT_FOUND_ERROR_MSG;
import static org.mberhe.management.common.util.ErrorMessages.buildErrorMessage;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public Mono<User> createUser(final UserDTO userDTO) {
    return this.userRepository.findByEmailIgnoreCaseOrPhoneNumberLike(userDTO.email(), userDTO.phoneNumber())
      .flatMap(existingUser ->
        Mono.<User>error(
          new EntityAlreadyExistsException(
            buildErrorMessage(USER_ALREADY_EXISTS_ERROR_MSG, userDTO.email(), userDTO.phoneNumber())
          )
        )
      )
      .switchIfEmpty(
        Mono.defer(() ->
          this.userRepository.save(
            User.builder()
              .firstName(userDTO.firstName())
              .lastName(userDTO.lastName())
              .email(userDTO.email())
              .phoneNumber(userDTO.phoneNumber())
              .role(userDTO.role())
              .build()
          )
        )
      );
  }

  @Override
  public Mono<User> updateUser(final Integer id, final UserDTO userDTO) {
    return this.userRepository.findById(id)
      .switchIfEmpty(Mono.error(new EntityNotFoundException(buildErrorMessage(USER_NOT_FOUND_ERROR_MSG, id))))
      .flatMap(existingUser -> {

        existingUser.setFirstName(userDTO.firstName());
        existingUser.setLastName(userDTO.lastName());
        existingUser.setEmail(userDTO.email());
        existingUser.setPhoneNumber(userDTO.phoneNumber());
        existingUser.setRole(userDTO.role());

        return this.userRepository.save(existingUser);
      });
  }

  @Override
  public Mono<Page<User>> getAll(final CustomPagination pagination) {
    final PageRequest pageRequest = createPaginationObject(pagination);

    return this.userRepository.findAllBy(pageRequest)
      .collectList()
      .zipWith(this.userRepository.count())
      .map(tuple -> new PageImpl<>(tuple.getT1(), pageRequest, tuple.getT2()));
  }

  @Override
  public Mono<Page<User>> getByName(final String name, final CustomPagination pagination) {
    final PageRequest pageRequest = createPaginationObject(pagination);
    return this.userRepository.findByFirstNameIgnoreCaseLikeOrLastNameIgnoreCaseLike(name, name, pageRequest)
      .collectList()
      .zipWith(this.userRepository.countByFirstNameIgnoreCaseLikeOrLastNameIgnoreCaseLike(name, name))
      .map(tuple -> new PageImpl<>(tuple.getT1(), pageRequest, tuple.getT2()));
  }

  @Override
  public Mono<User> getByEmailOrPhoneNumber(final String email, final String phoneNumber) {
    return this.userRepository.findByEmailIgnoreCaseOrPhoneNumberLike(email, phoneNumber);
  }

  @Override
  public Mono<User> getByEmail(final String email) {
    return this.userRepository.findByEmail(email);
  }

  @Override
  public Mono<User> getByPhoneNumber(final String phoneNumber) {
    return this.userRepository.findByPhoneNumber(phoneNumber);
  }

  @Override
  public Mono<User> getById(final Integer id) {
    return this.userRepository.findById(id);
  }

  @Override
  public Mono<Void> deleteById(final Integer id) {
    return this.userRepository.deleteById(id);
  }
}