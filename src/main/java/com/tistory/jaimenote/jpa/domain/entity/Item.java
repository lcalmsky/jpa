package com.tistory.jaimenote.jpa.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Item {
  @Id
  @GeneratedValue
  @Column(name = "item_id")
  private Long id;

  private String name;

  private Integer price;

  private Integer stockQuantity;

}
