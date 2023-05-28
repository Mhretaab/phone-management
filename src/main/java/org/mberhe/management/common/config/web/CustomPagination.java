package org.mberhe.management.common.config.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomPagination {
  @Builder.Default
  private int page = 0;
  @Builder.Default
  private int size = 10;
  @Builder.Default
  private String sortBy = "createdAt";
  @Builder.Default
  private String sortOrder = "ASC";
}