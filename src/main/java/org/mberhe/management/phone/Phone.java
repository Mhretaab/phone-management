package org.mberhe.management.phone;

import jakarta.validation.constraints.NotBlank;
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
@Table(value = "phones")
public class Phone extends BaseEntity {
  @Size(max = 25, message = "Brand name cannot be more than 25")
  @NotBlank(message = "Brand name cannot be null or empty")
  private String brand;
  @Size(max = 25, message = "Model name cannot be more than 25")
  @NotBlank(message = "Model name cannot be null or empty")
//  @EqualsAndHashCode.Include
  private String model;

  @Size(max = 25, message = "Assigned ID  cannot be more than 25")
  @NotBlank(message = "Assigned ID  cannot be null or empty")
  private String assignedId;
}