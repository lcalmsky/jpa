## Overview

JPA의 데이터 타입은 크게 엔터티 타입(Entity Type)과 값 타입(Value Type)으로 나뉩니다.

각각 어떤 특징을 가지는지 알아보도록 하겠습니다.

## Entity Type

`@Entity`로 정의하는 객체로 식별자(`@Id`)로 계속 추적할 수 있습니다.

식별자를 제외한 속성들은 설정에 따라 얼마든지 변경 가능합니다.

## Value Type

`int`, `Integer`, `String` 처럼 단순히 값으로 사용되는 `primitive` 타입이나 `refenrece` 타입을 말합니다.

식별자가 없고 값만 있기 때문에 변경시 추적이 불가능합니다.

`Value Type`은 세 가지로 분류됩니다.

### Primitive Type

primitive 타입과 primitive 타입을 감싸는(boxing) wrapper type, String이 여기에 해당합니다.

* primitive type
  * int, double, long, boolean, ...
* wrapper type
  * Integer, Double, Long, Boolean, ...
* string

생명 주기를 엔터티와 함께합니다.

엔터티가 삭제되면 기본값 타입 또한 삭제됩니다.

기본값 타입은 공유하면 안 되는데, 한 번 수정하게 되면 공유한 쪽도 같이 영향을 받기 때문입니다.

특히 자바의 primitive 타입이면 당연히 공유는 말도 안 되고, wrapper type을 사용하더라도 공유 가능하지만 함부로 변경하면 안 됩니다.

### Embedded Type

자바의 데이터 클래스 처럼, 직접 새로운 타입을 정의해서 사용할 수 있습니다.

예를 들어 회원 엔터티가 주소라는 임베디드 타입을 가질 수 있습니다.

```java
package com.tistory.jaimenote.jpa.relation.domain.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

  @Embedded
  private Address address;

  @Embeddable
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class Address {
    private String city;
    private String street;
    private String zipcode;
  }
}
```

값을 정의한 곳에 `@Embeddable`을, 사용하는 곳에 `@Embedded`를 사용합니다.

실제 컬럼은 `member` 테이블 내 속성과 매핑되지만 객체 관점에서 조금 더 객체지향적(재사용, 응집도 등)으로 사용할 수 있습니다.

`Address` 자체 기능을 만들어서 사용할 수도 있습니다.

따라서 `ORM`을 이용해 테이블을 클래스와 매핑하다보면 잘 설계할 수록 클래스 수가 많아질 수 있습니다.

`Embedded` 타입 역시 엔터티와 생명주기를 같이합니다.

같은 엔터티 내에서 `Embedded` 타입을 여러 개 사용하면서 컬럼을 재정의 할 수도 있습니다.

아래 소스 코드는 집주소와, 회사주소를 같은 엔터티가 가지고 있을 때의 예시입니다.

```java
package com.tistory.jaimenote.jpa.relation.domain.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "city", column = @Column(name = "HOME_CITY")),
      @AttributeOverride(name = "street", column = @Column(name = "HOME_STREET")),
      @AttributeOverride(name = "zipcode", column = @Column(name = "HOME_ZIPCODE"))
  })
  private Address homeAddress;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "city", column = @Column(name = "COMPANY_CITY")),
      @AttributeOverride(name = "street", column = @Column(name = "COMPANY_STREET")),
      @AttributeOverride(name = "zipcode", column = @Column(name = "COMPANY_ZIPCODE"))
  })
  private Address companyAddress;

  @Embeddable
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class Address {

    private String city;
    private String street;
    private String zipcode;
  }
}
```

`@AttributeOverride(s)`를 이용해 컬럼을 재정의하여 사용 가능합니다.

`Embedded` 타입에 `null`을 할당할 경우 매핑한 컬럼은 모두 `null`이 됩니다.

### Collection Type

값 타입(Value Type)을 하나 이상 저장할 때 사용합니다.

@ElementCollection, @CollectionTable을 사용해 구현할 수 있습니다.

```java
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
```

`address` 라는 테이블을 생성하고 `member_id` 컬럼을 `FK`로 사용합니다.

```text
Hibernate: 
    
    create table member (
       member_id bigint not null,
        name varchar(255),
        primary key (member_id)
    )
Hibernate: 
    
    create table address (
       member_id bigint not null,
        city varchar(255),
        street varchar(255),
        zipcode varchar(255)
    )
```

fetchType을 지정해 즉시 또는 지연 로딩을 선택할 수 있습니다.

엔터티 수정에 대해 모든 영향을 받으므로 `cascade = CascadeType.ALL` + `orphanRemoval = true` 이 두 가지가 기본으로 적용된 타입이라고 할 수 있습니다.

컬렉션 타입은 식별자가 없고 엔터티의 ID + 각각의 값의 복합키로 구성됩니다.

따라서 변경사항이 발생하면 추적이 어렵고, 기존 연관 데이터를 모두 삭제하고 컬렉션에 있는 값을 모두 다시 저장하는 방식으로 동작합니다.

```
addresses.get(3).setCity("foo");
```

이런식으로 컬렉션 중간의 한 객체를 수정하더라도 추적하려면 모든 값을 비교해야하는데 그게 더 비효율적이기 때문에 `addresses`를 찾을 때 사용했던 `id`로 모든 데이터를 지운 뒤 수정한 데이터를 다시 추가합니다.

복합키로 동작하므로 `null`을 입력할 수 없고 중복된 데이터는 허용되지 않습니다.

(따라서 위의 예시도 List 보단 Set을 사용하는 것이 더 좋은 설계 방법입니다.)

실무에서는 데이터를 저장한 뒤 꾸준하게 추적 및 업데이트 할 필요가 있다면 `@OneToMany` 매핑을 더 선호합니다.

## 주의점

위에서 설명한 값 타입(Value Type)은 모두 여러 엔터티에서 공유하면 부작용이 발생할 수 있습니다.

이는 기본 타입의 경우 할당하게 되면 값을 복사하고, 객체타입은 참조를 전달하는 자바의 특성 떄문입니다.

따라서 값타입은 복사하는 형태로(객체타입의 경우 불변객체 타입으로 복사) 사용해야 합니다.

> 불변객체는 생성한 이후 절대 값을 변경할 수 없는 객체로 생성자를 통해 초기화하고 `setter`를 제공하지 않는 방법으로 구현할 수 있습니다.

값 타입은 정말 값만 사용할 필요가 있을 때만 사용해야 합니다.

엔터티 타입과 혼동하여 값 타입으로 만드는 실수를 해서는 안 됩니다.

식별자가 필요하고 데이터를 계속해서 관리할 필요가 있다면 엔터티타입으로 설계해야 갑자기 데이터가 손실되거나 변경되는 등의 예상치못한 상황들을 피할 수 있습니다.