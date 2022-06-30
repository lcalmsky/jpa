package com.tistory.jaimenote.jpa.domain.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class Member {

  @Id
  @GeneratedValue
  @Column(name = "member_id")
  private Long id;

  private String name;

  private String city;

  private String street;

  private String zipcode;

  @OneToMany(mappedBy = "member")
  @Exclude
  private List<Order> orders = new ArrayList<>();

}
