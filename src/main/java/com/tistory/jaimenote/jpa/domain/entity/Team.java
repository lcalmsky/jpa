package com.tistory.jaimenote.jpa.domain.entity;

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
public class Team {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private Team(String name) {
    this.name = name;
  }

  public static Team withName(String name) {
    return new Team(name);
  }
}
