package com.tistory.jaimenote.jpa.domain.entity;

import com.tistory.jaimenote.jpa.domain.support.OrderStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Exclude;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Order {

  @Id
  @GeneratedValue
  @Column(name = "order_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @OneToMany(mappedBy = "order")
  @Exclude
  private List<OrderItem> orderItems = new ArrayList<>();

  private LocalDateTime orderDateTime;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @OneToOne
  @JoinColumn(name = "delivery_id")
  private Delivery delivery;

}