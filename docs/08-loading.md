![](https://img.shields.io/badge/spring--boot-2.7.1-red) ![](https://img.shields.io/badge/gradle-7.4.1-brightgreen) ![](https://img.shields.io/badge/java-11-blue)
 
> 소스 코드는 [여기](https://github.com/lcalmsky/jpa) 있습니다. (commit hash: 85838b8)
> ```shell
> > git clone https://github.com/lcalmsky/jpa
> > git checkout 85838b8
> ```
> **Warning:** 최종 커밋 기준으로 작성되어 있어 모든 테스트 결과를 정확히 확인할 순 없으니 참고 부탁드립니다.

## Overview

즉시 로딩(EAGER)과 지연 로딩(LAZY)에 대해 알아봅니다.

## 지연 로딩(Lazy Loading)

`Unit`를 조회하면서 `Factory`의 정보가 필요하지 않을 때 굳이 `Factory`를 같이 조회할 필요가 없습니다. 

그럴 때 지연 로딩을 사용합니다.

사용법은 간단합니다.

매핑관계를 지정한 애너테이션에 `fetch` 속성을 `FetchType.LAZY`로 설정하시면 됩니다.

```java
package com.tistory.jaimenote.jpa.loading.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Exclude;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Unit {

  @Id
  @GeneratedValue
  @Column(name = "unit_id")
  private Long id;
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "factory_id")
  @Exclude
  private Factory factory;

  private Unit(String name, Factory factory) {
    this.name = name;
    this.factory = factory;
  }

  public static Unit create(String name, Factory factory) {
    return new Unit(name, factory);
  }
}
```

```java
package com.tistory.jaimenote.jpa.loading.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Factory {

  @Id
  @GeneratedValue
  @Column(name = "factory_id")
  private Long id;
  private String name;

  private Factory(String name) {
    this.name = name;
  }

  public static Factory create(String teamName) {
    return new Factory(teamName);
  }
}

```

이렇게 지정하면 `unit`만 조회해 올 때는 `join` 쿼리나 `factory`을 찾기위한 추가 쿼리가 발생하지 않습니다.

```java
package com.tistory.jaimenote.jpa.loading.infra.repository;

import com.tistory.jaimenote.jpa.loading.domain.entity.Factory;
import com.tistory.jaimenote.jpa.loading.domain.entity.Unit;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
class UnitRepositoryTest {

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  FactoryRepository factoryRepository;

  @PersistenceContext
  EntityManager entityManager;

  private Unit marine;

  @BeforeEach
  void setup() {
    Factory barracks = Factory.create("barracks");
    factoryRepository.save(barracks);
    marine = Unit.create("marine", barracks);
    unitRepository.save(marine);
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  @Rollback(false)
  void findMemberWithLazyFetch() {
    unitRepository.findById(marine.getId());
  }

  @Test
  @Rollback(false)
  void findMemberAndTeamWithLazyFetch() {
    unitRepository.findById(marine.getId())
        .map(Unit::getFactory)
        .ifPresent(System.out::println);
  }
}
```

테스트 코드를 작성하고 실행해보면

```text
Hibernate: 
    select
        unit0_.unit_id as unit_id1_9_0_,
        unit0_.factory_id as factory_3_9_0_,
        unit0_.name as name2_9_0_ 
    from
        unit unit0_ 
    where
        unit0_.unit_id=?
2022-07-05 15:13:47.462 TRACE 94447 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [BIGINT] - [2]
```

첫 테스트에 대해서는 `unit`에 대한 조회만 발생하고,

```text
Hibernate: 
    select
        unit0_.unit_id as unit_id1_9_0_,
        unit0_.factory_id as factory_3_9_0_,
        unit0_.name as name2_9_0_ 
    from
        unit unit0_ 
    where
        unit0_.unit_id=?
2022-07-05 15:15:00.753 TRACE 94560 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [BIGINT] - [2]
Hibernate: 
    select
        factory0_.factory_id as factory_1_3_0_,
        factory0_.name as name2_3_0_ 
    from
        factory factory0_ 
    where
        factory0_.factory_id=?
2022-07-05 15:15:00.772 TRACE 94560 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [BIGINT] - [1]
Factory(id=1, name=barracks)
```

두 번째 테스트에 대해서는 `factory`를 출력하기 위해 `Unit::getFactory` 하는 부분이 실행될 때 추가 쿼리가 발생된 것을 확인할 수 있습니다.

## 즉시 로딩(Eager Loading)

지연 로딩과 반대로 `factory`와 `unit`을 한꺼번에 조회해야 하는 경우가 많은 경우 `fetch = FetchType.EAGER`를 사용해 `unit`만 조회하더라도 `unit`과 관련있는 모든 객체들을 함께 조회하게 합니다.

```java
// 생략
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Unit {
  // 생략
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "factory_id")
  @Exclude
  private Factory factory;
  // 생략
}
```

이렇게 수정한 뒤 첫 번째 테스트만 수행해보면,

```text
Hibernate: 
    select
        unit0_.unit_id as unit_id1_9_0_,
        unit0_.factory_id as factory_3_9_0_,
        unit0_.name as name2_9_0_,
        factory1_.factory_id as factory_1_3_1_,
        factory1_.name as name2_3_1_ 
    from
        unit unit0_ 
    left outer join
        factory factory1_ 
            on unit0_.factory_id=factory1_.factory_id 
    where
        unit0_.unit_id=?
2022-07-05 15:19:07.099 TRACE 94883 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [BIGINT] - [2]
```

이렇게 한번에 조회한 것을 확인할 수 있습니다.

관계 설정이 제대로 되어있어 `join`을 사용할 수 있는 경우 JPA가 알아서 `join` 쿼리를 생성합니다.

## 주의점

가급적이면 실무에서는 지연 로딩을 사용합니다.

즉시 로딩을 사용할 경우 예상치 못한 SQL이 여러 차례 발생할 수 있기 때문입니다.

`join`이 사용되지 않는 즉시 로딩은 `N+1 problem`을 유발하기도 합니다. (자세한 내용은 [이 포스팅](https://jaime-note.tistory.com/54) 참조)

`@ManyToOne`과 `@OneToOne` 처럼 `XToOne`인 경우는 기본이 즉시 로딩이므로 반드시 지연 로딩으로 설정해주어야 합니다.

반면에 `@OneToMany`과 `@ManyToMany` 처럼 `XToMany`인 경우는 기본이 지연로딩이므로 따로 설정해 줄 필요가 없습니다.

무조건 지연로딩으로 설정해서 사용해야한다가 아니라 **예상할 수 없는 상황을 대비해 일단 설정은 지연로딩으로 해야한다**는 뜻입니다.

지연로딩으로 설정해도 `fetch join`이나 `EntityGraph` 등을 활용해 `join` 쿼리로 한 번에 가져올 수 있습니다.

`fetch join`은 다른 포스팅에서도 자주 다루었지만 `JPA` 카테고리에 추가로 포스팅 할 예정입니다.

