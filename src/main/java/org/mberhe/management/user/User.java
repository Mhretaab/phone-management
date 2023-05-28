package org.mberhe.management.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mberhe.management.common.config.persistence.BaseEntity;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "users")
public class User extends BaseEntity {
  @Size(max = 15, message = "First name cannot be more than 15")
  @NotBlank(message = "First name cannot be null or empty")
  private String firstName;
  @Size(max = 15, message = "Last name cannot be more than 15")
  @NotBlank(message = "First name cannot be null or empty")
  private String lastName;
  @Email(message = "Invalid email")
  private String email;
  @Size(max = 15, message = "PhoneNumber cannot be more than 15")
  @NotBlank(message = "First name cannot be null or empty")
  private String phoneNumber;
  @NotNull(message = "Role cannot be null")
  private Role role;
}