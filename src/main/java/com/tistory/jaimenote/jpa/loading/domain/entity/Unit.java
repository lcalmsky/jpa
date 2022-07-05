package com.tistory.jaimenote.jpa.loading.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Exclude;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Unit {

  @Id
  @GeneratedValue
  @Column(name = "unit_id")
  private Long id;
  private String name;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "factory_id")
  @Exclude
  private Factory factory;

  private Unit(String name, Factory factory) {
    this.name = name;
    this.factory = factory;
  }

  public static Unit create(String name, Factory factory) {
    return new Unit(name, factory);
  }
}
