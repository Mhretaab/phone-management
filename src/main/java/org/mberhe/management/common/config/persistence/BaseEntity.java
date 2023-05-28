package org.mberhe.management.common.config.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {
  @Id
  protected Integer id;
  @CreatedDate
  protected LocalDateTime createdAt;
  @LastModifiedDate
  protected LocalDateTime updatedAt;
  @JsonIgnore
  @Version
  protected Long version;
}