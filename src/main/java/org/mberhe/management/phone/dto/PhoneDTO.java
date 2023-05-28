package org.mberhe.management.phone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PhoneDTO(
  @Size(max = 25, message = "Brand name cannot be more than 25")
  @NotBlank(message = "Brand name cannot be null or empty")
  String brand,
  @Size(max = 25, message = "Model name cannot be more than 25")
  @NotBlank(message = "Model name cannot be null or empty")
  String model,
  @Size(max = 25, message = "Assigned ID cannot be more than 25")
  @NotBlank(message = "Assigned ID  cannot be null or empty")
  String assignedId
) {
}