package com.tistory.jaimenote.jpa.relation.domain.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
public class Member {

  @Id
  @GeneratedValue
  @Column(name = "member_id")
  private Long id;

  private String name;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "address",
      joinColumns = @JoinColumn(name = "member_id")
  )
  List<Address> addresses = new ArrayList<>();

  @Embeddable
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class Address {

    private String city;
    private String street;
    private String zipcode;
  }
}

//  @Embedded
//  @AttributeOverrides({
//      @AttributeOverride(name = "city", column = @Column(name = "HOME_CITY")),
//      @AttributeOverride(name = "street", column = @Column(name = "HOME_STREET")),
//      @AttributeOverride(name = "zipcode", column = @Column(name = "HOME_ZIPCODE"))
//  })
//  private Address homeAddress;
//
//  @Embedded
//  @AttributeOverrides({
//      @AttributeOverride(name = "city", column = @Column(name = "COMPANY_CITY")),
//      @AttributeOverride(name = "street", column = @Column(name = "COMPANY_STREET")),
//      @AttributeOverride(name = "zipcode", column = @Column(name = "COMPANY_ZIPCODE"))
//  })
//  private Address companyAddress;
