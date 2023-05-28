package org.mberhe.management.phone.borrowing;

import jakarta.validation.constraints.Positive;

public record PhoneBorrowingDTO(
  @Positive
  Integer phoneId,
  @Positive
  Integer testerId
) {
}
