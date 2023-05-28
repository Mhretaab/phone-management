package org.mberhe.management.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserDTO(
  @Size(max = 15, message = "First name cannot be more than 15")
  @NotBlank(message = "First name cannot be null or empty")
  String firstName,
  @Size(max = 15, message = "Last name cannot be more than 15")
  @NotBlank(message = "First name cannot be null or empty")
  String lastName,
  @Email(message = "Invalid email")
  String email,
  @Size(max = 15, message = "PhoneNumber cannot be more than 15")
  @NotBlank(message = "First name cannot be null or empty")
  String phoneNumber,
  @NotNull(message = "Role cannot be null")
  Role role
) {
}