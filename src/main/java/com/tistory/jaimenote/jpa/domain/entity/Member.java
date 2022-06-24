package com.tistory.jaimenote.jpa.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private Long teamId;

  private Member(String name) {
    this.name = name;
  }

  public static Member withName(String name) {
    return new Member(name);
  }

  public Member withTeamId(Long teamId) {
    this.teamId = teamId;
    return this;
  }
}
