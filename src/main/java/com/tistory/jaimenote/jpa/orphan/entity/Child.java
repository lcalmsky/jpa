package com.tistory.jaimenote.jpa.orphan.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Child {

  @Id
  @GeneratedValue
  @Column(name = "child_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "parent_id")
  private Parent parent;

  public static Child createChild() {
    return new Child();
  }

  public void serveAsParent(Parent parent) {
    this.parent = parent;
  }
}