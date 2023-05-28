package org.mberhe.management.common.service;

import org.mberhe.management.common.config.web.CustomPagination;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public interface Service {
  default PageRequest createPaginationObject(final CustomPagination pagination) {
    final Sort sort = Sort.by(Sort.Direction.fromString(pagination.getSortOrder()), pagination.getSortBy());
    return PageRequest.of(pagination.getPage(), pagination.getSize(), sort);
  }
}