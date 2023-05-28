package org.mberhe.management.phone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneAvailability {
  private String tester;
  private Integer phoneId;
  private Boolean available;
}
