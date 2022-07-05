package com.tistory.jaimenote.jpa.loading.domain.entity;

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
public class Factory {

  @Id
  @GeneratedValue
  @Column(name = "factory_id")
  private Long id;
  private String name;

  private Factory(String name) {
    this.name = name;
  }

  public static Factory create(String teamName) {
    return new Factory(teamName);
  }
}
