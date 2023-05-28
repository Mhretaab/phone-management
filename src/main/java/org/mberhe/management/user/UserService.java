package org.mberhe.management.user;


import org.mberhe.management.common.config.web.CustomPagination;
import org.mberhe.management.common.service.Service;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

public interface UserService extends Service {
  Mono<User> createUser(UserDTO userDTO);

  Mono<User> updateUser(Integer id, UserDTO userDTO);

  Mono<Page<User>> getAll(CustomPagination pagination);

  Mono<Page<User>> getByName(String name, CustomPagination pagination);

  Mono<User> getByEmailOrPhoneNumber(String email, String phoneNumber);

  Mono<User> getByEmail(String email);

  Mono<User> getByPhoneNumber(String phoneNumber);

  Mono<User> getById(Integer id);

  Mono<Void> deleteById(Integer id);
}