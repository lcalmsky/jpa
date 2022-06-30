package com.tistory.jaimenote.jpa.domain.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Exclude;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Category {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @ManyToOne
  @JoinColumn(name = "category_parent_id")
  private Category parent;

  @OneToMany(mappedBy = "parent")
  @Exclude
  private List<Category> child = new ArrayList<>();

  @ManyToMany
  @JoinTable(
      name = "category_item",
      joinColumns = @JoinColumn(name = "category_id"),
      inverseJoinColumns = @JoinColumn(name = "item_id")
  )
  @Exclude
  private List<Item> items = new ArrayList<>();

}
