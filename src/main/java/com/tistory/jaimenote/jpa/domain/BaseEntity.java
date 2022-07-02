package com.tistory.jaimenote.jpa.domain;

import java.time.LocalDateTime;
import javax.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@ToString
public abstract class BaseEntity {

  private final LocalDateTime createdDate;
  private final LocalDateTime lastModifiedDate;
  private String createdBy;
  private String lastModifiedBy;
}
