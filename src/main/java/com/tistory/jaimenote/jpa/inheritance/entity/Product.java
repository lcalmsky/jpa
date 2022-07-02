package com.tistory.jaimenote.jpa.inheritance.entity;

import com.tistory.jaimenote.jpa.domain.BaseEntity;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@ToString(callSuper = true)
public class Product extends BaseEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Setter(AccessLevel.PROTECTED)
  private String name;

  @Setter(AccessLevel.PROTECTED)
  private Integer price;

  public Product() {
    super(LocalDateTime.now(), LocalDateTime.now());
  }

  private Product(LocalDateTime createdDate, LocalDateTime lastModifiedDate, String name,
      Integer price) {
    super(createdDate, lastModifiedDate);
    this.name = name;
    this.price = price;
  }

  public static Product create(String name, Integer price) {
    LocalDateTime now = LocalDateTime.now();
    return new Product(now, now, name, price);
  }
}
