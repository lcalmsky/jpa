package com.tistory.jaimenote.jpa.relation.domain.entity;

import com.tistory.jaimenote.jpa.domain.BaseEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.ToString;
import lombok.ToString.Exclude;

@Entity
@Getter
@ToString(callSuper = true)
public class Member extends BaseEntity {

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

  protected Member() {
    super(LocalDateTime.now(), LocalDateTime.now());
  }

  private Member(LocalDateTime createdDate, LocalDateTime lastModifiedDate,
      String name, String city, String street, String zipcode,
      List<Order> orders) {
    super(createdDate, lastModifiedDate);
    this.name = name;
    this.city = city;
    this.street = street;
    this.zipcode = zipcode;
    this.orders = orders;
  }

  public static Member create(String name, String city, String street, String zipcode,
      List<Order> orders) {
    LocalDateTime now = LocalDateTime.now();
    return new Member(now, now, name, city, street, zipcode, orders);
  }
}
