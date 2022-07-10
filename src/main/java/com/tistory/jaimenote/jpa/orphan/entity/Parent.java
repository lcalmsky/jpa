package com.tistory.jaimenote.jpa.orphan.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Parent {

  @Id
  @GeneratedValue
  @Column(name = "parent_id")
  private Long id;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Child> children = new ArrayList<>();

  public static Parent createParent() {
    return new Parent();
  }

  public void takeAsChild(Child child) {
    children.add(child);
    child.serveAsParent(this);
  }
}
