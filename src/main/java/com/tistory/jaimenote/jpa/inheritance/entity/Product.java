package com.tistory.jaimenote.jpa.inheritance.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Product {

  @Id
  @GeneratedValue
  private Long id;

  @Setter(AccessLevel.PROTECTED)
  private String name;

  @Setter(AccessLevel.PROTECTED)
  private Integer price;
}
