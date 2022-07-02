![](https://img.shields.io/badge/spring--boot-2.7.1-red) ![](https://img.shields.io/badge/gradle-7.4.1-brightgreen) ![](https://img.shields.io/badge/java-11-blue)
 
> 소스 코드는 [여기](https://github.com/lcalmsky/jpa) 있습니다. (commit hash: c93b796)
> ```shell
> > git clone https://github.com/lcalmsky/jpa
> > git checkout c93b796
> ```
> **Warning:** 이번 소스 코드는 최종 커밋 기준으로 작성되어 있어 모든 테스트 결과를 정확히 확인할 순 없으니 참고 부탁드립니다.

## Overview

객체간 상속을 이용하지만 실제로는 `Entity`나 `Table`과 관계가 전혀 없을 때 사용하는 `@MappedSuperclass`에 대해 알아봅니다.

## @MappedSuperclass

객체에서 공통 속성을 추상 객체로 이동시키고 자식 객체들이 해당 속성을 공통으로 사용하는 것은 매우 흔한 일입니다.

이전 포스팅에서는 `Entity` 간 관계(슈퍼타입, 서브타입)도 중요했었는데요, 여기서 다룰 내용은 Entity(Table)와 매핑하는 내용은 아닙니다.

부모 객체가 가진 속성을 상속하는 자식 클래스가 사용하는 것은 동일한데 오직 매핑 정보만 제공하는 방식입니다.

따라서 부모 객체를 통한 검색(select) 자체가 불가능 합니다.

애초에 추상클래스로 만들어서 객체 자체를 생성할 수 없게 하는 게 설계상 유리합니다.

객체, Entity간 관계를 나타낸 다이어그램은 다음과 같습니다.

![](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/lcalmsky/jpa/master/diagrams/08-object-relation.puml)

![](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/lcalmsky/jpa/master/diagrams/08-table-relation.puml)

특징을 정리하면 다음과 같습니다.

* 테이블과 관계 없는 데이터
* 단순히 공통으로 사용할 매핑 정보
  * 주로 생성일, 수정일, 등록자, 수정자 같은 정보
* @Entity는 같은 @Entity이거나 @MappedSuperclass로 지정한 클래스만 상속 가능

## 구현 및 테스트

구현을 위해 기존 패키지를 리팩터링 하였습니다.

```text
domain -> relation.domain
```

그리고 공통으로 사용할 `entity`만 추가하기위한 용도로 `domain` 패키지를 다시 생성하였습니다.

```text
jpa
├── App.java
├── domain
├── inheritance
└── relation
```

먼저 `domain` 패키지 하위에 `BaseEntity` 클래스를 생성합니다.

`/src/main/java/com/tistory/jaimenote/jpa/domain/BaseEntity.java`

```java
package com.tistory.jaimenote.jpa.domain;

import java.time.LocalDateTime;
import javax.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@ToString
public abstract class BaseEntity {

  private final LocalDateTime createdDate;
  private final LocalDateTime lastModifiedDate;
  private String createdBy;
  private String lastModifiedBy;
}

```

자식클래스에서 반드시 `createdDate`, `lastModifiedDate`를 입력하도록 `@RequiredArgsConstructor`를 사용하였고, `createdBy`, `lastModifiedBy`는 선택적으로 `setter`를 이용해 입력할 수 있게 하였습니다.

`BaseEntity` 객체를 따로 생성할 수 없도록 `abstract`로 선언하였습니다.

그리고 각각 다른 도메인인 `inheritance`, `relation` 내의 `entity`에서 `BaseEntity`를 상속하도록 수정해보겠습니다.

`/src/main/java/com/tistory/jaimenote/jpa/relation/domain/entity/Member.java`

```java
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
```

부모 클래스의 `createdDate`, `lastModifiedDate`에 매핑될 수 있는 값을 기본 생성자에서 초기화해 줄 수 있게 하였고, `static` 생성자에서도 부모 클래스에 매핑할 값은 자동으로 현재 시점으로 입력하도록 하였습니다.

`/src/main/java/com/tistory/jaimenote/jpa/inheritance/entity/Product.java`

```java
package com.tistory.jaimenote.jpa.inheritance.entity;

import com.tistory.jaimenote.jpa.domain.BaseEntity;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@ToString(callSuper = true)
public class Product extends BaseEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Setter(AccessLevel.PROTECTED)
  private String name;

  @Setter(AccessLevel.PROTECTED)
  private Integer price;

  public Product() {
    super(LocalDateTime.now(), LocalDateTime.now());
  }

  private Product(LocalDateTime createdDate, LocalDateTime lastModifiedDate, String name,
          Integer price) {
    super(createdDate, lastModifiedDate);
    this.name = name;
    this.price = price;
  }

  public static Product create(String name, Integer price) {
    LocalDateTime now = LocalDateTime.now();
    return new Product(now, now, name, price);
  }
}

```

`Member` 클래스와 마찬가지로 생성자 및 `static` 생성자를 수정해주었습니다.

서로 관련이 없는 `Member`와 `Product`가 각각 `BaseEntity`를 상속하도록 수정하였습니다.

다음으로 테스트 클래스를 생성하여 `Member`와 `Product` `Entity`를 생성해 저장해보았습니다.

`/src/test/java/com/tistory/jaimenote/jpa/inheritance/infra/repository/ProductRepositoryTest.java`

```java
package com.tistory.jaimenote.jpa.domain;

import com.tistory.jaimenote.jpa.inheritance.entity.Product;
import com.tistory.jaimenote.jpa.inheritance.infra.repository.ProductRepository;
import com.tistory.jaimenote.jpa.relation.domain.entity.Member;
import com.tistory.jaimenote.jpa.relation.infra.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
class BaseEntityTest {

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  ProductRepository productRepository;

  @Test
  @Rollback(false)
  void baseEntityTest() {
    Member member = Member.create("name", "city", "street", "zipcode", null);
    memberRepository.save(member);

    Product product = Product.create("name", 1000);
    productRepository.save(product);
  }
}
```

```text
Hibernate: 
    insert 
    into
        member
        (created_by, created_date, last_modified_by, last_modified_date, city, name, street, zipcode, member_id) 
    values
        (?, ?, ?, ?, ?, ?, ?, ?, ?)
2022-07-02 23:00:10.383 TRACE 97194 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [VARCHAR] - [null]
2022-07-02 23:00:10.385 TRACE 97194 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [2] as [TIMESTAMP] - [2022-07-02T23:00:10.285475]
2022-07-02 23:00:10.387 TRACE 97194 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [3] as [VARCHAR] - [null]
2022-07-02 23:00:10.387 TRACE 97194 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [4] as [TIMESTAMP] - [2022-07-02T23:00:10.285475]
2022-07-02 23:00:10.387 TRACE 97194 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [5] as [VARCHAR] - [city]
2022-07-02 23:00:10.387 TRACE 97194 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [6] as [VARCHAR] - [name]
2022-07-02 23:00:10.387 TRACE 97194 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [7] as [VARCHAR] - [street]
2022-07-02 23:00:10.387 TRACE 97194 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [8] as [VARCHAR] - [zipcode]
2022-07-02 23:00:10.388 TRACE 97194 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [9] as [BIGINT] - [1]
Hibernate: 
    insert 
    into
        product
        (created_by, created_date, last_modified_by, last_modified_date, name, price, dtype, id) 
    values
        (?, ?, ?, ?, ?, ?, 'Product', ?)
2022-07-02 23:00:10.391 TRACE 97194 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [VARCHAR] - [null]
2022-07-02 23:00:10.393 TRACE 97194 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [2] as [TIMESTAMP] - [2022-07-02T23:00:10.352951]
2022-07-02 23:00:10.393 TRACE 97194 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [3] as [VARCHAR] - [null]
2022-07-02 23:00:10.393 TRACE 97194 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [4] as [TIMESTAMP] - [2022-07-02T23:00:10.352951]
2022-07-02 23:00:10.393 TRACE 97194 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [5] as [VARCHAR] - [name]
2022-07-02 23:00:10.394 TRACE 97194 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [6] as [INTEGER] - [1000]
2022-07-02 23:00:10.394 TRACE 97194 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [7] as [BIGINT] - [2]
```

`Member`와 `Product`의 속성 외에도 `@MappedSuperclass`로 지정한 `BaseEntity`의 속성이 모두 추가된 것을 확인할 수 있습니다.

사실 생성일, 수정일, 생성자, 수정자는 JPA에서 자동으로 지원해주는 기능이기도 합니다.

자세한 내용은 [이 포스팅](https://jaime-note.tistory.com/60)의 **Auditing 사용** 항목을 참고하시면 됩니다.