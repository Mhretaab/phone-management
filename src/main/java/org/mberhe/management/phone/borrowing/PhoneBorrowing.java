package org.mberhe.management.phone.borrowing;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mberhe.management.common.config.persistence.BaseEntity;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "phone_borrowings")
public class PhoneBorrowing extends BaseEntity {
  @Positive
  private Integer phoneId;
  @Positive
  private Integer testerId;
  @FutureOrPresent
  private LocalDate borrowedDate;
  private LocalDate returnedDate;
}
