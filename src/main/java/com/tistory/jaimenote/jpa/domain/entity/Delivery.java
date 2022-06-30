package com.tistory.jaimenote.jpa.domain.entity;

import com.tistory.jaimenote.jpa.domain.support.DeliveryStatus;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Delivery {

  @Id
  @GeneratedValue
  private Long id;

  private String city;

  private String street;

  private String zipcode;

  @Enumerated(EnumType.STRING)
  private DeliveryStatus deliveryStatus;

  @OneToOne(mappedBy = "delivery")
  private Order order;
}
